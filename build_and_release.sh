#!/bin/bash

# Match Mania - Build and Release Script
# This script automates the build and release process

set -e  # Exit on error

echo "🎮 Match Mania - Build and Release Script"
echo "=========================================="
echo ""

# Check if version is provided
if [ -z "$1" ]; then
    echo "Usage: ./build_and_release.sh <version>"
    echo "Example: ./build_and_release.sh 2.0.2"
    exit 1
fi

VERSION=$1
echo "📦 Building version: $VERSION"
echo ""

# Set Android SDK path
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin

# Clean and build
echo "🧹 Cleaning previous builds..."
./gradlew clean

echo ""
echo "🔨 Building APKs..."
./gradlew assembleDebug assembleRelease

# Check if build was successful
if [ ! -f "app/build/outputs/apk/release/app-release.apk" ]; then
    echo "❌ Build failed! Release APK not found."
    exit 1
fi

if [ ! -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "❌ Build failed! Debug APK not found."
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""

# Display APK info
echo "📊 APK Information:"
echo "-------------------"
echo "Release APK:"
ls -lh app/build/outputs/apk/release/app-release.apk
echo ""
echo "Debug APK:"
ls -lh app/build/outputs/apk/debug/app-debug.apk
echo ""

# Verify signatures
echo "🔐 Verifying signatures..."
echo ""
echo "Release APK Signature:"
$ANDROID_HOME/build-tools/33.0.1/apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk | grep "Signer #1 certificate DN"
echo ""
echo "Debug APK Signature:"
$ANDROID_HOME/build-tools/33.0.1/apksigner verify --print-certs app/build/outputs/apk/debug/app-debug.apk | grep "Signer #1 certificate DN"
echo ""

# Ask if user wants to create a release
read -p "🚀 Do you want to create a GitHub release v$VERSION? (y/n) " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    read -p "📝 Enter release title: " TITLE
    echo ""
    echo "Creating release v$VERSION..."
    
    gh release create "v$VERSION" \
        --title "$TITLE" \
        --notes "## Match Mania v$VERSION

### Downloads
- **MatchMania-release-v$VERSION.apk** - Production-ready signed APK
- **MatchMania-debug-v$VERSION.apk** - Debug version for testing

### Installation Note
If you had a previous version installed with a different signature, you may need to uninstall it first.

---
© 2025 Daniel Elliott. All rights reserved." \
        "app/build/outputs/apk/release/app-release.apk#MatchMania-release-v$VERSION.apk" \
        "app/build/outputs/apk/debug/app-debug.apk#MatchMania-debug-v$VERSION.apk"
    
    echo ""
    echo "✅ Release created successfully!"
    echo "🔗 View release: https://github.com/ssfdre38/card-stack-game/releases/tag/v$VERSION"
else
    echo "⏭️  Skipping release creation."
    echo ""
    echo "To create a release manually later, run:"
    echo "gh release create v$VERSION \\"
    echo "  --title \"Your Title\" \\"
    echo "  --notes \"Your release notes\" \\"
    echo "  app/build/outputs/apk/release/app-release.apk#MatchMania-release-v$VERSION.apk \\"
    echo "  app/build/outputs/apk/debug/app-debug.apk#MatchMania-debug-v$VERSION.apk"
fi

echo ""
echo "🎉 Done!"
