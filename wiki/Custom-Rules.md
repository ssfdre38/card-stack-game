# Custom Rules

Match Mania offers customizable rules to tailor the game experience to your preferences. This guide explains each rule option and how to configure them.

## Accessing Custom Rules

1. Launch Match Mania
2. Tap **Settings** from the main menu
3. Select **Custom Rules**
4. Toggle rules on or off as desired
5. Changes are automatically saved

## Available Rules

### Stacking (+2 on +2)

**Default**: OFF

**Description**: Allows players to play a Draw Two card on top of another Draw Two card, passing the cumulative penalty to the next player.

**When Enabled**:
- Player A plays +2 on Player B
- Player B plays +2 on Player C
- Player C draws 4 cards (2+2)
- Player C's turn is skipped

**When Disabled**:
- Player must draw the cards immediately
- No defense against Draw Two cards

**Strategy Impact**:
- High impact on gameplay dynamics
- Adds an element of defense
- Can lead to dramatic penalty accumulation
- Favors players with multiple +2 cards

**Recommendation**: Enable for more interactive gameplay, disable for traditional rules.

---

### Draw to Match

**Default**: OFF

**Description**: When you cannot play a card, you must keep drawing until you get a playable card, then play it immediately.

**When Enabled**:
- Cannot pass your turn by drawing one card
- Must draw until you can play
- The playable card must be played immediately
- Can lead to drawing many cards if unlucky

**When Disabled**:
- Draw one card when you can't play
- If it's playable, you may choose to play it or keep it (depending on Force Play rule)
- Turn ends after drawing one card

**Strategy Impact**:
- Dramatically changes game pace
- Reduces control over hand size
- Can lead to rapid hand growth
- Eliminates the "draw and pass" strategy

**Recommendation**: Enable for faster games with less strategic depth, disable for more strategic control.

---

### Force Play

**Default**: OFF

**Description**: When you draw a card from the pile, if it's playable, you must play it immediately.

**When Enabled**:
- No choice when drawing a playable card
- Reduces hand hoarding
- Makes the game more luck-dependent

**When Disabled**:
- You can choose to keep the drawn card
- Allows building up your hand strategically
- More control over your plays

**Strategy Impact**:
- Moderate impact on strategy
- Affects hand management
- Can force suboptimal plays
- Speeds up the game slightly

**Recommendation**: Enable for casual play, disable for strategic depth.

---

## Rules in Development

The following rules are planned for future releases:

### Jump-In
- Play an identical card out of turn
- Requires exact match (color and number/action)
- Adds a reaction-time element

### Seven-O
- Playing a 7 lets you swap hands with another player
- Playing a 0 rotates all hands in play direction
- High strategic impact

### Progressive Hands
- Start with more or fewer than 7 cards
- Adjustable starting hand size
- Changes game length and difficulty

### Challenge Rule
- Challenge a Wild Draw Four play
- If successful, opponent draws 4; if failed, you draw 6
- Adds a bluffing element

### Score-Based Games
- Play to a target score instead of single games
- Points awarded based on remaining cards
- Multi-round tournaments

## Rule Combinations

### Casual Play
- Stacking: OFF
- Draw to Match: OFF
- Force Play: OFF
- **Effect**: Traditional, strategic gameplay

### Fast & Furious
- Stacking: ON
- Draw to Match: ON
- Force Play: ON
- **Effect**: Rapid, unpredictable games

### Strategic Mode
- Stacking: OFF
- Draw to Match: OFF
- Force Play: OFF
- **Effect**: Maximum control and planning

### Chaos Mode
- Stacking: ON
- Draw to Match: ON
- Force Play: ON
- **Effect**: Highest variance and excitement

## Creating Your Own Variants

Experiment with different combinations to find your preferred style:

1. **Start with defaults** and change one rule at a time
2. **Play several games** with each configuration
3. **Note the differences** in game length and strategy
4. **Find your favorite** combination

## Rule Presets (Coming Soon)

Future versions may include saved rule presets:
- Classic
- Tournament
- Speed
- Expert
- Custom 1-3

## Technical Notes

### Rule Persistence
- Settings are saved automatically
- Persist across app restarts
- Stored locally on device
- No cloud sync (yet)

### AI Adaptation
- AI players automatically adapt to active rules
- Strategic decisions account for current ruleset
- No disadvantage in any configuration

### Rule Validation
- Invalid rule combinations are prevented
- Conflicting rules show warnings
- Help text explains each rule's effect

## Feedback

Have suggestions for new rules or rule modifications? 

- [Request a Feature](https://github.com/ssfdre38/match-mania/issues/new?template=feature_request.md)
- [Join the Discussion](https://github.com/ssfdre38/match-mania/discussions)

---

**Related**: [Game Rules](Game-Rules) | [How to Play](How-to-Play) | [AI Players](AI-Players)
