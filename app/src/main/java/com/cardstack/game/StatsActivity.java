package com.cardstack.game;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {
    private PlayerStats stats;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        
        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Statistics");
        }
        
        stats = new PlayerStats(this);
        
        displayStats();
        setupButtons();
    }
    
    private void displayStats() {
        // Overview stats
        ((TextView) findViewById(R.id.totalGamesText)).setText(String.valueOf(stats.getTotalGames()));
        ((TextView) findViewById(R.id.gamesWonText)).setText(String.valueOf(stats.getGamesWon()));
        ((TextView) findViewById(R.id.gamesLostText)).setText(String.valueOf(stats.getGamesLost()));
        ((TextView) findViewById(R.id.winRateText)).setText(String.format(Locale.US, "%.1f%%", stats.getWinRate()));
        
        // Detailed stats
        ((TextView) findViewById(R.id.cardsPlayedText)).setText(String.valueOf(stats.getCardsPlayed()));
        ((TextView) findViewById(R.id.cardsDrawnText)).setText(String.valueOf(stats.getCardsDrawn()));
        ((TextView) findViewById(R.id.specialCardsText)).setText(String.valueOf(stats.getSpecialCardsPlayed()));
        ((TextView) findViewById(R.id.wildsPlayedText)).setText(String.valueOf(stats.getWildsPlayed()));
        
        // Time stats
        ((TextView) findViewById(R.id.totalPlayTimeText)).setText(formatTime(stats.getTotalPlayTime()));
        ((TextView) findViewById(R.id.averageGameTimeText)).setText(formatTime((long) stats.getAverageGameTime()));
        
        long fastestWin = stats.getFastestWin();
        ((TextView) findViewById(R.id.fastestWinText)).setText(
            fastestWin > 0 && fastestWin < Long.MAX_VALUE ? formatTime(fastestWin) : "N/A"
        );
        
        ((TextView) findViewById(R.id.longestGameText)).setText(
            stats.getLongestGame() > 0 ? formatTime(stats.getLongestGame()) : "N/A"
        );
        
        // Streak stats
        ((TextView) findViewById(R.id.currentStreakText)).setText(String.valueOf(stats.getWinStreak()));
        ((TextView) findViewById(R.id.bestStreakText)).setText(String.valueOf(stats.getBestStreak()));
        
        // Recent matches
        displayRecentMatches();
    }
    
    private void displayRecentMatches() {
        LinearLayout matchHistoryLayout = findViewById(R.id.matchHistoryLayout);
        matchHistoryLayout.removeAllViews();
        
        List<PlayerStats.MatchResult> matches = stats.getRecentMatches(10);
        
        if (matches.isEmpty()) {
            TextView noMatchesText = new TextView(this);
            noMatchesText.setText("No games played yet!");
            noMatchesText.setTextColor(0xFF999999);
            noMatchesText.setTextSize(14);
            noMatchesText.setPadding(0, 16, 0, 16);
            matchHistoryLayout.addView(noMatchesText);
            return;
        }
        
        for (PlayerStats.MatchResult match : matches) {
            TextView matchView = new TextView(this);
            String result = match.won ? "✅ WIN" : "❌ LOSS";
            String time = formatTime(match.duration);
            matchView.setText(String.format(Locale.US, "%s - %s - %d cards played", 
                result, time, match.cardsPlayed));
            matchView.setTextColor(match.won ? 0xFF4CAF50 : 0xFFFF5252);
            matchView.setTextSize(14);
            matchView.setPadding(0, 8, 0, 8);
            matchHistoryLayout.addView(matchView);
        }
    }
    
    private void setupButtons() {
        Button resetButton = findViewById(R.id.resetStatsButton);
        resetButton.setOnClickListener(v -> showResetConfirmation());
    }
    
    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("Reset Statistics")
            .setMessage("Are you sure you want to reset all statistics? This cannot be undone.")
            .setPositiveButton("Reset", (dialog, which) -> {
                stats.resetStats();
                displayStats();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            return String.format(Locale.US, "%dm %ds", seconds / 60, seconds % 60);
        } else {
            return String.format(Locale.US, "%dh %dm", seconds / 3600, (seconds % 3600) / 60);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
