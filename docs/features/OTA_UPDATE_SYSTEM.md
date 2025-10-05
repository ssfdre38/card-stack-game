# Match Mania - OTA Update System

## Overview
Match Mania includes an Over-The-Air (OTA) update system that automatically checks GitHub releases for new versions and notifies users when updates are available.

## Features

### ✅ Automatic Update Checking
- Checks for updates automatically on app launch
- Checks once every 24 hours (not every launch)
- Runs in background, doesn't block UI
- No user action required

### ✅ Manual Update Check
- "Check for Updates" button in Settings
- Forces immediate check regardless of last check time
- Shows "You're on the latest version" message if up-to-date

### ✅ Smart Notifications
- Only notifies when newer version is available
- Compares version codes (not just names)
- Respects user preferences (skip version option)

### ✅ User-Friendly Dialog
- Shows current and new version numbers
- Displays release notes and title
- Multiple action options:
  - **Download**: Direct download link to APK
  - **Details**: View full release notes on GitHub
  - **Skip**: Skip this version (won't notify again)
  - **Cancel**: Dismiss (will check again next time)

## Technical Details

### Architecture

**UpdateChecker.java**:
- Main OTA update class
- Handles GitHub API communication
- Manages update preferences
- Shows update dialogs

**Key Components**:
```java
UpdateChecker(Context) - Normal constructor (24hr interval)
UpdateChecker(Context, boolean) - Force check constructor
checkForUpdates() - Main entry point
```

### GitHub Integration

**API Endpoint**:
```
https://api.github.com/repos/ssfdre38/match-mania/releases/latest
```

**Response Parsing**:
- Tag name (e.g., "v2.2.7")
- Version name (e.g., "2.2.7")
- Release title
- Release notes (body)
- Asset download URLs
- HTML URL for release page

### Version Code Mapping

The system maps version names to version codes:

| Version | Version Code |
|---------|--------------|
| 2.2.0   | 5            |
| 2.2.1   | 10           |
| 2.2.2   | 12           |
| 2.2.3   | 13           |
| 2.2.4   | 14           |
| 2.2.5   | 15           |
| 2.2.6   | 16           |
| 2.2.7   | 17           |
| 2.2.8+  | 18+          |

**Formula**: For 2.2.x where x ≥ 2: `versionCode = 12 + (x - 2)`

### Permissions

Required permissions in AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Storage

**SharedPreferences** (`UpdatePrefs`):
- `last_check_time`: Timestamp of last update check
- `skip_version`: Tag name of skipped version

### Network

- Connection timeout: 10 seconds
- Read timeout: 10 seconds
- HTTP method: GET
- Accept header: application/vnd.github.v3+json

## User Experience

### First Launch
1. App starts
2. Background check initiated
3. If update available: Dialog appears
4. User chooses action

### Subsequent Launches
1. App starts
2. If < 24 hours since last check: No action
3. If ≥ 24 hours: Background check initiated
4. If update available and not skipped: Dialog appears

### Manual Check (Settings)
1. User taps "Check for Updates"
2. Toast: "Checking for updates..."
3. Immediate background check
4. Result shown:
   - Update available: Dialog appears
   - Up-to-date: Toast message
   - Error: Error toast

### Update Dialog Options

**Download Button**:
- Opens browser to APK download URL
- User downloads and installs manually
- Standard Android install flow

**Details Button**:
- Opens browser to GitHub release page
- Shows full release notes
- Shows all assets

**Skip Button**:
- Saves version to preferences
- Won't notify for this version again
- Can be cleared by manual check in Settings

**Cancel/Back**:
- Dismisses dialog
- Will check again on next launch (if interval passed)

## Integration Points

### MainActivity.java
```java
protected void onCreate(Bundle savedInstanceState) {
    // ... setup code ...
    
    // Check for updates on app start
    new UpdateChecker(this).checkForUpdates();
}
```

### SettingsActivity.java
```java
checkUpdateButton.setOnClickListener(v -> {
    Toast.makeText(this, "Checking for updates...", Toast.LENGTH_SHORT).show();
    UpdateChecker checker = new UpdateChecker(this, true);
    checker.clearSkippedVersion();
    checker.checkForUpdates();
});
```

## Release Requirements

For OTA to work properly, GitHub releases must:

1. ✅ Use semantic versioning (e.g., v2.2.7)
2. ✅ Include release APK as asset
3. ✅ Name APK with "release" in filename
4. ✅ Include release notes in body
5. ✅ Mark as "Latest Release" on GitHub

**Example APK naming**:
- ✅ `MatchMania-release-v2.2.7.apk`
- ✅ `match-mania-release.apk`
- ❌ `MatchMania-debug-v2.2.7.apk`
- ❌ `app.apk`

## Error Handling

### Network Errors
- Silently fails (no annoying error messages)
- Logs error to LogCat
- Updates last check time (prevents repeated failures)
- Manual check shows error toast

### Parsing Errors
- Logs error details
- Returns null (no update available)
- Doesn't crash app

### GitHub API Rate Limits
- Unauthenticated: 60 requests/hour per IP
- Should never hit limit (checks once/24hrs)
- Manual checks count toward limit

## Privacy

- ✅ No user data collected
- ✅ No analytics or tracking
- ✅ Only contacts GitHub (public API)
- ✅ No personal information sent
- ✅ All data stored locally (SharedPreferences)

## Testing

### Test Scenarios

1. **New Install**:
   - Install older version
   - Launch app
   - Should see update dialog

2. **Up-to-Date**:
   - Install latest version
   - Manual check in Settings
   - Should see "latest version" message

3. **Skip Version**:
   - See update dialog
   - Click "Skip"
   - Restart app
   - Should not see dialog again
   - Manual check should show dialog

4. **Network Failure**:
   - Turn off internet
   - Launch app
   - Should not show error
   - Turn on internet
   - Manual check should work

5. **24-Hour Interval**:
   - Launch app (check happens)
   - Close and relaunch immediately
   - Should not check again
   - Wait 24+ hours
   - Launch app
   - Should check again

### Debug Commands

**Clear Preferences** (adb):
```bash
adb shell pm clear com.matchmania.game
```

**Check Last Update Time**:
```bash
adb shell run-as com.matchmania.game cat /data/data/com.matchmania.game/shared_prefs/UpdatePrefs.xml
```

## Future Enhancements

Possible improvements:

1. ⭐ **In-App Updates**: Use Android's In-App Update API
2. ⭐ **Auto-Download**: Download APK in background
3. ⭐ **Delta Updates**: Only download changed files
4. ⭐ **Beta Channel**: Opt-in to beta releases
5. ⭐ **Update Notifications**: Push notifications for critical updates
6. ⭐ **Changelog Formatting**: Parse markdown in release notes
7. ⭐ **Version Comparison**: More robust version code extraction
8. ⭐ **Update Statistics**: Track update adoption rates

## Troubleshooting

### Updates Not Showing

**Problem**: App says up-to-date but new version exists

**Solutions**:
1. Check version code mapping in UpdateChecker
2. Verify GitHub release is "Latest Release"
3. Check APK filename contains "release"
4. Verify internet connection
5. Check GitHub API status

### Dialog Not Appearing

**Problem**: Update available but no dialog

**Solutions**:
1. Check if version was skipped
2. Clear app data (Settings > Apps > Match Mania > Clear Data)
3. Wait 24 hours from last check
4. Use manual check in Settings
5. Check LogCat for errors

### Download Button Not Working

**Problem**: Download button does nothing

**Solutions**:
1. Verify APK asset exists in release
2. Check asset naming convention
3. Verify browser app installed
4. Check storage permissions
5. Try "Details" button instead

## Support

For issues with OTA updates:

1. Check GitHub Issues: https://github.com/ssfdre38/match-mania/issues
2. View Latest Release: https://github.com/ssfdre38/match-mania/releases/latest
3. Manual Download: Always available on GitHub Releases page

## Summary

The OTA update system provides a seamless way to keep Match Mania up-to-date. It balances user convenience with respect for bandwidth and preferences, while maintaining security and privacy. The system is fully automated but gives users complete control through manual checks and skip options.

**Result**: Users always have the latest features and bug fixes! 🚀📱✨

---

**Created**: January 4, 2025  
**Version**: 1.0  
**Status**: ✅ Implemented

Copyright © 2025 Daniel Elliott  
Licensed under Apache License 2.0
