# Automated Testing System - Match Mania

## Overview

Complete automated testing system for Match Mania that tests across multiple Android versions with comprehensive UI, gameplay, and OTA testing. Automatically captures screenshots on errors and generates detailed HTML reports.

## Location

```
/home/ubuntu/match-mania/emulator-testing/
```

## Quick Start

### 1. One-Time Setup (Install System Images)
```bash
cd /home/ubuntu/match-mania/emulator-testing
./setup-system-images.sh
```
This downloads Android system images for API 24, 26, 28, 30, and 33.

### 2. Run Tests
```bash
./run-comprehensive-tests.sh
```

### 3. View Results
- HTML Report: `reports/test_YYYYMMDD_HHMMSS_report.html`
- Screenshots: `screenshots/`
- Logs: `logs/`

## What Gets Tested

### Multi-Version Support
- ✅ Android 7.0 (API 24) - Nougat
- ✅ Android 8.0 (API 26) - Oreo
- ✅ Android 9.0 (API 28) - Pie
- ✅ Android 11 (API 30) - Current
- ✅ Android 13 (API 33) - Tiramisu

### Test Categories

**UI Tests (7 tests):**
- App launch verification
- All UI elements check (14 elements)
- Portrait layout verification
- Landscape layout verification  
- Screen rotation testing
- Button interaction testing
- Card interaction testing

**Gameplay Tests (5 tests):**
- Draw card functionality
- Play card functionality
- New game functionality
- Game state persistence
- AI turn processing

**OTA Tests (4 tests):**
- Settings accessibility
- Check for updates functionality
- Version display verification
- Update notification system

**Performance Tests (3 tests):**
- App startup time monitoring
- Memory usage tracking
- Crash detection

## Automatic Screenshot Capture

Screenshots are automatically taken:
- ✅ On every error (mandatory)
- ✅ At test completion
- ✅ For key actions
- ✅ For each test phase

Screenshots are saved with descriptive names:
```
{session_id}_{test_name}_{reason}_{timestamp}.png
```

Example:
```
test_20251005_143000_ui_elements_error_20251005_143045.png
```

## HTML Report Features

Generated reports include:
- 📊 Summary statistics (total/passed/failed/errors)
- 📈 Pass rate percentage
- 📸 Screenshot gallery with captions
- 📝 Complete test log with timestamps
- 🎨 Clean, professional HTML design

## Files Created

```
emulator-testing/
├── configs/
│   └── test-config.json          # Test configuration
├── screenshots/                  # Auto-captured screenshots
├── reports/                      # HTML test reports
├── logs/                         # Test execution logs
├── run-comprehensive-tests.sh    # Main test script (executable)
├── quick-test.sh                 # Quick test script (executable)
├── setup-system-images.sh        # Setup helper (executable)
└── README.md                     # Full documentation
```

## Configuration

Edit `configs/test-config.json` to customize:

### Enable/Disable Versions
```json
{
  "api_level": 30,
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
    "error": true,        // Screenshot on errors
    "test_end": true,     // Screenshot at test end
    "key_actions": true   // Screenshot for key actions
  }
}
```

## Test Execution Flow

1. **Initialize**: Create session ID, setup logging
2. **Build**: Compile APK if not present
3. **For Each Android Version**:
   - Create/configure AVD
   - Start emulator
   - Wait for boot (120s timeout)
   - Install app
   - Launch app
   - Run test suite (UI, gameplay, OTA, performance)
   - Capture screenshots
   - Stop emulator
4. **Finalize**: Generate HTML report with all results

## Error Handling

When errors occur:
1. Error is logged with details
2. Screenshot automatically captured
3. Error location tracked (line number)
4. Test continues with next test
5. All errors included in final report

## Integration with Development

### Before Release Checklist
```bash
# 1. Build latest code
cd /home/ubuntu/match-mania
./gradlew assembleDebug

# 2. Run comprehensive tests
cd emulator-testing
./run-comprehensive-tests.sh

# 3. Review results
# Open HTML report and check for failures

# 4. Fix any issues and re-test

# 5. Proceed with release
```

