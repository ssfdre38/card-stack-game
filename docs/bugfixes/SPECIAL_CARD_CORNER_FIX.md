# Special Card Corner Icon Fix - v2.1.1

## Issue Description
Special cards (Skip, Reverse, Draw Two, Wild, Wild Draw Four) had corner icons that were either not displaying properly or appearing outside the card boundaries. The icons were being drawn with complex canvas transformations that caused them to overflow.

## Root Cause
The previous implementation used canvas scaling and translation that didn't properly account for card boundaries:
```java
canvas.translate(cornerMargin, height * 0.15f);
canvas.scale(cornerSize * 0.35f, cornerSize * 0.35f);
drawCardIcon(canvas, 0, 0, card.getType());
```

This approach had issues with:
1. Complex transformation chains that could place icons outside clipping bounds
2. Inconsistent sizing based on scaling factors
3. No explicit bounds checking for corner icon positions

## Solution Implemented

### 1. Simplified Corner Icon Positioning
Replaced complex scaling/translation with direct positioning:
```java
float cornerIconSize = isSmall ? 15 : 25;
float cornerPadding = isSmall ? 25 : 35;

// Top-left corner icon
float topLeftX = cornerPadding;
float topLeftY = cornerPadding;

canvas.save();
canvas.translate(topLeftX, topLeftY);
drawCardIcon(canvas, 0, 0, card.getType(), cornerIconSize);
canvas.restore();
```

### 2. Added Explicit Size Parameter
Created an overloaded `drawCardIcon()` method that accepts explicit size:
```java
private void drawCardIcon(Canvas canvas, float cx, float cy, Card.Type type, float size) {
    // Uses the provided size instead of calculating from isSmall flag
}
```

### 3. Added Bounds Checking
Added explicit boundary checks before drawing:
```java
if (topLeftX < width - cornerPadding && topLeftY < height - cornerPadding) {
    // Draw corner icon
}
```

## Benefits
- **Consistent Sizing**: Corner icons now have predictable, fixed sizes
- **Proper Positioning**: Icons stay within card boundaries at all times
- **Better Visual Quality**: Icons are properly centered in their corner positions
- **Maintainable Code**: Simpler transformation logic is easier to debug and modify

## Testing
Build and test with:
```bash
./gradlew assembleDebug assembleRelease
```

Both APKs have been tested and released to GitHub.

## Files Modified
- `app/src/main/java/com/cardstack/game/CardView.java`
  - Modified special card corner icon drawing logic (lines 157-189)
  - Added overloaded `drawCardIcon()` method with size parameter (lines 217-234)

## Version
- Version Code: 9
- Version Name: 2.1.1
- Release Date: October 4, 2024

---

**Copyright © 2025 Daniel Elliott**
