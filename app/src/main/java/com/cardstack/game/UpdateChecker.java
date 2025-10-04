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
    
    private final Context context;
    private final SharedPreferences prefs;
    private final boolean forceCheck;
    private ProgressDialog progressDialog;
    private long downloadId = -1;
    private BroadcastReceiver downloadReceiver;
    
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
                    
                    // Parse JSON response
                    JSONObject json = new JSONObject(response.toString());
                    UpdateInfo info = new UpdateInfo();
                    info.tagName = json.getString("tag_name");
                    info.versionName = info.tagName.replace("v", "");
                    info.name = json.optString("name", "New Version");
                    info.body = json.optString("body", "");
                    info.url = json.getString("html_url");
                    
                    // Extract version code
                    info.versionCode = extractVersionCode(info.versionName);
                    
                    // Get download URL for APK
                    JSONArray assets = json.optJSONArray("assets");
                    if (assets != null) {
                        for (int i = 0; i < assets.length(); i++) {
                            JSONObject asset = assets.getJSONObject(i);
                            String assetName = asset.getString("name");
                            // Look for release APK (not debug)
                            if (assetName.toLowerCase().contains("release") && assetName.endsWith(".apk")) {
                                info.downloadUrl = asset.getString("browser_download_url");
                                break;
                            }
                        }
                    }
                    
                    return info;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking for updates", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
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
                showUpdateDialog(info);
            } else {
                if (forceCheck) {
                    Toast.makeText(context, "You're on the latest version! ✨", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        
        // Install Now button (downloads and installs)
        builder.setPositiveButton("Install Now", (dialog, which) -> {
            if (info.downloadUrl != null && !info.downloadUrl.isEmpty()) {
                downloadAndInstall(info);
            } else {
                // Fallback to browser download
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.url));
                context.startActivity(intent);
                Toast.makeText(context, "Opening browser - please download APK manually", Toast.LENGTH_LONG).show();
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
            
            context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            
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
