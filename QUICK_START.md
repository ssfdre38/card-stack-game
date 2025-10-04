# Quick Start Guide - Card Stack

## Installation Methods

### Method 1: Android Studio (Recommended)

1. **Open the Project**
   ```bash
   # Start Android Studio
   # File > Open
   # Navigate to: /home/ubuntu/CardStackGame
   # Click OK
   ```

2. **Wait for Gradle Sync**
   - Android Studio will automatically download dependencies
   - This may take a few minutes on first run

3. **Run on Emulator**
   ```
   # Create an emulator if you don't have one:
   # Tools > Device Manager > Create Device
   # Select a phone (e.g., Pixel 6)
   # Select a system image (API 24+)
   # Click Run (green triangle icon)
   ```

4. **Or Build APK**
   ```
   # Build > Build Bundle(s) / APK(s) > Build APK(s)
   # APK will be at: app/build/outputs/apk/debug/app-debug.apk
   ```

### Method 2: Command Line (Advanced)

```bash
cd /home/ubuntu/CardStackGame

# If you have Gradle wrapper (recommended):
./gradlew assembleDebug

# Install on connected device:
adb install app/build/outputs/apk/debug/app-debug.apk
```

## How to Play

### Starting the Game
- Launch "Card Stack" from your app drawer
- Game starts automatically with you vs 3 AI players
- You always go first

### Your Turn
1. **Look at your hand** - Cards displayed at bottom
2. **Playable cards** appear at full brightness
3. **Tap a card** to play it
4. **Wild cards** will prompt you to choose a color
5. **No playable cards?** Tap "Draw Card"
   - If drawn card is playable, you can play it
   - Otherwise, turn is skipped

### Understanding Cards

**Card Format**: `[Color][Value]`
- Examples: R5 (Red 5), BS (Blue Skip), G+2 (Green Draw Two)

**Colors**:
- R = Red
- B = Blue  
- G = Green
- Y = Yellow

**Special Cards**:
- **S** (Skip) - Next player loses turn
- **R** (Reverse) - Change direction
- **+2** (Draw Two) - Next player draws 2 cards
- **W** (Wild) - Choose any color
- **W+4** (Wild Draw Four) - Choose color, next player draws 4

### Winning
- First player to play all cards wins!
- Dialog will show the winner
- Click "New Game" to play again

## Game Strategy Tips

### Playing Against AI

1. **Use Action Cards Wisely**
   - Skip/Reverse can give you another turn
   - Draw Two weakens next player

2. **Wild Cards**
   - Save Wild Draw Four for emergencies
   - Use regular Wild to control color

3. **Color Management**
   - Try to keep multiple colors in hand
   - Don't let one color dominate

4. **Numbers Matter**
   - Multiple cards of same number = flexibility
   - Can match by number OR color

### What the AI Does

The AI players follow this strategy:
1. Play Wild Draw Four if possible
2. Then Draw Two cards
3. Then Skip/Reverse
4. Then regular Wild cards
5. Finally number cards
6. Draw if no playable cards

AI chooses wild colors based on what they have most of in their hand.

## Troubleshooting

### Build Issues

**"SDK not found"**
```bash
# In Android Studio:
# Tools > SDK Manager
# Install Android SDK Platform 34
# Install Android SDK Build-Tools 34
```

**"Gradle sync failed"**
```bash
# Try:
# File > Invalidate Caches / Restart
# Let Gradle re-download dependencies
```

### Runtime Issues

**App won't install**
```bash
# Uninstall old version first:
adb uninstall com.cardstack.game

# Then reinstall:
adb install app-debug.apk
```

**App crashes on start**
- Check Android version is 7.0+ (API 24+)
- Try clean build: Build > Clean Project, then Build > Rebuild Project

## Project Location

All files are in: `/home/ubuntu/CardStackGame/`

## File Structure Quick Reference

```
CardStackGame/
├── app/src/main/
│   ├── java/com/cardstack/game/
│   │   ├── MainActivity.java      # Main game screen
│   │   ├── Card.java              # Card properties
│   │   ├── Deck.java              # Shuffling logic
│   │   ├── Player.java            # AI logic
│   │   └── GameEngine.java        # Game rules
│   ├── res/
│   │   ├── layout/activity_main.xml    # UI design
│   │   ├── values/themes.xml           # Colors/styles
│   │   └── mipmap-*/ic_launcher*.png   # App icons
│   └── AndroidManifest.xml        # App config
└── README.md                      # Full documentation
```

## Next Steps

1. **Test the game** - Make sure it works as expected
2. **Customize** - Modify colors, add features, adjust AI
3. **Publish** - Deploy to Play Store after thorough testing
4. **Extend** - Add sounds, animations, multiplayer, etc.

## Support Files

- **README.md** - Complete documentation
- **GAME_SUMMARY.md** - Feature overview
- **QUICK_START.md** - This file

## Have Fun!

The game is ready to play. Enjoy competing against the AI players and trying to win by playing all your cards first!
