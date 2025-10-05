#!/bin/bash

################################################################################
# Match Mania - Developer Build Script
# 
# This script helps developers build and test Match Mania locally.
# It does NOT deploy to websites or create official releases.
# 
# Usage:
#   ./scripts/dev-build.sh [options]
#
# Options:
#   --debug         Build debug APK only (default)
#   --release       Build release APK (requires signing keys)
#   --both          Build both debug and release APKs
#   --clean         Clean build directory before building
#   --test          Run tests after building
#   --install       Install APK to connected device/emulator
#   --help          Show this help message
#
# Examples:
#   ./scripts/dev-build.sh --debug --clean
#   ./scripts/dev-build.sh --both --install
#   ./scripts/dev-build.sh --release
#
################################################################################

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Default options
BUILD_TYPE="debug"
CLEAN_BUILD=false
RUN_TESTS=false
INSTALL_APK=false
SHOW_HELP=false

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --debug)
            BUILD_TYPE="debug"
            shift
            ;;
        --release)
            BUILD_TYPE="release"
            shift
            ;;
        --both)
            BUILD_TYPE="both"
            shift
            ;;
        --clean)
            CLEAN_BUILD=true
            shift
            ;;
        --test)
            RUN_TESTS=true
            shift
            ;;
        --install)
            INSTALL_APK=true
            shift
            ;;
        --help|-h)
            SHOW_HELP=true
            shift
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            SHOW_HELP=true
            shift
            ;;
    esac
done

