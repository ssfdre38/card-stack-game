#!/bin/bash

################################################################################
# Match Mania - Setup System Images
# Downloads required Android system images for testing
# Cross-platform: Works on Linux, macOS, and Windows (WSL/Git Bash)
################################################################################

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
    echo "❌ Error: Android SDK not found!"
    echo ""
    echo "Please install Android SDK and set environment variable:"
    echo "  export ANDROID_SDK_ROOT=/path/to/android/sdk"
    echo ""
    echo "Common locations:"
    echo "  Linux:   ~/Android/Sdk"
    echo "  macOS:   ~/Library/Android/sdk"
    echo "  Windows: %LOCALAPPDATA%/Android/Sdk"
    echo ""
    echo "Download from: https://developer.android.com/studio"
    exit 1
fi

export ANDROID_SDK_ROOT
export ANDROID_HOME="$ANDROID_SDK_ROOT"
export PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools"

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║        Match Mania - System Images Setup                      ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "✓ Android SDK: $ANDROID_SDK_ROOT"
echo ""

# System images to install
declare -a IMAGES=(
    "system-images;android-24;google_apis;x86_64"
    "system-images;android-26;google_apis;x86_64"
    "system-images;android-28;google_apis;x86_64"
    "system-images;android-30;google_apis;x86_64"
    "system-images;android-33;google_apis;x86_64"
)

echo "This script will download the following system images:"
echo ""
for image in "${IMAGES[@]}"; do
    echo "  • $image"
done
echo ""
echo "⚠️  Warning: Each image is approximately 400-800 MB"
echo "   Total download size: ~2-4 GB"
echo ""

read -p "Continue? (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "❌ Installation cancelled"
    exit 1
fi

echo ""
echo "Starting installation..."
echo ""

for image in "${IMAGES[@]}"; do
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Installing: $image"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    # Check if already installed
    if sdkmanager --list | grep -q "$image.*Installed"; then
        echo "✓ Already installed, skipping..."
    else
        echo "Downloading and installing..."
        yes | sdkmanager "$image"
        
        if [ $? -eq 0 ]; then
            echo "✓ Successfully installed"
        else
            echo "❌ Failed to install $image"
        fi
    fi
    echo ""
done

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║  ✅ System Images Setup Complete!                             ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "You can now run the comprehensive tests:"
echo "  cd $(dirname "$0")"
echo "  ./run-comprehensive-tests.sh"
echo ""
