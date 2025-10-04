# Card Stack - Android Card Game

Card Stack is an Android card game similar to Uno, featuring AI opponents and cryptographically secure random card shuffling. The game avoids using any trademarked names or designs.

## Features

- **4-Player Game**: Play against 3 AI opponents
- **True Randomness**: Uses Java's `SecureRandom` for cryptographically secure card shuffling
- **Smart AI Players**: AI opponents make strategic decisions based on card types and hand composition
- **Classic Card Game Rules**: Similar to popular matching card games
- **Modern UI**: Clean, dark-themed interface with color-coded cards
- **Original Design**: No trademarked names or graphics

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

## Card Distribution

The deck contains 108 cards:
- **Number Cards (0-9)**: 19 of each color (Red, Blue, Green, Yellow)
  - One 0 card per color
  - Two each of 1-9 per color
- **Action Cards**: 8 of each type per color
  - Skip, Reverse, Draw Two
- **Wild Cards**: 4 Wild, 4 Wild Draw Four

## Building the App

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK 24 or higher
- Java 8 or higher

### Build Steps

1. Open the project in Android Studio:
   ```bash
   # Open Android Studio and select "Open an existing project"
   # Navigate to the CardStackGame directory
   ```

2. Wait for Gradle to sync dependencies

3. Build the APK:
   - Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
   - Or run: `./gradlew assembleDebug` (on macOS/Linux)
   - Or run: `gradlew.bat assembleDebug` (on Windows)

4. The APK will be located at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

5. Install on device:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Running in Emulator

1. Create an Android Virtual Device (AVD) in Android Studio
2. Click the **Run** button (green triangle)
3. Select your emulator

## Project Structure

```
CardStackGame/
├── app/
│   ├── src/main/
│   │   ├── java/com/cardstack/game/
│   │   │   ├── MainActivity.java      # Main game activity
│   │   │   ├── Card.java              # Card model
│   │   │   ├── Deck.java              # Deck with secure shuffling
│   │   │   ├── Player.java            # Player with AI logic
│   │   │   └── GameEngine.java        # Game logic engine
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml  # Main UI layout
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── mipmap-*/              # App icons
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## Technical Details

### Random Number Generation
The game uses `java.security.SecureRandom` for card shuffling, providing cryptographically strong random number generation. This ensures true randomness in card distribution.

### AI Strategy
AI players use a strategic decision-making process:
1. Prioritize playing Wild Draw Four cards
2. Then Draw Two cards
3. Then Skip/Reverse cards
4. Then regular Wild cards
5. Finally, number cards
6. When choosing wild card colors, AI selects the color most prevalent in their hand

### Game Engine
The `GameEngine` class manages:
- Turn order and direction
- Card playing validation
- Special card effects
- Automatic deck reshuffling when depleted
- Win condition detection

## Icon Design

The app icon features three overlapping cards in red, blue, and green, fanned out to create a dynamic stack effect. The design is original and doesn't use any trademarked elements.

## License

This is a personal project created for educational purposes. The game mechanics are inspired by popular card games but use original implementation and design.

## Future Enhancements

Potential improvements:
- Multiplayer over network
- Difficulty levels for AI
- Statistics tracking
- Sound effects and animations
- Customizable rules
- Tournament mode

## Support

For issues or questions, please open an issue in the project repository.

---

**Note**: This game is an original creation and does not use any trademarked names, logos, or designs. All game mechanics and visual elements are implemented independently.
