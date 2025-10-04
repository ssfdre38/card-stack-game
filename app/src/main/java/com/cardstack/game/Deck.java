package com.cardstack.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private final SecureRandom random;

    public Deck() {
        cards = new ArrayList<>();
        random = new SecureRandom();
        initializeDeck();
    }

    private void initializeDeck() {
        Card.Color[] colors = {Card.Color.RED, Card.Color.BLUE, Card.Color.GREEN, Card.Color.YELLOW};

        for (Card.Color color : colors) {
            cards.add(new Card(color, Card.Type.NUMBER, 0));
            
            for (int i = 1; i <= 9; i++) {
                cards.add(new Card(color, Card.Type.NUMBER, i));
                cards.add(new Card(color, Card.Type.NUMBER, i));
            }
            
            for (int i = 0; i < 2; i++) {
                cards.add(new Card(color, Card.Type.SKIP, 0));
                cards.add(new Card(color, Card.Type.REVERSE, 0));
                cards.add(new Card(color, Card.Type.DRAW_TWO, 0));
            }
        }

        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Card.Color.WILD, Card.Type.WILD, 0));
            cards.add(new Card(Card.Color.WILD, Card.Type.WILD_DRAW_FOUR, 0));
        }

        shuffle();
    }

    public void shuffle() {
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card draw() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public void addCard(Card card) {
        cards.add(0, card);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
