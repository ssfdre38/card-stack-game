package com.cardstack.game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand;
    private final boolean isAI;

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isAI() {
        return isAI;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public int getCardCount() {
        return hand.size();
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }

    public Card chooseCardToPlay(Card topCard) {
        if (!isAI) {
            return null;
        }

        List<Card> playableCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                playableCards.add(card);
            }
        }

        if (playableCards.isEmpty()) {
            return null;
        }

        for (Card card : playableCards) {
            if (card.getType() == Card.Type.WILD_DRAW_FOUR) {
                return card;
            }
        }

        for (Card card : playableCards) {
            if (card.getType() == Card.Type.DRAW_TWO) {
                return card;
            }
        }

        for (Card card : playableCards) {
            if (card.getType() == Card.Type.SKIP || card.getType() == Card.Type.REVERSE) {
                return card;
            }
        }

        for (Card card : playableCards) {
            if (card.getType() == Card.Type.WILD) {
                return card;
            }
        }

        return playableCards.get(0);
    }

    public Card.Color chooseWildColor() {
        if (!isAI) {
            return null;
        }

        int[] colorCounts = new int[4];
        for (Card card : hand) {
            switch (card.getColor()) {
                case RED:
                    colorCounts[0]++;
                    break;
                case BLUE:
                    colorCounts[1]++;
                    break;
                case GREEN:
                    colorCounts[2]++;
                    break;
                case YELLOW:
                    colorCounts[3]++;
                    break;
            }
        }

        int maxIndex = 0;
        for (int i = 1; i < 4; i++) {
            if (colorCounts[i] > colorCounts[maxIndex]) {
                maxIndex = i;
            }
        }

        switch (maxIndex) {
            case 0:
                return Card.Color.RED;
            case 1:
                return Card.Color.BLUE;
            case 2:
                return Card.Color.GREEN;
            case 3:
                return Card.Color.YELLOW;
            default:
                return Card.Color.RED;
        }
    }
}
