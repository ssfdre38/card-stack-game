# Match Mania v2.0.1 - Build and Release Notes

## Build Summary

Successfully built and released Match Mania v2.0.1 with proper signing configuration.

### APK Files Generated

1. **MatchMania-release-v2.0.1.apk** (4.3 MB)
   - Production-ready release APK
   - Signed with custom keystore
   - Certificate: CN=Daniel Elliott, OU=Match Mania, O=Match Mania, L=San Francisco, ST=California, C=US
   - SHA-256: 6be3cb711fe8c7e7aac01441d42d20ce5097180b38765403a0f46235500bad1e

2. **MatchMania-debug-v2.0.1.apk** (5.3 MB)
   - Debug version for testing
   - Signed with Android Debug keystore
   - Suitable for development and testing

### Changes Made

- ✅ Created custom signing keystore with your credentials
- ✅ Updated build.gradle with proper signing configuration
- ✅ Built both debug and release APKs successfully
- ✅ Verified APK signatures
- ✅ Pushed changes to GitHub repository
- ✅ Created release v2.0.1 with both APKs

### About the "App Not Installed" Issue

The installation error you experienced could be due to one of these reasons:

#### 1. **Signature Mismatch (Most Likely)**
If you had a previous version of the app installed that was signed with a different keystore, Android will refuse to install the new version. This is a security feature.

**Solution:** Uninstall the old version first, then install the new one.

#### 2. **Corrupted APK Download**
Sometimes APK files can get corrupted during download.

**Solution:** Re-download the APK and try again.

#### 3. **Insufficient Storage**
The device might not have enough free space.

**Solution:** Clear some space and try again.

#### 4. **Installation from Unknown Sources**
On some Android versions, you need to enable installation from unknown sources.

**Solution:** Go to Settings > Security > Unknown Sources and enable it.

#### 5. **Android Version Compatibility**
The app requires Android 7.0 (API 24) or higher.

**Solution:** Check if your device meets the minimum requirement.

### Installation Instructions

1. **If you have a previous version installed:**
   ```
   Settings > Apps > Match Mania > Uninstall
   ```

2. **Download the appropriate APK:**
   - For regular use: `MatchMania-release-v2.0.1.apk`
   - For testing/debugging: `MatchMania-debug-v2.0.1.apk`

3. **Enable Unknown Sources (if needed):**
   - Android 8.0+: Settings > Apps > Special Access > Install Unknown Apps
   - Android 7.x: Settings > Security > Unknown Sources

4. **Install the APK:**
   - Tap the downloaded APK file
   - Follow the installation prompts

### Testing with Emulator

To test the APK in an Android emulator:

```bash
# Start an emulator (if you have one configured)
emulator -avd <avd_name>

# Install the APK
adb install -r app-release.apk

# Or for debug version
adb install -r app-debug.apk
```

### Signing Configuration

The release APK is signed with these details:
- Keystore: matchmania-release-key.jks
- Alias: Match Mania
- Owner: Daniel Elliott (ssfdre38@gmail.com)
- Organization: Match Mania
- Location: San Francisco, California, US
- Validity: 10,000 days (27+ years)

**Note:** The keystore file is stored locally and NOT committed to the repository for security reasons. Keep it safe as you'll need it for future releases!

### App Details

- **Package Name:** com.matchmania.game
- **App Name:** Match Mania
- **Version:** 2.0.1 (Build 6)
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)

### Future Releases

For future releases:
1. Make your code changes
2. Update versionCode and versionName in `app/build.gradle`
3. Build: `./gradlew clean assembleDebug assembleRelease`
4. Test the APKs
5. Create a release: `gh release create vX.X.X app-release.apk app-debug.apk`

### Release Link

https://github.com/ssfdre38/card-stack-game/releases/tag/v2.0.1

---

**Built on:** October 4, 2025
**Build Tool:** Gradle 8.2
**Android Gradle Plugin:** 8.1.4
**Build Time:** ~6 seconds

© 2025 Daniel Elliott. All rights reserved.
