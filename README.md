# Match Mania - Android Card Game

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Android-5.0%2B-green.svg)](https://developer.android.com)
[![Latest Release](https://img.shields.io/github/v/release/ssfdre38/match-mania)](https://github.com/ssfdre38/match-mania/releases/latest)
[![Website](https://img.shields.io/badge/Website-matchmaina.ssfdre38.xyz-blue.svg)](https://matchmaina.ssfdre38.xyz)

Match Mania is an Android card game featuring AI opponents, player statistics, customizable profiles, and cryptographically secure random card shuffling.

**🌐 Official Website:** [https://matchmaina.ssfdre38.xyz](https://matchmaina.ssfdre38.xyz)

## 📋 Table of Contents

- [Official Website](#official-website)
- [Features](#features)
- [Installation](#installation)
- [Game Rules](#game-rules)
- [Building from Source](#building-the-app)
- [Automated Testing](#automated-testing-system)
- [Contributing](#contributing)
- [Documentation](#documentation)
- [Support](#support)
- [License](#license)

## Official Website

**🌐 [https://matchmaina.ssfdre38.xyz](https://matchmaina.ssfdre38.xyz)**

Visit our official website for:
- 📥 Direct APK downloads for all versions
- 📝 Complete changelog and release notes
- 📸 Screenshots and game information
- 📚 Full documentation and guides
- 🧪 [Automated Testing System](https://matchmaina.ssfdre38.xyz/testing.html) - Developer resources
- ℹ️ About the game and developer info

**Recent Website Updates:**
- ✨ Testing page redesigned for better consistency with main site
- 📱 Enhanced responsive design for mobile and tablet devices
- 🎨 Improved visual hierarchy and professional polish
- 📖 Complete testing documentation with quick start guide

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
Download the latest APK from:
- **Official Website**: [https://matchmaina.ssfdre38.xyz](https://matchmaina.ssfdre38.xyz) (Recommended)
- **GitHub Releases**: [releases page](https://github.com/ssfdre38/match-mania/releases/latest)

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

## 🚀 Recent Development (October 2025)

### Latest Improvements

**v2.3.15 - Website Design Consistency (Current)**
- 🎨 Testing page completely redesigned to match main site aesthetics
- 📊 Enhanced stats section with larger numbers and better visual impact
- 🔢 Numbered step badges in Quick Start guide for better UX
- 📱 Improved mobile responsiveness across all sections
- ⚖️ Maintained complete copyright and attribution compliance

**v2.3.14 - Layout & Rotation Support**
- 📱 Fixed player card layout appearing below screen on phones
- 🔄 Added full screen rotation support (portrait/landscape/all orientations)
- 📐 Improved ScrollView and layout constraints
- ✅ Better tablet and large screen support

**v2.3.13 - Perfect Card Display**
- 🎴 Player cards now display at full height without cutting
- 📏 Optimized space allocation (top card reduced, player area increased)
- 💯 Guaranteed 500dp height for player card area
- 🖼️ Cards display at ideal 240dp×360dp on tablets

### Testing System Development
- 🧪 Created comprehensive automated testing framework
- 📋 19 test cases across 4 categories (UI, Gameplay, OTA, Performance)
- 🖥️ Support for 5 Android versions (API 24-33)
- 📸 Automatic screenshot capture on errors
- 📊 Professional HTML reports with statistics
- 🌐 Dedicated testing documentation website

## 🧪 Automated Testing System

Match Mania includes a comprehensive automated testing system for developers:

**Features:**
- ✅ Tests across 5 Android versions (API 24, 26, 28, 30, 33)
- ✅ 19 automated test cases covering UI, gameplay, OTA, and performance
- ✅ Automatic screenshot capture on errors
- ✅ Professional HTML reports with test results and statistics
- ✅ Easy configuration via JSON
- ✅ Complete documentation

**Quick Start:**
```bash
cd emulator-testing
./setup-system-images.sh    # One-time setup (~2-4 GB download)
./run-comprehensive-tests.sh # Run all tests (~50-75 minutes)
```

**Learn More:**
- 📚 [Testing Documentation](emulator-testing/README.md)
- 🌐 [Testing System Website](https://matchmaina.ssfdre38.xyz/testing.html)
- ⚖️ [Copyright & Attribution](emulator-testing/COPYRIGHT.md)

## Version History

**Latest Releases:**
- **v2.3.15** (Oct 2025) - Website design consistency improvements
- **v2.3.14** (Oct 2025) - Phone layout fixes and full screen rotation support
- **v2.3.13** (Oct 2025) - Perfect player card display on all screen sizes
- **v2.3.12** (Oct 2025) - Tablet card width improvements (33% wider cards)
- **v2.3.11** (Oct 2025) - OTA user choice flow with auto-install
- **v2.3.10** (Oct 2025) - Enhanced tablet card display and about page
- **v2.3.9** (Oct 2025) - Always auto-download OTA updates
- **v2.3.8** (Oct 2025) - Tablet card width fix for landscape mode
- **v2.3.7** (Oct 2025) - Android 13+ OTA update fix
- **v2.3.6** (Oct 2025) - Tablet card display improvements
- **v2.3.5** (Oct 2025) - Website-first OTA updates with GitHub fallback
- **v2.3.0** (Oct 2025) - OTA update system with automatic GitHub release checking
- **v2.2.7** (Oct 2025) - Alphabetized game settings
- **v2.2.0** (Oct 2025) - Smooth card animations and visual effects

**Previous Versions:**
- **v2.0.0** - Match Mania rebrand with signed APK, statistics, and profile customization
- **v1.4.0** - Added statistics tracking and profile customization
- **v1.3.0** - Custom game rules & settings
- **v1.0.0** - Initial release

*For detailed release notes, see [CHANGELOG.md](CHANGELOG.md), [docs/releases/](docs/releases/), or [GitHub Releases](https://github.com/ssfdre38/match-mania/releases)*

## 📚 Documentation

Comprehensive documentation is available in multiple locations:

### Wiki & Guides
- **[Installation Guide](https://github.com/ssfdre38/match-mania/wiki/Installation-Guide)** - Detailed setup instructions
- **[Game Rules](https://github.com/ssfdre38/match-mania/wiki/Game-Rules)** - Complete gameplay mechanics
- **[Custom Rules](https://github.com/ssfdre38/match-mania/wiki/Custom-Rules)** - Configure your game
- **[FAQ](https://github.com/ssfdre38/match-mania/wiki/FAQ)** - Common questions answered
- **[Quick Start Guide](QUICK_START.md)** - Get started quickly

### Project Documentation

Organized documentation in the [docs/](docs/) directory:

- 📦 **[Release Notes](docs/releases/)** - Version-specific release documentation
- 🐛 **[Bug Fixes](docs/bugfixes/)** - Bug fix summaries and patches
- ✨ **[Features](docs/features/)** - Feature implementation details
- 🔧 **[Development](docs/development/)** - Development process and reviews
- 🧪 **[Testing](docs/testing/)** - Test reports and quality assurance
- 📚 **[Archives](docs/archives/)** - Historical documents and references

### Additional Resources

- 📋 [CHANGELOG.md](CHANGELOG.md) - Complete version history
- 🧪 [Testing System](emulator-testing/README.md) - Automated testing framework
- ⚖️ [Copyright Info](emulator-testing/COPYRIGHT.md) - Third-party attributions
- 🤝 [Contributing Guidelines](CONTRIBUTING.md) - How to contribute
- 🔒 [Security Policy](SECURITY.md) - Security guidelines

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

- 🌐 Visit our [Official Website](https://matchmaina.ssfdre38.xyz)
- 📖 Check the [Wiki](https://github.com/ssfdre38/match-mania/wiki)
- 💬 Join [Discussions](https://github.com/ssfdre38/match-mania/discussions)
- 🐛 Report [Issues](https://github.com/ssfdre38/match-mania/issues)
- 📧 Email: ssfdre38@gmail.com

### 💖 Support Development

Match Mania is free and open source. If you enjoy the game, consider supporting its development:

- 💖 [GitHub Sponsors](https://github.com/sponsors/ssfdre38)
- 💵 [Cash App: $ssfdre38](https://cash.app/$ssfdre38)

Your support helps maintain the app, add new features, and keep it free for everyone!

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
