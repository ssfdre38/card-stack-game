package com.cardstack.game;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private GameSettings settings;
    
    private SeekBar startingCardsSeekBar;
    private TextView startingCardsValue;
    private Switch actionStackingSwitch;
    private Switch drawOnNoPlaySwitch;
    private Switch jumpInSwitch;
    private Switch sevenZeroSwitch;
    private Switch progressiveSwitch;
    private Switch forcePlaySwitch;
    private Switch challengeDrawFourSwitch;
    private Switch drawToMatchSwitch;
    private Button resetButton;
    private Button saveButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Game Settings");
        }
        
        settings = new GameSettings(this);
        
        initializeViews();
        loadSettings();
        setupListeners();
    }
    
    private void initializeViews() {
        startingCardsSeekBar = findViewById(R.id.startingCardsSeekBar);
        startingCardsValue = findViewById(R.id.startingCardsValue);
        actionStackingSwitch = findViewById(R.id.actionStackingSwitch);
        drawOnNoPlaySwitch = findViewById(R.id.drawOnNoPlaySwitch);
        jumpInSwitch = findViewById(R.id.jumpInSwitch);
        sevenZeroSwitch = findViewById(R.id.sevenZeroSwitch);
        progressiveSwitch = findViewById(R.id.progressiveSwitch);
        forcePlaySwitch = findViewById(R.id.forcePlaySwitch);
        challengeDrawFourSwitch = findViewById(R.id.challengeDrawFourSwitch);
        drawToMatchSwitch = findViewById(R.id.drawToMatchSwitch);
        resetButton = findViewById(R.id.resetButton);
        saveButton = findViewById(R.id.saveButton);
    }
    
    private void loadSettings() {
        int startingCards = settings.getStartingCards();
        startingCardsSeekBar.setProgress(startingCards - 5); // 5-10 range, mapped to 0-5
        startingCardsValue.setText(String.valueOf(startingCards));
        
        actionStackingSwitch.setChecked(settings.isActionStackingEnabled());
        drawOnNoPlaySwitch.setChecked(settings.isDrawOnNoPlayEnabled());
        jumpInSwitch.setChecked(settings.isJumpInEnabled());
        sevenZeroSwitch.setChecked(settings.isSevenZeroRuleEnabled());
        progressiveSwitch.setChecked(settings.isProgressiveUnoEnabled());
        forcePlaySwitch.setChecked(settings.isForcePlayEnabled());
        challengeDrawFourSwitch.setChecked(settings.isChallengeDrawFourEnabled());
        drawToMatchSwitch.setChecked(settings.isDrawToMatchEnabled());
    }
    
    private void setupListeners() {
        startingCardsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int cards = progress + 5; // Map 0-5 to 5-10
                startingCardsValue.setText(String.valueOf(cards));
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        saveButton.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, "Settings saved! Start a new game to apply.", Toast.LENGTH_SHORT).show();
        });
        
        resetButton.setOnClickListener(v -> {
            settings.resetToDefaults();
            loadSettings();
            Toast.makeText(this, "Settings reset to defaults", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void saveSettings() {
        int startingCards = startingCardsSeekBar.getProgress() + 5;
        settings.setStartingCards(startingCards);
        settings.setActionStackingEnabled(actionStackingSwitch.isChecked());
        settings.setDrawOnNoPlayEnabled(drawOnNoPlaySwitch.isChecked());
        settings.setJumpInEnabled(jumpInSwitch.isChecked());
        settings.setSevenZeroRuleEnabled(sevenZeroSwitch.isChecked());
        settings.setProgressiveUnoEnabled(progressiveSwitch.isChecked());
        settings.setForcePlayEnabled(forcePlaySwitch.isChecked());
        settings.setChallengeDrawFourEnabled(challengeDrawFourSwitch.isChecked());
        settings.setDrawToMatchEnabled(drawToMatchSwitch.isChecked());
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
