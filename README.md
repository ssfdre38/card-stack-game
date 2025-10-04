# Match Mania - Android Card Game

Match Mania is an Android card game featuring AI opponents, player statistics, customizable profiles, and cryptographically secure random card shuffling.

## Features

### 🎮 Core Gameplay
- **4-Player Game**: Play against 3 intelligent AI opponents with unique names and avatars
- **True Randomness**: Uses Java's `SecureRandom` for cryptographically secure card shuffling
- **Smart AI Players**: Strategic AI decisions based on card types and hand composition
- **Classic Card Game Rules**: Match cards by color or number with special action cards
- **Customizable Rules**: 8 different rule options to customize gameplay

### 👤 Personalization (NEW in v2.0!)
- **Custom Player Name**: Choose your own name (up to 20 characters)
- **48 Avatar Options**: Select from emojis including smileys, people, animals, and symbols
- **Profile Persistence**: Your name and avatar are saved across games

### 📊 Statistics System (NEW in v2.0!)
- **Complete Stats Tracking**: Games played, won, lost, win rate
- **Card Statistics**: Track cards played, drawn, special cards, and wilds
- **Time Tracking**: Total play time, average game duration, fastest win, longest game
- **Win Streaks**: Current streak and all-time best streak
- **Match History**: View your last 50 games with details
- **Reset Option**: Clear stats when you want a fresh start

### 🎨 Modern UI
- **Beautiful Card Designs**: Color-coded cards with visual icons
- **Dark Theme Interface**: Easy on the eyes
- **Screen Rotation Support**: Play in portrait or landscape
- **4-Button Header**: Easy access to Profile, Stats, About, and Settings
- **Enhanced Game Over**: View stats immediately after games

## Game Rules

### Setup
- Each player starts with 7 cards
- The remaining cards form the draw pile
- One card is placed face-up to start the discard pile

### Gameplay
- Players take turns matching the top card by color or number
- Special cards:
  - **Skip (S)**: Next player loses their turn
  - **Reverse (R)**: Reverses the direction of play
  - **Draw Two (+2)**: Next player draws 2 cards and loses their turn
  - **Wild (W)**: Can be played on any card, player chooses the color
  - **Wild Draw Four (W+4)**: Next player draws 4 cards, player chooses color

### Winning
- First player to play all their cards wins!

## Building the App

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK 24 or higher
- Java 8 or higher

### Build Steps

1. Open the project in Android Studio
2. Wait for Gradle to sync dependencies
3. Build the signed APK:
   ```bash
   ./gradlew assembleRelease
   ```
4. The signed APK will be located at:
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

## Installation

### Download
Download the latest APK from the [releases page](https://github.com/ssfdre38/match-mania/releases/latest).

**Choose the right APK:**
- **MatchMania-release-vX.X.X.apk** - For regular use (recommended)
- **MatchMania-debug-vX.X.X.apk** - For testing/debugging only

### Requirements
- Android 7.0 (API 24) or higher
- ~5 MB of free storage

### Installation Steps

1. **Enable Unknown Sources**
   - Android 8.0+: Settings > Apps > Special Access > Install Unknown Apps > Select your browser/file manager > Allow
   - Android 7.x: Settings > Security > Unknown Sources > Enable

2. **Download and Install**
   - Download the release APK
   - Tap the downloaded file
   - Follow the installation prompts

### Troubleshooting "App Not Installed" Error

If you get an "App not installed" or "Package manager error" when trying to install:

1. **Uninstall Previous Version** (Most Common Fix)
   - The new version uses a different signature
   - Go to Settings > Apps > Match Mania > Uninstall
   - Then install the new APK

2. **Clear Download Cache**
   - Re-download the APK file
   - Make sure the download completed successfully

3. **Check Storage Space**
   - Ensure you have at least 10 MB free space

4. **Verify Android Version**
   - Check Settings > About Phone > Android Version
   - Must be 7.0 or higher

### For Developers

If building from source:
```bash
# Quick build and release
./build_and_release.sh 2.0.2

# Manual build
./gradlew clean assembleDebug assembleRelease

# Install to connected device
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Version History

- **v2.0.2** - Repository renamed to match-mania, updated all links and references
- **v2.0.1** - Verified build with proper signing (both debug and release APKs)
- **v2.0.0** - Match Mania rebrand with signed APK, statistics, and profile customization
- **v1.4.0** - Added statistics tracking and profile customization
- **v1.3.3** - Random AI names and avatars
- **v1.3.2** - Corner numbers fixed, About page added
- **v1.3.0** - Custom game rules & settings
- **v1.2.1** - Correct game rules implementation
- **v1.1.0** - Added visual card icons
- **v1.0.0** - Initial release

## Developer

**Created by:** Daniel Elliott  
**Email:** ssfdre38@gmail.com  
**Copyright:** © 2025 Daniel Elliott

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Note**: This game is an original creation and does not use any trademarked names, logos, or designs. All game mechanics and visual elements are implemented independently.
