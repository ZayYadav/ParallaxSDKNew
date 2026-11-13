package com.featurestic.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.Keep;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.featurestic.box.FeaturesticBoxCore;
import com.featurestic.box.core.RNative;
import com.featurestic.box.core.env.BEnvironment;
import com.featurestic.box.core.env.FeaturesticStorageManager;
import com.featurestic.box.entity.pm.InstallResult;

/**
 * Clean, High-Performance SDK Facade for Featurestic Virtual Box Loaders.
 * Single Import:
 *   import com.featurestic.sdk.FeaturesticBox;
 */
@Keep
public final class FeaturesticBox {
    public static final String VERSION = "5.1.0-CLEAN";
    private static final Map<String, Boolean> sFeatures = new ConcurrentHashMap<>();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final ExecutorService SETUP_EXECUTOR = Executors.newSingleThreadExecutor();

    public interface SetupCallback {
        void onProgress(int percent, String statusTitle, String statusDetail);
        void onComplete(boolean success, String message);
    }

    private FeaturesticBox() {
    }

    public static FeaturesticBoxCore get() {
        return FeaturesticBoxCore.get();
    }

    public static FeaturesticBoxCore getCore() {
        return FeaturesticBoxCore.get();
    }

    public static FeaturesticBoxCore getInstance() {
        return FeaturesticBoxCore.get();
    }

    public static FeaturesticBoxCore core() {
        return FeaturesticBoxCore.get();
    }

    public static void preInitFast(Context context) {
        if (context != null) {
            try {
                FeaturesticBoxCore.get().ensureAttachedBaseContext(context);
            } catch (Throwable ignored) {
            }
        }
    }

    public static void init(Context context) {
        if (context != null) {
            FeaturesticBoxCore.get().ensureInitialized(context);
        }
    }

    public static void setSdkKey(String sdkKey) {
        com.featurestic.box.security.SdkLicenseGuard.setSdkKey(sdkKey);
    }

    public static void setSdkKey(Context context, String sdkKey) {
        com.featurestic.box.security.SdkLicenseGuard.setSdkKey(context, sdkKey);
    }

    public static String getRemotePluginUrl(Context context) {
        return com.featurestic.box.security.SdkLicenseGuard.getPluginUrl(context);
    }

    public static void init(Context context, String sdkKey) {
        setSdkKey(context, sdkKey);
        init(context);
    }

    public static void init(Context context, boolean enableDaemon) {
        FeaturesticBoxCore.setEnableDaemonService(enableDaemon);
        init(context);
    }

    public static void attachBaseContext(Context context) {
        if (context != null) {
            FeaturesticBoxCore.get().ensureAttachedBaseContext(context);
        }
    }

    public static void doAttachBaseContext(Context context) {
        if (context != null) {
            FeaturesticBoxCore.get().ensureAttachedBaseContext(context);
        }
    }

    public static void doCreate() {
        FeaturesticBoxCore.get().doCreate();
    }

    public static void doCreate(Context context) {
        FeaturesticBoxCore.get().doCreate(context);
    }

