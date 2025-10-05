# OTA Update System - Priority and Fallback Flow

## System Overview

The Match Mania OTA update system is designed with **website-first priority** and GitHub as a reliable fallback. This ensures users always get the fastest downloads while maintaining high availability.

## Update Source Priority

### Priority Order (Automatic)

```
1st Priority: Website API & Downloads
    ↓ (if unavailable)
2nd Priority: GitHub API & Releases
```

### How It Works

#### Website as Primary Source ✅
- **API**: `https://matchmaina.ssfdre38.xyz/api/latest-version.json`
- **Downloads**: `https://matchmaina.ssfdre38.xyz/downloads/MatchMania-release-vX.X.X.apk`
- **Timeout**: 5 seconds (fast response)
- **Benefits**: 
  - Faster downloads (direct from website)
  - No GitHub API rate limits
  - Full control over distribution
  - Can track download analytics

#### GitHub as Fallback 🔄
- **API**: `https://api.github.com/repos/ssfdre38/match-mania/releases/latest`
- **Downloads**: GitHub release assets
- **Timeout**: 10 seconds (reliable fallback)
- **Benefits**:
  - High availability (GitHub infrastructure)
  - Free hosting
  - Version history preserved
  - Community visibility

## Complete Update Flow

### Automatic Update Check (Every 24 Hours)

```
App Launch
    ↓
24+ hours since last check? → NO → Continue normally
    ↓ YES
Check Website API (5s timeout)
    ↓
Website API responds? → YES → Use website data
    ↓ NO
Check GitHub API (10s timeout)
    ↓
GitHub API responds? → YES → Use GitHub data
    ↓ NO
Show error (if manual check) or silently fail (if automatic)
    ↓
[If update found and download_url available]
    ↓
Start Background Download
    ↓
Download from: download_url (usually website)
    ↓
Download Complete → Verify APK
    ↓
Show "Update Ready to Install!" Dialog
```

### Manual Update Check (Settings Button)

```
User taps "Check for Updates"
    ↓
Show "Checking for updates..." toast
    ↓
Check Website API (5s timeout)
    ↓
Website API responds? → YES → Use website data
    ↓ NO
Check GitHub API (10s timeout)
    ↓
GitHub API responds? → YES → Use GitHub data
    ↓ NO
Show "Could not check for updates" error
    ↓
[If update found]
    ↓
Already downloaded? → YES → Show "Install Now" dialog
    ↓ NO
download_url available? → YES → Start background download
    ↓ NO
Show dialog with "Details" button (opens GitHub/website)
```

## API Response Formats

### Website API Format (Primary)

```json
{
  "version": "v2.3.8",
  "name": "Match Mania v2.3.8",
  "changelog": "- Feature 1\n- Feature 2\n- Bug fix",
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

**Key Fields for Auto-Download:**
- `download_url`: **MUST** be provided for auto-download to work
- `github_url`: Used for "Details" button (fallback to website_url if missing)
- `version`: Version tag (e.g., "v2.3.8")
- `versionCode`: Numeric code for version comparison

### GitHub API Format (Fallback)

The app automatically parses GitHub's API response:
- Extracts version from `tag_name`
- Gets changelog from `body`
- Finds release APK in `assets` array
- Uses `html_url` for Details button

## Download Priority Logic

### When Both Sources Available

1. **Check Website API first** (always)
   - If returns valid `download_url` → Use website download
   - Auto-download starts immediately
   - GitHub is never contacted

2. **GitHub API only used if:**
   - Website API times out (5 seconds)
   - Website API returns error
   - Website API returns invalid JSON

### When Download URL Missing

If website API responds but no `download_url`:
```
Show dialog with:
- "Install Now" button → Opens browser to github_url or website_url
- "Details" button → Opens github_url or website_url
- "Skip" button → Skip this version
```

## Download Source Determination

The system uses the `download_url` field from whichever API responded first:

| API Source | download_url Value | Download From |
|------------|-------------------|---------------|
| Website API | `https://matchmaina.ssfdre38.xyz/downloads/...` | Website ✅ |
| GitHub API | `https://github.com/.../releases/download/...` | GitHub 🔄 |
| Either | Empty or null | Show dialog (no auto-download) |

## Release Script Integration

The `release.sh` script automatically handles both sources:

### Step 4: Deploy to Website (First)
```bash
# Copy APK to website
sudo cp app-release.apk /var/www/matchmaina.ssfdre38.xyz/html/downloads/

# Update website API JSON
# - Sets download_url to website
# - Includes github_url for Details button
# - Sets proper permissions
```

### Step 6: Create GitHub Release (Second)
```bash
# Create GitHub release with:
# - Release notes emphasizing website as primary download
# - APK as fallback option
# - Links back to website
```

## Website API JSON Generation

The release script automatically generates proper JSON:

```bash
{
  "download_url": "https://matchmaina.ssfdre38.xyz/downloads/...",  # Website (primary)
  "github_url": "https://github.com/ssfdre38/match-mania/releases/tag/...",  # Details link
  "website_url": "https://matchmaina.ssfdre38.xyz"  # Fallback for Details
}
```