# Print colored messages
print_header() {
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

# Show help message
show_help() {
    cat << EOF

${CYAN}Match Mania - Developer Build Script${NC}

This script helps developers build and test Match Mania locally.

${YELLOW}Usage:${NC}
  ./scripts/dev-build.sh [options]

${YELLOW}Options:${NC}
  --debug         Build debug APK only (default)
  --release       Build release APK (requires signing keys)
  --both          Build both debug and release APKs
  --clean         Clean build directory before building
  --test          Run tests after building
  --install       Install APK to connected device/emulator
  --help, -h      Show this help message

${YELLOW}Examples:${NC}
  ${GREEN}# Build debug APK with clean build${NC}
  ./scripts/dev-build.sh --debug --clean

  ${GREEN}# Build both debug and release, then install${NC}
  ./scripts/dev-build.sh --both --install

  ${GREEN}# Build release APK only${NC}
  ./scripts/dev-build.sh --release

  ${GREEN}# Build debug, run tests, and install${NC}
  ./scripts/dev-build.sh --debug --test --install

${YELLOW}Project Structure:${NC}
  app/                      - Android app source code
  app/build.gradle          - Build configuration (version info here)
  scripts/                  - Helper scripts (this file)
  emulator-testing/         - Automated testing system
  docs/                     - Documentation

${YELLOW}Before Building:${NC}
  1. Ensure you have Android SDK installed
  2. Set ANDROID_HOME environment variable
  3. For release builds, you need signing keys (or use debug)

${YELLOW}Version Information:${NC}
  To change version: Edit app/build.gradle
  - versionName: User-facing version (e.g., "2.3.15")
  - versionCode: Incremental build number (e.g., 33)

${YELLOW}Contributing:${NC}
  See CONTRIBUTING.md for contribution guidelines
  See docs/development/ for development documentation

EOF
}

if [ "$SHOW_HELP" = true ]; then
    show_help
    exit 0
fi

# Get project directory (script is in scripts/ subdirectory)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR="$( cd "$SCRIPT_DIR/.." && pwd )"

# Check if we're in the right directory
if [ ! -f "$PROJECT_DIR/app/build.gradle" ]; then
    print_error "Cannot find app/build.gradle"
    print_info "Please run this script from the project root or scripts directory"
    exit 1
fi

cd "$PROJECT_DIR"

# Print header
clear
print_header "🎮 Match Mania - Developer Build Script"
echo ""

# Get current version from build.gradle
CURRENT_VERSION=$(grep 'versionName' app/build.gradle | sed 's/.*versionName "\(.*\)"/\1/')
VERSION_CODE=$(grep 'versionCode' app/build.gradle | head -1 | sed 's/.*versionCode \(.*\)/\1/' | tr -d ' ')

print_info "Project: Match Mania"
print_info "Version: v$CURRENT_VERSION (build $VERSION_CODE)"
print_info "Build Type: $BUILD_TYPE"
echo ""

# Check for Android SDK
if [ -z "$ANDROID_HOME" ]; then
    print_warning "ANDROID_HOME not set. Trying common locations..."
    
    # Try common Android SDK locations
    if [ -d "$HOME/Android/Sdk" ]; then
        export ANDROID_HOME="$HOME/Android/Sdk"
        print_success "Found Android SDK at $ANDROID_HOME"
    elif [ -d "$HOME/Library/Android/sdk" ]; then
        export ANDROID_HOME="$HOME/Library/Android/sdk"
        print_success "Found Android SDK at $ANDROID_HOME"
    else
        print_error "Android SDK not found!"
        print_info "Please install Android SDK and set ANDROID_HOME environment variable"
        print_info "Visit: https://developer.android.com/studio"
        exit 1
    fi
fi

# Add Android tools to PATH
export PATH="$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin"

# Clean build if requested
if [ "$CLEAN_BUILD" = true ]; then
    print_header "🧹 Cleaning Previous Builds"
    ./gradlew clean
    print_success "Clean complete"
    echo ""
fi

# Run tests if requested
if [ "$RUN_TESTS" = true ]; then
    print_header "🧪 Running Tests"
    ./gradlew test
    print_success "Tests complete"
    echo ""
fi

# Build APK(s)
print_header "🔨 Building APK(s)"

if [ "$BUILD_TYPE" = "debug" ] || [ "$BUILD_TYPE" = "both" ]; then
    print_info "Building debug APK..."
    ./gradlew assembleDebug
    
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        print_success "Debug APK built successfully"
        DEBUG_SIZE=$(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)
        echo -e "   ${CYAN}Location:${NC} app/build/outputs/apk/debug/app-debug.apk"
        echo -e "   ${CYAN}Size:${NC} $DEBUG_SIZE"
    else
        print_error "Debug APK build failed!"
        exit 1
    fi
fi

if [ "$BUILD_TYPE" = "release" ] || [ "$BUILD_TYPE" = "both" ]; then
    echo ""
    print_info "Building release APK..."
    
    # Check if signing keys exist
    if [ ! -f "app/matchmania-release-key.jks" ]; then
        print_warning "Signing key not found: app/matchmania-release-key.jks"
        print_info "Release APK will not be signed or may fail to build"
        print_info "For testing, use --debug instead"
        echo ""
        read -p "Continue anyway? (y/N): " confirm
        if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
            print_error "Build cancelled"
            exit 1
        fi
    fi
    
    ./gradlew assembleRelease
    
    if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
        print_success "Release APK built successfully"
        RELEASE_SIZE=$(du -h app/build/outputs/apk/release/app-release.apk | cut -f1)
        echo -e "   ${CYAN}Location:${NC} app/build/outputs/apk/release/app-release.apk"
        echo -e "   ${CYAN}Size:${NC} $RELEASE_SIZE"
    else
        print_error "Release APK build failed!"
        exit 1
    fi
fi

echo ""
print_success "Build complete!"
echo ""

# Install APK if requested
if [ "$INSTALL_APK" = true ]; then
    print_header "📱 Installing APK"
    
    # Check if device is connected
    if ! adb devices | grep -q "device$"; then
        print_error "No Android device connected!"
        print_info "Connect a device or start an emulator and try again"
        exit 1
    fi
    
    # Determine which APK to install
    if [ "$BUILD_TYPE" = "release" ]; then
        APK_PATH="app/build/outputs/apk/release/app-release.apk"
    else
        APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    fi
    
    if [ ! -f "$APK_PATH" ]; then
        print_error "APK not found: $APK_PATH"
        exit 1
    fi
    
    print_info "Installing $APK_PATH..."
    adb install -r "$APK_PATH"
    
    print_success "APK installed successfully!"
    print_info "Launch Match Mania on your device to test"
    echo ""
fi

# Print summary
print_header "📊 Build Summary"
echo ""
echo -e "${CYAN}Version:${NC} v$CURRENT_VERSION (build $VERSION_CODE)"
echo -e "${CYAN}Build Type:${NC} $BUILD_TYPE"
echo ""

if [ "$BUILD_TYPE" = "debug" ] || [ "$BUILD_TYPE" = "both" ]; then
    echo -e "${GREEN}✓ Debug APK:${NC}"
    echo "  app/build/outputs/apk/debug/app-debug.apk"
fi

if [ "$BUILD_TYPE" = "release" ] || [ "$BUILD_TYPE" = "both" ]; then
    echo -e "${GREEN}✓ Release APK:${NC}"
    echo "  app/build/outputs/apk/release/app-release.apk"
fi

echo ""
print_header "🎉 All Done!"
echo ""

# Show next steps
echo -e "${CYAN}Next Steps:${NC}"
echo "  1. Test the APK on a device or emulator"
echo "  2. Run automated tests: ./emulator-testing/run-comprehensive-tests.sh"
echo "  3. Check CHANGELOG.md before releasing"
echo "  4. See CONTRIBUTING.md for contribution guidelines"
echo ""

# Show device commands if ADB is available
if command -v adb &> /dev/null; then
    echo -e "${CYAN}Quick Commands:${NC}"
    echo "  ${GREEN}Install debug APK:${NC}"
    echo "    adb install -r app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "  ${GREEN}View logs:${NC}"
    echo "    adb logcat | grep MatchMania"
    echo ""
    echo "  ${GREEN}Uninstall:${NC}"
    echo "    adb uninstall com.matchmania.game"
    echo ""
fi

print_success "Build script completed successfully! 🚀"
echo ""
