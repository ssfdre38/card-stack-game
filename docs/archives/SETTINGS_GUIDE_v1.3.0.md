# Settings System - v1.3.0

## New Features

### 1. Custom Game Rules Settings ⚙️
Players can now customize their game experience with a comprehensive settings menu!

### 2. Fixed Card UI Overflow Issue ✅
Numbers and icons now properly constrained within card boundaries - no more overflow!

---

## Accessing Settings

**From Main Screen:**
- Tap the ⚙ (gear) icon in the top-right corner
- Or start a new game after changing settings

---

## Available Settings

### 🎴 Starting Cards (5-10)
**Default: 7 cards**

Adjust how many cards each player starts with:
- **5 cards**: Quick games (easier to win)
- **7 cards**: Standard gameplay (balanced)
- **10 cards**: Long games (more strategic)

---

### 🔄 Action Card Stacking
**Default: Enabled ✅**

Allow action cards to match by type regardless of color:
- **Enabled**: Skip→Skip, Reverse→Reverse, +2→+2 (any colors)
- **Disabled**: Action cards ONLY match by color

**Example when enabled:**
- ✅ Red Skip on Blue Skip
- ✅ Green Reverse on Yellow Reverse

**Example when disabled:**
- ❌ Red Skip on Blue Skip (different colors)
- ✅ Red Skip on Red 3 (same color only)

---

### 📤 Draw When Can't Play
**Default: Enabled ✅**

Must draw a card when you have no valid plays:
- **Enabled**: Must draw if cannot play (standard rules)
- **Disabled**: Can skip turn without drawing

---

### 🚀 Jump-In Rule (Advanced)
**Default: Disabled**

Play an identical card out of turn:
- **Enabled**: If exact same card appears, any player can play it immediately
- **Disabled**: Must wait for your turn

**Example:** If someone plays Red 5, and you have Red 5, you can "jump in" and play it immediately.

⚠️ **Advanced feature** - Makes gameplay more chaotic!

---

### 7️⃣0️⃣ Seven-Zero Rule (Fun Variant)
**Default: Disabled**

Special actions when playing 7 or 0:
- **Play a 7**: Choose a player to swap hands with
- **Play a 0**: All players pass hands in direction of play

⚠️ **Fun variant** - Drastically changes strategy!

---

### 📈 Progressive Draw (Card Stacking)
**Default: Disabled**

Draw cards accumulate:
- **Enabled**: +2 on +2 = next player draws 4
- **Disabled**: Each draw card works independently

**Example when enabled:**
- Player 1 plays +2
- Player 2 plays +2
- Player 3 plays +2
- Player 4 must draw 6 cards!

⚠️ **Chaos mode** - Can lead to massive card draws!

---

### 🎯 Force Play
**Default: Disabled**

Must play if you have a valid card:
- **Enabled**: Cannot draw if you have a playable card
- **Disabled**: Can choose to draw even if you can play

**Strategy:** Enabling this removes the option to strategically draw cards.

---

### 🎲 Challenge Wild +4 (Advanced)
**Default: Disabled**

Challenge if Wild +4 was played illegally:
- **Enabled**: Can challenge if you suspect player had valid card
- **Disabled**: Cannot challenge Wild +4 plays

**How it works:**
- If challenged and player DID have valid card → They draw 4 instead
- If challenged and player DIDN'T have valid card → Challenger draws 6

⚠️ **Advanced feature** - Adds bluffing element!

---

## How Settings Work

### Applying Settings
1. Open Settings (⚙ button)
2. Adjust your preferences
3. Tap "Save Settings"
4. Start a **new game** to apply changes

⚠️ **Important:** Settings only apply to NEW games, not games in progress.

### Resetting to Defaults
Tap "Reset Defaults" to restore all settings to standard rules.

---

## Recommended Configurations

### 🎯 Standard Game (Default)
- Starting Cards: 7
- Action Stacking: Enabled
- Draw on No Play: Enabled
- All others: Disabled

**Best for:** First-time players, balanced gameplay

---

### ⚡ Quick Game
- Starting Cards: 5
- Action Stacking: Enabled
- Draw on No Play: Enabled
- All others: Disabled

**Best for:** Fast games, casual play

---

### 🧠 Strategic Game
- Starting Cards: 10
- Action Stacking: Disabled
- Draw on No Play: Enabled
- Force Play: Enabled
- All others: Disabled

**Best for:** Thoughtful, strategic gameplay

---

### 🎪 Chaos Mode
- Starting Cards: 7
- Action Stacking: Enabled
- Progressive Draw: Enabled
- Seven-Zero Rule: Enabled
- Jump-In: Enabled

**Best for:** Fun, unpredictable games!

⚠️ **Warning:** Very chaotic and random!

---

## Card UI Improvements

### Fixed Overflow Issues ✅

**Before (v1.2.1):**
- Numbers could extend beyond card boundaries
- Corner indicators misaligned
- Text could be cut off

**After (v1.3.0):**
- ✅ All text constrained within card bounds
- ✅ Dynamic text sizing to fit available space
- ✅ Proper padding and margins
- ✅ Corner indicators properly positioned
- ✅ Icons scaled appropriately

**Technical improvements:**
- Added text width measurement
- Automatic font size adjustment
- Increased oval padding (20% vs 15%)
- Better corner positioning (15% margin)
- Constrained small card text sizes

---

## Technical Details

### Settings Storage
- Stored in SharedPreferences
- Persistent across app restarts
- Per-device configuration
- No account needed

### Settings Class Structure
```
GameSettings.java
- Handles all preference storage
- Provides getter/setter methods
- Manages defaults

SettingsActivity.java
- User interface for settings
- Save/Reset functionality
- Real-time validation
```

### Integration
- MainActivity uses settings on game start
- GameEngine respects all rule variations
- Card.canPlayOn() checks action stacking setting
- AI players adapt to custom rules

---

## What's Changed in v1.3.0

### Files Added
1. **GameSettings.java** - Settings management class
2. **SettingsActivity.java** - Settings UI activity
3. **activity_settings.xml** - Settings screen layout

### Files Modified
1. **CardView.java** - Fixed overflow issues, constrained text
2. **Card.java** - Added allowActionStacking parameter
3. **GameEngine.java** - Uses settings for game rules
4. **Player.java** - AI respects settings
5. **MainActivity.java** - Added settings button, integration
6. **AndroidManifest.xml** - Registered SettingsActivity
7. **activity_main.xml** - Added settings button

---

## Future Setting Ideas

Potential additions for future versions:
- Number of AI opponents (2-7 players)
- AI difficulty levels
- Custom card deck sizes
- Timer per turn
- Sound effects on/off
- Animation speed
- Card back designs
- Color themes

---

## Benefits of Custom Rules

### For Players
- ✅ Personalize game experience
- ✅ Adjust difficulty
- ✅ Try new variations
- ✅ Match house rules

### For Game
- ✅ More replayability
- ✅ Accommodates different skill levels
- ✅ Educational (learn different rule sets)
- ✅ Keeps gameplay fresh

---

## Version History

**v1.0.0 - v1.2.1**: Fixed rules, no customization

**v1.3.0** ⭐ Current:
- ✅ Custom rules settings
- ✅ Fixed card UI overflow
- ✅ Comprehensive settings menu
- ✅ 8 configurable options

---

**Enjoy your customizable card game experience!** 🃏⚙️

