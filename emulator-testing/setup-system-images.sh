#!/bin/bash

################################################################################
# Match Mania - Setup System Images
# Downloads required Android system images for testing
################################################################################

export ANDROID_SDK_ROOT=/home/ubuntu/android-sdk
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║        Match Mania - System Images Setup                      ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
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
