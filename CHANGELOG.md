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

## [2.3.5] - 2025-10-04

### Added
- **Website-First OTA Updates**: App now checks official website API first for updates
- **Automatic Fallback**: If website unavailable, automatically falls back to GitHub API
- **Faster Update Checks**: Website API responds faster with 5-second timeout
- **Dual-Source Reliability**: No single point of failure for update distribution
- **Automated Release System**: Complete build and deployment script for streamlined releases

### Changed
- OTA update system now prioritizes website (https://matchmaina.ssfdre38.xyz/api/latest-version.json)
- GitHub API serves as reliable backup for update checks
- Improved error handling and logging for update sources

### Fixed
- OTA update system build issues resolved (correct method placement)
- Method structure corrected for proper Java compilation

### Technical
- Added `checkWebsiteAPI()` method for primary update source
- Added `checkGitHubAPI()` method for backup update source
- Website timeout: 5 seconds (fast response)
- GitHub timeout: 10 seconds (reliable fallback)
- Comprehensive logging for update source tracking

## [2.3.4] - 2025-10-04

### Changed
- Wiki updated with official website links
- Website featured as primary download source
- Documentation improvements across all platforms
- Prepared infrastructure for website-first OTA updates

### Added
- Official website API endpoint for version checking
- Downloads directory for website APK hosting

## [2.3.3] - 2025-10-04

### Added
- **Automatic Background Download**: Updates now download silently in background when detected
- **Smart Update Detection**: Checks if update already downloaded before re-downloading
- **APK Verification**: Validates downloaded APK file integrity (magic bytes check)
- **SHA-256 Hash Calculation**: Stores file hash for security verification
- **Install Ready Dialog**: Shows when update is downloaded and ready to install

### Changed
- **Two-Step Process**: Updates download automatically, then prompt user to install
- **Better User Experience**: Download happens in background, installation requires explicit approval
- **Improved Security**: File verification before installation
- **Google Play Protect Compatible**: Enhanced verification helps with security scanning

### Improved
- **Seamless Downloads**: No progress dialog interruption during automatic download
- **Download Persistence**: Downloaded updates saved until user installs or cancels
- **Error Recovery**: Better handling of download failures with fallback options
- **Storage Efficiency**: Verifies existing downloads before re-downloading

### Technical
- Added background download with DownloadManager (no UI interruption)
- Added APK file verification (checks magic bytes: PK signature)
- Added SHA-256 hash calculation and storage
- Added download state persistence (version, path, hash)
- Added `KEY_DOWNLOADED_VERSION`, `KEY_DOWNLOADED_PATH`, `KEY_DOWNLOAD_HASH` preferences
- Enhanced error handling and file validation
- Improved installation flow with separate download and install dialogs

### Security
- **File Verification**: Validates APK magic bytes before installation
- **Hash Storage**: SHA-256 hash stored for integrity verification
- **Size Check**: Ensures downloaded file meets minimum size requirements
- **Signature Verification**: Android system verifies APK signature during install

## [2.3.2] - 2025-10-04

### Added
- **Auto-Download & Install**: OTA system now automatically downloads and installs updates
- **Progress Dialog**: Visual progress bar during APK download
- **Smart Permissions**: Automatically requests install permission on Android 8+
- **FileProvider Integration**: Secure APK installation via FileProvider
- **Download Manager**: Uses Android DownloadManager for reliable downloads

### Changed
- **"Download" button** → **"Install Now"** button in update dialog
- Now downloads APK automatically instead of opening browser
- Launches Android package installer after download completes
- Shows download progress with percentage

### Improved
- **One-Click Updates**: Download and install with single button press
- **Better UX**: No more manual browser download and file navigation
- **Progress Feedback**: Users see download progress in real-time
- **Automatic Cleanup**: Old APK files automatically removed
- **Error Handling**: Better error messages and fallback options

### Technical
- Added DownloadManager integration
- Added ProgressDialog for download feedback
- Added BroadcastReceiver for download completion
- Added FileProvider configuration (file_paths.xml)
- Added REQUEST_INSTALL_PACKAGES permission
- Background thread for progress tracking

### Permissions
- INTERNET (existing)
- ACCESS_NETWORK_STATE (existing)
- REQUEST_INSTALL_PACKAGES (new - for Android 8+)

## [2.3.1] - 2025-10-04

### Fixed
- **About Page**: Version now displays dynamically from build configuration
- **Documentation**: Updated all version references to current (v2.3.0 → v2.3.1)
- **Documentation**: Updated all dates to October 2025

### Improved
- **About Page**: Features list updated with current capabilities
- **README**: Complete version history through v2.3.x
- **README**: Added OTA update system to features section
- **Consistency**: All version references and dates now accurate

### Technical
- AboutActivity.java: Dynamic version reading from PackageInfo
- README.md: Enhanced version history and features
- activity_about.xml: Updated build date and features
- VERSION_UPDATE_SUMMARY.md: Documentation of all updates

### Documentation
- All copyright notices: © 2025 Daniel Elliott
- All build dates: October 2025
- Complete version tracking through v2.3.1
- Future-proof version display (no more hard-coded versions)

## [2.3.0] - 2025-01-04

### Added
- **OTA Update System**: Automatic update checking using GitHub releases
- Checks for updates automatically on app launch (once per 24 hours)
- "Check for Updates" button in Settings for manual checks
- Update notification dialog with:
  - Current and new version information
  - Direct download link to latest APK
  - View details on GitHub
  - Skip version option
- Smart update detection using version codes
- Respects user preferences (skip version, check interval)

### Changed
- Version 2.2.7 → 2.3.0 (minor version bump for new feature)
- Added INTERNET and ACCESS_NETWORK_STATE permissions

### Technical
- New UpdateChecker.java class for OTA functionality
- GitHub API integration for release checking
- Background async update checking
- SharedPreferences for update preferences
- No user data collection, privacy-focused

### Documentation
- Added OTA_UPDATE_SYSTEM.md with full documentation
- Includes usage, testing, troubleshooting guides

## [2.2.7] - 2025-01-04

### Changed
- **Settings**: Alphabetized game play settings for easier navigation
- Settings now appear in alphabetical order:
  1. Action Card Stacking
  2. Challenge Wild +4
  3. Draw to Match
  4. Draw When Can't Play
  5. Force Play
  6. Jump-In Rule
  7. Progressive Draw
  8. Seven-Zero Rule
- Starting Cards slider remains at top (different control type)
- Improved settings organization and usability

### Technical
- activity_settings.xml: Reordered settings sections
- No functional changes, layout only
- Version 2.2.6 → 2.2.7

## [2.2.6] - 2025-01-04

### Changed
- **Readability**: Significantly larger corner numbers and icons on all cards
- Corner numbers: 16sp → 22sp (small cards, +37%), 24sp → 28sp (large cards, +17%)
- Corner icons: 12px → 18px (small cards, +50%), 20px → 24px (large cards, +20%)
- Increased edge padding: 8% → 10% for better spacing
- Increased corner padding: 10% → 12% for icons
- Improved text baseline positioning for better centering

### Improved
- Much better readability on phone screens
- Easier to identify cards at a glance
- Better accessibility for all users
- More professional playing card appearance
- Reduced eye strain during gameplay

### Technical
- CardView.java: Updated text sizes and padding calculations
- All elements still properly constrained within card bounds
- Version 2.2.5 → 2.2.6

## [2.2.5] - 2025-01-04

### Fixed
- **Card Display**: Fixed corner numbers appearing outside card boundaries on phone screens
- Replaced fixed pixel positioning with proportional calculations
- Corner elements now scale properly with card size
- Improved canvas clipping to prevent any overflow
- Reduced corner icon/text sizes for better fit

### Changed
- Corner numbers use proportional edge padding (8% of width)
- Corner icons use proportional padding (10% of width)
- Better bounds checking before drawing corners
- Tighter canvas clipping (exact card bounds)
- Smaller text sizes (16sp/24sp instead of 18sp/26sp)

### Technical
- CardView.java: Improved corner positioning algorithm
- All corner elements properly constrained
- Works on all screen sizes and densities
- Version 2.2.4 → 2.2.5

## [2.2.4] - 2025-01-04

### Fixed
- **UI Consistency**: Fixed card sizes looking different on phones vs tablets
- Cards now properly sized across all screen densities
- Responsive card sizing based on available screen width
- Maintains consistent 2:3 aspect ratio everywhere

### Added
- Tablet-optimized layout (layout-sw600dp) for screens ≥ 600dp wide
- Larger UI elements on tablets (text, buttons, cards)
- Better spacing and padding on large screens
- Smart card sizing with min/max bounds (70dp-100dp)

### Changed
- Replaced fixed density calculation with responsive algorithm
- Cards scale proportionally to screen width
- Improved screen space utilization
- Better visual consistency across devices

### Technical
- Created layout-sw600dp variant for tablets
- Added screen width-based card sizing
- Maintains proper aspect ratios
- Version 2.2.3 → 2.2.4

## [2.2.3] - 2025-01-04

### Fixed
- **Code Quality**: Fixed draw card turn advancement logic
- Changed GameEngine.nextPlayer() from private to public
- Fixed 3 instances where code tried to "play the top card" to advance turn
- Now correctly calls nextPlayer() directly when no playable card

### Changed
- Improved code clarity and maintainability
- Better API design for GameEngine
- More logical code flow

### Technical
- Same gameplay behavior (non-functional improvement)
- Code now matches intent clearly
- No breaking changes
- Version 2.2.2 → 2.2.3

## [2.2.2] - 2025-01-04

### Fixed
- **Critical**: Completely resolved cards disappearing from played card area
- Disabled all problematic animations to ensure 100% stability
- Removed top card flip animation
- Removed current player pulse animation
- Removed hand card animations (bounce, slide-in)
- Removed draw button animation
- Removed new game fade-in

### Changed
- Simplified updateUI() method - direct updates only
- Instant response for all actions (no animation delays)
- Cleaner, more maintainable code
- Better performance and battery life

### Technical
- Kept only shake animation for invalid moves (works reliably)
- Removed ~30 lines of animation code
- Eliminated all view state management issues
- Version 2.2.1 → 2.2.2

### Result
- 100% stable gameplay
- All UI elements always visible
- No disappearing cards ever
- Completely reliable

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
