# OTA Update System - Always Auto-Download from Website (v2.3.9)

## Overview

This update configures the OTA update system to **always automatically download** updates from the website, even when users manually check for updates. This provides a seamless update experience where updates are downloaded in the background and presented ready to install.

## Changes Summary

### 1. **UpdateChecker.java - Always Auto-Download**
   - Removed the `!forceCheck` condition that prevented auto-downloads during manual checks
   - Now auto-downloads updates whether detected automatically or manually checked
   - Enhanced validation to ensure download URL is not null AND not empty
   - Improved comments for clarity

### 2. **Website API - Enhanced Format**
   - Updated `/var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json`
   - Added `github_url` field for proper "Details" button navigation
   - Restructured JSON for consistency with app expectations
   - Added additional metadata fields (versionCode, releaseDate, fileSize, etc.)

### 3. **Automation Script**
   - Created `update-website-api.sh` script for easy API updates
   - Automatically reads version from build.gradle
   - Copies APK to downloads directory
   - Updates API JSON with proper formatting
   - Validates JSON syntax and API accessibility

## Detailed Changes

### UpdateChecker.java Modifications

**Location:** `app/src/main/java/com/cardstack/game/UpdateChecker.java`

#### Change 1: Always Auto-Download (Lines 153-159)

**Before:**
```java
// Not downloaded yet - start automatic download if enabled
if (autoDownloadEnabled && !forceCheck && info.downloadUrl != null) {
    downloadInBackground(info);
} else {
    showUpdateDialog(info);
}
```

**After:**
```java
// Not downloaded yet - start automatic download (always from website)
if (autoDownloadEnabled && info.downloadUrl != null && !info.downloadUrl.isEmpty()) {
    // Always auto-download when download URL is available (including manual checks)
    downloadInBackground(info);
} else {
    // No download URL available - show dialog for manual action
    showUpdateDialog(info);
}
```

**Impact:**
- ✅ Auto-downloads work on manual "Check for Updates" clicks
- ✅ More robust validation (checks for empty strings)
- ✅ Better user experience - no manual browser downloads needed
- ✅ Downloads happen silently in background with notification

#### Change 2: Proper GitHub URL Handling (Lines 194-197)

**Before:**
```java
info.url = json.optString("website_url", "https://matchmaina.ssfdre38.xyz");
info.downloadUrl = json.optString("download_url", "");
```

**After:**
```java
info.downloadUrl = json.optString("download_url", "");
// Use GitHub release URL for details page, or website as fallback
String githubUrl = json.optString("github_url", "");
info.url = !githubUrl.isEmpty() ? githubUrl : json.optString("website_url", "https://matchmaina.ssfdre38.xyz");
```

**Impact:**
- ✅ "Details" button opens GitHub release page (if provided)
- ✅ Falls back to website if GitHub URL not in API
- ✅ Users can see full changelog and release notes
- ✅ Maintains backward compatibility

### Website API Format

**Location:** `/var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json`

**Current Format:**
```json
{
  "version": "v2.3.8",
  "name": "Version 2.3.8 - Tablet Display Fix",
  "changelog": "- Fixed tablet card width calculation for better display in landscape mode\n- Cards on tablets now divide screen by 4 instead of 6 for wider cards\n- Improved card display on 7-10 inch tablets\n- Better utilization of available screen space on tablets",
  "download_url": "https://matchmaina.ssfdre38.xyz/downloads/MatchMania-release-v2.3.8.apk",
  "github_url": "https://github.com/ssfdre38/match-mania/releases/tag/v2.3.8",
  "website_url": "https://matchmaina.ssfdre38.xyz",
  "versionCode": 26,
  "releaseDate": "2025-01-04",
  "fileSize": "4.3 MB",
  "minAndroidVersion": "7.0",
  "minApiLevel": 24
}
```

#### Required Fields (for auto-download to work)
- `version`: Version tag (e.g., "v2.3.8")
- `download_url`: Direct APK download URL (MUST be provided for auto-download)

#### Recommended Fields
- `github_url`: GitHub release page URL (for Details button)
- `name`: Release title
- `changelog`: Release notes (use `\n` for line breaks)

#### Optional Metadata Fields
- `website_url`: Fallback URL if github_url not provided
- `versionCode`: Numeric version code
- `releaseDate`: ISO date format (YYYY-MM-DD)
- `fileSize`: Human-readable file size
- `minAndroidVersion`: Minimum Android version name
- `minApiLevel`: Minimum API level number

## Update Workflow

### Automatic Update Check (Every 24 Hours)

```
App Launch (24+ hours since last check)
    ↓
Check Website API
    ↓
Update Available? → NO → Continue normally
    ↓ YES
Download URL Valid? → NO → Show dialog with manual options
    ↓ YES
Start Background Download
    ↓
Show Notification (Download in progress)
    ↓
Download Complete
    ↓
Verify APK File
    ↓
Show "Update Ready to Install!" Dialog
    ↓
User taps "Install Now" → Launch Android Installer
```

### Manual Update Check (Settings)

```
User taps "Check for Updates"
    ↓
Check Website API Immediately
    ↓
Update Available? → NO → Show "You're on the latest version! ✨"
    ↓ YES
Already Downloaded? → YES → Show "Install Now" Dialog
    ↓ NO
Download URL Valid? → NO → Show dialog with manual options
    ↓ YES
Start Background Download (NEW BEHAVIOR!)
    ↓
Show Notification (Download in progress)
    ↓
Download Complete
    ↓
Verify APK File
    ↓
Show "Update Ready to Install!" Dialog
```

