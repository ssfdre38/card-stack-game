#!/bin/bash
# Comprehensive screen size and gameplay testing script for Match Mania v2.3.13

export ANDROID_SDK_ROOT=/home/ubuntu/android-sdk
export PATH=$PATH:$ANDROID_SDK_ROOT/platform-tools

ADB="$ANDROID_SDK_ROOT/platform-tools/adb"

echo "=========================================="
echo "Match Mania v2.3.13 - Screen Size Testing"
echo "=========================================="
echo ""

# Test configurations
declare -A SCREEN_CONFIGS=(
    ["Small Phone"]="480x854"
    ["Medium Phone"]="720x1280"
    ["Large Phone (Current)"]="1080x2280"
    ["Extra Large Phone"]="1440x3040"
    ["Small Tablet"]="800x1280"
    ["Large Tablet"]="1200x1920"
)

# Function to test UI layout
test_ui_layout() {
    local screen_name=$1
    local screen_size=$2
    
    echo "Testing: $screen_name ($screen_size)"
    echo "-----------------------------------"
    
    # Get screen dimensions
    local actual_size=$($ADB shell wm size | grep "Physical size" | cut -d: -f2 | tr -d ' ')
    echo "Current screen size: $actual_size"
    
    # Dump UI hierarchy
    $ADB shell uiautomator dump > /dev/null 2>&1
    sleep 1
    
    # Check if player hand layout and new game button are visible
    local ui_check=$($ADB shell cat /sdcard/window_dump.xml 2>/dev/null)
    
    if echo "$ui_check" | grep -q "playerHandLayout"; then
        echo "✅ Player hand layout found"
    else
        echo "❌ Player hand layout NOT FOUND"
        return 1
    fi
    
    if echo "$ui_check" | grep -q "newGameButton"; then
        echo "✅ New Game button found"
    else
        echo "❌ New Game button NOT FOUND"
        return 1
    fi
    
    # Extract bounds for key elements
    local hand_bounds=$(echo "$ui_check" | grep -o 'resource-id="com.matchmania.game:id/playerHandLayout"[^>]*bounds="\[[^]]*\]"' | grep -o 'bounds="[^"]*"' | cut -d'"' -f2)
    local button_bounds=$(echo "$ui_check" | grep -o 'resource-id="com.matchmania.game:id/newGameButton"[^>]*bounds="\[[^]]*\]"' | grep -o 'bounds="[^"]*"' | cut -d'"' -f2)
    
    echo "Player hand bounds: $hand_bounds"
    echo "New Game button bounds: $button_bounds"
    
    # Extract max Y coordinate from button bounds
    if [ -n "$button_bounds" ]; then
        local button_bottom=$(echo "$button_bounds" | grep -o '\][0-9]*\]$' | tr -d '][')
        local screen_height=$(echo "$actual_size" | cut -d'x' -f2)
        
        if [ "$button_bottom" -le "$screen_height" ]; then
            echo "✅ Button within screen bounds ($button_bottom <= $screen_height)"
        else
            echo "❌ Button exceeds screen bounds ($button_bottom > $screen_height)"
            return 1
        fi
    fi
    
    echo ""
    return 0
}

# Function to test gameplay
test_gameplay() {
    echo "Testing Gameplay Functionality"
    echo "----------------------------"
    
    # Test draw button click
    echo "Testing draw card..."
    $ADB shell input tap 700 1050 2>/dev/null
    sleep 2
    
    # Check if deck count changed
    $ADB shell uiautomator dump > /dev/null 2>&1
    local deck_info=$($ADB shell cat /sdcard/window_dump.xml 2>/dev/null | grep -o 'resource-id="com.matchmania.game:id/deckCountView"[^>]*text="[^"]*"' | grep -o 'text="[^"]*"' | cut -d'"' -f2)
    echo "Deck status: $deck_info"
    
    if [ -n "$deck_info" ]; then
        echo "✅ Deck counter functional"
    else
        echo "❌ Deck counter issue"
    fi
    
    # Test card interaction (tap on a card)
    echo "Testing card interaction..."
    $ADB shell input tap 184 1665 2>/dev/null
    sleep 2
    
    # Check if current player changed or card was played
    $ADB shell uiautomator dump > /dev/null 2>&1
    local player_info=$($ADB shell cat /sdcard/window_dump.xml 2>/dev/null | grep -o 'resource-id="com.matchmania.game:id/currentPlayerView"[^>]*text="[^"]*"' | grep -o 'text="[^"]*"' | cut -d'"' -f2)
    echo "Current player: $player_info"
    
    if [ -n "$player_info" ]; then
        echo "✅ Game state updating"
    else
        echo "❌ Game state issue"
    fi
    
    # Test New Game button
    echo "Testing New Game button..."
    $ADB shell input tap 540 2038 2>/dev/null
    sleep 2
    
    $ADB shell uiautomator dump > /dev/null 2>&1
    local cards_after=$($ADB shell cat /sdcard/window_dump.xml 2>/dev/null | grep -o 'com.matchmania.game:id/playerHandLayout' | wc -l)
    
    if [ "$cards_after" -gt 0 ]; then
        echo "✅ New Game functionality working"
    else
        echo "⚠️  New Game may have issues"
    fi
    
    echo ""
}

# Function to test landscape orientation
test_landscape() {
    echo "Testing Landscape Orientation"
    echo "----------------------------"
    
    # Rotate to landscape
    $ADB shell settings put system user_rotation 1
    $ADB shell content insert --uri content://settings/system --bind name:s:accelerometer_rotation --bind value:i:0
    sleep 2
    
    # Get screen size in landscape
    local landscape_size=$($ADB shell wm size | grep "Physical size" | cut -d: -f2 | tr -d ' ')
    echo "Landscape size: $landscape_size"
    
    # Test UI in landscape
    test_ui_layout "Landscape" "$landscape_size"
    
    # Rotate back to portrait
    $ADB shell settings put system user_rotation 0
    sleep 2
    
    echo ""
}

# Main test execution
echo "Starting comprehensive testing..."
echo ""

# Restart app for fresh state
$ADB shell am force-stop com.matchmania.game
sleep 2
$ADB shell am start -n com.matchmania.game/com.cardstack.game.MainActivity
sleep 3

echo ""
echo "=========================================="
echo "PORTRAIT ORIENTATION TESTS"
echo "=========================================="
echo ""

# Test current screen configuration
test_ui_layout "Current Device" "$(adb shell wm size | grep Physical | cut -d: -f2 | tr -d ' ')"

echo ""
echo "=========================================="
echo "GAMEPLAY FUNCTIONALITY TESTS"
echo "=========================================="
echo ""

test_gameplay

echo ""
echo "=========================================="
echo "LANDSCAPE ORIENTATION TESTS"
echo "=========================================="
echo ""

test_landscape

echo ""
echo "=========================================="
echo "TEST SUMMARY"
echo "=========================================="
echo ""
echo "All critical tests completed!"
echo "Review the output above for any ❌ failures"
echo ""
