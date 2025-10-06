package com.cardstack.game;

import android.content.Context;
import android.content.SharedPreferences;

public class GameSettings {
    private static final String PREFS_NAME = "CardStackSettings";
    private static final String KEY_STARTING_CARDS = "starting_cards";
    private static final String KEY_ALLOW_ACTION_STACKING = "allow_action_stacking";
    private static final String KEY_DRAW_ON_NO_PLAY = "draw_on_no_play";
    private static final String KEY_JUMP_IN_ENABLED = "jump_in_enabled";
    private static final String KEY_SEVEN_ZERO_RULE = "seven_zero_rule";
    private static final String KEY_PROGRESSIVE_UNO = "progressive_uno";
    private static final String KEY_FORCE_PLAY = "force_play";
    private static final String KEY_CHALLENGE_DRAW_FOUR = "challenge_draw_four";
    private static final String KEY_DRAW_TO_MATCH = "draw_to_match";
    
    private SharedPreferences prefs;
    
    // Default values
    public static final int DEFAULT_STARTING_CARDS = 7;
    public static final boolean DEFAULT_ACTION_STACKING = true;
    public static final boolean DEFAULT_DRAW_ON_NO_PLAY = true;
    public static final boolean DEFAULT_JUMP_IN = false;
    public static final boolean DEFAULT_SEVEN_ZERO = false;
    public static final boolean DEFAULT_PROGRESSIVE = false;
    public static final boolean DEFAULT_FORCE_PLAY = false;
    public static final boolean DEFAULT_CHALLENGE_DRAW_FOUR = false;
    public static final boolean DEFAULT_DRAW_TO_MATCH = false;
    
    public GameSettings(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    // Starting cards (5-10)
    public int getStartingCards() {
        return prefs.getInt(KEY_STARTING_CARDS, DEFAULT_STARTING_CARDS);
    }
    
    public void setStartingCards(int cards) {
        prefs.edit().putInt(KEY_STARTING_CARDS, cards).apply();
    }
    
    // Action card stacking (Skip on Skip, Reverse on Reverse, Draw Two on Draw Two)
    public boolean isActionStackingEnabled() {
        return prefs.getBoolean(KEY_ALLOW_ACTION_STACKING, DEFAULT_ACTION_STACKING);
    }
    
    public void setActionStackingEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_ALLOW_ACTION_STACKING, enabled).apply();
    }
    
    // Draw card when you can't play
    public boolean isDrawOnNoPlayEnabled() {
        return prefs.getBoolean(KEY_DRAW_ON_NO_PLAY, DEFAULT_DRAW_ON_NO_PLAY);
    }
    
    public void setDrawOnNoPlayEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DRAW_ON_NO_PLAY, enabled).apply();
    }
    
    // Jump-in rule (play same card out of turn)
    public boolean isJumpInEnabled() {
        return prefs.getBoolean(KEY_JUMP_IN_ENABLED, DEFAULT_JUMP_IN);
    }
    
    public void setJumpInEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_JUMP_IN_ENABLED, enabled).apply();
    }
    
    // Seven-Zero rule (7 = swap hands, 0 = rotate hands)
    public boolean isSevenZeroRuleEnabled() {
        return prefs.getBoolean(KEY_SEVEN_ZERO_RULE, DEFAULT_SEVEN_ZERO);
    }
    
    public void setSevenZeroRuleEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SEVEN_ZERO_RULE, enabled).apply();
    }
    
    // Progressive Draw (Draw cards stack)
    public boolean isProgressiveDrawEnabled() {
        return prefs.getBoolean(KEY_PROGRESSIVE_UNO, DEFAULT_PROGRESSIVE);
    }
    
    public void setProgressiveDrawEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_PROGRESSIVE_UNO, enabled).apply();
    }
    
    // Force play (must play if you have a valid card)
    public boolean isForcePlayEnabled() {
        return prefs.getBoolean(KEY_FORCE_PLAY, DEFAULT_FORCE_PLAY);
    }
    
    public void setForcePlayEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_FORCE_PLAY, enabled).apply();
    }
    
    // Challenge Wild Draw Four
    public boolean isChallengeDrawFourEnabled() {
        return prefs.getBoolean(KEY_CHALLENGE_DRAW_FOUR, DEFAULT_CHALLENGE_DRAW_FOUR);
    }
    
    public void setChallengeDrawFourEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_CHALLENGE_DRAW_FOUR, enabled).apply();
    }
    
    // Draw to Match (must draw until you get a playable card)
    public boolean isDrawToMatchEnabled() {
        return prefs.getBoolean(KEY_DRAW_TO_MATCH, DEFAULT_DRAW_TO_MATCH);
    }
    
    public void setDrawToMatchEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DRAW_TO_MATCH, enabled).apply();
    }
    
    // Reset to defaults
    public void resetToDefaults() {
        prefs.edit().clear().apply();
    }
}