**Important**: The `download_url` always points to the website, ensuring website downloads are prioritized.

## GitHub Release Notes

The release script includes clear messaging:

```markdown
### 📥 Downloads

**Primary (Recommended):** Download from the official website
- 🌐 Download from Website ← Recommended
- Includes automatic OTA update system
- Direct download from our servers

**Alternative:** Download from GitHub (fallback)
- Download the APK from the Assets section below
- Manual updates only
```

This encourages users to download from the website while keeping GitHub as a fallback.

## Error Handling & Fallbacks

### Scenario 1: Website Down
```
Website API (timeout) → GitHub API → Success
```
**Result**: Update found via GitHub, downloads from GitHub releases

### Scenario 2: Both Down
```
Website API (timeout) → GitHub API (timeout) → Fail
```
**Result**: 
- Automatic check: Silent failure, try again in 24 hours
- Manual check: Show "Could not check for updates" toast

### Scenario 3: Website Up, No download_url
```
Website API (success, no URL) → Show dialog
```
**Result**: User can click "Details" to visit website or GitHub

### Scenario 4: Website Up, GitHub Down
```
Website API (success) → Download from website
```
**Result**: Works perfectly, GitHub never contacted

## Benefits of This System

### For Users
✅ **Faster Downloads**: Website typically faster than GitHub  
✅ **Always Available**: GitHub fallback ensures updates always work  
✅ **Automatic**: Background downloads, no manual intervention  
✅ **Transparent**: Clear indication of download source

### For Developers
✅ **Full Control**: Website as primary distribution channel  
✅ **Analytics Ready**: Can track website API requests  
✅ **Bandwidth Control**: Serve from your infrastructure  
✅ **Instant Updates**: Update API, users get it within 24 hours  
✅ **Reliability**: GitHub as backup if website issues

### For Infrastructure
✅ **Load Balancing**: Website for most traffic, GitHub for failover  
✅ **Cost Effective**: Website bandwidth under your control  
✅ **Scalable**: Can add CDN to website later  
✅ **Resilient**: No single point of failure

## Testing Priority System

### Test 1: Normal Flow (Website Working)
```bash
# Expected: Website API used, download from website
1. Launch app (or manual check)
2. Check network logs: Should show matchmaina.ssfdre38.xyz API call
3. Download should be from matchmaina.ssfdre38.xyz/downloads/
4. GitHub should NOT be contacted
```

### Test 2: Website Down (Fallback to GitHub)
```bash
# Expected: GitHub API used, download from GitHub
1. Block matchmaina.ssfdre38.xyz (hosts file or firewall)
2. Launch app (or manual check)
3. Check network logs: Should show api.github.com call after timeout
4. Download should be from github.com/releases/download/
```

### Test 3: Both Down
```bash
# Expected: Error shown (manual) or silent fail (automatic)
1. Block both domains
2. Manual check: "Could not check for updates"
3. Automatic check: Silent, tries again in 24 hours
```

### Test 4: Website API with No download_url
```bash
# Expected: Dialog shown, no auto-download
1. Remove download_url from API JSON
2. Manual check: Dialog appears with Details button
3. Auto-download: Skipped, shows dialog instead
```

## Monitoring and Debugging

### Check Which Source Was Used

Look for these log messages in Android logcat:

```
UpdateChecker: Update info retrieved from website
UpdateChecker: Website API unavailable, falling back to GitHub
UpdateChecker: Successfully retrieved update info from GitHub
```

### Verify API Responses

```bash
# Test website API
curl -s https://matchmaina.ssfdre38.xyz/api/latest-version.json | jq .

# Test GitHub API
curl -s https://api.github.com/repos/ssfdre38/match-mania/releases/latest | jq '.tag_name, .assets[0].browser_download_url'
```

### Check Download Source

In the app logs, look for:
```
Starting background download for vX.X.X
Download URL: https://matchmaina.ssfdre38.xyz/downloads/...  (Website)
Download URL: https://github.com/.../releases/download/...   (GitHub)
```

## Configuration Files

### UpdateChecker.java Constants
```java
private static final String WEBSITE_API_URL = "https://matchmaina.ssfdre38.xyz/api/latest-version.json";
private static final String GITHUB_API_URL = "https://api.github.com/repos/ssfdre38/match-mania/releases/latest";
```

### Release Script Variables
```bash
WEBSITE_DIR="/var/www/matchmaina.ssfdre38.xyz/html"
# API: $WEBSITE_DIR/api/latest-version.json
# Downloads: $WEBSITE_DIR/downloads/
```

## Summary

✅ **Website is ALWAYS checked first** (5-second timeout)  
✅ **GitHub is ONLY used as fallback** (10-second timeout)  
✅ **download_url determines actual download source** (usually website)  
✅ **Auto-download works from BOTH sources** (no manual intervention)  
✅ **Release script updates BOTH sources** (website first, GitHub second)  
✅ **Users get fastest, most reliable updates** (website speed + GitHub reliability)

---

**Status**: ✅ Implemented and Working  
**Last Updated**: January 5, 2025  
**Version**: 2.3.9+
