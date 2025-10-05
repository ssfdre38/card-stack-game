# Match Mania v2.1.0 - Draw to Match Rule

## Release Date
October 4, 2024

## New Features

### Draw to Match Custom Rule
Added a new custom rule option in the Settings menu that changes how drawing cards works:

- **Standard Rule (Default)**: When you can't play, you draw one card and your turn ends
- **Draw to Match Rule (New)**: When you can't play, you must keep drawing cards until you get a playable card (matching color, number, or special card)

This rule makes the game more dynamic and can lead to dramatic changes in hand sizes, adding an exciting new dimension to gameplay strategy.

## How to Enable

1. Open the app and tap the Settings button
2. Scroll down to find the "Draw to Match" toggle
3. Enable the switch to activate this rule
4. Save settings and start a new game

## Implementation Details

- The rule applies to both human and AI players
- Safety limit of 20 cards to prevent infinite loops
- Works with all card types (numbers, Skip, Reverse, Draw Two, and Wild cards)
- Compatible with all other custom rules

## Technical Changes

- Updated `GameSettings.java` with new `isDrawToMatchEnabled()` method
- Modified `MainActivity.java` draw button logic to support the new rule
- Updated AI player drawing logic in `processAITurns()` method
- Added UI toggle in Settings screen (`activity_settings.xml`)
- Version bumped to 2.1.0 (versionCode 8)

## Files Modified

- `app/src/main/java/com/cardstack/game/GameSettings.java`
- `app/src/main/java/com/cardstack/game/SettingsActivity.java`
- `app/src/main/java/com/cardstack/game/MainActivity.java`
- `app/src/main/res/layout/activity_settings.xml`
- `app/build.gradle`

## Build Information

- Debug APK: MatchMania-debug-v2.1.0.apk (5.3 MB)
- Release APK: MatchMania-release-v2.1.0.apk (4.3 MB)
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)

## Copyright

Copyright © 2025 Daniel Elliott
Licensed under MIT License
