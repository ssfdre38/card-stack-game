package com.cardstack.game;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerStats {
    private static final String PREFS_NAME = "CardStackStats";
    private static final String KEY_TOTAL_GAMES = "total_games";
    private static final String KEY_GAMES_WON = "games_won";
    private static final String KEY_GAMES_LOST = "games_lost";
    private static final String KEY_CARDS_PLAYED = "cards_played";
    private static final String KEY_CARDS_DRAWN = "cards_drawn";
    private static final String KEY_SPECIAL_CARDS_PLAYED = "special_cards_played";
    private static final String KEY_WILDS_PLAYED = "wilds_played";
    private static final String KEY_FASTEST_WIN = "fastest_win";
    private static final String KEY_LONGEST_GAME = "longest_game";
    private static final String KEY_TOTAL_PLAY_TIME = "total_play_time";
    private static final String KEY_WIN_STREAK = "win_streak";
    private static final String KEY_BEST_STREAK = "best_streak";
    private static final String KEY_MATCH_HISTORY = "match_history";
    private static final String KEY_PLAYER_NAME = "player_name";
    private static final String KEY_PLAYER_AVATAR = "player_avatar";
    
    private final SharedPreferences prefs;
    private long currentGameStartTime;
    private int currentGameCardsPlayed;
    
    public PlayerStats(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentGameStartTime = 0;
        currentGameCardsPlayed = 0;
    }
    
    // Player customization
    public void setPlayerName(String name) {
        prefs.edit().putString(KEY_PLAYER_NAME, name).apply();
    }
    
    public String getPlayerName() {
        return prefs.getString(KEY_PLAYER_NAME, "You");
    }
    
    public void setPlayerAvatar(String avatar) {
        prefs.edit().putString(KEY_PLAYER_AVATAR, avatar).apply();
    }
    
    public String getPlayerAvatar() {
        return prefs.getString(KEY_PLAYER_AVATAR, "👤");
    }
    
    // Game tracking
    public void startGame() {
        currentGameStartTime = System.currentTimeMillis();
        currentGameCardsPlayed = 0;
    }
    
    public void recordCardPlayed(Card.Type type) {
        currentGameCardsPlayed++;
        incrementStat(KEY_CARDS_PLAYED);
        
        if (type != Card.Type.NUMBER) {
            incrementStat(KEY_SPECIAL_CARDS_PLAYED);
        }
        
        if (type == Card.Type.WILD || type == Card.Type.WILD_DRAW_FOUR) {
            incrementStat(KEY_WILDS_PLAYED);
        }
    }
    
    public void recordCardDrawn() {
        incrementStat(KEY_CARDS_DRAWN);
    }
    
    public void recordWin() {
        long gameDuration = (System.currentTimeMillis() - currentGameStartTime) / 1000; // seconds
        
        incrementStat(KEY_TOTAL_GAMES);
        incrementStat(KEY_GAMES_WON);
        addToStat(KEY_TOTAL_PLAY_TIME, gameDuration);
        
        // Update win streak
        int currentStreak = prefs.getInt(KEY_WIN_STREAK, 0) + 1;
        prefs.edit().putInt(KEY_WIN_STREAK, currentStreak).apply();
        
        int bestStreak = prefs.getInt(KEY_BEST_STREAK, 0);
        if (currentStreak > bestStreak) {
            prefs.edit().putInt(KEY_BEST_STREAK, currentStreak).apply();
        }
        
        // Update fastest win
        long fastestWin = prefs.getLong(KEY_FASTEST_WIN, Long.MAX_VALUE);
        if (gameDuration < fastestWin) {
            prefs.edit().putLong(KEY_FASTEST_WIN, gameDuration).apply();
        }
        
        // Update longest game
        long longestGame = prefs.getLong(KEY_LONGEST_GAME, 0);
        if (gameDuration > longestGame) {
            prefs.edit().putLong(KEY_LONGEST_GAME, gameDuration).apply();
        }
        
        addMatchHistory(true, gameDuration, currentGameCardsPlayed);
    }
    
    public void recordLoss(String winner) {
        long gameDuration = (System.currentTimeMillis() - currentGameStartTime) / 1000;
        
        incrementStat(KEY_TOTAL_GAMES);
        incrementStat(KEY_GAMES_LOST);
        addToStat(KEY_TOTAL_PLAY_TIME, gameDuration);
        
        // Reset win streak
        prefs.edit().putInt(KEY_WIN_STREAK, 0).apply();
        
        addMatchHistory(false, gameDuration, currentGameCardsPlayed);
    }
    
    // Statistics getters
    public int getTotalGames() {
        return prefs.getInt(KEY_TOTAL_GAMES, 0);
    }
    
    public int getGamesWon() {
        return prefs.getInt(KEY_GAMES_WON, 0);
    }
    
    public int getGamesLost() {
        return prefs.getInt(KEY_GAMES_LOST, 0);
    }
    
    public int getCardsPlayed() {
        return prefs.getInt(KEY_CARDS_PLAYED, 0);
    }
    
    public int getCardsDrawn() {
        return prefs.getInt(KEY_CARDS_DRAWN, 0);
    }
    
    public int getSpecialCardsPlayed() {
        return prefs.getInt(KEY_SPECIAL_CARDS_PLAYED, 0);
    }
    
    public int getWildsPlayed() {
        return prefs.getInt(KEY_WILDS_PLAYED, 0);
    }
    
    public long getFastestWin() {
        return prefs.getLong(KEY_FASTEST_WIN, 0);
    }
    
    public long getLongestGame() {
        return prefs.getLong(KEY_LONGEST_GAME, 0);
    }
    
    public long getTotalPlayTime() {
        return prefs.getLong(KEY_TOTAL_PLAY_TIME, 0);
    }
    
    public int getWinStreak() {
        return prefs.getInt(KEY_WIN_STREAK, 0);
    }
    
    public int getBestStreak() {
        return prefs.getInt(KEY_BEST_STREAK, 0);
    }
    
    public double getWinRate() {
        int total = getTotalGames();
        if (total == 0) return 0.0;
        return (double) getGamesWon() / total * 100.0;
    }
    
    public double getAverageGameTime() {
        int total = getTotalGames();
        if (total == 0) return 0.0;
        return (double) getTotalPlayTime() / total;
    }
    
    // Match history
    private void addMatchHistory(boolean won, long duration, int cardsPlayed) {
        try {
            String historyJson = prefs.getString(KEY_MATCH_HISTORY, "[]");
            JSONArray history = new JSONArray(historyJson);
            
            JSONObject match = new JSONObject();
            match.put("won", won);
            match.put("duration", duration);
            match.put("cardsPlayed", cardsPlayed);
            match.put("timestamp", System.currentTimeMillis());
            
            history.put(match);
            
            // Keep only last 50 matches
            if (history.length() > 50) {
                JSONArray trimmed = new JSONArray();
                for (int i = history.length() - 50; i < history.length(); i++) {
                    trimmed.put(history.get(i));
                }
                history = trimmed;
            }
            
            prefs.edit().putString(KEY_MATCH_HISTORY, history.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public List<MatchResult> getRecentMatches(int count) {
        List<MatchResult> matches = new ArrayList<>();
        try {
            String historyJson = prefs.getString(KEY_MATCH_HISTORY, "[]");
            JSONArray history = new JSONArray(historyJson);
            
            int start = Math.max(0, history.length() - count);
            for (int i = history.length() - 1; i >= start; i--) {
                JSONObject match = history.getJSONObject(i);
                matches.add(new MatchResult(
                    match.getBoolean("won"),
                    match.getLong("duration"),
                    match.getInt("cardsPlayed"),
                    match.getLong("timestamp")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return matches;
    }
    
    // Reset stats
    public void resetStats() {
        String playerName = getPlayerName();
        String playerAvatar = getPlayerAvatar();
        prefs.edit().clear().apply();
        setPlayerName(playerName);
        setPlayerAvatar(playerAvatar);
    }
    
    // Helper methods
    private void incrementStat(String key) {
        int current = prefs.getInt(key, 0);
        prefs.edit().putInt(key, current + 1).apply();
    }
    
    private void addToStat(String key, long value) {
        long current = prefs.getLong(key, 0);
        prefs.edit().putLong(key, current + value).apply();
    }
    
    // Match result class
    public static class MatchResult {
        public final boolean won;
        public final long duration;
        public final int cardsPlayed;
        public final long timestamp;
        
        public MatchResult(boolean won, long duration, int cardsPlayed, long timestamp) {
            this.won = won;
            this.duration = duration;
            this.cardsPlayed = cardsPlayed;
            this.timestamp = timestamp;
        }
    }
}
