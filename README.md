# Match Mania - Android Card Game

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Android-5.0%2B-green.svg)](https://developer.android.com)
[![Latest Release](https://img.shields.io/github/v/release/ssfdre38/match-mania)](https://github.com/ssfdre38/match-mania/releases/latest)

Match Mania is an Android card game featuring AI opponents, player statistics, customizable profiles, and cryptographically secure random card shuffling.

## 📋 Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Game Rules](#game-rules)
- [Building from Source](#building-the-app)
- [Contributing](#contributing)
- [Documentation](#documentation)
- [Support](#support)
- [License](#license)

## Features

### 🎮 Core Gameplay
- **4-Player Game**: Play against 3 intelligent AI opponents with unique names and avatars
- **True Randomness**: Uses Java's `SecureRandom` for cryptographically secure card shuffling
- **Smart AI Players**: Strategic AI decisions based on card types and hand composition
- **Classic Card Game Rules**: Match cards by color or number with special action cards
- **Customizable Rules**: 8 different rule options to customize gameplay

### 👤 Personalization
- **Custom Player Name**: Choose your own name (up to 20 characters)
- **48 Avatar Options**: Select from emojis including smileys, people, animals, and symbols
- **Profile Persistence**: Your name and avatar are saved across games

### 📊 Statistics System
- **Complete Stats Tracking**: Games played, won, lost, win rate
- **Card Statistics**: Track cards played, drawn, special cards, and wilds
- **Time Tracking**: Total play time, average game duration, fastest win, longest game
- **Win Streaks**: Current streak and all-time best streak
- **Match History**: View your last 50 games with details
- **Reset Option**: Clear stats when you want a fresh start

### 🚀 Updates & Settings (NEW in v2.3!)
- **OTA Update System**: Automatic update checking via GitHub releases
- **Check for Updates**: Manual update check in Settings
- **8 Customizable Rules**: Action stacking, jump-in, seven-zero, progressive draw, and more
- **Alphabetized Settings**: Easy-to-find game configuration options

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

**Latest Releases:**
- **v2.3.0** (Oct 2025) - OTA update system with automatic GitHub release checking
- **v2.2.7** (Oct 2025) - Alphabetized game settings
- **v2.2.6** (Oct 2025) - Larger corner elements for better readability
- **v2.2.5** (Oct 2025) - Fixed corner numbers overflowing on phone screens
- **v2.2.4** (Oct 2025) - Responsive UI with consistent card sizes across devices
- **v2.2.3** (Oct 2025) - Fixed played card area visibility
- **v2.2.2** (Oct 2025) - Game over dialog and animation improvements
- **v2.2.1** (Oct 2025) - Card animation timing fixes
- **v2.2.0** (Oct 2025) - Smooth card animations and visual effects

**Previous Versions:**
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

*For detailed release notes, see [CHANGELOG.md](CHANGELOG.md) or [GitHub Releases](https://github.com/ssfdre38/match-mania/releases)*

## 📚 Documentation

Comprehensive documentation is available in our [Wiki](https://github.com/ssfdre38/match-mania/wiki):

- **[Installation Guide](https://github.com/ssfdre38/match-mania/wiki/Installation-Guide)** - Detailed setup instructions
- **[Game Rules](https://github.com/ssfdre38/match-mania/wiki/Game-Rules)** - Complete gameplay mechanics
- **[Custom Rules](https://github.com/ssfdre38/match-mania/wiki/Custom-Rules)** - Configure your game
- **[FAQ](https://github.com/ssfdre38/match-mania/wiki/FAQ)** - Common questions answered
- **[Contributing](https://github.com/ssfdre38/match-mania/wiki/Contributing)** - How to contribute

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) to get started.

### Ways to Contribute

- 🐛 [Report bugs](https://github.com/ssfdre38/match-mania/issues/new?template=bug_report.md)
- 💡 [Request features](https://github.com/ssfdre38/match-mania/issues/new?template=feature_request.md)
- 🎮 [Report game balance issues](https://github.com/ssfdre38/match-mania/issues/new?template=game_balance.md)
- 💬 [Join discussions](https://github.com/ssfdre38/match-mania/discussions)
- 🔧 Submit pull requests

## 💬 Support

Need help? Have questions?

- 📖 Check the [Wiki](https://github.com/ssfdre38/match-mania/wiki)
- 💬 Join [Discussions](https://github.com/ssfdre38/match-mania/discussions)
- 🐛 Report [Issues](https://github.com/ssfdre38/match-mania/issues)
- 📧 Email: ssfdre38@gmail.com

## 🔄 CI/CD

This project uses GitHub Actions for:
- ✅ Automated builds on push/PR
- 🚀 Automated releases with signed APKs
- 🧪 Code quality checks and linting
- 📦 Artifact management

See [.github/workflows](.github/workflows) for workflow configurations.

## 🌟 Acknowledgments

Thanks to all contributors who help make Match Mania better!

## 👨‍💻 Developer

**Created by:** Daniel Elliott  
**Email:** ssfdre38@gmail.com  
**Copyright:** © 2025 Daniel Elliott

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

### What This Means

- ✅ You can use this code for personal and commercial purposes
- ✅ You can modify and distribute the code
- ✅ You must include the license and copyright notices
- ⚠️ This software is provided "as is" without warranties

## ⚖️ Legal Notice

This game is an original creation and does not use any trademarked names, logos, or designs. All game mechanics and visual elements are implemented independently.

---

**Enjoy Match Mania!** 🎮 If you like this project, please ⭐ star it on GitHub!
