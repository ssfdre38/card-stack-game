package com.cardstack.game;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private final List<Player> players;
    private final Deck deck;
    private final List<Card> discardPile;
    private int currentPlayerIndex;
    private boolean clockwise;
    private Card topCard;
    private Card.Color currentWildColor;
    private GameSettings settings;

    public GameEngine(GameSettings settings) {
        this.settings = settings;
        players = new ArrayList<>();
        deck = new Deck();
        discardPile = new ArrayList<>();
        currentPlayerIndex = 0;
        clockwise = true;
        currentWildColor = null;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void startGame() {
        int startingCards = settings.getStartingCards();
        
        for (Player player : players) {
            for (int i = 0; i < startingCards; i++) {
                Card card = deck.draw();
                if (card != null) {
                    player.addCard(card);
                }
            }
        }

        do {
            topCard = deck.draw();
        } while (topCard != null && (topCard.getType() == Card.Type.WILD || 
                                      topCard.getType() == Card.Type.WILD_DRAW_FOUR));

        if (topCard != null) {
            discardPile.add(topCard);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Card getTopCard() {
        if (currentWildColor != null) {
            return new Card(currentWildColor, topCard.getType(), topCard.getNumber());
        }
        return topCard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public boolean canPlayCard(Card card) {
        return card.canPlayOn(getTopCard(), settings.isActionStackingEnabled());
    }

    public String playCard(Card card, Card.Color wildColor) {
        Player currentPlayer = getCurrentPlayer();
        
        if (!canPlayCard(card)) {
            return "Cannot play that card!";
        }

        currentPlayer.removeCard(card);
        discardPile.add(card);
        topCard = card;

        if (card.getType() == Card.Type.WILD || card.getType() == Card.Type.WILD_DRAW_FOUR) {
            currentWildColor = wildColor;
        } else {
            currentWildColor = null;
        }

        if (currentPlayer.hasWon()) {
            return currentPlayer.getName() + " wins!";
        }

        processCardEffect(card);
        
        nextPlayer();

        return null;
    }

    private void processCardEffect(Card card) {
        switch (card.getType()) {
            case SKIP:
                nextPlayer();
                break;
            case REVERSE:
                if (players.size() == 2) {
                    nextPlayer();
                } else {
                    clockwise = !clockwise;
                }
                break;
            case DRAW_TWO:
                nextPlayer();
                Player nextPlayer = getCurrentPlayer();
                drawCards(nextPlayer, 2);
                break;
            case WILD_DRAW_FOUR:
                nextPlayer();
                Player nextPlayerWild = getCurrentPlayer();
                drawCards(nextPlayerWild, 4);
                break;
        }
    }

    public void drawCards(Player player, int count) {
        for (int i = 0; i < count; i++) {
            Card card = deck.draw();
            if (card == null) {
                reshuffleDeck();
                card = deck.draw();
            }
            if (card != null) {
                player.addCard(card);
            }
        }
    }

    private void reshuffleDeck() {
        if (discardPile.size() > 1) {
            for (int i = 0; i < discardPile.size() - 1; i++) {
                deck.addCard(discardPile.get(i));
            }
            Card top = discardPile.get(discardPile.size() - 1);
            discardPile.clear();
            discardPile.add(top);
            deck.shuffle();
        }
    }

    private void nextPlayer() {
        if (clockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    public Card drawCard() {
        Card card = deck.draw();
        if (card == null) {
            reshuffleDeck();
            card = deck.draw();
        }
        return card;
    }
}
