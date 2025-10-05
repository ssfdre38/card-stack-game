package com.cardstack.game;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class UpdateChecker {
    private static final String TAG = "UpdateChecker";
    private static final String WEBSITE_API_URL = "https://matchmaina.ssfdre38.xyz/api/latest-version.json";
    private static final String GITHUB_API_URL = "https://api.github.com/repos/ssfdre38/match-mania/releases/latest";
    private static final String PREFS_NAME = "UpdatePrefs";
    private static final String KEY_LAST_CHECK = "last_check_time";
    private static final String KEY_SKIP_VERSION = "skip_version";
    private static final String KEY_DOWNLOADED_VERSION = "downloaded_version";
    private static final String KEY_DOWNLOADED_PATH = "downloaded_path";
    private static final String KEY_DOWNLOAD_HASH = "download_hash";
    private static final long CHECK_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours
    
    private final Context context;
    private final SharedPreferences prefs;
    private final boolean forceCheck;
    private ProgressDialog progressDialog;
    private long downloadId = -1;
    private BroadcastReceiver downloadReceiver;
    private boolean autoDownloadEnabled = true;
    
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
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Could not get version code", e);
            return 0;
        }
    }
    
    private String getCurrentVersionName() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }
    
    private class CheckUpdateTask extends AsyncTask<Void, Void, UpdateInfo> {
        @Override
        protected UpdateInfo doInBackground(Void... voids) {
            // Try website API first
            UpdateInfo info = checkWebsiteAPI();
            if (info != null) {
                Log.d(TAG, "Update info retrieved from website");
                return info;
            }
            
            // Fallback to GitHub API
            Log.d(TAG, "Website API unavailable, falling back to GitHub");
            return checkGitHubAPI();
        }
        
        @Override
        protected void onPostExecute(UpdateInfo info) {
            // Update last check time
            prefs.edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();
            
            if (info == null) {
                if (forceCheck) {
                    Toast.makeText(context, "Could not check for updates", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            
            // Check if this version was skipped
            String skippedVersion = prefs.getString(KEY_SKIP_VERSION, "");
            if (!forceCheck && info.tagName.equals(skippedVersion)) {
                Log.d(TAG, "User skipped this version");
                return;
            }
            
            // Compare version codes
            int currentVersion = getCurrentVersionCode();
            if (info.versionCode > currentVersion) {
                // Check if already downloaded
                String downloadedVersion = prefs.getString(KEY_DOWNLOADED_VERSION, "");
                String downloadedPath = prefs.getString(KEY_DOWNLOADED_PATH, "");
                
                if (info.tagName.equals(downloadedVersion) && !downloadedPath.isEmpty()) {
                    File apkFile = new File(downloadedPath);
                    if (apkFile.exists() && verifyDownload(apkFile)) {
                        // APK already downloaded and verified - auto-install directly
                        Toast.makeText(context, "Update ready! Launching installer...", Toast.LENGTH_SHORT).show();
                        installApk(apkFile);
                    } else {
                        // Downloaded file corrupted or missing - show dialog to download
                        showUpdateDialog(info);
                    }
                } else {
                    // Not downloaded yet - ALWAYS show dialog first for user choice
                    showUpdateDialog(info);
                }
            } else {
                if (forceCheck) {
                    Toast.makeText(context, "You're on the latest version! ✨", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private UpdateInfo checkWebsiteAPI() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(WEBSITE_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                JSONObject json = new JSONObject(response.toString());
                UpdateInfo info = new UpdateInfo();
                info.tagName = json.getString("version");
                info.versionName = info.tagName.replace("v", "");
                info.name = json.optString("name", "New Version");
                info.body = json.optString("changelog", "");
                info.downloadUrl = json.optString("download_url", "");
                // Use GitHub release URL for details page, or website as fallback
                String githubUrl = json.optString("github_url", "");
                info.url = !githubUrl.isEmpty() ? githubUrl : json.optString("website_url", "https://matchmaina.ssfdre38.xyz");
                info.versionCode = extractVersionCode(info.versionName);
                
                Log.d(TAG, "Successfully retrieved update info from website: " + info.versionName);
                return info;
            }
        } catch (Exception e) {
            Log.w(TAG, "Website API check failed: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
    
    private UpdateInfo checkGitHubAPI() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GITHUB_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                JSONObject json = new JSONObject(response.toString());
                UpdateInfo info = new UpdateInfo();
                info.tagName = json.getString("tag_name");
                info.versionName = info.tagName.replace("v", "");
                info.name = json.optString("name", "New Version");
                info.body = json.optString("body", "");
                info.url = json.getString("html_url");
                info.versionCode = extractVersionCode(info.versionName);
                
                JSONArray assets = json.optJSONArray("assets");
                if (assets != null) {
                    for (int i = 0; i < assets.length(); i++) {
                        JSONObject asset = assets.getJSONObject(i);
                        String assetName = asset.getString("name");
                        if (assetName.toLowerCase().contains("release") && assetName.endsWith(".apk")) {
                            info.downloadUrl = asset.getString("browser_download_url");
                            break;
                        }
                    }
                }
                
                Log.d(TAG, "Successfully retrieved update info from GitHub: " + info.versionName);
                return info;
            }
        } catch (Exception e) {
            Log.e(TAG, "GitHub API check failed: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
    
    private int extractVersionCode(String versionName) {
        try {
            // Parse version string (e.g., "2.3.1")
            String[] parts = versionName.split("\\.");
            if (parts.length >= 3) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                int patch = Integer.parseInt(parts[2]);
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
            // 2.3.0 = 18, 2.3.1 = 19, 2.3.2 = 20, 2.3.3 = 21, etc.
            return 18 + patch;
        }
        // Fallback calculation for future versions
        return (major * 1000) + (minor * 100) + patch;
    }
    
    private void downloadInBackground(UpdateInfo info) {
        if (info.downloadUrl == null || info.downloadUrl.isEmpty()) {
            // No download URL - show regular dialog
            showUpdateDialog(info);
            return;
        }
        
        Log.d(TAG, "Starting background download for " + info.versionName);
        
        // Start download silently in background
        try {
            File outputFile = new File(context.getExternalCacheDir(), "MatchMania-update.apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
            
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.downloadUrl));
            request.setTitle("Match Mania Update");
            request.setDescription("Downloading version " + info.versionName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationUri(Uri.fromFile(outputFile));
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = downloadManager.enqueue(request);
            
            // Register receiver for download completion
            downloadReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        // Check if download was successful
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadId);
                        Cursor cursor = downloadManager.query(query);
                        
                        if (cursor.moveToFirst()) {
                            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (columnIndex >= 0 && cursor.getInt(columnIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                                // Verify and save download info
                                if (verifyDownload(outputFile)) {
                                    prefs.edit()
                                        .putString(KEY_DOWNLOADED_VERSION, info.tagName)
                                        .putString(KEY_DOWNLOADED_PATH, outputFile.getAbsolutePath())
                                        .putString(KEY_DOWNLOAD_HASH, calculateFileHash(outputFile))
                                        .apply();
                                    
                                    Log.d(TAG, "Background download completed successfully - launching installer");
                                    
                                    // Automatically launch package manager to install
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        Toast.makeText(context, "Update downloaded! Launching installer...", Toast.LENGTH_SHORT).show();
                                        installApk(outputFile);
                                    });
                                } else {
                                    Log.e(TAG, "Downloaded file verification failed");
                                    new Handler(Looper.getMainLooper()).post(() -> 
                                        Toast.makeText(context, "Update download failed verification. Please try again.", Toast.LENGTH_LONG).show()
                                    );
                                }
                            } else {
                                Log.e(TAG, "Background download failed");
                            }
                        }
                        cursor.close();
                        
                        // Unregister receiver
                        try {
                            context.unregisterReceiver(downloadReceiver);
                        } catch (IllegalArgumentException e) {
                            // Already unregistered
                        }
                    }
                }
            };
            
            // Register receiver with appropriate flags for Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
            } else {
                context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error starting background download", e);
            // Fallback to regular dialog
            showUpdateDialog(info);
        }
    }
    
    private void showInstallDialog(UpdateInfo info, File apkFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Ready to Install! 🎉");
        
        String message = "Version " + info.versionName + " has been downloaded and is ready to install.\n\n" +
                "Current Version: " + getCurrentVersionName() + "\n" +
                "New Version: " + info.versionName + "\n\n" +
                "The update has been verified and is safe to install.";
        
        builder.setMessage(message);
        
        // Install Now button
        builder.setPositiveButton("Install Now", (dialog, which) -> {
            installApk(apkFile);
        });
        
        // Later button
        builder.setNeutralButton("Later", (dialog, which) -> {
            Toast.makeText(context, "Update will remain downloaded. Install anytime from Settings.", Toast.LENGTH_LONG).show();
        });
        
        // Cancel/Skip button
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Delete downloaded file
            if (apkFile.exists()) {
                apkFile.delete();
            }
            prefs.edit()
                .remove(KEY_DOWNLOADED_VERSION)
                .remove(KEY_DOWNLOADED_PATH)
                .remove(KEY_DOWNLOAD_HASH)
                .apply();
        });
        
        builder.setCancelable(true);
        builder.show();
    }
    
    private boolean verifyDownload(File file) {
        if (!file.exists()) {
            return false;
        }
        
        // Check file size (should be at least 1 MB)
        if (file.length() < 1024 * 1024) {
            Log.e(TAG, "Downloaded file too small: " + file.length() + " bytes");
            return false;
        }
        
        // Verify it's a valid APK by checking magic bytes
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            fis.read(header);
            // APK files are ZIP files, which start with PK (0x50 0x4B 0x03 0x04)
            if (header[0] == 0x50 && header[1] == 0x4B) {
                return true;
            }
            Log.e(TAG, "File is not a valid APK (wrong magic bytes)");
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error verifying download", e);
            return false;
        }
    }
    
    private String calculateFileHash(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] hash = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating file hash", e);
            return "";
        }
    }
    
    private void showUpdateDialog(UpdateInfo info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Available! 🎉");
        
        String message = "A new version is available!\n\n" +
                "Current Version: " + getCurrentVersionName() + "\n" +
                "New Version: " + info.versionName + "\n\n" +
                info.name + "\n\n" +
                "The update will download in the background and install when ready.";
        
        builder.setMessage(message);
        
        // Download button - starts background download with auto-install
        builder.setPositiveButton("Download", (dialog, which) -> {
            if (info.downloadUrl != null && !info.downloadUrl.isEmpty()) {
                // Start background download - will auto-install when complete
                downloadInBackground(info);
                Toast.makeText(context, "Downloading update in background...", Toast.LENGTH_SHORT).show();
            } else {
                // Fallback to browser download
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.url));
                context.startActivity(intent);
                Toast.makeText(context, "Opening browser - please download APK manually", Toast.LENGTH_LONG).show();
            }
        });
        
        // Skip This Version button
        builder.setNegativeButton("Skip", (dialog, which) -> {
            prefs.edit().putString(KEY_SKIP_VERSION, info.tagName).apply();
            Toast.makeText(context, "Update skipped. Check Settings to update later.", Toast.LENGTH_SHORT).show();
        });
        
        // Close button (cancel)
        builder.setNeutralButton("Close", (dialog, which) -> {
            // Just close the dialog - will check again next time
            dialog.dismiss();
        });
        
        builder.setCancelable(true);
        builder.show();
    }
    
    private void downloadAndInstall(UpdateInfo info) {
        // Check for install permission on Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!context.getPackageManager().canRequestPackageInstalls()) {
                Toast.makeText(context, "Please enable 'Install from Unknown Sources' in Settings", Toast.LENGTH_LONG).show();
                // Open settings
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                return;
            }
        }
        
        // Show progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading update...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
        
        // Start download
        try {
            File outputFile = new File(context.getExternalCacheDir(), "MatchMania-update.apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
            
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.downloadUrl));
            request.setTitle("Match Mania Update");
            request.setDescription("Downloading version " + info.versionName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationUri(Uri.fromFile(outputFile));
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = downloadManager.enqueue(request);
            
            // Register receiver for download completion
            downloadReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        
                        // Check if download was successful
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadId);
                        Cursor cursor = downloadManager.query(query);
                        
                        if (cursor.moveToFirst()) {
                            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (columnIndex >= 0 && cursor.getInt(columnIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                                installApk(outputFile);
                            } else {
                                Toast.makeText(context, "Download failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                        cursor.close();
                        
                        // Unregister receiver
                        try {
                            context.unregisterReceiver(downloadReceiver);
                        } catch (IllegalArgumentException e) {
                            // Already unregistered
                        }
                    }
                }
            };
            
            // Register receiver with appropriate flags for Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
            } else {
                context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
            
            // Start progress tracking
            new Thread(() -> {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = columnIndex >= 0 ? cursor.getInt(columnIndex) : -1;
                        
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            downloading = false;
                        } else {
                            int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                            int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                            
                            if (bytesDownloadedIndex >= 0 && bytesTotalIndex >= 0) {
                                long bytesDownloaded = cursor.getLong(bytesDownloadedIndex);
                                long bytesTotal = cursor.getLong(bytesTotalIndex);
                                
                                if (bytesTotal > 0) {
                                    final int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                                    if (progressDialog != null) {
                                        new Handler(Looper.getMainLooper()).post(() -> 
                                            progressDialog.setProgress(progress)
                                        );
                                    }
                                }
                            }
                        }
                    }
                    cursor.close();
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
            
        } catch (Exception e) {
            Log.e(TAG, "Error downloading update", e);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void installApk(File apkFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Use FileProvider for Android N+
                apkUri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", apkFile);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(apkFile);
            }
            
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            
            Toast.makeText(context, "Starting installation...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error installing APK", e);
            Toast.makeText(context, "Installation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