### Key Improvement
**Before:** Manual checks showed dialog → User clicks "Install Now" → Download starts with progress bar → Install

**After:** Manual checks → Download starts immediately in background → User sees "Ready to Install!" → Install

## Using the Update Script

### Automatic Update of Website API

```bash
# Navigate to project directory
cd /home/ubuntu/match-mania

# Run the update script
./update-website-api.sh
```

The script will:
1. ✅ Read current version from build.gradle
2. ✅ Find and verify the release APK
3. ✅ Copy APK to website downloads directory
4. ✅ Extract changelog from CHANGELOG.md
5. ✅ Generate properly formatted JSON
6. ✅ Update the API file with correct permissions
7. ✅ Validate JSON syntax
8. ✅ Test API accessibility via HTTPS

### Manual API Update

If you need to manually update the API:

```bash
# Edit the API file
sudo nano /var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json

# Ensure proper permissions
sudo chown www-data:www-data /var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json
sudo chmod 644 /var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json

# Validate JSON
python3 -m json.tool /var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json

# Test accessibility
curl -s https://matchmaina.ssfdre38.xyz/api/latest-version.json
```

## Benefits

### For Users
1. **Seamless Updates**: Downloads happen automatically in background
2. **No Browser Needed**: Never redirects to website for downloads
3. **Works Always**: Auto-download works for both automatic and manual checks
4. **One-Tap Install**: Just tap "Install Now" when ready
5. **Full Changelog**: Details button shows complete release notes on GitHub

### For Developers
1. **Full Control**: Serve updates from your own website
2. **Instant Rollout**: Update API, users get it within 24 hours (or immediately if they check)
3. **Fallback Safety**: GitHub API still works if website down
4. **Easy Updates**: Use the automated script for API updates
5. **Analytics Ready**: Can track API requests on website

### For Distribution
1. **Bandwidth Control**: Host APKs on your infrastructure
2. **Fast Downloads**: Direct from website (no GitHub redirect)
3. **Reliable**: Auto-download with retry logic
4. **Verified**: APK verification before showing install prompt
5. **Transparent**: Users always know what they're installing

## Testing

### Test Scenario 1: Manual Check with Auto-Download
1. Open app → Settings → Check for Updates
2. **Expected:** Download notification appears immediately
3. **Expected:** "Update Ready to Install!" dialog after download
4. **Expected:** Tapping "Install Now" launches Android installer

### Test Scenario 2: Details Button Navigation
1. Trigger update check (manual or automatic)
2. Tap "Details" button in update dialog
3. **Expected:** Opens GitHub release page (not website)
4. **Expected:** Shows complete changelog and download options

### Test Scenario 3: Missing Download URL
1. Remove `download_url` from API JSON
2. Trigger update check
3. **Expected:** Shows standard dialog (no auto-download)
4. **Expected:** "Install Now" opens GitHub or shows error

### Test Scenario 4: Already Downloaded
1. Complete one update download
2. Check for same update again
3. **Expected:** Shows "Update Ready to Install!" immediately
4. **Expected:** No re-download of APK file

## Backward Compatibility

✅ **100% Backward Compatible**
- Works with old API format (will just show dialog)
- Works if website API unavailable (uses GitHub API)
- Works on Android 7.0+ (API 24+)
- Works with or without `github_url` field
- No database changes or migrations needed

## Security Considerations

### Download Verification
- ✅ APK files verified using magic bytes (PK signature)
- ✅ File size checked (minimum 1 MB)
- ✅ SHA-256 hash calculated and stored
- ✅ Android system verifies signature during install

### Network Security
- ✅ HTTPS only for API and downloads
- ✅ Connection timeouts configured
- ✅ Certificate validation by Android system
- ✅ FileProvider used for secure APK access

### Permission Model
- ✅ INTERNET permission (required)
- ✅ ACCESS_NETWORK_STATE permission (required)
- ✅ REQUEST_INSTALL_PACKAGES permission (Android 8+)
- ✅ No storage permissions needed (uses cache directory)

## File Locations

### Android App
- `app/src/main/java/com/cardstack/game/UpdateChecker.java` - OTA update logic

### Website (matchmaina.ssfdre38.xyz)
- `/var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json` - Version API
- `/var/www/matchmaina.ssfdre38.xyz/html/downloads/` - APK files directory

### Project Scripts
- `update-website-api.sh` - Automated API update script

## Version History

- **v2.3.9** (This update)
  - Always auto-download from website (manual checks too)
  - Enhanced download URL validation
  - Proper GitHub URL handling in API
  - Created automation script for API updates

- **v2.3.5**
  - Website-first update checks
  - GitHub API fallback

- **v2.3.3**
  - Background download system
  - APK verification

- **v2.3.2**
  - Auto-download and install feature

- **v2.3.0**
  - Initial OTA update system

## Related Documentation

- [OTA_UPDATE_SYSTEM.md](OTA_UPDATE_SYSTEM.md) - Complete OTA documentation
- [OTA_WEBSITE_DOWNLOAD_FIX.md](OTA_WEBSITE_DOWNLOAD_FIX.md) - Previous fix documentation
- [OTA_AUTO_INSTALL_v2.3.2.md](OTA_AUTO_INSTALL_v2.3.2.md) - Auto-install feature
- [DEPLOYMENT_v2.3.3_COMPLETE.md](DEPLOYMENT_v2.3.3_COMPLETE.md) - Background downloads

---

**Status:** ✅ Ready for Testing  
**Version:** 2.3.9 (pending release)  
**Date:** January 5, 2025  
**Changes:** 2 code modifications, 1 API update, 1 automation script
