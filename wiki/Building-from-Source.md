# Building from Source

This guide will help you build Match Mania from source code.

## Prerequisites

### Required Software

1. **Android Studio** (Latest stable version)
   - Download from [developer.android.com/studio](https://developer.android.com/studio)
   - Install with default components

2. **Java Development Kit (JDK)**
   - JDK 11 or higher required
   - Usually included with Android Studio
   - Verify: `java -version`

3. **Android SDK**
   - API Level 21 (Android 5.0) minimum
   - API Level 33 (Android 13) recommended
   - Installed through Android Studio SDK Manager

4. **Git**
   - For cloning the repository
   - Download from [git-scm.com](https://git-scm.com/)

### Optional Tools

- **GitHub CLI** (`gh`) - For repository management
- **ADB (Android Debug Bridge)** - Included with Android SDK
- **Gradle** - Included in project (gradlew)

## Getting the Source Code

### Option 1: Clone with Git

```bash
# Clone the repository
git clone https://github.com/ssfdre38/match-mania.git
cd match-mania
```

### Option 2: Download ZIP

1. Visit [github.com/ssfdre38/match-mania](https://github.com/ssfdre38/match-mania)
2. Click "Code" > "Download ZIP"
3. Extract the ZIP file
4. Navigate to the extracted folder

## Building with Android Studio

### Step 1: Open Project

1. Launch Android Studio
2. Select **Open an Existing Project**
3. Navigate to the `match-mania` folder
4. Click **OK**

### Step 2: Sync Gradle

1. Android Studio will automatically start syncing
2. Wait for "Gradle sync finished" notification
3. If prompted, accept any SDK updates

### Step 3: Build Debug APK

**Method A: Using Build Menu**
1. Click **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**
2. Wait for build to complete
3. Click **locate** in the notification

**Method B: Using Gradle Panel**
1. Open **Gradle** panel (right side)
2. Navigate to **app** > **Tasks** > **build**
3. Double-click **assembleDebug**

**Output Location:**
```
app/build/outputs/apk/debug/app-debug.apk
```

### Step 4: Build Release APK (Unsigned)

1. Click **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**
2. Select **release** build variant
3. Build will create unsigned APK

**Output Location:**
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

## Building from Command Line

### On Linux/Mac

```bash
# Navigate to project directory
cd match-mania

# Make gradlew executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Build release APK (unsigned)
./gradlew assembleRelease

# Build both
./gradlew assemble

# Clean build
./gradlew clean assembleDebug
```

### On Windows

```cmd
# Navigate to project directory
cd match-mania

# Build debug APK
gradlew.bat assembleDebug

# Build release APK (unsigned)
gradlew.bat assembleRelease

# Build both
gradlew.bat assemble
```

## Building Signed Release APK

To create a production-ready signed APK:

### Step 1: Create Keystore

```bash
keytool -genkey -v -keystore match-mania.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias matchmania
```

You'll be prompted for:
- Keystore password
- Key password
- Name and organizational details

**Important:** Keep your keystore and passwords secure!

### Step 2: Configure Signing

**Option A: Via gradle.properties**

Create/edit `gradle.properties`:
```properties
MATCH_MANIA_KEYSTORE_FILE=/path/to/match-mania.jks
MATCH_MANIA_KEYSTORE_PASSWORD=your_keystore_password
MATCH_MANIA_KEY_ALIAS=matchmania
MATCH_MANIA_KEY_PASSWORD=your_key_password
```

**Option B: Via Command Line**

```bash
./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=/path/to/match-mania.jks \
  -Pandroid.injected.signing.store.password=your_keystore_password \
  -Pandroid.injected.signing.key.alias=matchmania \
  -Pandroid.injected.signing.key.password=your_key_password
```

### Step 3: Build Signed APK

```bash
./gradlew assembleRelease
```

**Output Location:**
```
app/build/outputs/apk/release/app-release.apk
```

## Using the Build Script

The project includes a convenient build script:

```bash
# Make executable
chmod +x build_and_release.sh

# Build and release
./build_and_release.sh 2.1.2

# Script performs:
# - Version bump
# - Clean build
# - Signed release build
# - Debug build
# - APK renaming
# - Git tagging
```

## Installing on Device

### Via Android Studio

1. Connect device via USB
2. Enable USB debugging on device
3. Click **Run** (▶️) button
4. Select your device

### Via ADB

```bash
# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release APK (overwrites existing)
adb install -r app/build/outputs/apk/release/app-release.apk

# Uninstall first
adb uninstall com.matchmania
adb install app/build/outputs/apk/release/app-release.apk
```

## Running on Emulator

### Create AVD (Android Virtual Device)

1. Open **AVD Manager** in Android Studio
2. Click **Create Virtual Device**
3. Choose device definition (e.g., Pixel 4)
4. Select system image (API 30+ recommended)
5. Click **Finish**

### Run on Emulator

1. Start the AVD
2. Click **Run** (▶️) in Android Studio
3. Select the emulator

**Or via command line:**
```bash
# List emulators
emulator -list-avds

# Start emulator
emulator -avd Pixel_4_API_30

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Build Variants

Match Mania has two build variants:

### Debug
- Includes debugging information
- Larger APK size
- Can be installed alongside release version (different signatures)
- Good for development

### Release
- Optimized and minimized
- Smaller APK size
- Must be signed for distribution
- For production use

## Troubleshooting

### Gradle Sync Failed

**Solutions:**
- Check internet connection
- Invalidate caches: **File** > **Invalidate Caches / Restart**
- Update Android Studio
- Check `build.gradle` for syntax errors

### SDK Not Found

**Solutions:**
- Open SDK Manager and install required SDKs
- Set `ANDROID_HOME` environment variable
- Check `local.properties` for correct SDK path

### Build Failed

**Solutions:**
- Check error messages in Build Output
- Run `./gradlew clean`
- Update Gradle: Edit `gradle/wrapper/gradle-wrapper.properties`
- Check JDK version compatibility

### Keystore Issues

**Solutions:**
- Verify keystore path is correct
- Check password is correct
- Ensure alias exists: `keytool -list -keystore match-mania.jks`
- Generate new keystore if lost (will need new package signing)

## Optimization

### Reduce APK Size

Enable ProGuard/R8 in `app/build.gradle`:
```gradle
buildTypes {
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

### Speed Up Builds

In `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
```

## Next Steps

- Test your build thoroughly
- Check [Contributing Guide](Contributing) for code standards
- Submit pull requests for improvements
- Create releases following project conventions

## Additional Resources

- [Android Developer Docs](https://developer.android.com/docs)
- [Gradle Build Tool](https://gradle.org/guides/)
- [Git Documentation](https://git-scm.com/doc)
- [Match Mania Wiki](https://github.com/ssfdre38/match-mania/wiki)

---

**Need Help?** [Open an issue](https://github.com/ssfdre38/match-mania/issues) or [start a discussion](https://github.com/ssfdre38/match-mania/discussions)
