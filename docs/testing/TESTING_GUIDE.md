# Match Mania - Testing Guide

This guide explains how to test the Match Mania APKs on your Android device or emulator.

## Testing on a Physical Android Device

### Prerequisites
- Android device with Android 7.0 (API 24) or higher
- USB cable for ADB testing (optional)
- Internet connection to download APKs

### Method 1: Direct Installation (Easiest)

1. **Download the APK on your device:**
   - Go to https://github.com/ssfdre38/card-stack-game/releases/tag/v2.0.1
   - Download `MatchMania-release-v2.0.1.apk` to your device

2. **Enable Unknown Sources:**
   - **Android 8.0+**: Settings > Apps > Special Access > Install Unknown Apps > [Your Browser] > Allow
   - **Android 7.x**: Settings > Security > Unknown Sources > Toggle ON

3. **Install:**
   - Open your Downloads folder
   - Tap the APK file
   - Tap "Install"
   - If you get "App not installed", uninstall any previous version first

### Method 2: Using ADB (Developer Method)

1. **Enable Developer Options on your device:**
   - Settings > About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings > Developer Options
   - Enable "USB Debugging"

2. **Connect your device to computer:**
   ```bash
   # Check if device is connected
   adb devices
   ```

3. **Install the APK:**
   ```bash
   cd /home/ubuntu/card-stack-game
   
   # Install release version
   adb install -r app/build/outputs/apk/release/app-release.apk
   
   # Or install debug version
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

4. **View logs while testing:**
   ```bash
   # View all logs
   adb logcat
   
   # Filter for Match Mania logs only
   adb logcat | grep MatchMania
   
   # Clear logs first, then view
   adb logcat -c && adb logcat | grep -i match
   ```

5. **Uninstall if needed:**
   ```bash
   adb uninstall com.matchmania.game
   ```

## Testing on Android Emulator

### Setting Up Android Emulator

1. **Install Android Emulator (if not already installed):**
   ```bash
   export ANDROID_HOME=$HOME/Android/Sdk
   
   # Install emulator
   $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "emulator"
   
   # Install system image (Android 11)
   $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "system-images;android-30;google_apis;x86_64"
   
   # Create AVD
   $ANDROID_HOME/cmdline-tools/latest/bin/avdmanager create avd \
     -n MatchMania_Test \
     -k "system-images;android-30;google_apis;x86_64" \
     -d "pixel_4"
   ```

2. **Start the emulator:**
   ```bash
   $ANDROID_HOME/emulator/emulator -avd MatchMania_Test &
   
   # Wait for emulator to boot (30-60 seconds)
   adb wait-for-device
   ```

3. **Install the APK:**
   ```bash
   cd /home/ubuntu/card-stack-game
   adb install -r app/build/outputs/apk/release/app-release.apk
   ```

4. **Launch the app:**
   ```bash
   adb shell am start -n com.matchmania.game/com.cardstack.game.MainActivity
   ```

## What to Test

### Basic Functionality
- [ ] App launches without crashing
- [ ] Main menu displays correctly
- [ ] New Game button starts a game
- [ ] Cards are dealt correctly (7 cards per player)

### Gameplay Testing
- [ ] Can play valid cards (matching color or number)
- [ ] Cannot play invalid cards
- [ ] AI players make valid moves
- [ ] Draw card button works
- [ ] Special cards work correctly:
  - [ ] Skip (S)
  - [ ] Reverse (R)
  - [ ] Draw Two (+2)
  - [ ] Wild (W)
  - [ ] Wild Draw Four (W+4)

### UI Testing
- [ ] Screen rotation works correctly
- [ ] All buttons are visible and functional
- [ ] Cards display properly without overflow
- [ ] Colors are distinct and readable

### Profile Customization
- [ ] Can change player name
- [ ] Can select different avatars
- [ ] Changes persist after closing app

### Statistics
- [ ] Stats update after each game
- [ ] Win/loss counts are accurate
- [ ] Match history displays correctly
- [ ] Reset stats works

### Settings
- [ ] Can toggle all rule options
- [ ] Settings persist after restart
- [ ] Custom rules affect gameplay correctly

### About Page
- [ ] Displays correct version number
- [ ] Copyright information is correct
- [ ] License information is present

## Performance Testing

### Memory Usage
```bash
# Monitor memory usage
adb shell dumpsys meminfo com.matchmania.game

# Check for memory leaks
# Play several games and check if memory keeps increasing
```

### CPU Usage
```bash
# Monitor CPU usage
adb shell top | grep matchmania
```

### Battery Impact
```bash
# Check battery usage after playing for 15-30 minutes
adb shell dumpsys batterystats | grep matchmania
```

## Debugging

### View Crash Logs
```bash
# View recent crashes
adb logcat -b crash

# Save logs to file
adb logcat > matchmania_test_log.txt
```

### Check APK Information
```bash
# Verify package info
adb shell dumpsys package com.matchmania.game

# Check permissions
adb shell dumpsys package com.matchmania.game | grep permission
```

### Take Screenshots
```bash
# Take screenshot
adb shell screencap -p /sdcard/screenshot.png

# Pull screenshot to computer
adb pull /sdcard/screenshot.png ./
```

### Record Video
```bash
# Record video (max 180 seconds)
adb shell screenrecord /sdcard/demo.mp4

# Pull video to computer
adb pull /sdcard/demo.mp4 ./
```

## Known Issues

### Installation Issue: "App not installed"
**Cause:** Previous version with different signature is installed.
**Solution:** 
```bash
# Uninstall old version
adb uninstall com.matchmania.game

# Then install new version
adb install -r app/build/outputs/apk/release/app-release.apk
```

### Emulator Performance
**Issue:** Emulator runs slowly
**Solution:** 
- Enable hardware acceleration
- Allocate more RAM to emulator
- Use x86_64 system images (faster than ARM)

## Reporting Issues

When reporting issues, please include:

1. **Device Information:**
   - Android version
   - Device model
   - Screen size/resolution

2. **Steps to Reproduce:**
   - What you were doing
   - What you expected to happen
   - What actually happened

3. **Logs:**
   - Relevant logcat output
   - Screenshots if applicable

4. **APK Version:**
   - Which APK (debug or release)
   - Version number (check About page)

## Automated Testing Script

Create a quick test script:

```bash
#!/bin/bash
# quick_test.sh

echo "🧪 Starting Match Mania Quick Test..."

# Check if device is connected
if ! adb devices | grep -q "device$"; then
    echo "❌ No device connected!"
    exit 1
fi

echo "✓ Device connected"

# Uninstall old version
echo "🗑️  Uninstalling old version..."
adb uninstall com.matchmania.game 2>/dev/null

# Install new version
echo "📦 Installing APK..."
if adb install -r app/build/outputs/apk/release/app-release.apk; then
    echo "✓ APK installed successfully"
else
    echo "❌ Installation failed!"
    exit 1
fi

# Launch app
echo "🚀 Launching app..."
adb shell am start -n com.matchmania.game/com.cardstack.game.MainActivity

echo "✅ Test complete! App should be running on device."
echo "📝 Monitor logs with: adb logcat | grep -i match"
```

Save this as `quick_test.sh`, make it executable (`chmod +x quick_test.sh`), and run it before each test session.

---

**Happy Testing! 🎮**

If you find any issues or have suggestions, please open an issue on GitHub.
