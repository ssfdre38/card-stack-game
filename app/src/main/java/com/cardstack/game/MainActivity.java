package com.cardstack.game;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GameEngine gameEngine;
    private LinearLayout playerHandLayout;
    private CardView topCardView;
    private TextView currentPlayerView;
    private TextView player1CardsView;
    private TextView player2CardsView;
    private TextView player3CardsView;
    private TextView deckCountView;
    private Button drawButton;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerHandLayout = findViewById(R.id.playerHandLayout);
        topCardView = findViewById(R.id.topCardView);
        currentPlayerView = findViewById(R.id.currentPlayerView);
        player1CardsView = findViewById(R.id.player1CardsView);
        player2CardsView = findViewById(R.id.player2CardsView);
        player3CardsView = findViewById(R.id.player3CardsView);
        deckCountView = findViewById(R.id.deckCountView);
        drawButton = findViewById(R.id.drawButton);

        handler = new Handler();

        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(v -> startNewGame());

        drawButton.setOnClickListener(v -> {
            Card card = gameEngine.drawCard();
            if (card != null) {
                gameEngine.getCurrentPlayer().addCard(card);
                if (gameEngine.canPlayCard(card)) {
                    Toast.makeText(this, "Card drawn - you can play it!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Card drawn - cannot play, turn skipped", Toast.LENGTH_SHORT).show();
                    String result = gameEngine.playCard(gameEngine.getTopCard(), gameEngine.getTopCard().getColor());
                    if (result != null && result.contains("wins")) {
                        showWinnerDialog(result);
                    } else {
                        updateUI();
                        processAITurns();
                    }
                }
                updateUI();
            }
        });

        startNewGame();
    }

    private void startNewGame() {
        gameEngine = new GameEngine();
        
        gameEngine.addPlayer(new Player("You", false));
        gameEngine.addPlayer(new Player("AI 1", true));
        gameEngine.addPlayer(new Player("AI 2", true));
        gameEngine.addPlayer(new Player("AI 3", true));

        gameEngine.startGame();
        updateUI();
    }

    private void updateUI() {
        Card topCard = gameEngine.getTopCard();
        topCardView.setCard(topCard);

        Player currentPlayer = gameEngine.getCurrentPlayer();
        currentPlayerView.setText("Current Player: " + currentPlayer.getName());

        player1CardsView.setText("AI 1: " + gameEngine.getPlayers().get(1).getCardCount() + " cards");
        player2CardsView.setText("AI 2: " + gameEngine.getPlayers().get(2).getCardCount() + " cards");
        player3CardsView.setText("AI 3: " + gameEngine.getPlayers().get(3).getCardCount() + " cards");

        deckCountView.setText("Deck: " + gameEngine.getDeckSize());

        updatePlayerHand();

        drawButton.setEnabled(!currentPlayer.isAI());
    }

    private void updatePlayerHand() {
        playerHandLayout.removeAllViews();
        
        Player humanPlayer = gameEngine.getPlayers().get(0);
        
        for (Card card : humanPlayer.getHand()) {
            CardView cardView = new CardView(this);
            cardView.setCard(card);
            cardView.setSmall(true);
            
            int cardWidth = (int) (getResources().getDisplayMetrics().density * 80);
            int cardHeight = (int) (getResources().getDisplayMetrics().density * 120);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    cardWidth,
                    cardHeight
            );
            params.setMargins(8, 8, 8, 8);
            cardView.setLayoutParams(params);

            cardView.setOnClickListener(v -> playHumanCard(card));
            
            if (!gameEngine.getCurrentPlayer().isAI() && gameEngine.canPlayCard(card)) {
                cardView.setAlpha(1.0f);
                cardView.setClickable(true);
            } else {
                cardView.setAlpha(0.5f);
                cardView.setClickable(gameEngine.getCurrentPlayer().isAI() ? false : gameEngine.canPlayCard(card));
            }

            playerHandLayout.addView(cardView);
        }
    }

    private void playHumanCard(Card card) {
        if (!gameEngine.canPlayCard(card)) {
            Toast.makeText(this, "Cannot play that card!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (card.getType() == Card.Type.WILD || card.getType() == Card.Type.WILD_DRAW_FOUR) {
            showColorChoiceDialog(card);
        } else {
            String result = gameEngine.playCard(card, card.getColor());
            if (result != null && result.contains("wins")) {
                showWinnerDialog(result);
            } else {
                updateUI();
                processAITurns();
            }
        }
    }

    private void showColorChoiceDialog(Card card) {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        Card.Color[] colorEnums = {Card.Color.RED, Card.Color.BLUE, Card.Color.GREEN, Card.Color.YELLOW};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a color");
        builder.setItems(colors, (dialog, which) -> {
            String result = gameEngine.playCard(card, colorEnums[which]);
            if (result != null && result.contains("wins")) {
                showWinnerDialog(result);
            } else {
                updateUI();
                processAITurns();
            }
        });
        builder.show();
    }

    private void processAITurns() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Player currentPlayer = gameEngine.getCurrentPlayer();
                
                if (!currentPlayer.isAI()) {
                    return;
                }

                Card cardToPlay = currentPlayer.chooseCardToPlay(gameEngine.getTopCard());
                
                if (cardToPlay == null) {
                    Card drawnCard = gameEngine.drawCard();
                    if (drawnCard != null) {
                        currentPlayer.addCard(drawnCard);
                        Toast.makeText(MainActivity.this, 
                                currentPlayer.getName() + " drew a card", 
                                Toast.LENGTH_SHORT).show();
                        
                        if (gameEngine.canPlayCard(drawnCard)) {
                            cardToPlay = drawnCard;
                        }
                    }
                }

                if (cardToPlay != null) {
                    Card.Color wildColor = Card.Color.RED;
                    if (cardToPlay.getType() == Card.Type.WILD || 
                        cardToPlay.getType() == Card.Type.WILD_DRAW_FOUR) {
                        wildColor = currentPlayer.chooseWildColor();
                    } else {
                        wildColor = cardToPlay.getColor();
                    }

                    String result = gameEngine.playCard(cardToPlay, wildColor);
                    Toast.makeText(MainActivity.this, 
                            currentPlayer.getName() + " played " + cardToPlay.getDisplayText(), 
                            Toast.LENGTH_SHORT).show();
                    
                    if (result != null && result.contains("wins")) {
                        updateUI();
                        showWinnerDialog(result);
                        return;
                    }
                } else {
                    String result = gameEngine.playCard(gameEngine.getTopCard(), gameEngine.getTopCard().getColor());
                    if (result != null && result.contains("wins")) {
                        updateUI();
                        showWinnerDialog(result);
                        return;
                    }
                }

                updateUI();

                if (gameEngine.getCurrentPlayer().isAI()) {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void showWinnerDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over!");
        builder.setMessage(message);
        builder.setPositiveButton("New Game", (dialog, which) -> startNewGame());
        builder.setCancelable(false);
        builder.show();
    }
}
