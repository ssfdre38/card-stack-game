# Match Mania v2.1.1 Release Notes

## Bug Fixes

### Card UI Improvements
- **Fixed Special Card Corner Icons**: Corrected the positioning of corner icons on special cards (Skip, Reverse, Draw Two, Wild, Wild Draw Four) to ensure they display properly within card bounds
- Icons no longer overflow or appear truncated on special cards
- Improved icon sizing and padding for better visual consistency
- Added proper clipping and bounds checking for all card elements

### Technical Changes
- Added overloaded `drawCardIcon()` method to support custom icon sizes for corner decorations
- Improved canvas transformation handling for rotated corner icons
- Enhanced corner icon positioning algorithm to account for card padding and bounds
- Optimized icon rendering to prevent drawing outside card boundaries

## What's Included
- **Debug APK**: `MatchMania-debug-v2.1.1.apk` - For testing and development
- **Release APK**: `MatchMania-release-v2.1.1.apk` - Signed production-ready version

## Installation
Download either the debug or release APK and install on your Android device (API 24+).

## Known Issues
None reported for this version.

---

**Copyright © 2025 Daniel Elliott**  
Licensed under the MIT License
