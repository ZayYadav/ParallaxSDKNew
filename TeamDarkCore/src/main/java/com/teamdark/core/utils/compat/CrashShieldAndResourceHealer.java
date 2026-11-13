package com.teamdark.core.utils.compat;

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;

import com.teamdark.lsparanoid.Obfuscate;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.utils.FileUtils;

/**
 * Anti-Anogs Offset Crash Guard, Background Exception Shield & Resource Healer.
 *
 * 1. Pre-creates and sanitizes libanogs.so / libtersafe.so / libgcloud.so working
 *    directories (files/ano_tmp, files/tss_tmp, cache/ano_tmp, app_bugly, app_crashSight)
 *    BEFORE Application.onCreate() so libanogs.so never hits a NULL fopen/mmap pointer
 *    or stale function-offset cache from a previous session.
 * 2. Absorbs non-fatal background thread exceptions (e.g. DeadSystemException,
 *    RemoteException, GMS/analytics/telemetry worker crashes) so a background
 *    service hiccup never kills an active game match.
 */
@Obfuscate
public final class CrashShieldAndResourceHealer {
    private static final String TAG = "TeamDarkCrashShield";
    private static volatile boolean sInstalled = false;

    private CrashShieldAndResourceHealer() {
    }

    private static File getCrashMarkerFile(String pkg, int userId) {
        String safePkg = (pkg != null && !pkg.isEmpty()) ? pkg.replace('.', '_') : "app";
        return new File(BEnvironment.getSystemDir(), "crash_heal_" + safePkg + "_u" + Math.max(0, userId) + ".flag");
    }

    /**
     * Prepares clean, writable working directories required by libanogs.so, libUE4.so,
     * and libtersafe.so before any native library is loaded into memory.
     */
    public static void prepareSafeAnogsEnvironment(Context appContext, String pkg, int userId) {
        if (pkg == null || pkg.isEmpty()) return;
        try {
            File dataDir = BEnvironment.getDataDir(pkg, userId);
            File deDataDir = BEnvironment.getDeDataDir(pkg, userId);
            String[] requiredDirs = new String[]{
                    "files",
                    "files/ano_tmp",
                    "files/tss_tmp",
                    "cache",
                    "cache/ano_tmp",
                    "code_cache",
                    "databases",
                    "shared_prefs",
                    "app_webview",
                    "app_bugly",
                    "app_crashrecord",
                    "app_crashSight",
                    "app_textures"
            };
            for (String sub : requiredDirs) {
                File d1 = new File(dataDir, sub);
                FileUtils.mkdirs(d1);
                d1.setReadable(true, false);
                d1.setWritable(true, false);
                d1.setExecutable(true, false);

                File d2 = new File(deDataDir, sub);
                FileUtils.mkdirs(d2);
            }

            // Purge stale/corrupted .tmp / .mmap files inside ano_tmp and tss_tmp BEFORE libanogs.so loads
            // so stale offset tables from a previous session never cause an anogs offset crash on launch!
            cleanStaleAnogsTempFiles(new File(dataDir, "files/ano_tmp"));
            cleanStaleAnogsTempFiles(new File(dataDir, "files/tss_tmp"));
            cleanStaleAnogsTempFiles(new File(dataDir, "cache/ano_tmp"));
        } catch (Throwable ignored) {
        }
    }

