package com.cardstack.game;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GameEngine gameEngine;
    private GameSettings settings;
    private PlayerStats stats;
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

        settings = new GameSettings(this);
        stats = new PlayerStats(this);
        
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

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivityForResult(intent, 1);
        });

        Button statsButton = findViewById(R.id.statsButton);
        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
        });

        Button aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        drawButton.setOnClickListener(v -> {
            if (settings.isDrawToMatchEnabled()) {
                // Draw to Match: Keep drawing until player gets a playable card
                int cardsDrawn = 0;
                Card card = null;
                boolean foundPlayable = false;
                
                while (!foundPlayable && cardsDrawn < 20) { // Safety limit
                    card = gameEngine.drawCard();
                    if (card != null) {
                        stats.recordCardDrawn();
                        cardsDrawn++;
                        gameEngine.getCurrentPlayer().addCard(card);
                        
                        if (gameEngine.canPlayCard(card)) {
                            foundPlayable = true;
                        }
                    } else {
                        break; // No more cards in deck
                    }
                }
                
                if (foundPlayable) {
                    Toast.makeText(this, "Drew " + cardsDrawn + " card(s) - you can play now!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Drew " + cardsDrawn + " card(s) - no playable cards, turn skipped", Toast.LENGTH_SHORT).show();
                    // Advance to next player since no playable card was found
                    gameEngine.nextPlayer();
                    updateUI();
                    processAITurns();
                }
                updateUI();
            } else {
                // Standard rule: Draw one card
                Card card = gameEngine.drawCard();
                if (card != null) {
                    stats.recordCardDrawn();
                    gameEngine.getCurrentPlayer().addCard(card);
                    if (gameEngine.canPlayCard(card)) {
                        Toast.makeText(this, "Card drawn - you can play it!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Card drawn - cannot play, turn skipped", Toast.LENGTH_SHORT).show();
                        // Advance to next player since card is not playable
                        gameEngine.nextPlayer();
                        updateUI();
                        processAITurns();
                    }
                    updateUI();
                }
            }
        });

        startNewGame();
        
        // Check for updates on app start (once per day)
        new UpdateChecker(this).checkForUpdates();
    }

    private void startNewGame() {
        gameEngine = new GameEngine(settings);
        
        // Start tracking stats
        stats.startGame();
        
        // Create human player profile with custom name/avatar
        String playerName = stats.getPlayerName();
        String playerAvatar = stats.getPlayerAvatar();
        PlayerProfile humanProfile = new PlayerProfile(playerName, playerAvatar);
        gameEngine.addPlayer(new Player(playerName, false, humanProfile));
        
        // Create AI players with random profiles
        List<PlayerProfile> aiProfiles = PlayerProfile.generateAIProfiles(3);
        gameEngine.addPlayer(new Player(aiProfiles.get(0).getName(), true, aiProfiles.get(0)));
        gameEngine.addPlayer(new Player(aiProfiles.get(1).getName(), true, aiProfiles.get(1)));
        gameEngine.addPlayer(new Player(aiProfiles.get(2).getName(), true, aiProfiles.get(2)));

        gameEngine.startGame();
        updateUI();
    }

    private void updateUI() {
        updateUI(false);
    }
    
    private void updateUI(boolean animateTopCard) {
        Card topCard = gameEngine.getTopCard();
        topCardView.setCard(topCard);

        Player currentPlayer = gameEngine.getCurrentPlayer();
        currentPlayerView.setText("Current Player: " + currentPlayer.getDisplayName());

        // Display AI players with avatars and names
        player1CardsView.setText(gameEngine.getPlayers().get(1).getDisplayName() + ": " + 
                                gameEngine.getPlayers().get(1).getCardCount() + " cards");
        player2CardsView.setText(gameEngine.getPlayers().get(2).getDisplayName() + ": " + 
                                gameEngine.getPlayers().get(2).getCardCount() + " cards");
        player3CardsView.setText(gameEngine.getPlayers().get(3).getDisplayName() + ": " + 
                                gameEngine.getPlayers().get(3).getCardCount() + " cards");

        deckCountView.setText("Deck: " + gameEngine.getDeckSize());

        updatePlayerHand();

        drawButton.setEnabled(!currentPlayer.isAI());
    }

    private void updatePlayerHand() {
        playerHandLayout.removeAllViews();
        
        Player humanPlayer = gameEngine.getPlayers().get(0);
        
        // Calculate card size based on available screen width for consistent appearance
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int availableWidth = screenWidth - (int)(getResources().getDisplayMetrics().density * 32); // Subtract padding
        
        // Determine optimal card size based on screen size
        // Use a fixed ratio (2:3 aspect ratio for cards) and limit to reasonable sizes
        int maxCardWidth = (int)(getResources().getDisplayMetrics().density * 100); // 100dp max
        int minCardWidth = (int)(getResources().getDisplayMetrics().density * 70);  // 70dp min
        
        // Calculate card width: should fit ~5-6 cards comfortably on screen with overlap
        int cardWidth = Math.max(minCardWidth, Math.min(maxCardWidth, availableWidth / 6));
        int cardHeight = (int)(cardWidth * 1.5); // Maintain 2:3 aspect ratio
        
        for (Card card : humanPlayer.getHand()) {
            CardView cardView = new CardView(this);
            cardView.setCard(card);
            cardView.setSmall(true);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    cardWidth,
                    cardHeight
            );
            params.setMargins(8, 8, 8, 8);
            cardView.setLayoutParams(params);

            cardView.setOnClickListener(v -> playHumanCard(card, cardView));
            
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

    private void playHumanCard(Card card, CardView cardView) {
        if (!gameEngine.canPlayCard(card)) {
            Toast.makeText(this, "Cannot play that card!", Toast.LENGTH_SHORT).show();
            // Shake animation for invalid move
            Animation shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
            cardView.startAnimation(shakeAnim);
            return;
        }
        
        // Track card played
        stats.recordCardPlayed(card.getType());

        if (card.getType() == Card.Type.WILD || card.getType() == Card.Type.WILD_DRAW_FOUR) {
            showColorChoiceDialog(card);
        } else {
            String result = gameEngine.playCard(card, card.getColor());
            if (result != null && result.contains("wins")) {
                handleGameEnd(result);
            } else {
                updateUI(); // Animate top card change
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
                handleGameEnd(result);
            } else {
                updateUI(); // Animate top card change
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

                Card cardToPlay = currentPlayer.chooseCardToPlay(gameEngine.getTopCard(), settings.isActionStackingEnabled());
                
                if (cardToPlay == null) {
                    if (settings.isDrawToMatchEnabled()) {
                        // Draw to Match: Keep drawing until AI gets a playable card
                        int cardsDrawn = 0;
                        boolean foundPlayable = false;
                        
                        while (!foundPlayable && cardsDrawn < 20) { // Safety limit
                            Card drawnCard = gameEngine.drawCard();
                            if (drawnCard != null) {
                                cardsDrawn++;
                                currentPlayer.addCard(drawnCard);
                                
                                if (gameEngine.canPlayCard(drawnCard)) {
                                    cardToPlay = drawnCard;
                                    foundPlayable = true;
                                }
                            } else {
                                break; // No more cards in deck
                            }
                        }
                        
                        Toast.makeText(MainActivity.this, 
                                currentPlayer.getDisplayName() + " drew " + cardsDrawn + " card(s)", 
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Standard rule: Draw one card
                        Card drawnCard = gameEngine.drawCard();
                        if (drawnCard != null) {
                            currentPlayer.addCard(drawnCard);
                            Toast.makeText(MainActivity.this, 
                                    currentPlayer.getDisplayName() + " drew a card", 
                                    Toast.LENGTH_SHORT).show();
                            
                            if (gameEngine.canPlayCard(drawnCard)) {
                                cardToPlay = drawnCard;
                            }
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
                            currentPlayer.getDisplayName() + " played " + cardToPlay.getDisplayText(), 
                            Toast.LENGTH_SHORT).show();
                    
                    if (result != null && result.contains("wins")) {
                        updateUI();
                        showWinnerDialog(result);
                        return;
                    }
                } else {
                    // AI has no card to play, skip turn
                    Toast.makeText(MainActivity.this, 
                            currentPlayer.getDisplayName() + " has no playable card - turn skipped", 
                            Toast.LENGTH_SHORT).show();
                    gameEngine.nextPlayer();
                }

                updateUI();

                if (gameEngine.getCurrentPlayer().isAI()) {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void handleGameEnd(String result) {
        // Check if human player won
        Player humanPlayer = gameEngine.getPlayers().get(0);
        if (humanPlayer.hasWon()) {
            stats.recordWin();
        } else {
            String winnerName = result.split(" wins")[0];
            stats.recordLoss(winnerName);
        }
        
        showWinnerDialog(result);
    }

    private void showWinnerDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over!");
        builder.setMessage(message);
        builder.setPositiveButton("New Game", (dialog, which) -> startNewGame());
        builder.setNeutralButton("View Stats", (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
            startNewGame();
        });
        builder.setCancelable(false);
        builder.show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Refresh game to use new profile
            updateUI();
        }
    }
}
