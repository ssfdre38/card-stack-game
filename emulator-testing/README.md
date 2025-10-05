# Match Mania - Automated Testing System

## Overview

Comprehensive automated testing system for Match Mania that tests the app across multiple Android versions with UI, gameplay, and OTA update testing. Automatically captures screenshots on errors and generates detailed HTML reports.

## Directory Structure

```
emulator-testing/
├── configs/
│   └── test-config.json       # Test configuration
├── screenshots/               # Auto-captured screenshots
├── reports/                   # HTML test reports
├── logs/                      # Test logs
├── run-comprehensive-tests.sh # Main test script
├── quick-test.sh             # Quick single-device test
└── README.md                  # This file
```

## Features

### ✅ Multi-Version Testing
- Tests on Android 7.0 (API 24) - minimum supported
- Tests on Android 8.0 (API 26) - Oreo
- Tests on Android 9.0 (API 28) - Pie
- Tests on Android 11 (API 30) - Current
- Tests on Android 13 (API 33) - Latest

### ✅ Comprehensive Test Categories

**UI Tests:**
- Launch app verification
- All UI elements check (14 elements)
- Portrait layout verification
- Landscape layout verification
- Screen rotation testing
- Button click testing
- Card interaction testing

**Gameplay Tests:**
- Draw card functionality
- Play card functionality
- New game functionality
- Game state persistence
- AI turn processing

**OTA Tests:**
- Check for updates functionality
- Version display verification
- Update notification system
- Settings access

**Performance Tests:**
- App startup time
- Memory usage monitoring
- Crash detection

### ✅ Automatic Screenshot Capture
- Screenshots on every error
- Screenshots at test completion
- Screenshots for key actions
- Organized by test session

### ✅ Detailed Reporting
- HTML reports with summary statistics
- Pass/fail rates
- Screenshot gallery
- Complete test logs
- Timestamp tracking

## Quick Start

### 🚀 Easiest Way (Fully Automated)
```bash
cd emulator-testing

# If Android SDK is not installed, the scripts will offer to install it automatically!
./setup-system-images.sh
./run-comprehensive-tests.sh
```

**That's it!** The scripts will:
1. Detect if Android SDK is installed
2. Offer to install it automatically if missing
3. Install required system images
4. Run all tests

### Manual SDK Installation (Optional)
If you prefer to install the SDK manually first:
```bash
cd emulator-testing
./install-android-sdk.sh
```

### Run Quick Test (Single Device)
```bash
cd emulator-testing
./quick-test.sh
```

### Run Comprehensive Tests (All Versions)
```bash
cd emulator-testing
./run-comprehensive-tests.sh
```

## Prerequisites

### Platform Support
✅ **Linux** - Tested on Ubuntu, Debian, Fedora, Arch  
✅ **macOS** - Tested on macOS 10.15+  
✅ **Windows** - Works via WSL2 or Git Bash  

### Automatic Installation Available! 🎉
**No manual setup required!** The testing scripts can automatically install everything needed:
- ✅ **Android SDK** - Automatically installed if not found
- ✅ **System images** - Installed via `setup-system-images.sh`
- ✅ **Essential tools** - Platform-tools, build-tools, emulator

### System Requirements
The scripts will check for and guide you to install:
- `curl` or `wget` (for downloading)
- `unzip` (for extracting files)
- `java` (JDK 8 or higher)

**Automatic Installation on Linux!**
The install script automatically detects your package manager and offers to install missing tools:
- ✅ **apt** (Ubuntu, Debian, Linux Mint)
- ✅ **dnf** (Fedora, RHEL 8+, CentOS Stream)
- ✅ **yum** (CentOS 7, RHEL 7)
- ✅ **pacman** (Arch, Manjaro, EndeavourOS)
- ✅ **zypper** (openSUSE, SUSE Linux Enterprise)

Just press 'Y' when prompted and the script installs everything automatically!

**Manual installation instructions provided for:**
- macOS: `brew install`
- Windows: Download links provided

### Setting Up Android SDK

#### Option 1: Automatic Installation (Recommended) 🎉

The easiest way! Run the installer script:
```bash
cd emulator-testing
./install-android-sdk.sh
```

**What it does:**
- ✅ Detects your platform (Linux/macOS/Windows)
- ✅ Downloads Android SDK command-line tools (~150 MB)
- ✅ Installs to the correct location for your OS
- ✅ Installs essential components (platform-tools, build-tools, emulator)
- ✅ Adds environment variables to your shell profile
- ✅ Accepts licenses automatically

**Or even easier:** Just run `./setup-system-images.sh` or `./run-comprehensive-tests.sh` and they will offer to install the SDK if it's missing!

#### Option 2: Manual Installation

If Android SDK is not auto-detected, set the environment variable:

**Linux/macOS:**
```bash
export ANDROID_SDK_ROOT=/path/to/android/sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
```

