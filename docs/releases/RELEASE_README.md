# 🚀 Automated Release System

The Match Mania project now includes a fully automated release system!

## Quick Start

```bash
./release.sh
```

That's it! The script will handle everything:
- ✅ Build APK
- ✅ Deploy to website
- ✅ Update GitHub
- ✅ Sync wikis
- ✅ Update changelogs

## What You Need

Before running the script:

1. **Update CHANGELOG.md** with your version's changes
2. **Test your changes** locally
3. **Run the script** and answer the prompts

## Example Release

```bash
$ ./release.sh

🚀 Match Mania Release Script
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ℹ Current version: v2.3.4 (code: 22)

Enter new version number (e.g., 2.3.5): 2.3.5
Enter new version code (e.g., 23): 23

⚠ This will release v2.3.5 (code: 23)
Continue? (y/N): y

... [Script runs automatically] ...

✅ Release Complete!
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✓ Match Mania v2.3.5 has been released!

📍 Download Locations:
   • Website: https://matchmaina.ssfdre38.xyz
   • GitHub:  https://github.com/ssfdre38/match-mania/releases/tag/v2.3.5
```

## Files

- **release.sh** - Main release script
- **RELEASE_GUIDE.md** - Detailed documentation
- **CHANGELOG.md** - Version history
- **RELEASE_README.md** - This file

## Documentation

See [RELEASE_GUIDE.md](RELEASE_GUIDE.md) for:
- Detailed step-by-step instructions
- Troubleshooting guide
- Manual release procedures
- Best practices
- Advanced usage

## Features

The release script automatically:

### 🔨 Build & Package
- Updates version in build.gradle
- Builds signed release APK
- Verifies build success

### 🌐 Website Deployment
- Uploads APK to downloads directory
- Updates API JSON with version info
- Updates website HTML
- Regenerates wiki HTML

### 📚 Documentation
- Updates version in all wiki files
- Syncs GitHub wiki repository
- Regenerates website wiki
- Maintains consistency

### 📦 GitHub Release
- Creates git tag
- Pushes to repository
- Creates GitHub release
- Attaches APK
- Formats changelog

### ✅ Verification
- Reports all URLs
- Confirms deployment
- Lists download locations

## Quick Reference

```bash
# Release new version
./release.sh

# Check current version
grep versionName app/build.gradle

# View releases
gh release list

# Build without releasing
./gradlew assembleRelease
```

## Support

Questions? Check:
1. [RELEASE_GUIDE.md](RELEASE_GUIDE.md) - Complete guide
2. Script output - Detailed error messages
3. Build log - `/tmp/gradle_build.log`

## Version Numbering

- **Major.Minor.Patch** (e.g., 2.3.5)
- **Version Code** increments by 1 each release
- Follow semantic versioning

Example:
- 2.3.4 = code 22
- 2.3.5 = code 23
- 2.4.0 = code 24

---

**Happy releasing! 🎉**

For detailed documentation, see [RELEASE_GUIDE.md](RELEASE_GUIDE.md)
