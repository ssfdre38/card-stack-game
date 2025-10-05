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

### Run Quick Test (Single Device)
```bash
cd /home/ubuntu/match-mania/emulator-testing
./quick-test.sh
```

### Run Comprehensive Tests (All Versions)
```bash
cd /home/ubuntu/match-mania/emulator-testing
./run-comprehensive-tests.sh
```

## Prerequisites

### Required
- Android SDK installed at `/home/ubuntu/android-sdk`
- System images for testing versions
- Compiled APK (auto-built if missing)

### Install System Images
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

### Emulator Won't Start
```bash
# Check if another emulator is running
adb devices

# Kill existing emulators
adb emu kill
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
cd /home/ubuntu/match-mania
./gradlew assembleDebug
```

### Permission Denied
```bash
# Make scripts executable
chmod +x run-comprehensive-tests.sh
chmod +x quick-test.sh
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
cd /home/ubuntu/match-mania/emulator-testing
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
