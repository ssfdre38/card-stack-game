# Changelog

All notable changes to Match Mania will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned Features
- Real-time multiplayer (local and online)
- Additional rule variations (Jump-In, Seven-O, Progressive)
- Card theme customization
- Tournament mode with scoring
- Cloud save and sync
- More AI difficulty levels
- Sound effects and music
- Haptic feedback
- Achievements system expansion
- Tutorial mode for new players

## [2.2.1] - 2025-01-04

### Fixed
- **Critical Bug**: Fixed cards disappearing from hand after playing them
- Removed problematic card play animation from hand cards
- Cards now play immediately with better responsiveness
- Hand display refreshes correctly every time
- Top card flip animation still works perfectly

### Changed
- Reduced bounce animation intensity for playable cards (more subtle)
- Improved animation stability by removing fillAfter flags
- Faster card play response (removed 300ms animation delay)

### Technical
- Simplified `playHumanCard()` method in MainActivity
- Updated bounce.xml animation parameters
- No breaking changes or migration required

## [2.2.0] - 2025-01-04

### Added
- **Smooth Animations System**: Comprehensive animation framework throughout the game
- **Card Play Animation**: Cards scale up, fade out, and move when played
- **Card Draw Animation**: New cards appear with fade-in and scale effect
- **Card Flip Animation**: Top card flips smoothly when changed (3D effect)
- **Current Player Pulse**: Subtle pulse animation highlights whose turn it is
- **Invalid Move Shake**: Cards shake when trying to play illegal moves
- **Staggered Hand Animations**: Cards slide in sequentially with 30ms delays
- **Playable Card Bounce**: Subtle bounce effect on cards that can be played
- **Button Feedback**: Draw button bounces when pressed
- **New Game Fade**: Smooth fade-in transition when starting new game

### Changed
- Enhanced visual feedback for all user interactions
- Improved UI responsiveness with animation timing
- Updated `MainActivity.java` with comprehensive animation support
- Optimized animation durations for best user experience (200-500ms)

### Technical
- Created 9 animation XML resource files
- Added animation imports to MainActivity
- Hardware-accelerated animations for 60 FPS performance
- Zero performance impact with optimized timing
- Compatible with Android 7.0+ (API 24)

## [2.1.1] - 2025-01-04

### Fixed
- Fixed special card corner icon rendering issues
- Improved corner number placement on Skip, Reverse, and Draw Two cards
- Enhanced card drawing code for better visual consistency

### Documentation
- Added GitHub workflows for CI/CD
- Created comprehensive wiki documentation
- Added issue templates for bugs, features, and balance
- Added pull request template
- Enabled GitHub Discussions

## [2.1.0] - 2025-01-04

### Added
- **Draw to Match Rule**: Optional rule that forces players to draw until they get a playable card
- Added rule to custom settings menu
- AI players now adapt strategy for Draw to Match rule

### Changed
- Improved settings menu organization
- Enhanced rule descriptions in settings

### Fixed
- Minor UI adjustments for rule toggles

## [2.0.2] - 2025-01-04

### Changed
- Repository renamed from `card-game-android` to `match-mania`
- Updated all internal references and links
- Updated copyright information throughout codebase
- Improved About page with correct copyright notice

### Documentation
- Updated README with new repository name
- Fixed all GitHub links in documentation
- Updated build scripts with new app name

## [2.0.1] - 2025-01-04

### Fixed
- Resolved APK installation issues
- Properly signed release builds with keystore
- Verified both debug and release APKs install correctly
- Fixed package manager errors on install

### Changed
- Improved build process and automation
- Enhanced testing procedures

## [2.0.0] - 2025-01-04

### Added
- **Official App Name**: Rebranded as "Match Mania"
- **Signed APKs**: Production-ready signed release builds
- **About Page**: Added proper licensing and copyright information
- **Enhanced UI**: Improved card design with better visual consistency

### Changed
- Updated all branding from generic name to Match Mania
- Improved APK signing process with keystore
- Enhanced build and release workflow

### Documentation
- Created comprehensive release notes
- Updated installation guides
- Added troubleshooting documentation

## [1.4.0] - 2025-01-03

### Added
- **Player Statistics System**
  - Track games played, won, and lost
  - Win rate and win streak tracking
  - Card statistics (played, drawn, special cards)
  - Time tracking (total play time, average game duration)
  - Match history with last 50 games
  - Reset statistics option
  
- **Player Customization**
  - Custom player names (up to 20 characters)
  - 48 avatar options (emojis, people, animals, symbols)
  - Profile persistence across sessions
  - Customization accessible from settings