**Windows (Git Bash/WSL):**
```bash
export ANDROID_SDK_ROOT="$LOCALAPPDATA/Android/Sdk"
export ANDROID_HOME=$ANDROID_SDK_ROOT
```

Or add to your shell profile (`~/.bashrc`, `~/.zshrc`, etc.):
```bash
echo 'export ANDROID_SDK_ROOT=~/Android/Sdk' >> ~/.bashrc
echo 'export ANDROID_HOME=$ANDROID_SDK_ROOT' >> ~/.bashrc
source ~/.bashrc
```

**Download manually:**
- Android Studio: https://developer.android.com/studio
- Command-line tools only: https://developer.android.com/studio#command-tools

### Install System Images

Use the provided setup script (works on all platforms):
```bash
cd emulator-testing
./setup-system-images.sh
```

Or install manually:
```bash
sdkmanager "system-images;android-24;google_apis;x86_64"
sdkmanager "system-images;android-26;google_apis;x86_64"
sdkmanager "system-images;android-28;google_apis;x86_64"
sdkmanager "system-images;android-30;google_apis;x86_64"
sdkmanager "system-images;android-33;google_apis;x86_64"
```

## Configuration

Edit `configs/test-config.json` to customize:

### Enable/Disable Android Versions
```json
{
  "api_level": 30,
  "version": "11",
  "name": "R",
  "enabled": true    // Set to false to skip
}
```

### Enable/Disable Test Categories
```json
{
  "ui_tests": { "enabled": true },
  "gameplay_tests": { "enabled": true },
  "ota_tests": { "enabled": true },
  "performance_tests": { "enabled": true }
}
```

### Screenshot Settings
```json
{
  "screenshot_on": {
    "error": true,          // Screenshot on errors
    "test_start": false,    // Screenshot at test start
    "test_end": true,       // Screenshot at test end
    "key_actions": true     // Screenshot for key actions
  }
}
```

### Timeout Settings
```json
{
  "timeout_settings": {
    "emulator_boot": 120,   // Seconds to wait for emulator
    "app_launch": 30,       // Seconds to wait for app
    "ui_action": 10,        // Seconds for UI actions
    "test_timeout": 300     // Total test timeout
  }
}
```

## Test Results

### Reports
HTML reports are generated in `reports/` directory:
- Summary statistics (total/passed/failed/errors)
- Pass rate percentage
- Screenshot gallery with captions
- Complete test log

### Screenshots
All screenshots saved to `screenshots/` directory with naming:
```
{session_id}_{test_name}_{reason}_{timestamp}.png
```

Example:
```
test_20251005_073000_ui_elements_error_20251005_073045.png
```

### Logs
Detailed logs saved to `logs/` directory:
- Main test log: `{session_id}.log`
- Emulator logs: `{session_id}_emulator_{avd_name}.log`
- UI dumps: `ui_dump_{session_id}.xml`

## Test Execution Flow

1. **Initialize**
   - Create test session ID
   - Setup logging
   - Build APK if needed

2. **For Each Android Version**
   - Create/recreate AVD
   - Start emulator
   - Wait for boot completion
   - Install app
   - Run test suite
   - Stop emulator

3. **Test Suite Per Version**
   - UI element tests
   - Rotation tests
   - Gameplay tests
   - OTA tests
   - Performance tests

4. **Cleanup & Report**
   - Stop all emulators
   - Generate HTML report
   - Archive logs and screenshots

## Common Issues

### Android SDK Not Found
**Error:** "Android SDK not found!"

**Solution:**
1. Install Android Studio or SDK command-line tools
2. Set environment variable:
   ```bash
   # Linux/macOS
   export ANDROID_SDK_ROOT=~/Android/Sdk
   
   # Windows (Git Bash)
   export ANDROID_SDK_ROOT="$LOCALAPPDATA/Android/Sdk"
   ```
3. Add to shell profile for persistence

### Emulator Won't Start
```bash
# Check if another emulator is running
adb devices

# Kill existing emulators
adb emu kill

# On Linux: Check KVM support
ls -l /dev/kvm
# If missing, enable virtualization in BIOS

# On macOS: Check HAXM
kextstat | grep intel
```

### System Image Missing
```bash
# List available images
sdkmanager --list | grep system-images

# Install needed image
sdkmanager "system-images;android-XX;google_apis;x86_64"
```

### APK Not Found
```bash
# Build APK manually
cd ..  # Go to project root
./gradlew assembleDebug
```

### Permission Denied
```bash
# Make scripts executable
chmod +x *.sh
```

### Windows-Specific Issues

**Git Bash Path Issues:**
```bash
# Use forward slashes, not backslashes
export ANDROID_SDK_ROOT="C:/Android/Sdk"
```

**WSL2 Issues:**
- Use WSL2, not WSL1 for better emulator support
- Ensure Android SDK is installed in WSL filesystem, not Windows
- Or mount Windows SDK: `/mnt/c/Users/YourName/AppData/Local/Android/Sdk`

