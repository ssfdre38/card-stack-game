#!/bin/bash

################################################################################
# Match Mania - Comprehensive Automated Testing Script
# Tests app across multiple Android versions with UI, OTA, and gameplay tests
# Automatically captures screenshots on errors
# Cross-platform: Works on Linux, macOS, and Windows (WSL/Git Bash)
################################################################################

set -e  # Exit on error (will be trapped)

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Directories (use relative paths)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
SCREENSHOTS_DIR="$SCRIPT_DIR/screenshots"
REPORTS_DIR="$SCRIPT_DIR/reports"
LOGS_DIR="$SCRIPT_DIR/logs"
CONFIG_FILE="$SCRIPT_DIR/configs/test-config.json"

# Create required directories if they don't exist
mkdir -p "$SCREENSHOTS_DIR" "$REPORTS_DIR" "$LOGS_DIR"

# Detect platform and set Android SDK path
detect_android_sdk() {
    # Check if ANDROID_SDK_ROOT or ANDROID_HOME is already set
    if [ -n "$ANDROID_SDK_ROOT" ]; then
        echo "$ANDROID_SDK_ROOT"
        return 0
    elif [ -n "$ANDROID_HOME" ]; then
        echo "$ANDROID_HOME"
        return 0
    fi
    
    # Detect platform
    case "$(uname -s)" in
        Linux*)
            # Common Linux locations
            local locations=(
                "$HOME/Android/Sdk"
                "$HOME/android-sdk"
                "/usr/local/android-sdk"
                "/opt/android-sdk"
            )
            ;;
        Darwin*)
            # macOS locations
            local locations=(
                "$HOME/Library/Android/sdk"
                "$HOME/Android/Sdk"
            )
            ;;
        MINGW*|MSYS*|CYGWIN*)
            # Windows (Git Bash/MSYS2/Cygwin)
            local locations=(
                "$LOCALAPPDATA/Android/Sdk"
                "$HOME/AppData/Local/Android/Sdk"
                "C:/Android/Sdk"
            )
            ;;
        *)
            echo "Unknown platform: $(uname -s)" >&2
            return 1
            ;;
    esac
    
    # Try to find SDK in common locations
    for location in "${locations[@]}"; do
        if [ -d "$location" ]; then
            echo "$location"
            return 0
        fi
    done
    
    echo "Android SDK not found. Please set ANDROID_SDK_ROOT or ANDROID_HOME" >&2
    return 1
}

# Set Android SDK path
ANDROID_SDK_ROOT=$(detect_android_sdk)
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error: Android SDK not found!${NC}"
    echo ""
    echo "Would you like to automatically install Android SDK?"
    echo ""
    read -p "Install Android SDK now? (Y/n): " -n 1 -r
    echo ""
    
    if [[ ! $REPLY =~ ^[Nn]$ ]]; then
        if [ -f "$SCRIPT_DIR/install-android-sdk.sh" ]; then
            bash "$SCRIPT_DIR/install-android-sdk.sh"
            if [ $? -eq 0 ]; then
                echo ""
                echo -e "${GREEN}✓ SDK installed! Please restart this script.${NC}"
                echo "  Run: ./run-comprehensive-tests.sh"
                exit 0
            else
                echo -e "${RED}❌ SDK installation failed!${NC}"
                exit 1
            fi
        else
            echo -e "${RED}❌ Error: install-android-sdk.sh not found!${NC}"
            echo "Please download from: https://github.com/ssfdre38/match-mania"
            exit 1
        fi
    fi
    
    echo ""
    echo "Manual installation:"
    echo "  1. Download from: https://developer.android.com/studio"
    echo "  2. Or run: ./install-android-sdk.sh"
    echo ""
    echo "Common SDK locations:"
    echo "  Linux:   ~/Android/Sdk"
    echo "  macOS:   ~/Library/Android/sdk"
    echo "  Windows: %LOCALAPPDATA%/Android/Sdk"
    exit 1
fi

export ANDROID_SDK_ROOT
export ANDROID_HOME="$ANDROID_SDK_ROOT"
export PATH="$PATH:$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin"

