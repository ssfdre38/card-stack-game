package com.cardstack.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private static final String TAG = "UpdateChecker";
    private static final String GITHUB_API_URL = "https://api.github.com/repos/ssfdre38/match-mania/releases/latest";
    private static final String PREFS_NAME = "UpdatePrefs";
    private static final String KEY_LAST_CHECK = "last_check_time";
    private static final String KEY_SKIP_VERSION = "skip_version";
    private static final long CHECK_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours
    
    private Context context;
    private SharedPreferences prefs;
    private boolean forceCheck;
    
    public UpdateChecker(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.forceCheck = false;
    }
    
    public UpdateChecker(Context context, boolean forceCheck) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.forceCheck = forceCheck;
    }
    
    public void checkForUpdates() {
        // Check if we should check for updates
        if (!forceCheck) {
            long lastCheck = prefs.getLong(KEY_LAST_CHECK, 0);
            long currentTime = System.currentTimeMillis();
            
            if (currentTime - lastCheck < CHECK_INTERVAL) {
                Log.d(TAG, "Skipping update check - checked recently");
                return;
            }
        }
        
        // Perform update check in background
        new CheckUpdateTask().execute();
    }
    
    private int getCurrentVersionCode() {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
        } catch (Exception e) {
            Log.e(TAG, "Error getting version code", e);
            return 0;
        }
    }
    
    private String getCurrentVersionName() {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            Log.e(TAG, "Error getting version name", e);
            return "Unknown";
        }
    }
    
    private class CheckUpdateTask extends AsyncTask<Void, Void, UpdateInfo> {
        
        @Override
        protected UpdateInfo doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            
            try {
                URL url = new URL(GITHUB_API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    Log.e(TAG, "GitHub API returned code: " + responseCode);
                    return null;
                }
                
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                
                return parseUpdateInfo(response.toString());
                
            } catch (Exception e) {
                Log.e(TAG, "Error checking for updates", e);
                return null;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        Log.e(TAG, "Error closing reader", e);
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        
        @Override
        protected void onPostExecute(UpdateInfo updateInfo) {
            // Update last check time
            prefs.edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();
            
            if (updateInfo == null) {
                if (forceCheck) {
                    Toast.makeText(context, "Unable to check for updates. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            
            int currentVersion = getCurrentVersionCode();
            String skippedVersion = prefs.getString(KEY_SKIP_VERSION, "");
            
            if (updateInfo.versionCode > currentVersion && !updateInfo.tagName.equals(skippedVersion)) {
                showUpdateDialog(updateInfo);
            } else if (forceCheck) {
                if (updateInfo.versionCode > currentVersion && updateInfo.tagName.equals(skippedVersion)) {
                    showUpdateDialog(updateInfo);
                } else {
                    Toast.makeText(context, "You're on the latest version! (" + getCurrentVersionName() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private UpdateInfo parseUpdateInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            
            UpdateInfo info = new UpdateInfo();
            info.tagName = obj.getString("tag_name");
            info.versionName = info.tagName.replace("v", ""); // Remove 'v' prefix
            info.name = obj.getString("name");
            info.body = obj.getString("body");
            info.url = obj.getString("html_url");
            
            // Extract version code from tag name (e.g., v2.2.7 -> version code from build)
            // We'll need to parse the release notes or use a convention
            // For now, we'll try to match common patterns
            info.versionCode = extractVersionCode(info.versionName);
            
            // Get download URL for release APK
            if (obj.has("assets") && obj.getJSONArray("assets").length() > 0) {
                for (int i = 0; i < obj.getJSONArray("assets").length(); i++) {
                    JSONObject asset = obj.getJSONArray("assets").getJSONObject(i);
                    String name = asset.getString("name");
                    if (name.contains("release") && name.endsWith(".apk")) {
                        info.downloadUrl = asset.getString("browser_download_url");
                        break;
                    }
                }
            }
            
            return info;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing update info", e);
            return null;
        }
    }
    
    private int extractVersionCode(String versionName) {
        // Convert version name like "2.2.7" to approximate version code
        // This is a fallback - ideally version code should be in release notes
        try {
            String[] parts = versionName.split("\\.");
            if (parts.length >= 3) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                int patch = Integer.parseInt(parts[2]);
                // Simple conversion: 2.2.7 -> 2027 (but we know 2.2.7 = version 17)
                // For now, we'll use a mapping based on our known versions
                return mapVersionToCode(major, minor, patch);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error extracting version code", e);
        }
        return 0;
    }
    
    private int mapVersionToCode(int major, int minor, int patch) {
        // Map known versions to their codes
        // This is based on our actual version history
        if (major == 2 && minor == 2) {
            // 2.2.0 = 5, 2.2.1 = 10, 2.2.2 = 12, 2.2.3 = 13, etc.
            if (patch == 0) return 5;
            if (patch == 1) return 10;
            return 12 + (patch - 2); // 2.2.2 = 12, 2.2.3 = 13, 2.2.4 = 14, etc.
        }
        if (major == 2 && minor == 3) {
            // 2.3.0 = 18, 2.3.1 = 19, 2.3.2 = 20, etc.
            return 18 + patch;
        }
        // Fallback calculation for future versions
        return (major * 1000) + (minor * 100) + patch;
    }
    
    private void showUpdateDialog(UpdateInfo info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Available! 🎉");
        
        String message = "A new version is available!\n\n" +
                "Current Version: " + getCurrentVersionName() + "\n" +
                "New Version: " + info.versionName + "\n\n" +
                info.name;
        
        builder.setMessage(message);
        
        // Download button
        builder.setPositiveButton("Download", (dialog, which) -> {
            if (info.downloadUrl != null && !info.downloadUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.downloadUrl));
                context.startActivity(intent);
            } else {
                // Fallback to release page
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.url));
                context.startActivity(intent);
            }
        });
        
        // View Details button
        builder.setNeutralButton("Details", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.url));
            context.startActivity(intent);
        });
        
        // Skip This Version button
        builder.setNegativeButton("Skip", (dialog, which) -> {
            prefs.edit().putString(KEY_SKIP_VERSION, info.tagName).apply();
            Toast.makeText(context, "Update skipped. Check Settings to update later.", Toast.LENGTH_SHORT).show();
        });
        
        builder.setCancelable(true);
        builder.show();
    }
    
    public void clearSkippedVersion() {
        prefs.edit().remove(KEY_SKIP_VERSION).apply();
    }
    
    private static class UpdateInfo {
        String tagName;
        String versionName;
        String name;
        String body;
        String url;
        String downloadUrl;
        int versionCode;
    }
}