### CI/CD Integration Example
```bash
#!/bin/bash
# ci-test.sh

cd /home/ubuntu/match-mania/emulator-testing
./run-comprehensive-tests.sh

# Exit with failure if any tests failed
if [ $FAILED_TESTS -gt 0 ]; then
    echo "❌ Tests failed: $FAILED_TESTS"
    exit 1
fi

echo "✅ All tests passed"
exit 0
```

## Maintenance

### Clean Old Test Data
```bash
# Remove old screenshots (>7 days)
find screenshots/ -name "*.png" -mtime +7 -delete

# Remove old reports (>7 days)
find reports/ -name "*.html" -mtime +7 -delete

# Remove old logs (>7 days)
find logs/ -name "*.log" -mtime +7 -delete
```

### Update Test Cases

Edit `run-comprehensive-tests.sh` functions:
- `test_ui_elements()` - Modify UI checks
- `test_gameplay()` - Modify gameplay tests
- `test_ota()` - Modify OTA tests
- `test_performance()` - Modify performance tests

## Troubleshooting

### Emulator Won't Start
```bash
# Kill existing emulators
adb devices | grep emulator | cut -f1 | xargs -I {} adb -s {} emu kill

# Check processes
ps aux | grep emulator

# Try again
./run-comprehensive-tests.sh
```

### System Image Missing
```bash
# List available images
sdkmanager --list | grep system-images

# Install specific image
sdkmanager "system-images;android-30;google_apis;x86_64"

# Or use setup script
./setup-system-images.sh
```

### Screenshots Not Capturing
```bash
# Verify adb works
adb devices

# Test manual screenshot
adb shell screencap -p > test.png

# Check screenshot directory permissions
ls -la screenshots/
```

## Advanced Usage

### Test Specific Version Only
Edit `run-comprehensive-tests.sh` and comment out unwanted versions:
```bash
# In main() function:
# run_test_suite 24 "7.0" || true  # Skip
run_test_suite 30 "11" || true     # Only this
# run_test_suite 33 "13" || true   # Skip
```

### Add Custom Test
```bash
# Add to run-comprehensive-tests.sh

test_my_custom_feature() {
    CURRENT_TEST="my_custom_feature"
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log "Testing my custom feature..."
    
    # Your test logic here
    adb shell input tap 500 500
    sleep 2
    take_screenshot "custom_test" "my_feature"
    
    # Check result
    if [[ condition ]]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        log_success "Custom test passed"
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        log_error "Custom test failed"
        return 1
    fi
}

# Then call it in run_test_suite():
test_my_custom_feature || true
```

## Best Practices

1. **Run Before Every Release**: Catch bugs early
2. **Review All Screenshots**: Visual confirmation of issues
3. **Check Full Logs**: Detailed execution trace
4. **Archive Test Reports**: Track quality over time
5. **Update Test Config**: Keep tests current with features
6. **Regular Maintenance**: Clean old test data
7. **Document Changes**: Update tests when features change

## Example Session Output

```bash
$ ./run-comprehensive-tests.sh

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Match Mania Comprehensive Test Suite
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Session ID: test_20251005_143000
Starting automated tests...

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Testing on Android 11 (API 30)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✓ AVD created successfully
✓ Emulator started successfully
✓ App installed successfully
✓ App launched successfully
✓ All UI elements present
✓ Rotation test passed
✓ Gameplay test passed
✓ OTA update UI found
✓ No crashes detected
✓ Test suite completed for API 30

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Test Suite Complete
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Total Tests: 5
✓ Passed: 5

Report: reports/test_20251005_143000_report.html
Screenshots: screenshots/
Logs: logs/test_20251005_143000.log
```

## Summary

The automated testing system provides:
- ✅ Comprehensive multi-version testing
- ✅ Automatic error detection and screenshot capture
- ✅ Professional HTML reports
- ✅ Easy configuration and customization
- ✅ Suitable for CI/CD integration
- ✅ Complete documentation

Perfect for ensuring Match Mania quality across all supported Android versions!

---

**Created**: October 5, 2025  
**Version**: 1.0  
**Status**: Ready for use

For detailed documentation, see: `emulator-testing/README.md`