# Test settings
APP_PACKAGE="com.matchmania.game"
APP_ACTIVITY="com.cardstack.game.MainActivity"
APK_PATH="$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"

# Global test results
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0
ERRORS_DETECTED=0

# Test session info
TEST_SESSION_ID="test_$(date +%Y%m%d_%H%M%S)"
SESSION_LOG="$LOGS_DIR/${TEST_SESSION_ID}.log"
SESSION_REPORT="$REPORTS_DIR/${TEST_SESSION_ID}_report.html"

# Logging functions
log() {
    echo -e "${BLUE}[$(date +'%H:%M:%S')]${NC} $1" | tee -a "$SESSION_LOG"
}

log_success() {
    echo -e "${GREEN}✓${NC} $1" | tee -a "$SESSION_LOG"
}

log_error() {
    echo -e "${RED}✗${NC} $1" | tee -a "$SESSION_LOG"
    ERRORS_DETECTED=$((ERRORS_DETECTED + 1))
}

log_warning() {
    echo -e "${YELLOW}⚠${NC} $1" | tee -a "$SESSION_LOG"
}

log_info() {
    echo -e "${CYAN}ℹ${NC} $1" | tee -a "$SESSION_LOG"
}

print_header() {
    echo "" | tee -a "$SESSION_LOG"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" | tee -a "$SESSION_LOG"
    echo -e "${CYAN}$1${NC}" | tee -a "$SESSION_LOG"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" | tee -a "$SESSION_LOG"
    echo "" | tee -a "$SESSION_LOG"
}

# Screenshot function
take_screenshot() {
    local reason=$1
    local test_name=$2
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local filename="${TEST_SESSION_ID}_${test_name}_${reason}_${timestamp}.png"
    local filepath="$SCREENSHOTS_DIR/$filename"
    
    log_info "Capturing screenshot: $filename"
    adb shell screencap -p > "$filepath" 2>/dev/null || {
        log_warning "Failed to capture screenshot"
        return 1
    }
    
    log_success "Screenshot saved: $filename"
    echo "$filepath"
}

# Error handler
handle_error() {
    local line_no=$1
    local test_name=${2:-"unknown"}
    log_error "Error in test '$test_name' at line $line_no"
    take_screenshot "error" "$test_name"
    FAILED_TESTS=$((FAILED_TESTS + 1))
}

trap 'handle_error ${LINENO} ${CURRENT_TEST}' ERR

# Wait for device
wait_for_device() {
    local timeout=${1:-120}
    log "Waiting for emulator device (timeout: ${timeout}s)..."
    adb wait-for-device shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done' &
    local wait_pid=$!
    
    ( sleep $timeout; kill -9 $wait_pid 2>/dev/null ) &
    local timeout_pid=$!
    
    if wait $wait_pid 2>/dev/null; then
        kill -9 $timeout_pid 2>/dev/null
        log_success "Device ready"
        return 0
    else
        log_error "Device boot timeout after ${timeout}s"
        return 1
    fi
}

# Create AVD if needed
create_avd() {
    local avd_name=$1
    local api_level=$2
    local device_config=$3
    
    log "Creating AVD: $avd_name (API $api_level)"
    
    # Check if AVD already exists
    if avdmanager list avd | grep -q "Name: $avd_name"; then
        log_info "AVD already exists, recreating..."
        avdmanager delete avd -n "$avd_name" 2>/dev/null || true
    fi
    
    # Create AVD
    echo "no" | avdmanager create avd \
        -n "$avd_name" \
        -k "system-images;android-${api_level};google_apis;x86_64" \
        -d "pixel" \
        --force \
        2>&1 | tee -a "$SESSION_LOG"
    
    if [ $? -eq 0 ]; then
        log_success "AVD created successfully"
        return 0
    else
        log_error "Failed to create AVD"
        return 1
    fi
}

