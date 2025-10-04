package com.cardstack.game;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private PlayerStats stats;
    private EditText nameInput;
    private TextView selectedAvatarView;
    private String selectedAvatar;
    
    private static final String[] AVATARS = {
        "👤", "😀", "😎", "🤓", "🥳", "🤠", 
        "👨", "👩", "👦", "👧", "🧑", "👴",
        "👵", "🤖", "👽", "👻", "💀", "🎃",
        "😺", "🐶", "🐱", "🐭", "🐹", "🐰",
        "🦊", "🐻", "🐼", "🐨", "🐯", "🦁",
        "🐮", "🐷", "🐸", "🐵", "🦄", "🐲",
        "🌟", "⭐", "✨", "💫", "🔥", "⚡",
        "🌈", "🎨", "🎭", "🎪", "🎮", "🎯"
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Customize Profile");
        }
        
        stats = new PlayerStats(this);
        
        nameInput = findViewById(R.id.playerNameInput);
        selectedAvatarView = findViewById(R.id.selectedAvatar);
        
        // Load current profile
        nameInput.setText(stats.getPlayerName());
        selectedAvatar = stats.getPlayerAvatar();
        selectedAvatarView.setText(selectedAvatar);
        
        setupAvatarGrid();
        setupSaveButton();
    }
    
    private void setupAvatarGrid() {
        GridLayout avatarGrid = findViewById(R.id.avatarGrid);
        avatarGrid.removeAllViews();
        
        for (String avatar : AVATARS) {
            Button avatarButton = new Button(this);
            avatarButton.setText(avatar);
            avatarButton.setTextSize(24);
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(4, 4, 4, 4);
            avatarButton.setLayoutParams(params);
            
            if (avatar.equals(selectedAvatar)) {
                avatarButton.setBackgroundColor(0xFF4CAF50);
            } else {
                avatarButton.setBackgroundColor(0xFF16213e);
            }
            
            avatarButton.setOnClickListener(v -> {
                selectedAvatar = avatar;
                selectedAvatarView.setText(avatar);
                updateAvatarSelection();
            });
            
            avatarGrid.addView(avatarButton);
        }
    }
    
    private void updateAvatarSelection() {
        GridLayout avatarGrid = findViewById(R.id.avatarGrid);
        for (int i = 0; i < avatarGrid.getChildCount(); i++) {
            Button button = (Button) avatarGrid.getChildAt(i);
            if (button.getText().toString().equals(selectedAvatar)) {
                button.setBackgroundColor(0xFF4CAF50);
            } else {
                button.setBackgroundColor(0xFF16213e);
            }
        }
    }
    
    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.saveProfileButton);
        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                name = "You";
            }
            
            stats.setPlayerName(name);
            stats.setPlayerAvatar(selectedAvatar);
            
            Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
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
