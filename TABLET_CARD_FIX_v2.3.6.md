# Tablet Card Display Fix - Version 2.3.6

**Date**: October 4, 2025  
**Issue**: Cards displaying as narrow on tablets in landscape mode  
**Status**: ✅ FIXED

## Problem Description

On tablets (7-10 inch screens), player cards in the hand were appearing narrow and not utilizing the available screen space effectively. This was particularly noticeable in landscape mode where there's plenty of horizontal space.

## Root Cause

The card width calculation in `MainActivity.java` was limiting the maximum card width to 100dp for all devices. While this works well for phones, tablets have much larger screens and can accommodate wider cards without issues.

## Solution

Enhanced the card sizing logic to detect tablet devices and apply appropriate size limits:

### Code Changes

**File**: `app/src/main/java/com/cardstack/game/MainActivity.java`

```java
// Before (lines 198-199):
int maxCardWidth = (int)(getResources().getDisplayMetrics().density * 100); // 100dp max
int minCardWidth = (int)(getResources().getDisplayMetrics().density * 70);  // 70dp min

// After (lines 198-206):
// Check if device is a tablet (smallest width >= 600dp)
float smallestWidthDp = getResources().getConfiguration().smallestScreenWidthDp;
boolean isTablet = smallestWidthDp >= 600;

int maxCardWidth = isTablet ? 
    (int)(getResources().getDisplayMetrics().density * 140) : // 140dp max for tablets
    (int)(getResources().getDisplayMetrics().density * 100);  // 100dp max for phones
int minCardWidth = (int)(getResources().getDisplayMetrics().density * 70);  // 70dp min
```

### Technical Details

- **Detection Method**: Uses Android's `smallestScreenWidthDp` configuration qualifier
- **Tablet Threshold**: Devices with smallest width >= 600dp are considered tablets
- **Card Size Increase**: Maximum card width increased from 100dp to 140dp for tablets (40% increase)
- **Aspect Ratio**: Maintains proper 2:3 card aspect ratio for all screen sizes
- **Phone Compatibility**: Phone card sizes remain unchanged (100dp max)

## Testing Results

### Tablet (sw600dp+)
- ✅ Cards fill screen width appropriately
- ✅ Better visual appearance in landscape mode
- ✅ Maintains proper aspect ratio
- ✅ No overlapping or spacing issues

### Phone
- ✅ Card sizes unchanged
- ✅ No regression in phone display
- ✅ Portrait and landscape modes work correctly

## Version Information

- **Version**: 2.3.6
- **Version Code**: 24
- **Previous Version**: 2.3.5
- **Build Date**: October 4, 2025

## Files Modified

1. `app/build.gradle` - Updated version to 2.3.6
2. `app/src/main/java/com/cardstack/game/MainActivity.java` - Enhanced card sizing logic
3. `CHANGELOG.md` - Added v2.3.6 release notes

## Deployment

- ✅ GitHub Release: https://github.com/ssfdre38/match-mania/releases/tag/v2.3.6
- ✅ Website: https://matchmaina.ssfdre38.xyz
- ✅ Website API: Updated to v2.3.6
- ✅ APK Available: MatchMania-release-v2.3.6.apk (4.3MB)

## Changelog Entry

### Fixed
- **Tablet Card Display**: Fixed narrow card display on tablets in landscape mode
- Cards now properly scale to fill screen on tablets (up to 140dp instead of 100dp)
- Improved card sizing detection using `smallestScreenWidthDp` for tablets (>=600dp)
- Better visual appearance on 7-10 inch tablets

### Technical
- Enhanced card width calculation to detect tablet devices
- Maximum card width increased from 100dp to 140dp for tablets
- Maintains proper 2:3 aspect ratio for all screen sizes
- Phone card sizes remain unchanged (100dp max)

## Future Considerations

- Consider adding user preference for card size scaling
- Monitor feedback from tablet users for further optimizations
- Potential to add different card sizes for different tablet sizes (7", 10", etc.)