### macOS-Specific Issues

**HAXM Not Installed:**
```bash
# Install Intel HAXM for faster emulation
brew install --cask intel-haxm
```

**Permission Issues:**
```bash
# Grant Terminal full disk access in System Preferences
# System Preferences > Security & Privacy > Privacy > Full Disk Access
```

### Linux-Specific Issues

**KVM Not Available:**
```bash
# Check KVM support
egrep -c '(vmx|svm)' /proc/cpuinfo  # Should be > 0

# Install KVM
sudo apt install qemu-kvm libvirt-daemon-system  # Ubuntu/Debian
sudo dnf install @virtualization                   # Fedora
sudo pacman -S qemu libvirt                       # Arch

# Add user to kvm group
sudo usermod -aG kvm $USER
# Log out and back in
```

## Advanced Usage

### Test Specific Version Only
Edit script and comment out unwanted versions:
```bash
# run_test_suite 24 "7.0" || true  # Skip API 24
run_test_suite 30 "11" || true     # Only run API 30
```

### Custom Device Configs
Modify AVD creation in script:
```bash
create_avd() {
    # Add custom device config
    echo "hw.lcd.density=440" >> ~/.android/avd/${avd_name}.avd/config.ini
    echo "hw.lcd.width=1080" >> ~/.android/avd/${avd_name}.avd/config.ini
    echo "hw.lcd.height=2280" >> ~/.android/avd/${avd_name}.avd/config.ini
}
```

### Integrate with CI/CD
```bash
#!/bin/bash
# Run tests and exit with test result code
cd emulator-testing
./run-comprehensive-tests.sh

# Check results and exit accordingly
if [ $FAILED_TESTS -gt 0 ]; then
    exit 1
else
    exit 0
fi
```

## Maintenance

### Clean Old Test Data
```bash
# Remove old screenshots (keep last 7 days)
find screenshots/ -name "*.png" -mtime +7 -delete

# Remove old reports (keep last 7 days)
find reports/ -name "*.html" -mtime +7 -delete

# Remove old logs (keep last 7 days)
find logs/ -name "*.log" -mtime +7 -delete
```

### Update Test Cases
Edit the test functions in `run-comprehensive-tests.sh`:
- `test_ui_elements()` - Add/modify UI checks
- `test_gameplay()` - Add/modify gameplay tests
- `test_ota()` - Add/modify OTA tests
- `test_performance()` - Add/modify performance tests

## Best Practices

1. **Run Before Releases**: Always run comprehensive tests before releasing
2. **Check Screenshots**: Review error screenshots to diagnose issues
3. **Monitor Logs**: Check logs for warnings and errors
4. **Update Configs**: Keep test-config.json updated with new features
5. **Archive Results**: Save test reports for version history

## Support

For issues or questions:
1. Check logs in `logs/` directory
2. Review screenshots in `screenshots/` directory
3. Check emulator logs for system-level issues
4. Verify Android SDK installation

## Version History

- **v1.0** (2025-10-05): Initial release
  - Multi-version testing support
  - Automated screenshot capture
  - HTML report generation
  - UI, gameplay, OTA, and performance tests

---

© 2025 Match Mania Testing System

---

## Copyright and Attribution

### Match Mania Testing System

**Copyright:** © 2025 Daniel Elliott  
**License:** MIT License  
**Repository:** https://github.com/ssfdre38/match-mania

### Third-Party Software

This testing system uses the following third-party software and tools:

#### Android SDK Tools
- **Copyright:** © Google LLC
- **License:** Android Software Development Kit License Agreement
- **Components:** Android Emulator, adb, avdmanager, sdkmanager, System Images
- **Website:** https://developer.android.com/studio
- **License Terms:** https://developer.android.com/studio/terms

#### Android Open Source Project
- **Copyright:** © Google LLC and The Android Open Source Project
- **License:** Apache License 2.0
- **Components:** Android OS System Images (API 24, 26, 28, 30, 33)
- **Website:** https://source.android.com

#### Other Tools
- **Bash Shell:** © Free Software Foundation (GNU GPL v3)
- **Python:** © Python Software Foundation (PSF License)
- **GNU Utilities:** © Free Software Foundation (GNU GPL)

### Trademark Notice

Android is a trademark of Google LLC. All trademarks and registered trademarks are the property of their respective owners.

### Disclaimer

This testing system is independent software created for testing the Match Mania application. It is not affiliated with, endorsed by, or sponsored by Google LLC or The Android Open Source Project.

The testing system uses publicly available Android SDK tools in accordance with their respective licenses for the purpose of testing and quality assurance.

### Full Copyright Information

For complete copyright and attribution details, see [COPYRIGHT.md](COPYRIGHT.md)

---

**Last Updated:** October 5, 2025  
© 2025 Daniel Elliott. All rights reserved.
