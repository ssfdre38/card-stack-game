# OTA Update System - Website Download Fix

## Issue Identified

When updates were checked from the website API, the OTA update system had two problems:

### Problem 1: "Details" Button Redirected to Website
When clicking "Details" in the update dialog, users were redirected to the Match Mania website instead of the GitHub release page with full changelog and release notes.

**Root Cause:** In `checkWebsiteAPI()` method (line 194), the `info.url` field was being set to the website URL instead of the GitHub release URL. This affected the "Details" button which opens `info.url`.

### Problem 2: Auto-Download Not Working from Website API
Automatic background downloads weren't triggering properly when updates came from the website API.

**Root Cause:** In the auto-download check (line 154), the code checked if `info.downloadUrl != null` but didn't verify if it was an empty string. If the website API returned an empty `download_url` field, the null check would pass but the download would fail.

## Changes Made

### Fix 1: Proper URL Priority for Details Button

**File:** `app/src/main/java/com/cardstack/game/UpdateChecker.java`  
**Lines:** 194-197

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

**Behavior Change:**
- Now reads `github_url` field from website API
- Sets `info.url` to GitHub release page if available
- Falls back to website URL only if GitHub URL not provided
- "Details" button now properly opens GitHub release page with full changelog

### Fix 2: Enhanced Auto-Download Validation

**File:** `app/src/main/java/com/cardstack/game/UpdateChecker.java`  
**Line:** 154

**Before:**
```java
if (autoDownloadEnabled && !forceCheck && info.downloadUrl != null) {
```

**After:**
```java
if (autoDownloadEnabled && !forceCheck && info.downloadUrl != null && !info.downloadUrl.isEmpty()) {
```

**Behavior Change:**
- Now validates that `downloadUrl` is not null AND not empty
- Prevents attempting downloads with empty URL strings
- Shows manual download dialog if download URL missing
- More robust error handling for incomplete API responses

## Expected Website API Format

For optimal functionality, the website API (`https://matchmaina.ssfdre38.xyz/api/latest-version.json`) should return:

```json
{
  "version": "v2.3.8",
  "name": "Version 2.3.8 - Tablet Display Fix",
  "changelog": "- Fixed tablet card width calculation\n- Improved landscape mode display",
  "download_url": "https://matchmaina.ssfdre38.xyz/downloads/MatchMania-release-v2.3.8.apk",
  "github_url": "https://github.com/ssfdre38/match-mania/releases/tag/v2.3.8",
  "website_url": "https://matchmaina.ssfdre38.xyz"
}
```

### Required Fields
- `version`: Version tag (e.g., "v2.3.8")
- `download_url`: Direct APK download URL

### Optional Fields
- `name`: Release title (defaults to "New Version")
- `changelog`: Release notes (defaults to "")
- `github_url`: GitHub release page URL (for Details button)
- `website_url`: Website URL (fallback if github_url missing)

## Update Flow After Fix

### Automatic Update (App Launch)
1. App checks website API for updates every 24 hours
2. If newer version found with valid `download_url`:
   - **Downloads APK automatically in background**
   - Shows notification during download
   - Verifies downloaded APK file
   - Shows "Update Ready to Install!" dialog when complete
3. If `download_url` missing/empty:
   - Shows standard update dialog
   - "Install Now" requires valid download URL or opens browser

### Manual Update (Settings)
1. User taps "Check for Updates"
2. App checks website API immediately
3. If newer version found:
   - Shows update dialog (no auto-download for manual checks)
   - "Install Now" downloads APK with progress bar
   - "Details" opens GitHub release page (if github_url provided)
   - "Skip" marks version as skipped

### Button Behavior
- **Install Now**: Downloads APK directly if `download_url` provided, otherwise opens website
- **Details**: Opens GitHub release page if `github_url` provided, otherwise opens website
- **Skip**: Skips this version until next manual check
- **Later**: (Install dialog) Keeps downloaded APK for later installation

## Testing Recommendations

### Test Case 1: Website API with Complete Data
```json
{
  "version": "v2.3.9",
  "download_url": "https://matchmaina.ssfdre38.xyz/downloads/MatchMania-release-v2.3.9.apk",
  "github_url": "https://github.com/ssfdre38/match-mania/releases/tag/v2.3.9"
}
```
**Expected:** Auto-download works, Details opens GitHub

### Test Case 2: Website API without GitHub URL
```json
{
  "version": "v2.3.9",
  "download_url": "https://matchmaina.ssfdre38.xyz/downloads/MatchMania-release-v2.3.9.apk",
  "website_url": "https://matchmaina.ssfdre38.xyz"
}
```
**Expected:** Auto-download works, Details opens website

### Test Case 3: Website API without Download URL
```json
{
  "version": "v2.3.9",
  "github_url": "https://github.com/ssfdre38/match-mania/releases/tag/v2.3.9"
}
```
**Expected:** Shows dialog, Install Now opens GitHub, Details opens GitHub

### Test Case 4: Website API Unavailable
**Expected:** Falls back to GitHub API, works as before

## Benefits

### For Users
- **Seamless Updates**: Auto-download works reliably from website API
- **Better Information**: Details button shows full GitHub release notes
- **Flexible Options**: Works whether website provides download URL or not
- **Fallback Safety**: GitHub API always available as backup

### For Developers
- **Dual Distribution**: Can serve updates from website OR GitHub
- **Gradual Rollout**: Can control download URLs from website API
- **Analytics Ready**: Website API calls can be logged/tracked
- **Error Recovery**: Empty/missing URLs handled gracefully

## Backward Compatibility

✅ **Fully Backward Compatible**
- Works with existing GitHub API (no changes needed)
- Works if website API returns old format (uses defaults)
- Works if website API unavailable (falls back to GitHub)
- No database migrations or preference changes needed

## Version Information

- **Fixed in:** v2.3.9 (pending)
- **Affects:** v2.3.5+ (versions with website API support)
- **Files Modified:** 
  - `app/src/main/java/com/cardstack/game/UpdateChecker.java` (2 changes, 3 lines)

## Related Documentation

- [OTA_UPDATE_SYSTEM.md](OTA_UPDATE_SYSTEM.md) - Complete OTA system documentation
- [OTA_AUTO_INSTALL_v2.3.2.md](OTA_AUTO_INSTALL_v2.3.2.md) - Auto-install feature docs
- [DEPLOYMENT_v2.3.3_COMPLETE.md](DEPLOYMENT_v2.3.3_COMPLETE.md) - Background download docs

---

**Summary:** Two small but important fixes ensure the OTA update system works perfectly when updates come from the website API, with proper auto-downloads and correct navigation to GitHub release pages for full details.
