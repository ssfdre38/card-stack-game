# Phone Layout Fix - v2.3.13

## Issue
Player card area was extending below the visible screen on phones, making cards partially or completely invisible. The layout used a fixed height of 500dp for the card scroll view, which is too large for most phone screens.

## Root Cause
In `app/src/main/res/layout/activity_main.xml` (phone layout):
- The `HorizontalScrollView` containing player cards had a fixed height: `android:layout_height="500dp"`
- This caused the layout to extend beyond the screen boundaries on phones
- The "New Game" button and player cards were pushed below the visible area

## Solution
Changed the `HorizontalScrollView` to use flexible sizing with weight-based layout:

### Before:
```xml
<HorizontalScrollView
    android:layout_width="match_parent"
    android:layout_height="500dp"
    android:scrollbars="none">
```

### After:
```xml
<HorizontalScrollView
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_weight="1"
    android:scrollbars="none">
```

## Additional Changes
- Reduced "New Game" button top margin from `16dp` to `8dp` for better space utilization
- This provides slightly more room for player cards while maintaining good visual spacing

## Benefits
1. **Adaptive Layout**: Cards area now uses all available screen space
2. **No Overflow**: Content stays within visible screen boundaries
3. **Works on All Phones**: Automatically adapts to different screen sizes
4. **Consistent Approach**: Matches the tablet layout's flexible sizing strategy

## Testing
- ✅ Build successful (assembleDebug)
- ✅ Layout XML validated
- ✅ No compilation errors

## Files Modified
- `app/src/main/res/layout/activity_main.xml` (lines 179-201)

## Version
- Version: 2.3.13
- Date: January 2025
- Build: Successful

## Notes
The tablet layout (`layout-sw600dp/activity_main.xml`) already used the correct flexible approach with `android:layout_height="0dp"` and `android:layout_weight="1"`, so no changes were needed there.

## Next Steps
1. Test on physical phones with various screen sizes
2. Verify card display in both portrait and landscape orientations
3. Check that all UI elements are visible and properly positioned
4. Update CHANGELOG.md with this fix for the next release
