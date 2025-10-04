# Game Rules

This guide explains the complete rules of Match Mania and how to play.

## Game Objective

Be the first player to play all your cards. When you have one card remaining, the game will automatically call out for you!

## Game Setup

### Players
- 1 human player (you)
- 3 AI opponents
- Total: 4 players

### Initial Deal
- Each player receives **7 cards** at the start
- One card is placed face-up to start the discard pile
- Remaining cards form the draw pile

## Card Types

### Number Cards (0-9)
- Available in four colors: **Red**, **Yellow**, **Green**, **Blue**
- Each color has two of each number (except 0, which has one per color)

### Action Cards

#### Skip
- Next player loses their turn
- Can be played on matching color or another Skip card

#### Reverse
- Reverses the direction of play
- In 2-player games, acts like a Skip
- Can be played on matching color or another Reverse card

#### Draw Two (+2)
- Next player draws 2 cards and loses their turn
- Can be played on matching color or another Draw Two
- **Stacking**: If enabled in rules, player can play another +2 to pass the penalty to the next player

### Wild Cards

#### Wild
- Can be played on any card
- Player chooses the color to continue play
- For AI players, color is automatically selected

#### Wild Draw Four (+4)
- Can be played on any card when you don't have a playable card
- Next player draws 4 cards and loses their turn
- Player who played it chooses the color
- **Challenge Rule**: Currently not implemented

## Playing the Game

### Valid Plays

You can play a card if it matches the top discard card in:
- **Color** (e.g., Red 5 on Red 7)
- **Number** (e.g., Blue 3 on Red 3)
- **Action Type** (e.g., Skip on Skip, Draw Two on Draw Two)
- **Wild cards** can always be played

### Taking Your Turn

1. **Check your hand** for playable cards (they'll be highlighted)
2. **Play a card** by tapping it, or
3. **Draw a card** from the pile by tapping the draw pile
4. If you draw a playable card, you can play it immediately or keep it

### Drawing Cards

- If you have no playable cards, you must draw
- **Standard Rule**: Draw one card, then your turn ends
- **Draw to Match Rule** (optional): Keep drawing until you get a playable card

## Special Rules (Customizable)

### Stacking
- When **enabled**: Players can play another +2 on a +2 to pass the cumulative penalty to the next player
- Example: Player 1 plays +2, Player 2 plays +2, Player 3 draws 4 cards
- When **disabled**: Player must draw immediately

### Draw to Match
- When **enabled**: If you can't play, draw cards until you get a playable one (which you must play)
- When **disabled**: Draw one card and your turn ends, even if it's playable

### Force Play
- When **enabled**: If you draw a playable card, you must play it
- When **disabled**: You can choose to keep the drawn card

### Jump-In
- **Currently not implemented** (planned for future release)
- Would allow players with an identical card to play out of turn

### Seven-O
- **Currently not implemented** (planned for future release)
- Playing a 7 would swap hands with another player
- Playing a 0 would rotate all hands in the direction of play

### Progressive UNO
- **Currently not implemented** (planned for future release)
- Starting hand size and draw pile size variations

## Winning the Game

### Calling Out
- When you play your second-to-last card, the game automatically announces for you
- If you forget (impossible in this digital version), no penalty applies

### Victory
- First player to play all their cards wins
- Game displays the winner and updates statistics
- Option to play again or return to menu

## Scoring

Currently, Match Mania is played as single games without cumulative scoring. Each game results in:
- Win or Loss for the player
- Statistics updated (games played, won, win rate, etc.)

**Note**: Tournament/points-based scoring may be added in future versions.

## AI Behavior

AI players use strategic decision-making:
- Evaluate all legal moves
- Prioritize action cards when beneficial
- Choose optimal colors when playing Wild cards
- Adapt strategy based on game state
- Each AI has a simulated "thinking" delay for realism

See [AI Players](AI-Players) for more details.

## Rule Variations

Match Mania allows you to customize rules via the Settings menu:

1. Open **Settings** from the main menu
2. Select **Custom Rules**
3. Toggle rules on/off
4. Changes apply to all new games

See [Custom Rules](Custom-Rules) for detailed configuration options.

## Common Scenarios

### Can I play a Wild Draw Four anytime?
In the standard rules, Wild Draw Four can only be played if you don't have a card matching the current color. However, Match Mania currently allows it anytime (this may be configurable in future updates).

### What happens if the draw pile is empty?
The discard pile (except the top card) is shuffled and becomes the new draw pile.

### Can I play a Skip on a different colored Skip?
Yes! Action cards can be played on matching action type, regardless of color.

### What if I draw the exact card I need?
If Force Play is enabled, you must play it. Otherwise, you can choose to keep it.

## Strategy Tips

1. **Action Cards**: Save action cards for strategic moments
2. **Color Management**: Try to control the color to match your hand
3. **Card Counting**: Pay attention to what colors have been played
4. **Wild Cards**: Save for crucial moments or when you're stuck
5. **One Card Left**: Plan ahead so your last card is easily playable

---

**Next**: [How to Play](How-to-Play) | [Custom Rules](Custom-Rules) | [AI Players](AI-Players)