# Start emulator
start_emulator() {
    local avd_name=$1
    
    log "Starting emulator: $avd_name"
    
    # Kill any existing emulator
    adb devices | grep emulator | cut -f1 | xargs -I {} adb -s {} emu kill 2>/dev/null || true
    sleep 2
    
    # Start emulator in background
    emulator -avd "$avd_name" \
        -no-snapshot-save \
        -no-audio \
        -no-boot-anim \
        -no-window \
        -gpu swiftshader_indirect \
        -memory 2048 \
        > "$LOGS_DIR/${TEST_SESSION_ID}_emulator_${avd_name}.log" 2>&1 &
    
    local emulator_pid=$!
    echo $emulator_pid > "$LOGS_DIR/emulator.pid"
    
    log_info "Emulator PID: $emulator_pid"
    
    # Wait for device
    if wait_for_device 180; then
        log_success "Emulator started successfully"
        sleep 5  # Extra time for system to stabilize
        return 0
    else
        log_error "Emulator failed to start"
        kill -9 $emulator_pid 2>/dev/null || true
        return 1
    fi
}

# Stop emulator
stop_emulator() {
    log "Stopping emulator..."
    adb emu kill 2>/dev/null || true
    
    if [ -f "$LOGS_DIR/emulator.pid" ]; then
        local pid=$(cat "$LOGS_DIR/emulator.pid")
        kill -9 $pid 2>/dev/null || true
        rm "$LOGS_DIR/emulator.pid"
    fi
    
    sleep 3
    log_success "Emulator stopped"
}

# Install app
install_app() {
    CURRENT_TEST="install_app"
    log "Installing app..."
    
    if [ ! -f "$APK_PATH" ]; then
        log_error "APK not found: $APK_PATH"
        return 1
    fi
    
    adb install -r "$APK_PATH" 2>&1 | tee -a "$SESSION_LOG"
    
    if [ ${PIPESTATUS[0]} -eq 0 ]; then
        log_success "App installed successfully"
        return 0
    else
        log_error "App installation failed"
        return 1
    fi
}

# Launch app
launch_app() {
    CURRENT_TEST="launch_app"
    log "Launching app..."
    
    adb shell am start -n "${APP_PACKAGE}/${APP_ACTIVITY}" 2>&1 | tee -a "$SESSION_LOG"
    sleep 3
    
    # Check if app is running
    if adb shell "pidof $APP_PACKAGE" > /dev/null 2>&1; then
        log_success "App launched successfully"
        take_screenshot "app_launched" "launch"
        return 0
    else
        log_error "App failed to launch"
        take_screenshot "launch_failed" "launch"
        return 1
    fi
}