- **Enhanced Game Over Screen**
  - Display winner with statistics
  - Quick access to play again
  - View detailed stats option

## [1.3.3] - 2025-01-03

### Added
- **Dynamic AI Players**: AI opponents now have randomized names and avatars
- 20+ unique AI names
- Different avatar for each AI player
- Visual distinction between all players

### Changed
- Improved player identification in UI
- Enhanced visual appeal with varied avatars

## [1.3.2] - 2025-01-03

### Fixed
- **Card UI Fix**: Resolved number overflow on cards
- Fixed corner number rendering on all card types
- Improved text sizing and positioning
- Better handling of special card icons

### Added
- **About Page**: Added licensing and copyright information
- Display app version
- Developer credits
- License information

## [1.3.1] - 2025-01-03

### Fixed
- Card corner numbers not displaying correctly
- Text positioning issues on card borders
- Improved card rendering code

## [1.3.0] - 2025-01-03

### Added
- **Custom Game Rules**: Configurable rules via Settings menu
  - Stacking rule (stack +2 cards)
  - Force Play rule (must play drawn playable cards)
  - Future-ready for additional rule variations
  
- **Settings Menu**: Central location for all game customization
- **Rule Descriptions**: Helpful text explaining each rule option

### Changed
- Improved menu navigation and structure
- Enhanced settings UI with better organization

## [1.2.1] - 2025-01-02

### Fixed
- **Game Rules Implementation**: Corrected card matching logic
  - Fixed color matching validation
  - Fixed number matching validation
  - Special cards can be played on matching type or color
  - Skip cards now properly match other skip cards
  - Draw Two cards now properly match other Draw Two cards

### Changed
- Improved AI decision-making with corrected rules
- Better validation of legal moves

## [1.2.0] - 2025-01-02

### Added
- **Screen Rotation Support**: App now supports both portrait and landscape orientations
- Responsive UI that adapts to orientation changes
- Proper state preservation during rotation

### Fixed
- Various layout issues in different orientations
- Improved card arrangement for landscape mode

## [1.1.0] - 2025-01-02

### Added
- **Visual Card Icons**: Cards now display icons matching their type
  - Number cards show their number
  - Skip cards show "S" symbol
  - Reverse cards show "R" symbol
  - Draw Two shows "+2"
  - Wild shows "W"
  - Wild Draw Four shows "W+4"
  
- **Enhanced Card Design**: Improved visual appearance
  - Corner numbers for easy identification
  - Center icons for quick recognition
  - Better color contrast

### Changed
- Improved card rendering system
- Enhanced visual hierarchy

## [1.0.0] - 2025-01-01

### Added
- **Initial Release**: First public version of Match Mania
- **4-Player Gameplay**: Human player vs 3 AI opponents
- **AI Players**: Strategic computer opponents
- **True Randomness**: Cryptographically secure card shuffling using SecureRandom
- **Classic Card Types**:
  - Number cards (0-9 in four colors)
  - Skip cards
  - Reverse cards
  - Draw Two cards
  - Wild cards
  - Wild Draw Four cards
  
- **Core Gameplay Mechanics**:
  - Match by color or number
  - Special action cards
  - Automatic calling with one card
  - Win detection
  
- **Beautiful UI**:
  - Dark theme interface
  - Color-coded cards
  - Clear player indicators
  - Intuitive controls

### Technical
- Built for Android 5.0 (API 21) and higher
- Java-based implementation
- Gradle build system
- No internet permission required
- No ads or in-app purchases

## Version Numbering

Match Mania follows Semantic Versioning (MAJOR.MINOR.PATCH):

- **MAJOR**: Incompatible API changes or major features
- **MINOR**: New functionality in a backward-compatible manner
- **PATCH**: Backward-compatible bug fixes

### Release Types

- **Stable Releases**: Thoroughly tested versions (e.g., 2.0.0)
- **Beta Releases**: Feature-complete but may have bugs (e.g., 2.1.0-beta)
- **Alpha Releases**: Early testing versions (e.g., 2.2.0-alpha)

## Links

- [Latest Release](https://github.com/ssfdre38/match-mania/releases/latest)
- [All Releases](https://github.com/ssfdre38/match-mania/releases)
- [Issue Tracker](https://github.com/ssfdre38/match-mania/issues)
- [Documentation](https://github.com/ssfdre38/match-mania/wiki)

---

**Note**: Dates are in YYYY-MM-DD format. Versions before 2.0.0 may have incomplete historical records.
