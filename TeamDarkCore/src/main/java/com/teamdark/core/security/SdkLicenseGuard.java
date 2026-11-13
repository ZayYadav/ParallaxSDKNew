package com.teamdark.core.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
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
import java.security.SecureRandom;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
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

    public static final String PANEL_URL = com.teamdark.core.BuildConfig.TD_LICENSE_URL;
    public static final String TELEMETRY_URL = ""; // Legacy telemetry is not enabled in the PHP panel.

    private static final String PREF_NAME = "tdcore_sdk_guard";
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

    /** No offline fallback: panel approval is required on every authorization. */
    public static boolean isLicensed(final Context context) {
        final String key = getSdkKey(context);
        if (context == null || key.isEmpty()) return false;
        return verifySync(context, key);
    }


    /** Panel-signed optional feature bits. No feature is enabled without a new server check. */
    public static boolean isFeatureAllowed(Context context, int featureBit) {
        if (featureBit < 0 || featureBit > 30 || !isLicensed(context)) return false;
        int mask = NativePolicyVerifier.authorizedFeatures();
        return (mask & (1 << featureBit)) != 0;
    }

    public static final String KEY_PLUGIN_URL = "plugin_url";

    /** Legacy accessor. PHP license service does not provide executable plugin URLs. */
    public static String getPluginUrl(Context context) {
        return "";
    }

    public static boolean verifySync(final Context context, final String key) {
        if (context == null || key == null || key.trim().isEmpty()) return false;
        // Run networking on a worker to avoid Android's network-on-main-thread exception.
        if (Looper.myLooper() == Looper.getMainLooper()) {
            FutureTask<Boolean> task = new FutureTask<>(() -> performOnlineCheck(context, key));
            Thread worker = new Thread(task, "TDCore-License-Check");
            worker.setDaemon(true);
            worker.start();
            try { return task.get(6, TimeUnit.SECONDS); }
            catch (Throwable ignored) { task.cancel(true); return false; }
        }
        return performOnlineCheck(context, key);
    }

    private static String certSha256(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo info;
            Signature[] signatures;
            if (Build.VERSION.SDK_INT >= 28) {
                info = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
                if (info.signingInfo == null) return "";
                signatures = info.signingInfo.getApkContentsSigners();
            } else {
                info = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                signatures = info.signatures;
            }
            if (signatures == null || signatures.length != 1) return "";
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(signatures[0].toByteArray());
            StringBuilder b = new StringBuilder(64);
            for (byte element : digest) b.append(String.format(java.util.Locale.ROOT, "%02X", element & 0xff));
            return b.toString();
        } catch (Throwable ignored) { return ""; }
    }

    private static String freshNonce() {
        byte[] raw = new byte[16];
        new SecureRandom().nextBytes(raw);
        StringBuilder s = new StringBuilder(32);
        for (byte v : raw) s.append(String.format(java.util.Locale.ROOT, "%02X", v & 0xff));
        return s.toString();
    }

    private static boolean performOnlineCheck(Context context, String key) {
        sCachedValid = false;
        if (!PANEL_URL.startsWith("https://") ||
            com.teamdark.core.BuildConfig.TD_LICENSE_PUBLIC_KEY.isEmpty()) return false;
        HttpsURLConnection conn = null;
        try {
            String device = getDeviceHwid(context);
            String pkg = context.getPackageName();
            String cert = certSha256(context);
            String nonce = freshNonce();
            if (!device.matches("[A-F0-9]{32,64}") || !cert.matches("[A-F0-9]{64}")) return false;
            String payload = "user_key=" + URLEncoder.encode(key.trim(), "UTF-8")
                + "&serial=" + URLEncoder.encode(device, "UTF-8")
                + "&pkg=" + URLEncoder.encode(pkg, "UTF-8")
                + "&signing_sha256=" + URLEncoder.encode(cert, "UTF-8")
                + "&nonce=" + URLEncoder.encode(nonce, "UTF-8");
            conn = (HttpsURLConnection) new URL(PANEL_URL).openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            try (OutputStream out = conn.getOutputStream()) {
                out.write(payload.getBytes(StandardCharsets.UTF_8));
            }
            if (conn.getResponseCode() != 200) return false;
            StringBuilder result = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                    if (result.length() > 16384) return false;
                }
            }
            JSONObject json = new JSONObject(result.toString());
            if (!json.optBoolean("status", false)) return false;
            String policy = json.optString("policy", "");
            String signature = json.optString("signature", "");
            boolean accepted = NativePolicyVerifier.verify(policy, signature,
                com.teamdark.core.BuildConfig.TD_LICENSE_PUBLIC_KEY,
                device, pkg, cert, nonce, System.currentTimeMillis()/1000L);
            sCachedValid = accepted;
            if (accepted) {
                SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                sp.edit().putBoolean(KEY_STATUS, true)
                    .putLong(KEY_LAST_VERIFIED, System.currentTimeMillis())
                    .remove(KEY_PLUGIN_URL).apply();
            }
            return accepted;
        } catch (Throwable ignored) {
            return false; // Network, server, parsing, certificate or JNI failure = deny
        } finally {
            if (conn != null) conn.disconnect();
            if (!sCachedValid) {
                try {
                    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        .edit().putBoolean(KEY_STATUS, false).remove(KEY_PLUGIN_URL).apply();
                } catch (Throwable ignored) {}
            }
        }
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
