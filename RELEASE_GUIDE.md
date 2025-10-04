# Match Mania Release Guide

This guide explains how to use the automated release script to deploy new versions of Match Mania.

## Prerequisites

Before running the release script, ensure you have:

1. **CHANGELOG.md** - Updated with the new version's changes
2. **Code Changes** - All changes committed locally (script will commit version bump)
3. **Testing** - New version tested and working
4. **Permissions** - Sudo access for website deployment

## Quick Release

To release a new version, simply run:

```bash
cd /home/ubuntu/match-mania
./release.sh
```

The script will:
1. Prompt you for the new version number (e.g., `2.3.5`)
2. Prompt you for the new version code (e.g., `23`)
3. Ask for confirmation
4. Automatically handle the rest!

## What the Script Does

### 1. **Version Update** ✏️
- Updates `app/build.gradle` with new version
- Reads changelog from `CHANGELOG.md`

### 2. **Build** 🔨
- Cleans previous builds
- Builds release APK
- Verifies build success

### 3. **Website Deployment** 🌐
- Uploads APK to `/var/www/matchmaina.ssfdre38.xyz/html/downloads/`
- Updates API JSON at `/api/latest-version.json`
- Updates website HTML with new version
- Regenerates website wiki HTML

### 4. **Wiki Updates** 📚
- Updates version references in wiki markdown files
- Updates both local wiki and GitHub wiki repository
- Regenerates HTML from markdown
- Commits and pushes wiki changes

### 5. **GitHub Release** 📦
- Commits version bump and changes
- Creates git tag
- Pushes to GitHub
- Creates GitHub release with APK
- Formats changelog for release notes

### 6. **Verification** ✅
- Reports all URLs and locations
- Confirms everything is live

## Detailed Steps

### Before Release

1. **Update CHANGELOG.md**

```markdown
## [2.3.5] - 2025-10-05

### Added
- New feature description
- Another feature

### Changed
- What changed
- Performance improvements

### Fixed
- Bug fix description
```

2. **Test Your Changes**
```bash
./gradlew assembleRelease
# Install and test the APK
```

3. **Commit Your Changes**
```bash
git add .
git commit -m "Feature: Description of changes"
```

### Running the Release

4. **Run Release Script**
```bash
./release.sh
```

5. **Follow Prompts**
```
Current version: v2.3.4 (code: 22)

Enter new version number (e.g., 2.3.5): 2.3.5
Enter new version code (e.g., 23): 23

This will release v2.3.5 (code: 23)
Continue? (y/N): y
```

6. **Wait for Completion**
The script will automatically:
- Update version
- Build APK
- Deploy to website
- Update wikis
- Create GitHub release
- Push everything

### After Release

7. **Verify Deployment**
- Check website: https://matchmaina.ssfdre38.xyz
- Check GitHub: https://github.com/ssfdre38/match-mania/releases
- Check API: https://matchmaina.ssfdre38.xyz/api/latest-version.json
- Test download links

8. **Announce Release**
- Social media
- Discord/Community
- Email list

## Manual Steps (If Needed)

### Update CHANGELOG.md Template

```markdown
## [X.Y.Z] - YYYY-MM-DD

### Added
- New features

### Changed
- Changes in existing functionality

### Deprecated
- Soon-to-be removed features

### Removed
- Removed features

### Fixed
- Bug fixes

### Security
- Security updates
```

### Manual Version Update

If you need to update the version manually:

```bash
# Edit app/build.gradle
nano app/build.gradle

# Change:
versionCode 22
versionName "2.3.4"

# To:
versionCode 23
versionName "2.3.5"
```

### Manual Website Update

```bash
# Upload APK
sudo cp app-release.apk /var/www/matchmaina.ssfdre38.xyz/html/downloads/MatchMania-release-vX.Y.Z.apk

# Update API JSON
sudo nano /var/www/matchmaina.ssfdre38.xyz/html/api/latest-version.json

# Update website HTML
sudo sed -i 's/vOLD/vNEW/g' /var/www/matchmaina.ssfdre38.xyz/html/index.html
```

### Manual GitHub Release

```bash
# Tag version
git tag vX.Y.Z

# Push tag
git push origin vX.Y.Z

# Create release
gh release create vX.Y.Z \
  --title "Match Mania vX.Y.Z" \
  --notes "Release notes here" \
  app-release.apk
```

## Troubleshooting

### Build Fails

```bash
# Check build log
cat /tmp/gradle_build.log

# Clean and rebuild
./gradlew clean
./gradlew assembleRelease
```

### Script Fails Mid-Process

The script is designed to be safe. If it fails:

1. Check the error message
2. Fix the issue
3. Run the script again

The script will skip steps that are already complete.

### Rollback a Release

If you need to rollback:

```bash
# Delete GitHub release
gh release delete vX.Y.Z

# Delete tag
git tag -d vX.Y.Z
git push origin :refs/tags/vX.Y.Z

# Revert version in build.gradle
git checkout HEAD^ app/build.gradle

# Remove website APK
sudo rm /var/www/matchmaina.ssfdre38.xyz/html/downloads/MatchMania-release-vX.Y.Z.apk
```

## Version Numbering

Follow Semantic Versioning (semver):

- **Major** (X.0.0): Breaking changes, major features
- **Minor** (X.Y.0): New features, backward compatible
- **Patch** (X.Y.Z): Bug fixes, small improvements

### Version Code Mapping

Version codes should increment monotonically:

- 2.3.3 = code 21
- 2.3.4 = code 22
- 2.3.5 = code 23
- 2.4.0 = code 24
- etc.

## Release Checklist

- [ ] All code changes committed
- [ ] CHANGELOG.md updated
- [ ] Version tested locally
- [ ] No critical bugs
- [ ] Documentation updated
- [ ] Run `./release.sh`
- [ ] Verify website download
- [ ] Verify GitHub release
- [ ] Test APK installation
- [ ] Announce release

## Best Practices

1. **Test Before Release**: Always test the build locally
2. **Meaningful Changelogs**: Write clear, user-friendly changelogs
3. **Increment Properly**: Version codes must always increase
4. **Release Notes**: Include website link in GitHub releases
5. **Backup**: Keep previous APK versions on website
6. **Verify**: Always check downloads work after release

## Script Configuration

The script uses these paths:

```bash
PROJECT_DIR="/home/ubuntu/match-mania"
WEBSITE_DIR="/var/www/matchmaina.ssfdre38.xyz/html"
WIKI_REPO="/home/ubuntu/match-mania-wiki"
```

To change paths, edit `release.sh` and update the configuration section.

## Support

If you encounter issues with the release script:

1. Check this guide
2. Review `/tmp/gradle_build.log` for build errors
3. Check script output for detailed error messages
4. Verify all prerequisites are met

## Advanced Usage

### Dry Run

To test without actually releasing:

```bash
# Comment out destructive operations in release.sh
# (push, release create, website deploy)
./release.sh
```

### Custom Changelog

To use a different changelog format, edit the `extract_changelog` function in `release.sh`.

### Additional Deployment Targets

To add more deployment targets (e.g., Play Store), add functions to `release.sh`:

```bash
deploy_to_play_store() {
    # Your implementation
}
```

## Quick Reference

```bash
# Standard release
./release.sh

# Check current version
grep versionName app/build.gradle

# View latest release
gh release view

# List all releases
gh release list

# Test build only
./gradlew assembleRelease

# Clean everything
./gradlew clean
```

---

**Happy Releasing! 🚀**
