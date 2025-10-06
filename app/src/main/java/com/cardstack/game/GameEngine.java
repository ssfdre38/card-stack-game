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
    private int progressiveDrawStack; // For Progressive UNO
    private Card.Type stackedCardType; // Track if stacking Draw Two or Draw Four
    private int lastPlayerIndex; // For Challenge Draw Four

    public GameEngine(GameSettings settings) {
        this.settings = settings;
        players = new ArrayList<>();
        deck = new Deck();
        discardPile = new ArrayList<>();
        currentPlayerIndex = 0;
        clockwise = true;
        currentWildColor = null;
        progressiveDrawStack = 0;
        stackedCardType = null;
        lastPlayerIndex = -1;
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

        lastPlayerIndex = currentPlayerIndex;
        currentPlayer.removeCard(card);
        discardPile.add(card);
        topCard = card;

        if (card.getType() == Card.Type.WILD || card.getType() == Card.Type.WILD_DRAW_FOUR) {
            currentWildColor = wildColor;
        } else {
            currentWildColor = null;
        }

        if (currentPlayer.hasWon()) {
            return currentPlayer.getDisplayName() + " wins! 🎉";
        }

        String effectResult = processCardEffect(card);
        
        // Don't advance turn if special effect (Seven-Zero) needs handling
        if (effectResult == null || !effectResult.startsWith("SPECIAL:")) {
            nextPlayer();
        }

        return effectResult;
    }

    private String processCardEffect(Card card) {
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
                if (settings.isProgressiveUnoEnabled() && progressiveDrawStack >= 0) {
                    // Progressive UNO: Stack the draw count
                    progressiveDrawStack += 2;
                    stackedCardType = Card.Type.DRAW_TWO;
                    return "PROGRESSIVE:DRAW_TWO:" + progressiveDrawStack;
                } else {
                    nextPlayer();
                    Player nextPlayer = getCurrentPlayer();
                    drawCards(nextPlayer, 2);
                }
                break;
            case WILD_DRAW_FOUR:
                if (settings.isProgressiveUnoEnabled() && progressiveDrawStack >= 0) {
                    // Progressive UNO: Stack the draw count
                    progressiveDrawStack += 4;
                    stackedCardType = Card.Type.WILD_DRAW_FOUR;
                    return "PROGRESSIVE:WILD_DRAW_FOUR:" + progressiveDrawStack;
                } else {
                    // Check if Challenge Draw Four is enabled
                    if (settings.isChallengeDrawFourEnabled()) {
                        return "CHALLENGE_AVAILABLE:" + lastPlayerIndex;
                    } else {
                        nextPlayer();
                        Player nextPlayerWild = getCurrentPlayer();
                        drawCards(nextPlayerWild, 4);
                    }
                }
                break;
            case NUMBER:
                // Seven-Zero Rule
                if (settings.isSevenZeroRuleEnabled()) {
                    if (card.getNumber() == 7) {
                        return "SPECIAL:SEVEN_SWAP";
                    } else if (card.getNumber() == 0) {
                        return "SPECIAL:ZERO_ROTATE";
                    }
                }
                break;
        }
        return null;
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

    public void nextPlayer() {
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

    // Progressive UNO methods
    public int getProgressiveDrawStack() {
        return progressiveDrawStack;
    }

    public Card.Type getStackedCardType() {
        return stackedCardType;
    }

    public void resolveProgressiveStack() {
        if (progressiveDrawStack > 0) {
            nextPlayer();
            Player targetPlayer = getCurrentPlayer();
            drawCards(targetPlayer, progressiveDrawStack);
            progressiveDrawStack = 0;
            stackedCardType = null;
        }
    }

    public void resetProgressiveStack() {
        progressiveDrawStack = 0;
        stackedCardType = null;
    }

    // Seven-Zero Rule methods
    public void swapHandsWithPlayer(int targetPlayerIndex) {
        if (targetPlayerIndex < 0 || targetPlayerIndex >= players.size()) {
            return;
        }
        
        Player currentPlayer = getCurrentPlayer();
        Player targetPlayer = players.get(targetPlayerIndex);
        
        List<Card> tempHand = new ArrayList<>(currentPlayer.getHand());
        currentPlayer.getHand().clear();
        currentPlayer.getHand().addAll(targetPlayer.getHand());
        targetPlayer.getHand().clear();
        targetPlayer.getHand().addAll(tempHand);
    }

    public void rotateAllHands() {
        if (players.size() < 2) {
            return;
        }

        if (clockwise) {
            // Rotate clockwise: each player gives hand to next player
            List<Card> firstHand = new ArrayList<>(players.get(0).getHand());
            for (int i = 0; i < players.size() - 1; i++) {
                players.get(i).getHand().clear();
                players.get(i).getHand().addAll(players.get(i + 1).getHand());
            }
            players.get(players.size() - 1).getHand().clear();
            players.get(players.size() - 1).getHand().addAll(firstHand);
        } else {
            // Rotate counter-clockwise: each player gives hand to previous player
            List<Card> lastHand = new ArrayList<>(players.get(players.size() - 1).getHand());
            for (int i = players.size() - 1; i > 0; i--) {
                players.get(i).getHand().clear();
                players.get(i).getHand().addAll(players.get(i - 1).getHand());
            }
            players.get(0).getHand().clear();
            players.get(0).getHand().addAll(lastHand);
        }
    }

    // Challenge Draw Four methods
    public boolean canChallengeDrawFour(int challengedPlayerIndex) {
        if (challengedPlayerIndex < 0 || challengedPlayerIndex >= players.size()) {
            return false;
        }

        Player challengedPlayer = players.get(challengedPlayerIndex);
        Card.Color topColor = topCard.getColor();
        
        // Check if challenged player had a card matching the color before the Wild Draw Four
        for (Card card : challengedPlayer.getHand()) {
            if (card.getColor() == topColor) {
                return true; // Challenge succeeds - they had a matching color
            }
        }
        return false; // Challenge fails - they didn't have matching color
    }

    public void executeChallengeResult(boolean challengeSucceeds, int challengedPlayerIndex) {
        if (challengeSucceeds) {
            // Challenger wins: challenged player draws 4
            Player challengedPlayer = players.get(challengedPlayerIndex);
            drawCards(challengedPlayer, 4);
        } else {
            // Challenger loses: they draw 6 instead of 4
            nextPlayer();
            Player challenger = getCurrentPlayer();
            drawCards(challenger, 6);
        }
    }

    // Jump-In Rule methods
    public boolean canJumpIn(Card card, int playerIndex) {
        if (!settings.isJumpInEnabled()) {
            return false;
        }
        
        if (playerIndex == currentPlayerIndex) {
            return false; // Can't jump in on your own turn
        }

        // Can only jump in with exact match (same color AND same number)
        Card top = getTopCard();
        return card.getType() == Card.Type.NUMBER && 
               top.getType() == Card.Type.NUMBER &&
               card.getColor() == top.getColor() && 
               card.getNumber() == top.getNumber();
    }

    public void jumpInPlay(int playerIndex, Card card) {
        currentPlayerIndex = playerIndex;
        Player player = getCurrentPlayer();
        player.removeCard(card);
        discardPile.add(card);
        topCard = card;
        currentWildColor = null;
    }

    // Force Play check
    public boolean hasPlayableCard() {
        Player currentPlayer = getCurrentPlayer();
        for (Card card : currentPlayer.getHand()) {
            if (canPlayCard(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDrawAllowed() {
        if (settings.isForcePlayEnabled()) {
            // Can't draw if you have a playable card
            return !hasPlayableCard();
        }
        // Draw on no play setting
        return settings.isDrawOnNoPlayEnabled();
    }

    public GameSettings getSettings() {
        return settings;
    }

    public int getLastPlayerIndex() {
        return lastPlayerIndex;
    }
}
