package com.cardstack.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerProfile {
    private final String name;
    private final String avatar;
    
    private static final SecureRandom random = new SecureRandom();
    
    // Fun AI player names
    private static final String[] PLAYER_NAMES = {
        "Alex", "Blake", "Charlie", "Dana", "Ellis",
        "Finley", "Gray", "Harper", "Indigo", "Jordan",
        "Kai", "Logan", "Morgan", "Nova", "Oakley",
        "Parker", "Quinn", "Reese", "Sage", "Taylor",
        "River", "Skylar", "Phoenix", "Rowan", "Cameron",
        "Avery", "Riley", "Jamie", "Casey", "Drew"
    };
    
    // Avatar emojis (various characters)
    private static final String[] AVATARS = {
        "🤖", "👾", "🎮", "🎯", "🎲",
        "🎭", "🎪", "🎨", "🎬", "🎤",
        "🦊", "🦁", "🐯", "🐻", "🐼",
        "🐨", "🐸", "🦉", "🦅", "🦆",
        "🌟", "⭐", "✨", "💫", "🔥",
        "⚡", "🌈", "🎃", "👑", "💎"
    };
    
    public PlayerProfile(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public String getDisplayName() {
        return avatar + " " + name;
    }
    
    /**
     * Generate random unique profiles for AI players
     */
    public static List<PlayerProfile> generateAIProfiles(int count) {
        List<String> availableNames = new ArrayList<>();
        Collections.addAll(availableNames, PLAYER_NAMES);
        Collections.shuffle(availableNames, random);
        
        List<String> availableAvatars = new ArrayList<>();
        Collections.addAll(availableAvatars, AVATARS);
        Collections.shuffle(availableAvatars, random);
        
        List<PlayerProfile> profiles = new ArrayList<>();
        for (int i = 0; i < count && i < availableNames.size() && i < availableAvatars.size(); i++) {
            profiles.add(new PlayerProfile(availableNames.get(i), availableAvatars.get(i)));
        }
        
        return profiles;
    }
    
    /**
     * Get a random profile for a single AI player
     */
    public static PlayerProfile getRandomProfile() {
        String name = PLAYER_NAMES[random.nextInt(PLAYER_NAMES.length)];
        String avatar = AVATARS[random.nextInt(AVATARS.length)];
        return new PlayerProfile(name, avatar);
    }
    
    /**
     * Create player profile (for human player)
     */
    public static PlayerProfile createPlayerProfile() {
        return new PlayerProfile("You", "👤");
    }
}
