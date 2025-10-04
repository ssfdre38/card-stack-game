package com.cardstack.game;

public class Card {
    public enum Color {
        RED, BLUE, GREEN, YELLOW, WILD
    }

    public enum Type {
        NUMBER, SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR
    }

    private final Color color;
    private final Type type;
    private final int number;

    public Card(Color color, Type type, int number) {
        this.color = color;
        this.type = type;
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public boolean canPlayOn(Card topCard) {
        if (type == Type.WILD || type == Type.WILD_DRAW_FOUR) {
            return true;
        }
        
        if (topCard.color == Color.WILD) {
            return true;
        }
        
        return color == topCard.color || 
               type == topCard.type || 
               (type == Type.NUMBER && topCard.type == Type.NUMBER && number == topCard.number);
    }

    public String getDisplayText() {
        if (type == Type.WILD) {
            return "W";
        } else if (type == Type.WILD_DRAW_FOUR) {
            return "W+4";
        }
        
        String colorStr = color.toString().substring(0, 1);
        
        switch (type) {
            case NUMBER:
                return colorStr + number;
            case SKIP:
                return colorStr + "S";
            case REVERSE:
                return colorStr + "R";
            case DRAW_TWO:
                return colorStr + "+2";
            default:
                return colorStr;
        }
    }

    public int getColorResource() {
        switch (color) {
            case RED:
                return android.graphics.Color.rgb(220, 20, 20);
            case BLUE:
                return android.graphics.Color.rgb(20, 100, 220);
            case GREEN:
                return android.graphics.Color.rgb(20, 180, 20);
            case YELLOW:
                return android.graphics.Color.rgb(255, 200, 0);
            case WILD:
                return android.graphics.Color.rgb(50, 50, 50);
            default:
                return android.graphics.Color.GRAY;
        }
    }
}