# UI Tests
test_ui_elements() {
    CURRENT_TEST="ui_elements"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log "Testing UI elements..."
    
    # Dump UI hierarchy
    adb shell uiautomator dump
    adb pull /sdcard/window_dump.xml "$LOGS_DIR/ui_dump_${TEST_SESSION_ID}.xml" 2>&1 | tee -a "$SESSION_LOG"
    
    # Check for critical UI elements
    local ui_file="$LOGS_DIR/ui_dump_${TEST_SESSION_ID}.xml"
    local elements=(
        "playerHandLayout"
        "newGameButton"
        "drawButton"
        "topCardView"
        "currentPlayerView"
    )
    
    local missing_elements=0
    for element in "${elements[@]}"; do
        if grep -q "$element" "$ui_file"; then
            log_success "Found UI element: $element"
        else
            log_error "Missing UI element: $element"
            missing_elements=$((missing_elements + 1))
        fi
    done
    
    take_screenshot "ui_check" "ui_elements"
    
    if [ $missing_elements -eq 0 ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        log_success "All UI elements present"
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        log_error "$missing_elements UI elements missing"
        return 1
    fi
}

# Rotation test
test_rotation() {
    CURRENT_TEST="rotation"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log "Testing screen rotation..."
    
    # Portrait
    adb shell settings put system user_rotation 0
    sleep 2
    take_screenshot "portrait" "rotation"
    
    # Landscape
    adb shell settings put system user_rotation 1
    sleep 2
    take_screenshot "landscape" "rotation"
    
    # Check if app is still running
    if adb shell "pidof $APP_PACKAGE" > /dev/null 2>&1; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        log_success "Rotation test passed"
        
        # Rotate back to portrait
        adb shell settings put system user_rotation 0
        sleep 1
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        log_error "App crashed during rotation"
        return 1
    fi
}

# Gameplay tests
test_gameplay() {
    CURRENT_TEST="gameplay"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log "Testing gameplay..."
    
    # Test draw button
    log_info "Testing draw button..."
    adb shell input tap 699 1012
    sleep 2
    take_screenshot "after_draw" "gameplay"
    
    # Test card interaction
    log_info "Testing card interaction..."
    adb shell input tap 184 1665
    sleep 2
    take_screenshot "after_card_play" "gameplay"
    
    # Test new game button
    log_info "Testing new game button..."
    adb shell input tap 540 2038
    sleep 2
    take_screenshot "new_game" "gameplay"
    
    # Check if app is still running
    if adb shell "pidof $APP_PACKAGE" > /dev/null 2>&1; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        log_success "Gameplay test passed"
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        log_error "App crashed during gameplay"
        return 1
    fi
}

# OTA test
test_ota() {
    CURRENT_TEST="ota"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log "Testing OTA update system..."
    
    # Open settings
    log_info "Opening settings..."
    adb shell input tap 968 352
    sleep 2
    take_screenshot "settings_opened" "ota"
    
    # Try to find "Check for Updates" button
    adb shell uiautomator dump
    adb pull /sdcard/window_dump.xml "$LOGS_DIR/ota_ui_dump_${TEST_SESSION_ID}.xml" 2>&1 > /dev/null
    
    if grep -q "Check for Updates\|Update" "$LOGS_DIR/ota_ui_dump_${TEST_SESSION_ID}.xml"; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        log_success "OTA update UI found"
        
        # Go back
        adb shell input keyevent KEYCODE_BACK
        sleep 1
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        log_warning "OTA update UI not found"
        
        # Go back
        adb shell input keyevent KEYCODE_BACK
        sleep 1
        return 1
    fi
}

# Performance test
test_performance() {
    CURRENT_TEST="performance"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log "Testing performance..."
    
    # Check memory usage
    local mem_info=$(adb shell dumpsys meminfo $APP_PACKAGE | grep "TOTAL" | head -1)
    log_info "Memory usage: $mem_info"
    
    # Check for crashes
    local crash_count=$(adb logcat -d | grep -c "FATAL EXCEPTION" || echo "0")
    
    if [ "$crash_count" -eq 0 ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        log_success "No crashes detected"
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        log_error "Detected $crash_count crashes"
        return 1
    fi
}

# Run test suite for one Android version
run_test_suite() {
    local api_level=$1
    local android_version=$2
    local avd_name="match_mania_test_api${api_level}"
    
    print_header "Testing on Android ${android_version} (API ${api_level})"
    
    # Create and start emulator
    create_avd "$avd_name" "$api_level" "default" || return 1
    start_emulator "$avd_name" || return 1
    
    # Install and launch app
    install_app || { stop_emulator; return 1; }
    launch_app || { stop_emulator; return 1; }
    
    # Run tests
    test_ui_elements || true
    test_rotation || true
    test_gameplay || true
    test_ota || true
    test_performance || true
    
    # Cleanup
    stop_emulator
    
    log_success "Test suite completed for API $api_level"
}

# Generate HTML report
generate_report() {
    log "Generating HTML report..."
    
    local pass_rate=0
    if [ $TOTAL_TESTS -gt 0 ]; then
        pass_rate=$((PASSED_TESTS * 100 / TOTAL_TESTS))
    fi
    
    cat > "$SESSION_REPORT" << EOF
<!DOCTYPE html>
<html>
<head>
    <title>Match Mania Test Report - $TEST_SESSION_ID</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #333; border-bottom: 3px solid #4CAF50; padding-bottom: 10px; }
        h2 { color: #666; margin-top: 30px; }
        .summary { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin: 20px 0; }
        .stat-box { padding: 20px; border-radius: 8px; text-align: center; }
        .stat-box h3 { margin: 0; font-size: 32px; }
        .stat-box p { margin: 5px 0 0 0; color: #666; }
        .passed { background: #e8f5e9; border-left: 4px solid #4CAF50; }
        .failed { background: #ffebee; border-left: 4px solid #f44336; }
        .total { background: #e3f2fd; border-left: 4px solid #2196F3; }
        .errors { background: #fff3e0; border-left: 4px solid #ff9800; }
        .screenshots { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; margin: 20px 0; }
        .screenshot { border: 1px solid #ddd; border-radius: 4px; padding: 10px; background: #fafafa; }
        .screenshot img { max-width: 100%; height: auto; border-radius: 4px; }
        .screenshot p { margin: 10px 0 0 0; font-size: 14px; color: #666; }
        .log { background: #f5f5f5; padding: 15px; border-radius: 4px; font-family: 'Courier New', monospace; font-size: 12px; max-height: 400px; overflow-y: auto; }
        .timestamp { color: #888; font-size: 14px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎮 Match Mania Automated Test Report</h1>
        <p class="timestamp">Test Session: $TEST_SESSION_ID</p>
        <p class="timestamp">Date: $(date)</p>
        
        <h2>📊 Test Summary</h2>
        <div class="summary">
            <div class="stat-box total">
                <h3>$TOTAL_TESTS</h3>
                <p>Total Tests</p>
            </div>
            <div class="stat-box passed">
                <h3>$PASSED_TESTS</h3>
                <p>Passed</p>
            </div>
            <div class="stat-box failed">
                <h3>$FAILED_TESTS</h3>
                <p>Failed</p>
            </div>
            <div class="stat-box errors">
                <h3>$ERRORS_DETECTED</h3>
                <p>Errors</p>
            </div>
        </div>
        
        <h2>📈 Pass Rate: ${pass_rate}%</h2>
        
        <h2>📸 Screenshots</h2>
        <div class="screenshots">
EOF
    
    # Add screenshots
    for screenshot in "$SCREENSHOTS_DIR"/${TEST_SESSION_ID}_*.png; do
        if [ -f "$screenshot" ]; then
            local basename=$(basename "$screenshot")
            echo "            <div class=\"screenshot\">" >> "$SESSION_REPORT"
            echo "                <img src=\"../screenshots/$basename\" alt=\"$basename\">" >> "$SESSION_REPORT"
            echo "                <p>$basename</p>" >> "$SESSION_REPORT"
            echo "            </div>" >> "$SESSION_REPORT"
        fi
    done
    
    cat >> "$SESSION_REPORT" << EOF
        </div>
        
        <h2>📝 Test Log</h2>
        <div class="log">
            <pre>$(cat "$SESSION_LOG")</pre>
        </div>
    </div>
</body>
</html>
EOF
    
    log_success "Report generated: $SESSION_REPORT"
}

# Main execution
main() {
    print_header "Match Mania Comprehensive Test Suite"
    log "Session ID: $TEST_SESSION_ID"
    log "Starting automated tests..."
    
    # Ensure APK is built
    if [ ! -f "$APK_PATH" ]; then
        log "Building APK..."
        cd "$PROJECT_DIR"
        ./gradlew assembleDebug 2>&1 | tee -a "$SESSION_LOG"
    fi
    
    # Test on Android 30 (currently installed)
    run_test_suite 30 "11" || true
    
    # Generate report
    generate_report
    
    print_header "Test Suite Complete"
    log "Total Tests: $TOTAL_TESTS"
    log_success "Passed: $PASSED_TESTS"
    if [ $FAILED_TESTS -gt 0 ]; then
        log_error "Failed: $FAILED_TESTS"
    fi
    if [ $ERRORS_DETECTED -gt 0 ]; then
        log_warning "Errors Detected: $ERRORS_DETECTED"
    fi
    log ""
    log "Report: $SESSION_REPORT"
    log "Screenshots: $SCREENSHOTS_DIR"
    log "Logs: $SESSION_LOG"
    
    # Open report if possible (cross-platform)
    if command -v xdg-open > /dev/null 2>&1; then
        # Linux
        xdg-open "$SESSION_REPORT" 2>/dev/null &
    elif command -v open > /dev/null 2>&1; then
        # macOS
        open "$SESSION_REPORT" 2>/dev/null &
    elif command -v start > /dev/null 2>&1; then
        # Windows (Git Bash/WSL)
        start "$SESSION_REPORT" 2>/dev/null &
    else
        log_info "Open manually: $SESSION_REPORT"
    fi
}

# Run main
main "$@"