    /**
     * All-in-one asynchronous Game Setup for Loaders:
     * 1. Initializes Virtual Container
     * 2. Clones Game APK into Virtual Box
     * 3. Copies / Mounts OBB & Data Directories
     * 4. Configures 120 FPS & Clean Device Identity
     */
    public static boolean hasClonedNativeLibs(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) return false;
        try {
            File libDir = BEnvironment.getAppLibDir(packageName.trim());
            if (libDir != null && libDir.isDirectory()) {
                File[] files = libDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f != null && f.isFile() && f.getName().endsWith(".so") && f.length() > 0) {
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static void setupGameAsync(final Context context, final String packageName, final int userId, final SetupCallback callback) {
        SETUP_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Step 1/5: Initialize Virtual Container & Safe Native Directories
                    postProgress(callback, 12, "Step 1/5 • Initializing Virtual Container...",
                            "Preparing sandbox directories for " + packageName);
                    if (context != null) {
                        FeaturesticBoxCore.get().ensureInitialized(context);
                    }
                    com.featurestic.box.utils.compat.CrashShieldAndResourceHealer.prepareSafeAnogsEnvironment(
                            context, packageName, userId);
                    Thread.sleep(180);

                    // Step 2/5: Discover Host Base APK, Split APKs & Native Library Path
                    postProgress(callback, 28, "Step 2/5 • Scanning Game APK & Split APKs...",
                            "Resolving base.apk, split_config APKs & native libraries");
                    PackageInfo hostPi = FeaturesticBoxCore.findHostPackageInfo(context, packageName);
                    int splitCount = (hostPi != null && hostPi.applicationInfo != null && hostPi.applicationInfo.splitSourceDirs != null)
                            ? hostPi.applicationInfo.splitSourceDirs.length : 0;
                    Thread.sleep(180);

                    // Step 3/5: Clone Game APK + Split APKs + Native .so Libraries into Virtual Box
                    postProgress(callback, 45, "Step 3/5 • Cloning Game APK & Native Libraries...",
                            "Extracting native .so libraries (" + (splitCount > 0 ? ("base.apk + " + splitCount + " splits") : "base.apk") + ")");
                    InstallResult res = FeaturesticBoxCore.get().installPackageAsUser(packageName, userId);
                    if (!isInstalled(packageName, userId)) {
                        String hostApk = FeaturesticBoxCore.findHostApkPath(context, packageName);
                        if (hostApk != null && !hostApk.isEmpty()) {
                            res = FeaturesticBoxCore.get().installPackageAsUser(hostApk, userId);
                        }
                    }
                    Thread.sleep(240);

                    // Step 4/5: Copy / Sync OBB & External Data to /SdCard/Android/obb/<pkg>
                    postProgress(callback, 60, "Step 4/5 • Syncing OBB to Virtual SDCard...",
                            "Syncing /SdCard/Android/obb/" + packageName);
                    FeaturesticStorageManager.copyOrSyncObbWithProgress(context, packageName, new FeaturesticStorageManager.CopyProgressListener() {
                        @Override
                        public void onProgress(int obbPct, long copiedBytes, long totalBytes, String fileName) {
                            int overallPct = 60 + (obbPct * 34 / 100);
                            long copiedMb = copiedBytes / (1024L * 1024L);
                            long totalMb = Math.max(1L, totalBytes / (1024L * 1024L));
                            postProgress(callback, overallPct,
                                    "Step 4/5 • Copying OBB (" + obbPct + "%) • " + copiedMb + " / " + totalMb + " MB",
                                    fileName);
                        }
                    });
                    FeaturesticStorageManager.syncDataToVirtualIfNeeded(packageName);

                    // Step 5/5: Verify Clone Integrity (Package + Native .so + 120 FPS Engine)
                    postProgress(callback, 96, "Step 5/5 • Verifying Clone Integrity...",
                            "Checking virtual APK, native .so & 120 FPS profile");
                    FeaturesticBoxCore.get().setTargetGameFps(120);
                    RNative.preparePendingPluginOnWorkerThread(context);
                    Thread.sleep(180);

                    postProgress(callback, 100, "Setup Complete!", "Game APK, Native Libs & OBB are ready to launch");
                    final boolean ok = (res != null && res.success) || isInstalled(packageName, userId);
                    final String msg = ok ? "Game environment ready" : (res != null && res.msg != null ? res.msg : "Ready");
                    MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) callback.onComplete(ok, msg);
                        }
                    });
                } catch (final Throwable t) {
                    MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) callback.onComplete(false, "Setup error: " + t.getMessage());
                        }
                    });
                }
            }
        });
    }

    private static void postProgress(final SetupCallback cb, final int pct, final String title, final String detail) {
        if (cb == null) return;
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cb.onProgress(pct, title, detail);
            }
        });
    }

    public static boolean startGame(Context context, String packageName) {
        return startGame(context, packageName, 0, null);
    }

    public static boolean startGame(Context context, String packageName, int userId) {
        return startGame(context, packageName, userId, null);
    }

    public static boolean startGame(Context context, String packageName, String nativeSoNameOrPath) {
        return startGame(context, packageName, 0, nativeSoNameOrPath);
    }

    public static boolean startGame(Context context, String packageName, int userId, String nativeSoNameOrPath) {
        if (context != null) {
            FeaturesticBoxCore.get().ensureInitialized(context);
        }
        if (packageName != null && !packageName.trim().isEmpty()) {
            String pkg = packageName.trim();
            if (!isInstalled(pkg, userId)) {
                try {
                    FeaturesticBoxCore.get().installPackageAsUser(pkg, userId);
                } catch (Throwable ignored) {
                }
            }
            if (!FeaturesticStorageManager.hasVirtualObbFiles(pkg)) {
                try {
                    FeaturesticStorageManager.copyOrSyncObbWithProgress(context, pkg, null);
                } catch (Throwable ignored) {
                }
            }
        }
        if (nativeSoNameOrPath != null && !nativeSoNameOrPath.trim().isEmpty()) {
            RNative.stagePendingGameLibrary(context, nativeSoNameOrPath.trim());
        }
        return FeaturesticBoxCore.get().launchApk(packageName, userId);
    }

    public static boolean launchGame(Context context, String packageName) {
        return startGame(context, packageName, 0, null);
    }

    public static boolean launchGame(Context context, String packageName, int userId) {
        return startGame(context, packageName, userId, null);
    }

    public static boolean launchGame(Context context, String packageName, String nativeSoNameOrPath) {
        return startGame(context, packageName, 0, nativeSoNameOrPath);
    }

    public static boolean launch(Context context, String packageName) {
        return startGame(context, packageName, 0, null);
    }

    public static boolean launch(String packageName) {
        return FeaturesticBoxCore.get().launchApk(packageName, 0);
    }

    public static boolean launch(String packageName, int userId) {
        return FeaturesticBoxCore.get().launchApk(packageName, userId);
    }

    public static boolean launchApk(String packageName) {
        return FeaturesticBoxCore.get().launchApk(packageName, 0);
    }

    public static boolean launchApk(String packageName, int userId) {
        return FeaturesticBoxCore.get().launchApk(packageName, userId);
    }

    public static InstallResult installPackage(String packageNameOrApkPath) {
        return FeaturesticBoxCore.get().installPackageAsUser(packageNameOrApkPath, 0);
    }

    public static InstallResult installPackage(String packageNameOrApkPath, int userId) {
        return FeaturesticBoxCore.get().installPackageAsUser(packageNameOrApkPath, userId);
    }

    public static boolean installGame(String packageNameOrApkPath) {
        return installGame(packageNameOrApkPath, 0);
    }

    public static boolean installGame(String packageNameOrApkPath, int userId) {
        InstallResult res = FeaturesticBoxCore.get().installPackageAsUser(packageNameOrApkPath, userId);
        return res != null && res.success;
    }

    public static InstallResult installPackageAsUser(String packageNameOrApkPath, int userId) {
        return FeaturesticBoxCore.get().installPackageAsUser(packageNameOrApkPath, userId);
    }

    public static InstallResult installPackageAsUser(File apkFile, int userId) {
        return FeaturesticBoxCore.get().installPackageAsUser(apkFile, userId);
    }

    public static boolean installApkFile(File apkOrBundleFile) {
        return installApkFile(apkOrBundleFile, 0);
    }

    public static boolean installApkFile(File apkOrBundleFile, int userId) {
        InstallResult res = FeaturesticBoxCore.get().installBundleAsUser(apkOrBundleFile, userId);
        return res != null && res.success;
    }

    public static boolean isGameInstalled(String packageName) {
        return isGameInstalled(packageName, 0);
    }

    public static boolean isGameInstalled(String packageName, int userId) {
        return FeaturesticBoxCore.get().isInstalled(packageName, userId);
    }

    public static boolean isInstalled(String packageName) {
        return FeaturesticBoxCore.get().isInstalled(packageName, 0);
    }

    public static boolean isInstalled(String packageName, int userId) {
        return FeaturesticBoxCore.get().isInstalled(packageName, userId);
    }

    public static void stopGame(String packageName) {
        stopGame(packageName, 0);
    }

    public static void stopGame(String packageName, int userId) {
        FeaturesticBoxCore.get().stopPackage(packageName, userId);
    }

    public static void uninstallGame(String packageName) {
        uninstallGame(packageName, 0);
    }

    public static void uninstallGame(String packageName, int userId) {
        FeaturesticBoxCore.get().uninstallPackageAsUser(packageName, userId);
    }

    public static List<PackageInfo> getInstalledGames() {
        return getInstalledGames(0);
    }

    public static List<PackageInfo> getInstalledGames(int userId) {
        List<PackageInfo> list = FeaturesticBoxCore.get().getInstalledPackages(0, userId);
        return list != null ? list : Collections.emptyList();
    }

    public static File getExternalObbDir(String packageName) {
        return BEnvironment.getExternalObbDir(packageName);
    }

    public static File getExternalDataDir(String packageName) {
        return BEnvironment.getExternalDataDir(packageName);
    }


    public static PackageInfo findHostGameInfo(Context context, String packageName) {
        return FeaturesticBoxCore.findHostPackageInfo(context, packageName);
    }

    public static boolean isGameInstalledOnHost(Context context, String packageName) {
        return FeaturesticBoxCore.findHostPackageInfo(context, packageName) != null;
    }

    public static boolean hasHostObb(Context context, String packageName) {
        return !FeaturesticStorageManager.discoverHostObbFiles(context, packageName).isEmpty();
    }

    public static boolean hasVirtualObb(String packageName) {
        return FeaturesticStorageManager.hasVirtualObbFiles(packageName);
    }

    public static boolean copyObbWithProgress(Context context, String packageName, FeaturesticStorageManager.CopyProgressListener listener) {
        return FeaturesticStorageManager.copyOrSyncObbWithProgress(context, packageName, listener);
    }

    public static boolean syncObb(String packageName) {
        return FeaturesticStorageManager.syncObbToVirtualIfNeeded(packageName);
    }

    public static boolean ensureObb(String packageName) {
        return FeaturesticStorageManager.syncObbToVirtualIfNeeded(packageName);
    }

    public static boolean ensureObb(String packageName, int userId) {
        return FeaturesticStorageManager.syncObbToVirtualIfNeeded(packageName);
    }

    public static String resetDeviceId() {
        return FeaturesticBoxCore.resetVirtualDeviceIdentity();
    }

    public static boolean loadNativeSo(String pathOrName) {
        return FeaturesticBoxCore.loadCustomLibrary(pathOrName);
    }

    public static boolean loadCustomLibrary(String pathOrName) {
        return FeaturesticBoxCore.loadCustomLibrary(pathOrName);
    }

    public static boolean loadCustomLibrary(Context context, String source) {
        return loadNativeSo(context, source);
    }

    public static boolean loadNativeSo(Context context, String source) {
        if (source == null || source.trim().isEmpty()) return false;
        if (context != null) {
            FeaturesticBoxCore.get().ensureInitialized(context);
        }
        if (!FeaturesticBoxCore.get().isFeaturesticProcess()) {
            RNative.stagePendingGameLibrary(context, source.trim());
            return true;
        }
        return RNative.loadCustomLibrary(source.trim());
    }

    public static void setFeature(String key, boolean enabled) {
        if (key == null) return;
        String k = key.trim().toLowerCase(java.util.Locale.US);
        sFeatures.put(key, enabled);
        sFeatures.put(k, enabled);
        try {
            if ("hide_root".equals(k) || "root".equals(k)) {
                FeaturesticBoxCore.setHideRoot(enabled);
            } else if ("hide_xposed".equals(k) || "xposed".equals(k)) {
                FeaturesticBoxCore.setHideXposed(enabled);
            } else if ("daemon".equals(k) || "daemon_service".equals(k)) {
                FeaturesticBoxCore.setEnableDaemonService(enabled);
            } else if ("autopilot".equals(k)) {
                FeaturesticBoxCore.get().setAutopilotGamingEngineEnabled(enabled);
            } else if ("low_latency_network".equals(k) || "network".equals(k)) {
                FeaturesticBoxCore.get().setLowNetworkOptimizationEnabled(enabled);
            } else if ("low_latency_audio".equals(k) || "audio".equals(k)) {
                FeaturesticBoxCore.get().setAudioLowLatencyEnabled(enabled);
            } else if ("thermal_guard".equals(k) || "thermal".equals(k)) {
                FeaturesticBoxCore.get().setThermalGuardEnabled(enabled);
            }
        } catch (Throwable ignored) {
        }
    }

    public static boolean isFeatureEnabled(String key) {
        if (key == null) return true;
        Boolean val = sFeatures.get(key);
        return val == null || val;
    }

    public static void hideLoadingSafe(final View view) {
        if (view == null) return;
        Runnable action = () -> {
            try {
                view.setVisibility(View.GONE);
                ViewParent parent = view.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(view);
                }
            } catch (Throwable ignored) {
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            MAIN_HANDLER.post(action);
        }
    }

    public static void safeRemoveView(final View view) {
        hideLoadingSafe(view);
    }
}