    private static void cleanStaleAnogsTempFiles(File dir) {
        if (dir == null || !dir.isDirectory()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f != null && f.isFile()) {
                try {
                    f.delete();
                } catch (Throwable ignored) {
                }
            }
        }
    }

    public static void installForVirtualProcess(final Context appContext) {
        if (sInstalled) {
            return;
        }
        sInstalled = true;
        final String pkg = BActivityThread.getAppPackageName();
        final int userId = BActivityThread.getUserId();

        prepareSafeAnogsEnvironment(appContext, pkg, userId);

        // Check if previous session crashed and auto-heal corrupted shader cache & OBB links
        try {
            File marker = getCrashMarkerFile(pkg, userId);
            if (marker.exists()) {
                marker.delete();
                healGameResources(appContext, pkg, userId);
            }
        } catch (Throwable ignored) {
        }

        final Thread.UncaughtExceptionHandler previousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (shouldAbsorbNonFatalException(thread, throwable)) {
                Log.w(TAG, "Absorbed non-fatal background exception on thread="
                        + (thread != null ? thread.getName() : "null") + ": " + throwable);
                return;
            }
            try {
                File marker = getCrashMarkerFile(pkg, userId);
                FileUtils.mkdirs(marker.getParentFile());
                marker.createNewFile();
            } catch (Throwable ignored) {
            }
            if (previousHandler != null) {
                previousHandler.uncaughtException(thread, throwable);
            }
        });
    }

    private static boolean shouldAbsorbNonFatalException(Thread thread, Throwable t) {
        if (t == null) {
            return true;
        }
        boolean isMainThread = false;
        try {
            Looper mainLooper = Looper.getMainLooper();
            isMainThread = mainLooper != null && mainLooper.getThread() == thread;
        } catch (Throwable ignored) {
        }
        if (isMainThread) {
            return false;
        }
        String msg = t.getMessage() != null ? t.getMessage() : "";
        String cls = t.getClass().getName();
        String tName = thread != null && thread.getName() != null ? thread.getName() : "";

        // Never kill the game when a background Binder, GMS, WebView, or anti-cheat/telemetry worker throws
        if (t instanceof RemoteException
                || cls.contains("DeadSystemException")
                || cls.contains("DeadObjectException")
                || (t instanceof SecurityException && !isMainThread)
                || (t instanceof IllegalStateException && (msg.contains("Service") || msg.contains("Broadcast") || msg.contains("SharedPreferences")))
                || tName.contains("Firebase")
                || tName.contains("Measurement")
                || tName.contains("Gms")
                || tName.contains("OkHttp")
                || tName.contains("Crashlytics")
                || tName.contains("CrashSight")
                || tName.contains("GCloud")
                || tName.contains("ano")
                || tName.contains("tss")
                || tName.contains("Sentry")
                || tName.contains("Bugly")) {
            return true;
        }
        return false;
    }

    public static int healGameResources(Context context, String packageName, int userId) {
        int cleanedCount = 0;
        String pkg = (packageName != null && !packageName.isEmpty())
                ? packageName
                : BActivityThread.getAppPackageName();
        if (pkg == null || pkg.isEmpty()) {
            return 0;
        }
        try {
            Context ctx = context != null ? context : TeamDarkCoreEngine.getContext();
            if (ctx != null && ctx.getCacheDir() != null) {
                cleanedCount += cleanCorruptCacheTree(ctx.getCacheDir());
            }
        } catch (Throwable ignored) {
        }
        try {
            File extData = BEnvironment.getExternalUserAppDataDir(userId, pkg);
            if (extData != null && extData.isDirectory()) {
                cleanedCount += cleanCorruptCacheTree(extData);
            }
        } catch (Throwable ignored) {
        }
        return cleanedCount;
    }

    private static int cleanCorruptCacheTree(File dir) {
        if (dir == null || !dir.isDirectory()) {
            return 0;
        }
        int removed = 0;
        File[] children = dir.listFiles();
        if (children == null) {
            return 0;
        }
        for (File child : children) {
            if (child == null) continue;
            String name = child.getName();
            if ("ano_tmp".equalsIgnoreCase(name) || "tss_tmp".equalsIgnoreCase(name)) {
                continue;
            }
            if (child.isDirectory()) {
                if ("GPUCache".equalsIgnoreCase(name)
                        || "ShaderCache".equalsIgnoreCase(name)
                        || "GrShaderCache".equalsIgnoreCase(name)
                        || "Code Cache".equalsIgnoreCase(name)) {
                    FileUtils.deleteDir(child);
                    removed++;
                } else {
                    removed += cleanCorruptCacheTree(child);
                }
            } else {
                String lower = name.toLowerCase(java.util.Locale.US);
                if (lower.endsWith(".ushaderprecache")
                        || lower.endsWith(".upipelinecache")
                        || lower.startsWith("pipelinecache")
                        || lower.endsWith(".tmp")
                        || (lower.endsWith(".shader") && child.length() == 0)) {
                    if (child.delete()) {
                        removed++;
                    }
                }
            }
        }
        return removed;
    }
}
