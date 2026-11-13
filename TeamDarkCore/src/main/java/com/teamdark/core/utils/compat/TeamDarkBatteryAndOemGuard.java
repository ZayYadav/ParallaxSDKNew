package com.teamdark.core.utils.compat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;

import com.teamdark.lsparanoid.Obfuscate;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.system.DaemonService;

/**
 * Automatic Background-Kill Shield & OEM Battery-Saver Keep-Alive Guard.
 *
 * Prevents Xiaomi (MIUI/HyperOS), Vivo (FuntouchOS/OriginOS), Oppo/Realme (ColorOS),
 * Samsung (OneUI), and Infinix/Tecno from killing the virtual game process when the
 * player minimizes the game for 1-3 minutes (e.g. to reply on WhatsApp, take a phone
 * call, or complete social login).
 */
@Obfuscate
public final class TeamDarkBatteryAndOemGuard {
    private static final long MINIMIZE_KEEP_ALIVE_MS = 180_000L; // 3 minutes
    private static PowerManager.WakeLock sMinimizeWakeLock;
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final Runnable RELEASE_TASK = TeamDarkBatteryAndOemGuard::releaseMinimizeKeepAlive;

    private TeamDarkBatteryAndOemGuard() {
    }

    /**
     * Automatically called when a virtual game Activity resumes.
     */
    public static void onGameResumed(Activity activity) {
        MAIN_HANDLER.removeCallbacks(RELEASE_TASK);
        ensureDaemonKeepAlive(activity);
    }

    /**
     * Automatically called when a virtual game Activity is paused/minimized.
     * Holds a 3-minute partial keep-alive lock so the OS LMK / OEM battery killer
     * does not terminate the game while the user switches to WhatsApp/Call/Login.
     */
    public static synchronized void onGameMinimized(Context context) {
        Context ctx = context != null ? context.getApplicationContext() : TeamDarkCoreEngine.getContext();
        if (ctx == null) return;
        try {
            if (sMinimizeWakeLock == null) {
                PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
                if (pm != null) {
                    sMinimizeWakeLock = pm.newWakeLock(
                            PowerManager.PARTIAL_WAKE_LOCK,
                            "TeamDark:MinimizeKeepAlive");
                    sMinimizeWakeLock.setReferenceCounted(false);
                }
            }
            if (sMinimizeWakeLock != null && !sMinimizeWakeLock.isHeld()) {
                sMinimizeWakeLock.acquire(MINIMIZE_KEEP_ALIVE_MS);
            }
            MAIN_HANDLER.removeCallbacks(RELEASE_TASK);
            MAIN_HANDLER.postDelayed(RELEASE_TASK, MINIMIZE_KEEP_ALIVE_MS);
        } catch (Throwable ignored) {
        }
        ensureDaemonKeepAlive(ctx);
    }

    public static synchronized void releaseMinimizeKeepAlive() {
        try {
            if (sMinimizeWakeLock != null && sMinimizeWakeLock.isHeld()) {
                sMinimizeWakeLock.release();
            }
        } catch (Throwable ignored) {
        }
    }

    private static void ensureDaemonKeepAlive(Context context) {
        try {
            Context ctx = context != null ? context.getApplicationContext() : TeamDarkCoreEngine.getContext();
            if (ctx == null) return;
            Intent intent = new Intent(ctx, DaemonService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ctx.startForegroundService(intent);
            } else {
                ctx.startService(intent);
            }
        } catch (Throwable ignored) {
        }
    }

    public static boolean isIgnoringBatteryOptimizations(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        try {
            Context ctx = context != null ? context : TeamDarkCoreEngine.getContext();
            if (ctx == null) return true;
            PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
            return pm == null || pm.isIgnoringBatteryOptimizations(ctx.getPackageName());
        } catch (Throwable ignored) {
            return true;
        }
    }

    public static void requestIgnoreBatteryOptimizationsIfNeeded(Activity hostActivity) {
        if (hostActivity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        try {
            if (!isIgnoringBatteryOptimizations(hostActivity)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + hostActivity.getPackageName()));
                hostActivity.startActivity(intent);
            }
        } catch (Throwable ignored) {
        }
    }
}
