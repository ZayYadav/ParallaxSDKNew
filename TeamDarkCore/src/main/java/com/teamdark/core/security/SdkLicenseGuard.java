package com.teamdark.core.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ============================================================================
 * FEATURESTIC BOX SDK LICENSE GUARD (ANTI-PIRACY & DEVELOPER PROTECTION)
 * ============================================================================
 * Protects TeamDarkCore.aar from unauthorized redistribution.
 * Requires an active SDK License Key generated from the TeamDark Box Control Center.
 * Validates against: https://teamdark-core-panel.onrender.com/connect
 */
public final class SdkLicenseGuard {

    public static final String PANEL_URL = "https://teamdark-core-panel.onrender.com/connect";
    public static final String TELEMETRY_URL = "https://teamdark-core-panel.onrender.com/api/telemetry/crash";

    private static final String PREF_NAME = "fbox_sdk_guard";
    private static final String KEY_SDK_KEY = "sdk_key";
    private static final String KEY_LAST_VERIFIED = "last_verified";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXP = "exp";
    private static final String KEY_STATUS = "status";

    private static volatile String sSdkKey = null;
    private static final AtomicBoolean sVerifying = new AtomicBoolean(false);
    private static volatile boolean sCachedValid = false;

    private SdkLicenseGuard() {}

    public static void setSdkKey(String key) {
        if (key != null && !key.trim().isEmpty()) {
            sSdkKey = key.trim();
        }
    }

    public static void setSdkKey(Context context, String key) {
        setSdkKey(key);
        if (context != null && key != null && !key.trim().isEmpty()) {
            try {
                SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                sp.edit().putString(KEY_SDK_KEY, key.trim()).apply();
            } catch (Throwable ignored) {}
        }
    }

    public static String getSdkKey(Context context) {
        if (sSdkKey != null && !sSdkKey.isEmpty()) {
            return sSdkKey;
        }
        if (context == null) return "";
        try {
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String saved = sp.getString(KEY_SDK_KEY, "");
            if (!saved.isEmpty()) {
                sSdkKey = saved;
                return sSdkKey;
            }
            // Check AndroidManifest meta-data
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                String meta = ai.metaData.getString("teamdark_sdk_key", "");
                if (!meta.isEmpty()) {
                    sSdkKey = meta;
                    return sSdkKey;
                }
            }
        } catch (Throwable ignored) {}
        return "";
    }

    public static boolean isLicensed(final Context context) {
        String key = getSdkKey(context);
        if (key.isEmpty()) {
            postToast(context, "TeamDarkCore SDK Error: Missing SDK License Key");
            return false;
        }

        // Check local cached validity (valid for 12 hours between online pings)
        if (context != null) {
            try {
                SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                long lastVerified = sp.getLong(KEY_LAST_VERIFIED, 0);
                boolean wasValid = sp.getBoolean(KEY_STATUS, false);
                long now = System.currentTimeMillis();
                if (wasValid && (now - lastVerified < 12 * 60 * 60 * 1000L)) {
                    sCachedValid = true;
                    // Trigger silent background re-validation if > 2 hours
                    if (now - lastVerified > 2 * 60 * 60 * 1000L) {
                        verifyAsync(context, key);
                    }
                    return true;
                }
            } catch (Throwable ignored) {}
        }

        // Synchronous check if no recent valid cache
        return verifySync(context, key);
    }

    public static final String KEY_PLUGIN_URL = "plugin_url";

    public static String getPluginUrl(Context context) {
        if (context == null) return "";
        try {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_PLUGIN_URL, "");
        } catch (Throwable ignored) {
            return "";
        }
    }

    public static boolean verifySync(final Context context, final String key) {
        if (key == null || key.trim().isEmpty()) return false;
        try {
            String hwid = getDeviceHwid(context);
            String pkg = context != null ? context.getPackageName() : "";
            String postData = "game=SDK&user_key=" + URLEncoder.encode(key.trim(), "UTF-8")
                    + "&pkg=" + URLEncoder.encode(pkg, "UTF-8")
                    + "&serial=" + URLEncoder.encode(hwid, "UTF-8");

            URL url = new URL(PANEL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();
            StringBuilder sb = new StringBuilder();
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                }
            }

            String body = sb.toString().trim();
            if (body.startsWith("{")) {
                JSONObject json = new JSONObject(body);
                boolean status = json.optBoolean("status", false);
                if (status) {
                    sCachedValid = true;
                    if (context != null) {
                        try {
                            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                            JSONObject data = json.optJSONObject("data");
                            String exp = data != null ? data.optString("exp", "") : "";
                            String token = data != null ? data.optString("token", "") : "";
                            String pUrl = data != null ? data.optString("plugin_url", "") : "";
                            sp.edit()
                                    .putBoolean(KEY_STATUS, true)
                                    .putLong(KEY_LAST_VERIFIED, System.currentTimeMillis())
                                    .putString(KEY_EXP, exp)
                                    .putString(KEY_TOKEN, token)
                                    .putString(KEY_PLUGIN_URL, pUrl)
                                    .apply();
                        } catch (Throwable ignored) {}
                    }
                    return true;
                } else {
                    final String reason = json.optString("message", json.optString("reason", "SDK key invalid"));
                    sCachedValid = false;
                    if (context != null) {
                        try {
                            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                            sp.edit().putBoolean(KEY_STATUS, false).apply();
                        } catch (Throwable ignored) {}
                    }
                    postToast(context, "TeamDarkCore SDK License Error: " + reason);
                    return false;
                }
            }
        } catch (Throwable e) {
            // If offline, check if previously validated
            if (context != null) {
                try {
                    SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    if (sp.getBoolean(KEY_STATUS, false)) {
                        return true;
                    }
                } catch (Throwable ignored) {}
            }
            postToast(context, "TeamDarkCore SDK License: Cannot connect to license server");
            return false;
        }
        return false;
    }

    public static void verifyAsync(final Context context, final String key) {
        if (!sVerifying.compareAndSet(false, true)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    verifySync(context, key);
                } finally {
                    sVerifying.set(false);
                }
            }
        }).start();
    }

    public static String getDeviceHwid(Context context) {
        try {
            String raw = com.teamdark.core.utils.compat.RealDeviceHelper.REAL_BOARD
                    + com.teamdark.core.utils.compat.RealDeviceHelper.REAL_BRAND
                    + com.teamdark.core.utils.compat.RealDeviceHelper.REAL_DEVICE
                    + com.teamdark.core.utils.compat.RealDeviceHelper.REAL_MANUFACTURER
                    + com.teamdark.core.utils.compat.RealDeviceHelper.REAL_MODEL
                    + com.teamdark.core.utils.compat.RealDeviceHelper.REAL_PRODUCT;
            String androidId = "";
            if (context != null) {
                try {
                    androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                } catch (Throwable ignored) {}
            }
            raw += androidId;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.substring(0, 32).toUpperCase();
        } catch (Throwable ignored) {
            return "UNKNOWN_SDK_HWID";
        }
    }

    private static void postToast(final Context context, final String msg) {
        if (context == null) return;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                } catch (Throwable ignored) {}
            }
        });
    }
}
