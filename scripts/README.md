# Match Mania Developer Scripts

This directory contains helpful scripts for developers working on Match Mania.

## 📁 Available Scripts

### `dev-build.sh` - Developer Build Script

A comprehensive build script for local development and testing.

**Features:**
- Build debug and/or release APKs
- Clean builds
- Run tests
- Install APK to device/emulator
- Color-coded output
- Automatic version detection

**Usage:**
```bash
./scripts/dev-build.sh [options]
```

**Common Options:**
- `--debug` - Build debug APK (default)
- `--release` - Build release APK
- `--both` - Build both debug and release
- `--clean` - Clean build directory first
- `--test` - Run tests after building
- `--install` - Install APK to connected device
- `--help` - Show full help message

**Examples:**

```bash
# Simple debug build
./scripts/dev-build.sh

# Clean debug build and install
./scripts/dev-build.sh --debug --clean --install

# Build both debug and release
./scripts/dev-build.sh --both

# Build, test, and install
./scripts/dev-build.sh --debug --test --install
```

## 🔧 Prerequisites

Before using these scripts, ensure you have:

1. **Android SDK** installed
   - Download from: https://developer.android.com/studio
   - Set `ANDROID_HOME` environment variable
   - Or script will auto-detect common locations

2. **Java Development Kit (JDK)**
   - JDK 8 or higher
   - Required for Gradle builds

3. **Git** (for version control)
   - Clone the repository first
   - Follow CONTRIBUTING.md guidelines

## 📱 Testing Your Build

### On Physical Device

1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect device via USB
4. Trust the computer when prompted
5. Run: `./scripts/dev-build.sh --debug --install`

### On Emulator

1. Create an AVD (Android Virtual Device) in Android Studio
2. Start the emulator
3. Run: `./scripts/dev-build.sh --debug --install`

### Automated Testing

For comprehensive testing across multiple Android versions:

```bash
cd emulator-testing
./run-comprehensive-tests.sh
```

See `emulator-testing/README.md` for details.

## 🚀 Release Process

**Note:** Official releases are handled by the project maintainer using private deployment scripts.

As a contributor, you should:
1. Make your changes
2. Test thoroughly using `dev-build.sh`
3. Run automated tests if available
4. Submit a Pull Request
5. Maintainer will handle the release

**Do NOT attempt to:**
- Create GitHub releases
- Deploy to the official website
- Update the production API endpoints
- Modify signing keys

## 📝 Version Management

Version information is stored in `app/build.gradle`:

```gradle
versionCode 33          // Incremental build number
versionName "2.3.15"    // User-facing version string
```

**For Contributors:**
- Do NOT change version numbers in PRs
- Maintainer will update versions during release
- Focus on your feature/bugfix code

**Versioning Scheme:**
- Format: `MAJOR.MINOR.PATCH`
- Example: `2.3.15`
  - Major: Breaking changes
  - Minor: New features
  - Patch: Bug fixes

## 🐛 Debugging

### View App Logs

```bash
# All logs
adb logcat

# Filter for Match Mania
adb logcat | grep MatchMania

# Save logs to file
adb logcat > logs.txt
```

### Common Issues

**Issue:** "ANDROID_HOME not set"
- **Solution:** Install Android SDK and set environment variable
  ```bash
  export ANDROID_HOME=$HOME/Android/Sdk
  ```

**Issue:** "Signing key not found" (for release builds)
- **Solution:** Use `--debug` instead for testing
- Release builds require official signing keys

**Issue:** "No device found" (when using --install)
- **Solution:** Connect a device or start an emulator
  ```bash
  adb devices  # Check connected devices
  ```

**Issue:** "Build failed"
- **Solution:** Check Gradle output for errors
- Try: `./gradlew clean` then rebuild
- Ensure all dependencies are installed

## 📚 Additional Resources

### Project Documentation
- **README.md** - Project overview and features
- **CONTRIBUTING.md** - Contribution guidelines
- **CHANGELOG.md** - Version history
- **docs/** - Organized documentation by category

### Development Docs
- `docs/development/` - Development processes
- `docs/features/` - Feature documentation
- `docs/testing/` - Testing guides

### External Resources
- [Android Developer Guides](https://developer.android.com/guide)
- [Gradle Build Tool](https://gradle.org/)
- [Git Documentation](https://git-scm.com/doc)

## 🤝 Getting Help

If you need help:

1. Check the documentation first
2. Search existing GitHub issues
3. Ask in GitHub Discussions
4. Open a new issue if needed

**Useful Links:**
- 💬 [GitHub Discussions](https://github.com/ssfdre38/match-mania/discussions)
- 🐛 [Report Issues](https://github.com/ssfdre38/match-mania/issues)
- 📖 [Project Wiki](https://github.com/ssfdre38/match-mania/wiki)

## ⚠️ Important Notes

### What's NOT in This Directory

These scripts are intentionally **excluded** from version control:
- `build_and_release.sh` - Maintainer's personal build script
- `release.sh` - Maintainer's deployment script

These scripts contain:
- Website deployment paths
- Server credentials
- Personal automation workflows
- Production environment specifics

**As a contributor, you don't need these scripts.**
Use `dev-build.sh` for all local development work.

### Security

- Never commit signing keys (`.jks`, `.keystore`)
- Never commit passwords or credentials
- Keep `local.properties` out of version control
- Review `.gitignore` before committing

## 🎯 Quick Start for New Developers

1. **Clone the repository**
   ```bash
   git clone https://github.com/ssfdre38/match-mania.git
   cd match-mania
   ```

2. **Make the script executable**
   ```bash
   chmod +x scripts/dev-build.sh
   ```

3. **Build and test**
   ```bash
   ./scripts/dev-build.sh --debug --clean --install
   ```

4. **Make your changes**
   - Edit code in `app/src/`
   - Follow coding standards
   - Test thoroughly

5. **Rebuild and test**
   ```bash
   ./scripts/dev-build.sh --debug --install
   ```

6. **Submit Pull Request**
   - Create a feature branch
   - Commit your changes
   - Push and create PR on GitHub

## 📜 License

Match Mania is licensed under the MIT License. See LICENSE file for details.

---

**Happy Coding! 🎮**

For questions or suggestions about these scripts, open an issue on GitHub.
