package com.teamdark.core;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import com.teamdark.core.utils.compat.HiddenApiReflection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamdark.black.android.app.BRActivityThread;
import com.teamdark.black.android.os.BRUserHandle;
import com.teamdark.core.app.LauncherActivity;
import com.teamdark.core.app.configuration.AppLifecycleCallback;
import com.teamdark.core.app.configuration.ClientConfiguration;
import com.teamdark.core.core.GmsCore;
import com.teamdark.core.core.RNative;
import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.core.system.DaemonService;
import com.teamdark.core.core.system.ServiceManager;
import com.teamdark.core.core.system.user.BUserHandle;
import com.teamdark.core.core.system.user.BUserInfo;
import com.teamdark.core.entity.AppConfig;
import com.teamdark.core.entity.pm.InstallOption;
import com.teamdark.core.entity.pm.InstallResult;
import com.teamdark.core.entity.pm.InstalledModule;
import com.teamdark.core.fake.delegate.ContentProviderDelegate;
import com.teamdark.core.fake.frameworks.BActivityManager;
import com.teamdark.core.fake.frameworks.BJobManager;
import com.teamdark.core.fake.frameworks.BPackageManager;
import com.teamdark.core.fake.frameworks.BStorageManager;
import com.teamdark.core.fake.frameworks.BUserManager;
import com.teamdark.core.fake.frameworks.BXposedManager;
import com.teamdark.core.fake.hook.HookManager;
import com.teamdark.core.proxy.ProxyManifest;
import com.teamdark.core.utils.FileUtils;
import com.teamdark.core.utils.Md5Utils;
import com.teamdark.core.utils.ShellUtils;
import com.teamdark.core.utils.Slog;
import com.teamdark.core.utils.compat.BuildCompat;
import com.teamdark.core.utils.compat.BundleCompat;
import com.teamdark.core.utils.compat.XposedParserCompat;
import com.teamdark.core.utils.provider.ProviderCall;

/**
 * Created by @TeamDarkCore on 3/30/21.
 */
@SuppressLint({"StaticFieldLeak", "NewApi"})
public class TeamDarkCoreEngine extends ClientConfiguration {
    public static final String TAG = "TeamDarkCoreEngine";

    private static volatile Context sContext;
    private static volatile PackageManager sHostPackageManager;

    static {
        try {
            HiddenApiReflection.exemptAll();
        } catch (Throwable ignored) {
        }
    }

    private static int resolveHostUidSafe() {
        try {
            return Process.myUid();
        } catch (Throwable t) {
            return 10000;
        }
    }

    private static int resolveHostUserIdSafe() {
        try {
            int uid = Process.myUid();
            if (uid >= 0) {
                return uid / 100000;
            }
        } catch (Throwable ignored) {
        }
        try {
            Integer id = BRUserHandle.get().myUserId();
            if (id != null && id >= 0) {
                return id;
            }
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static Handler createMainHandlerSafe() {
        try {
            Looper mainLooper = Looper.getMainLooper();
            if (mainLooper != null) {
                return new Handler(mainLooper);
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static final TeamDarkCoreEngine sTeamDarkCoreEngine = new TeamDarkCoreEngine();
    private ProcessType mProcessType = ProcessType.Main;
    private final Map<String, IBinder> mServices = new java.util.concurrent.ConcurrentHashMap<>();
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    private volatile ClientConfiguration mClientConfiguration;
    private final List<AppLifecycleCallback> mAppLifecycleCallbacks = new java.util.concurrent.CopyOnWriteArrayList<>();
    private final Handler mHandler = createMainHandlerSafe();
    private final int mHostUid = resolveHostUidSafe();
    private final int mHostUserId = resolveHostUserIdSafe();
    private AppConfig appConfig;
    private volatile boolean mAttachedBaseContext = false;
    private volatile boolean mCreated = false;

    public static TeamDarkCoreEngine get() {
        return sTeamDarkCoreEngine;
    }

    public static TeamDarkCoreEngine getInstance() {
        return sTeamDarkCoreEngine;
    }


    public static boolean BthreadMain() {
        return true;
    }

    public void showToast(final String message) {
        if (message == null || message.trim().isEmpty()) return;
        final Context ctx = getContext();
        if (ctx == null || mHandler == null) return;
        mHandler.post(() -> {
            try {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            } catch (Throwable ignored) {
            }
        });
    }

    public static String getAppPackageName() {
        try {
            return com.teamdark.core.app.BActivityThread.getAppPackageName();
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static String getAppProcessName() {
        try {
            return com.teamdark.core.app.BActivityThread.getAppProcessName();
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static int getBUid() {
        try {
            return com.teamdark.core.app.BActivityThread.getBUid();
        } catch (Throwable ignored) {
            return -1;
        }
    }

    public static int getUserId() {
        try {
            return com.teamdark.core.app.BActivityThread.getUserId();
        } catch (Throwable ignored) {
            return 0;
        }
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) {
        return BPackageManager.get().getApplicationInfo(packageName, flags, userId);
    }

    public Handler getHandler() {
        return mHandler;
    }

    public static PackageManager getPackageManager() {
        if (sHostPackageManager != null) {
            return sHostPackageManager;
        }
        Context ctx = getContext();
        return ctx != null ? ctx.getPackageManager() : null;
    }

    public static String getHostPkg() {
        return get().getHostPackageName();
    }

    public static int getHostUid() {
        return get().mHostUid;
    }

    public static int getHostUserId() {
        return get().mHostUserId;
    }

    public static Context getContext() {
        if (sContext == null) {
            try {
                Object at = mainThread();
                if (at != null) {
                    Application app = BRActivityThread.get(at).mInitialApplication();
                    if (app != null) {
                        sContext = app;
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return sContext;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public Thread.UncaughtExceptionHandler getExceptionHandler() {
        return mExceptionHandler;
    }

    public void setExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
        mExceptionHandler = exceptionHandler;
    }

    public static void setHideRoot(boolean hideRoot) {
        BEnvironment.sHideRoot = hideRoot;
    }

    public static void setHideXposed(boolean hide) {
        BEnvironment.sHideXposed = hide;
    }

    public static void setEnableDaemonService(boolean enableDaemonService) {
        BEnvironment.sEnableDaemonService = enableDaemonService;
    }

    private static ClientConfiguration createDefaultClientConfiguration(final Context context) {
        final String fallbackPkg = context != null ? context.getPackageName() : "";
        return new ClientConfiguration() {
            @Override
            public String getHostPackageName() {
                Context c = sContext != null ? sContext : context;
                return c != null ? c.getPackageName() : fallbackPkg;
            }

            @Override
            public boolean setHideRoot() {
                return true;
            }

            @Override
            public boolean isHideRoot() {
                return true;
            }

            @Override
            public boolean isHideXposed() {
                return true;
            }
        };
    }

    /**
     * Thread-safe, idempotent auto-initializer so even if a Loader APK only calls launchApk()
     * from an Activity or forgets Application.attachBaseContext(), all processes (:main, :core, :p0..:p24)
     * initialize cleanly without crashing or bouncing back.
     */
    public synchronized void ensureAttachedBaseContext(Context context) {
        Context candidate = context;
        if (candidate != null) {
            try {
                Context appCtx = candidate.getApplicationContext();
                if (appCtx != null) {
                    candidate = appCtx;
                }
            } catch (Throwable ignored) {
            }
        }
        if (candidate == null) {
            candidate = getContext();
        }
        if (candidate == null) {
            return;
        }
        if (!mAttachedBaseContext) {
            doAttachBaseContext(candidate, mClientConfiguration);
        }
    }

    public synchronized void ensureInitialized(Context context) {
        Context candidate = context;
        if (candidate != null) {
            try {
                Context appCtx = candidate.getApplicationContext();
                if (appCtx != null) {
                    candidate = appCtx;
                }
            } catch (Throwable ignored) {
            }
        }
        if (candidate == null) {
            candidate = getContext();
        }
        if (candidate == null) {
            return;
        }
        if (!mAttachedBaseContext) {
            doAttachBaseContext(candidate, mClientConfiguration);
        }
        if (!mCreated) {
            doCreate();
        }
    }

    /**
     * 1-line initialization helper for any Loader Application or Activity.
     */
    public void init(Context context) {
        ensureInitialized(context);
    }

    /**
     * 1-argument overload so Loaders do not even need to construct a ClientConfiguration.
     */
    public void doAttachBaseContext(Context context) {
        doAttachBaseContext(context, null);
    }

    public synchronized void doAttachBaseContext(Context context, ClientConfiguration clientConfiguration) {
        if (clientConfiguration != null) {
            mClientConfiguration = clientConfiguration;
            try {
                BEnvironment.sHideRoot = clientConfiguration.isHideRoot();
                BEnvironment.sHideXposed = clientConfiguration.isHideXposed();
                BEnvironment.sEnableDaemonService = clientConfiguration.isEnableDaemonService();
            } catch (Throwable ignored) {
            }
        }
        if (mAttachedBaseContext) {
            return;
        }
        if (context != null && sContext == null) {
            sContext = context;
        }
        if (sContext == null) {
            sContext = getContext();
        }
        if (mClientConfiguration == null) {
            mClientConfiguration = createDefaultClientConfiguration(sContext);
        }
        mAttachedBaseContext = true;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Class<?> hab = Class.forName("org.lsposed.hiddenapibypass.HiddenApiBypass");
                hab.getMethod("addHiddenApiExemptions", String[].class)
                        .invoke(null, (Object) new String[]{""});
            }
        } catch (Throwable ignored) {
        }
        try {
            if (sContext != null) {
                HiddenApiReflection.unseal(sContext);
            }
        } catch (Throwable ignored) {
        }
        try {
            if (sContext != null && sHostPackageManager == null) {
                sHostPackageManager = sContext.getPackageManager();
            }
        } catch (Throwable ignored) {
        }
        try {
            initNotificationManager();
        } catch (Throwable ignored) {
        }

        String hostPkg = TeamDarkCoreEngine.getHostPkg();
        String processName = getProcessName(getContext());
        if (processName == null || processName.isEmpty()) {
            processName = hostPkg != null ? hostPkg : "";
        }

        if (processName.equals(hostPkg)) {
            mProcessType = ProcessType.Main;
        } else if (processName.endsWith(":core") || processName.endsWith(":black")) {
            mProcessType = ProcessType.Server;
        } else {
            mProcessType = ProcessType.BAppClient;
        }

        try {
            BEnvironment.load();
        } catch (Throwable ignored) {
        }

        if (isMainProcess() || isServerProcess()) {
            if (BEnvironment.sEnableDaemonService) {
                try {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), DaemonService.class);
                    if (BuildCompat.isOreo_MR1()) {
                        getContext().startForegroundService(intent);
                    } else {
                        getContext().startService(intent);
                    }
                } catch (Throwable ignored) {
                }
            }
        }
        try {
            HookManager.get().init();
        } catch (Throwable ignored) {
        }
    }

    public void doCreate(Context context) {
        if (context != null && sContext == null) {
            doAttachBaseContext(context, mClientConfiguration);
        }
        doCreate();
    }

    public synchronized void doCreate() {
        if (!mAttachedBaseContext && sContext != null) {
            doAttachBaseContext(sContext, mClientConfiguration);
        }
        if (mCreated) {
            return;
        }
        mCreated = true;

        // fix contentProvider
        if (isTeamDarkProcess()) {
            try {
                ContentProviderDelegate.init();
            } catch (Throwable ignored) {
            }
        }
        if (!isServerProcess()) {
            try {
                ServiceManager.initTeamDarkManager();
            } catch (Throwable ignored) {
            }
        }
        // Main UI process stays lightweight on startup; services warm lazily when needed
    }

    public static Object mainThread() {
        return BRActivityThread.get().currentActivityThread();
    }

    public void startActivity(Intent intent, int userId) {
        if (intent == null) {
            return;
        }
        ensureInitialized(sContext);
        ClientConfiguration cfg = mClientConfiguration;
        if (cfg == null || cfg.isEnableLauncherActivity()) {
            LauncherActivity.launch(intent, userId);
        } else {
            getBActivityManager().startActivity(intent, userId);
        }
    }

    public void onBeforeMainLaunchApk(String packageName, int userid) {
        for (AppLifecycleCallback appLifecycleCallback : TeamDarkCoreEngine.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.beforeMainLaunchApk(packageName, userid);
        }
    }

    public static BJobManager getBJobManager() {
        return BJobManager.get();
    }

    public static BPackageManager getBPackageManager() {
        return BPackageManager.get();
    }

    public static BActivityManager getBActivityManager() {
        return BActivityManager.get();
    }

    public static BStorageManager getBStorageManager() {
        return BStorageManager.get();
    }

    /**
     * 1-argument overload that launches the package in default user slot 0.
     */
    public boolean launchApk(String packageName) {
        return launchApk(packageName, 0);
    }

    public boolean launchApk(String packageName, int userId) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return false;
        }
        ensureInitialized(sContext);


        // Auto-install from host phone if the Loader called launchApk() directly without calling installPackageAsUser() first!
        try {
            if (!isInstalled(packageName, userId)) {
                PackageManager pm = getPackageManager();
                if (pm != null) {
                    PackageInfo hostPkgInfo = pm.getPackageInfo(packageName, 0);
                    if (hostPkgInfo != null) {
                        installPackageAsUser(packageName, userId);
                    }
                }
            }
        } catch (Throwable ignored) {
        }

                onBeforeMainLaunchApk(packageName, userId);
        Intent launchIntent = getBPackageManager().getLaunchIntentForPackage(packageName, userId);
        if (launchIntent == null) {
            // Second-chance fallback: if package record was stale, re-install from host and retry once
            try {
                PackageManager pm = getPackageManager();
                if (pm != null && pm.getPackageInfo(packageName, 0) != null) {
                    installPackageAsUser(packageName, userId);
                    launchIntent = getBPackageManager().getLaunchIntentForPackage(packageName, userId);
                }
            } catch (Throwable ignored) {
            }
        }
        if (launchIntent == null) {
            // Fallback: query host PackageManager launch intent so LauncherActivity can still open and auto-install on worker thread
            try {
                PackageManager pm = getPackageManager();
                if (pm != null) {
                    Intent hostLaunch = pm.getLaunchIntentForPackage(packageName);
                    if (hostLaunch != null) {
                        launchIntent = hostLaunch;
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        if (launchIntent == null) {
            Slog.e(TAG, "Launch intent is null for package: " + packageName);
            return false;
        }
        if (launchIntent.getComponent() == null && launchIntent.getPackage() == null) {
            Slog.e(TAG, "Invalid launch intent: " + packageName);
            return false;
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(launchIntent, userId);
        } catch (Throwable e) {
            Slog.e(TAG, "Launch failed: " + packageName, e);
            return false;
        }
        return true;
    }

    public boolean isInstalled(String packageName) {
        return isInstalled(packageName, 0);
    }

    public boolean isInstalled(String packageName, int userId) {
        if (packageName == null || packageName.trim().isEmpty()) return false;
        ensureInitialized(sContext);
        try {
            return getBPackageManager().isInstalled(packageName, userId);
        } catch (Throwable ignored) {
            return false;
        }
    }

    public void uninstallPackageAsUser(String packageName, int userId) {
        ensureInitialized(sContext);
        getBPackageManager().uninstallPackageAsUser(packageName, userId);
    }

    public void uninstallPackage(String packageName) {
        ensureInitialized(sContext);
        getBPackageManager().uninstallPackage(packageName);
    }


    /**
     * 4-Layer Host Package Discovery (Prevents "Game not found" / NameNotFoundException on Android 11–16):
     *   1. PackageManager.getPackageInfo(pkg, 0)
     *   2. PackageManager.getApplicationInfo(pkg, 0)
     *   3. PackageManager.queryIntentActivities(MAIN/LAUNCHER)
     *   4. Shell "pm path <pkg>" -> PackageManager.getPackageArchiveInfo(apkPath, 0)
     */
    private static final Map<String, PackageInfo> sHostPkgCache = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Set<String> sShellCheckedMissingPkgs =
            java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<String, Boolean>());

    public static PackageInfo findHostPackageInfo(Context context, String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) return null;
        String pkg = packageName.trim();
        PackageInfo cached = sHostPkgCache.get(pkg);
        if (cached != null && cached.applicationInfo != null
                && cached.applicationInfo.sourceDir != null
                && new File(cached.applicationInfo.sourceDir).isFile()) {
            return cached;
        }
        Context ctx = context != null ? context : getContext();
        PackageManager pm = ctx != null ? ctx.getPackageManager() : getPackageManager();
        if (pm != null) {
            try {
                PackageInfo pi = pm.getPackageInfo(pkg, PackageManager.GET_META_DATA);
                if (pi != null && pi.applicationInfo != null) {
                    enrichSplitAndNativeDirsFromPmOrShell(pm, pkg, pi.applicationInfo);
                    sHostPkgCache.put(pkg, pi);
                    return pi;
                }
            } catch (Throwable ignored) {}
            try {
                PackageInfo pi = pm.getPackageInfo(pkg, 0);
                if (pi != null && pi.applicationInfo != null) {
                    enrichSplitAndNativeDirsFromPmOrShell(pm, pkg, pi.applicationInfo);
                    sHostPkgCache.put(pkg, pi);
                    return pi;
                }
            } catch (Throwable ignored) {}
            try {
                ApplicationInfo ai = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
                if (ai != null && ai.sourceDir != null) {
                    enrichSplitAndNativeDirsFromPmOrShell(pm, pkg, ai);
                    PackageInfo archivePi = pm.getPackageArchiveInfo(ai.sourceDir, PackageManager.GET_META_DATA);
                    if (archivePi != null) {
                        archivePi.applicationInfo = ai;
                        sHostPkgCache.put(pkg, archivePi);
                        return archivePi;
                    }
                    PackageInfo synthetic = new PackageInfo();
                    synthetic.packageName = pkg;
                    synthetic.applicationInfo = ai;
                    sHostPkgCache.put(pkg, synthetic);
                    return synthetic;
                }
            } catch (Throwable ignored) {}
            try {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                mainIntent.setPackage(pkg);
                List<android.content.pm.ResolveInfo> list = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);
                if (list != null && !list.isEmpty()) {
                    for (android.content.pm.ResolveInfo ri : list) {
                        if (ri != null && ri.activityInfo != null && ri.activityInfo.applicationInfo != null) {
                            ApplicationInfo ai = ri.activityInfo.applicationInfo;
                            if (pkg.equals(ai.packageName)) {
                                enrichSplitAndNativeDirsFromPmOrShell(pm, pkg, ai);
                                PackageInfo archivePi = ai.sourceDir != null ? pm.getPackageArchiveInfo(ai.sourceDir, PackageManager.GET_META_DATA) : null;
                                if (archivePi != null) {
                                    archivePi.applicationInfo = ai;
                                    sHostPkgCache.put(pkg, archivePi);
                                    return archivePi;
                                }
                                PackageInfo synthetic = new PackageInfo();
                                synthetic.packageName = pkg;
                                synthetic.applicationInfo = ai;
                                sHostPkgCache.put(pkg, synthetic);
                                return synthetic;
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}
        }
        // Layer 4: Shell "pm path <pkg>" fallback — parses BOTH base.apk AND all split_config.*.apk lines!
        if (sShellCheckedMissingPkgs.contains(pkg)) {
            return null;
        }
        try {
            List<String> apkPaths = queryPmPathsViaShell(pkg);
            if (!apkPaths.isEmpty()) {
                String baseApkPath = apkPaths.get(0);
                List<String> splitPaths = new ArrayList<>();
                for (String p : apkPaths) {
                    if (p.endsWith("/base.apk")) {
                        baseApkPath = p;
                        break;
                    }
                }
                for (String p : apkPaths) {
                    if (!p.equals(baseApkPath)) {
                        splitPaths.add(p);
                    }
                }
                PackageInfo archivePi = pm != null ? pm.getPackageArchiveInfo(baseApkPath, PackageManager.GET_META_DATA) : null;
                if (archivePi == null) {
                    archivePi = new PackageInfo();
                    archivePi.packageName = pkg;
                }
                if (archivePi.applicationInfo == null) {
                    archivePi.applicationInfo = new ApplicationInfo();
                    archivePi.applicationInfo.packageName = pkg;
                }
                archivePi.applicationInfo.sourceDir = baseApkPath;
                archivePi.applicationInfo.publicSourceDir = baseApkPath;
                if (!splitPaths.isEmpty()) {
                    String[] splitsArr = splitPaths.toArray(new String[0]);
                    archivePi.applicationInfo.splitSourceDirs = splitsArr;
                    archivePi.applicationInfo.splitPublicSourceDirs = splitsArr;
                }
                inferHostNativeLibraryDir(archivePi.applicationInfo);
                sHostPkgCache.put(pkg, archivePi);
                return archivePi;
            }
        } catch (Throwable ignored) {}
        sShellCheckedMissingPkgs.add(pkg);
        return null;
    }

    private static void enrichSplitAndNativeDirsFromPmOrShell(PackageManager pm, String pkg, ApplicationInfo ai) {
        if (ai == null) return;
        if (ai.metaData == null && pm != null) {
            try {
                ApplicationInfo metaAi = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
                if (metaAi != null && metaAi.metaData != null) {
                    ai.metaData = metaAi.metaData;
                }
            } catch (Throwable ignored) {
            }
        }
        if (ai.splitSourceDirs == null || ai.splitSourceDirs.length == 0) {
            List<String> paths = queryPmPathsViaShell(pkg);
            if (paths.size() > 1) {
                List<String> splits = new ArrayList<>();
                for (String p : paths) {
                    if (ai.sourceDir != null && p.equals(ai.sourceDir)) continue;
                    if (p.endsWith("/base.apk") && ai.sourceDir == null) {
                        ai.sourceDir = p;
                        ai.publicSourceDir = p;
                        continue;
                    }
                    splits.add(p);
                }
                if (!splits.isEmpty()) {
                    String[] arr = splits.toArray(new String[0]);
                    ai.splitSourceDirs = arr;
                    ai.splitPublicSourceDirs = arr;
                }
            }
        }
        inferHostNativeLibraryDir(ai);
    }

    private static void inferHostNativeLibraryDir(ApplicationInfo ai) {
        if (ai == null || ai.sourceDir == null) return;
        if (ai.nativeLibraryDir != null && new File(ai.nativeLibraryDir).exists()) {
            return;
        }
        File parent = new File(ai.sourceDir).getParentFile();
        if (parent != null) {
            String[] abiFolders = is64Bit()
                    ? new String[]{"lib/arm64", "lib/arm64-v8a", "lib/arm"}
                    : new String[]{"lib/arm", "lib/armeabi-v7a", "lib/arm64"};
            for (String sub : abiFolders) {
                File candidate = new File(parent, sub);
                if (candidate.exists()) {
                    ai.nativeLibraryDir = candidate.getAbsolutePath();
                    return;
                }
            }
        }
    }

    public static List<String> queryPmPathsViaShell(String pkg) {
        List<String> paths = new ArrayList<>();
        if (pkg == null || pkg.isEmpty()) return paths;
        try {
            ShellUtils.CommandResult res = ShellUtils.execCommand("pm path " + pkg, false);
            if (res != null && res.successMsg != null) {
                for (String line : res.successMsg.split("\n")) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("package:")) {
                        String apkPath = trimmed.substring("package:".length()).trim();
                        File f = new File(apkPath);
                        if (f.isFile()) {
                            paths.add(apkPath);
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return paths;
    }

    public static String findHostApkPath(Context context, String packageName) {
        PackageInfo pi = findHostPackageInfo(context, packageName);
        if (pi != null && pi.applicationInfo != null && pi.applicationInfo.sourceDir != null) {
            return pi.applicationInfo.sourceDir;
        }
        return null;
    }

    public InstallResult installPackage(String packageName) {
        return installPackageAsUser(packageName, 0);
    }

    public InstallResult installPackage(String packageName, int userId) {
        return installPackageAsUser(packageName, userId);
    }

    public InstallResult installPackage(File apk) {
        return installPackageAsUser(apk, 0);
    }

    public InstallResult installPackage(File apk, int userId) {
        return installPackageAsUser(apk, userId);
    }

    public InstallResult installPackageAsUser(String packageName) {
        return installPackageAsUser(packageName, 0);
    }

    public InstallResult installPackageAsUser(String packageNameOrApkPath, int userId) {
        ensureInitialized(sContext);
        if (packageNameOrApkPath == null || packageNameOrApkPath.trim().isEmpty()) {
            return new InstallResult().installError("Empty package name or APK path");
        }
        String input = packageNameOrApkPath.trim();
        try {
            // Check if the caller passed a file path (e.g. "/data/app/.../base.apk" or "/sdcard/Download/game.apk")
            if (input.startsWith("/") || input.endsWith(".apk") || input.endsWith(".xapk")
                    || input.endsWith(".apks") || input.endsWith(".apkm") || input.endsWith(".zip")) {
                File f = new File(input);
                if (f.exists() && f.isFile()) {
                    String lower = f.getName().toLowerCase(java.util.Locale.US);
                    if (lower.endsWith(".xapk") || lower.endsWith(".apks") || lower.endsWith(".apkm") || lower.endsWith(".zip")) {
                        return installBundleAsUser(f, userId);
                    }
                    boolean isSystemOrDataApp = input.startsWith("/data/app/")
                            || input.startsWith("/system/")
                            || input.startsWith("/product/")
                            || input.startsWith("/vendor/")
                            || input.startsWith("/apex/");
                    InstallOption opt = isSystemOrDataApp ? InstallOption.installBySystem() : InstallOption.installByStorage();
                    return getBPackageManager().installPackageAsUser(f.getAbsolutePath(), opt, userId);
                }
            }
            String hostApkPath = findHostApkPath(sContext, input);
            if (hostApkPath != null && !hostApkPath.isEmpty()) {
                InstallResult res = getBPackageManager().installPackageAsUser(
                        hostApkPath, InstallOption.installBySystem(), userId);
                return res != null ? res : new InstallResult().installError("Install failed");
            }
            File f = new File(input);
            if (f.exists() && f.isFile()) {
                return getBPackageManager().installPackageAsUser(f.getAbsolutePath(), InstallOption.installByStorage(), userId);
            }
            return new InstallResult().installError("Game not installed on device: " + input);
        } catch (Throwable t) {
            return new InstallResult().installError(t.getMessage());
        }
    }

    public InstallResult installPackageAsUser(File apk) {
        return installPackageAsUser(apk, 0);
    }

    public InstallResult installPackageAsUser(File apk, int userId) {
        ensureInitialized(sContext);
        return getBPackageManager().installPackageAsUser(apk.getAbsolutePath(), InstallOption.installByStorage(), userId);
    }

    public InstallResult installPackageAsUser(Uri apk) {
        return installPackageAsUser(apk, 0);
    }

    public InstallResult installPackageAsUser(Uri apk, int userId) {
        ensureInitialized(sContext);
        return getBPackageManager().installPackageAsUser(apk.toString(), InstallOption.installByStorage().makeUriFile(), userId);
    }

    public InstallResult installXPModule(File apk) {
        return getBPackageManager().installPackageAsUser(apk.getAbsolutePath(), InstallOption.installByStorage().makeXposed(), BUserHandle.USER_XPOSED);
    }

    public InstallResult installXPModule(Uri apk) {
        return getBPackageManager().installPackageAsUser(apk.toString(), InstallOption.installByStorage().makeXposed().makeUriFile(), BUserHandle.USER_XPOSED);
    }

    public InstallResult installXPModule(String packageName) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            String path = packageInfo.applicationInfo.sourceDir;
            return getBPackageManager().installPackageAsUser(path, InstallOption.installBySystem().makeXposed(), BUserHandle.USER_XPOSED);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new InstallResult().installError(e.getMessage());
        }
    }

    public void uninstallXPModule(String packageName) {
        uninstallPackage(packageName);
    }

    public boolean isXPEnable() {
        return BXposedManager.get().isXPEnable();
    }

    public void setXPEnable(boolean enable) {
        BXposedManager.get().setXPEnable(enable);
    }

    public boolean isXposedModule(File file) {
        return XposedParserCompat.isXPModule(file.getAbsolutePath());
    }

    public boolean isInstalledXposedModule(String packageName) {
        return isInstalled(packageName, BUserHandle.USER_XPOSED);
    }

    public boolean isModuleEnable(String packageName) {
        return BXposedManager.get().isModuleEnable(packageName);
    }

    public void setModuleEnable(String packageName, boolean enable) {
        BXposedManager.get().setModuleEnable(packageName, enable);
    }

    public List<InstalledModule> getInstalledXPModules() {
        return BXposedManager.get().getInstalledModules();
    }

    public List<ApplicationInfo> getInstalledApplications(int flags, int userId) {
        return getBPackageManager().getInstalledApplications(flags, userId);
    }

    public List<PackageInfo> getInstalledPackages(int flags, int userId) {
        return getBPackageManager().getInstalledPackages(flags, userId);
    }

    public void clearPackage(String packageName) {
        clearPackage(packageName, 0);
    }

    public void clearPackage(String packageName, int userId) {
        BPackageManager.get().clearPackage(packageName, userId);
    }

    public void stopPackage(String packageName) {
        stopPackage(packageName, 0);
    }

    public void stopPackage(String packageName, int userId) {
        BPackageManager.get().stopPackage(packageName, userId);
    }

    public boolean isAppRunning(String packageName) {
        return isAppRunning(packageName, 0);
    }

    public boolean isAppRunning(String packageName, int userId) {
        return getBPackageManager().isAppRunning(packageName, userId);
    }

    public List<BUserInfo> getUsers() {
        return BUserManager.get().getUsers();
    }

    public BUserInfo createUser(int userId) {
        return BUserManager.get().createUser(userId);
    }

    public ApplicationInfo getApplicationInfo(String packageName) {
        return BPackageManager.get().getApplicationInfo(packageName, 0, 0);
    }

    public void deleteUser(int userId) {
        BUserManager.get().deleteUser(userId);
    }

    public List<AppLifecycleCallback> getAppLifecycleCallbacks() {
        return mAppLifecycleCallbacks;
    }

    public void removeAppLifecycleCallback(AppLifecycleCallback appLifecycleCallback) {
        mAppLifecycleCallbacks.remove(appLifecycleCallback);
    }

    public void addAppLifecycleCallback(AppLifecycleCallback appLifecycleCallback) {
        mAppLifecycleCallbacks.add(appLifecycleCallback);
    }

    public boolean isSupportGms() {
        return GmsCore.isSupportGms();
    }

    public boolean isInstallGms(int userId) {
        return GmsCore.isInstalledGoogleService(userId);
    }

    public InstallResult installGms(int userId) {
        return GmsCore.installGApps(userId);
    }

    public boolean uninstallGms(int userId) {
        GmsCore.uninstallGApps(userId);
        return !GmsCore.isInstalledGoogleService(userId);
    }

    public IBinder getService(String name) {
        IBinder binder = mServices.get(name);
        if (binder != null && binder.isBinderAlive()) {
            return binder;
        }
        if (isMainProcess() || isServerProcess()) {
            try {
                com.teamdark.core.core.system.TeamDarkCoreSystem.getSystem().startup();
                binder = ServiceManager.getService(name);
                if (binder != null) {
                    mServices.put(name, binder);
                    return binder;
                }
            } catch (Throwable ignored) {
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("_G_|_server_name_", name);
        bundle.putString("_B_|_server_name_", name);
        Bundle vm = ProviderCall.callSafely(ProxyManifest.getBindProvider(), "VM", null, bundle);
        if (vm != null) {
            binder = BundleCompat.getBinder(vm, "_G_|_server_");
            if (binder == null) {
                binder = BundleCompat.getBinder(vm, "_B_|_server_");
            }
        }
        if (binder == null && (isMainProcess() || isServerProcess())) {
            try {
                com.teamdark.core.core.system.TeamDarkCoreSystem.getSystem().startup();
                binder = ServiceManager.getService(name);
            } catch (Throwable ignored) {
            }
        }
        Slog.d(TAG, "getService: " + name + ", " + binder);
        if (binder != null) {
            mServices.put(name, binder);
        }
        return binder;
    }

    //Process type
    private enum ProcessType {
        Server, //Server process
        BAppClient, //Black app process
        Main, //Main process
    }

    public boolean isTeamDarkProcess() {
        return mProcessType == ProcessType.BAppClient;
    }

    public synchronized void ensureVirtualClientProcess(Context context) {
        if (context != null && sContext == null) {
            sContext = context.getApplicationContext() != null ? context.getApplicationContext() : context;
        }
        mProcessType = ProcessType.BAppClient;
        try {
            RNative.disableHiddenApi();
        } catch (Throwable ignored) {
        }
        try {
            HiddenApiReflection.unseal(sContext);
        } catch (Throwable ignored) {
        }
        if (!mAttachedBaseContext && sContext != null) {
            doAttachBaseContext(sContext, mClientConfiguration);
            mProcessType = ProcessType.BAppClient;
        }
        try {
            HookManager.get().init();
            HookManager.get().checkEnv(com.teamdark.core.fake.service.HCallbackStub.class);
            HookManager.get().checkEnv(com.teamdark.core.fake.delegate.AppInstrumentation.class);
        } catch (Throwable ignored) {
        }
    }

    public boolean isBlackProcess() {
        return isTeamDarkProcess();
    }

    public boolean isMainProcess() {
        return mProcessType == ProcessType.Main;
    }

    public boolean isServerProcess() {
        return mProcessType == ProcessType.Server;
    }

    @Override
    public boolean setHideRoot() {
        return mClientConfiguration == null || mClientConfiguration.setHideRoot();
    }

    @Override
    public boolean isHideRoot() {
        return mClientConfiguration == null || mClientConfiguration.isHideRoot();
    }

    @Override
    public boolean isHideXposed() {
        return mClientConfiguration == null || mClientConfiguration.isHideXposed();
    }

    @Override
    public String getHostPackageName() {
        if (mClientConfiguration != null) {
            String pkg = mClientConfiguration.getHostPackageName();
            if (pkg != null && !pkg.isEmpty()) {
                return pkg;
            }
        }
        Context ctx = getContext();
        return ctx != null ? ctx.getPackageName() : "";
    }

    @Override
    public boolean requestInstallPackage(File file) {
        return mClientConfiguration != null && mClientConfiguration.requestInstallPackage(file);
    }

    private void startLogcat() {
        new Thread(() -> {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getContext().getPackageName() + "_logcat.txt");
            FileUtils.deleteDir(file);
            ShellUtils.execCommand("logcat -c", false);
            ShellUtils.execCommand("logcat -f " + file.getAbsolutePath(), false);
        }).start();
    }

    private static String getProcessName(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                String pn = android.app.Application.getProcessName();
                if (pn != null && !pn.isEmpty()) {
                    return pn;
                }
            } catch (Throwable ignored) {
            }
        }
        try {
            Object at = mainThread();
            if (at != null) {
                String pn = BRActivityThread.get(at).getProcessName();
                if (pn != null && !pn.isEmpty()) {
                    return pn;
                }
            }
        } catch (Throwable ignored) {
        }
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("/proc/self/cmdline"))) {
            String cmd = reader.readLine();
            if (cmd != null) {
                cmd = cmd.trim();
                int nul = cmd.indexOf('\0');
                if (nul > 0) {
                    cmd = cmd.substring(0, nul);
                }
                if (!cmd.isEmpty()) {
                    return cmd;
                }
            }
        } catch (Throwable ignored) {
        }
        try {
            if (context != null) {
                int myPid = Process.myPid();
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                if (am != null) {
                    List<ActivityManager.RunningAppProcessInfo> running = am.getRunningAppProcesses();
                    if (running != null) {
                        for (ActivityManager.RunningAppProcessInfo info : running) {
                            if (info != null && info.pid == myPid && info.processName != null) {
                                return info.processName;
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return context != null ? context.getPackageName() : "";
    }

    public static boolean is64Bit() {
        if (BuildCompat.isM()) {
            return Process.is64Bit();
        } else {
            return Build.CPU_ABI.equals("arm64-v8a");
        }
    }

    private void initNotificationManager() {
        Context ctx = TeamDarkCoreEngine.getContext();
        if (ctx == null) return;
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;
        String CHANNEL_ONE_ID = ctx.getPackageName() + ".teamdarkcore_core";
        String CHANNEL_ONE_NAME = "TeamDarkCore";
        if (BuildCompat.isOreo_MR1()) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            nm.createNotificationChannel(notificationChannel);
        }
    }

    public boolean checkSelfPermission(String permission) {
        PackageManager pm = getPackageManager();
        return pm != null && pm.checkPermission(permission, getHostPackageName()) == 0;
    }

    public boolean clearAppCache(String packageName) {
        return clearAppCache(packageName, 0);
    }

    /**
     * Clears internal, code_cache, and external cache directories for a virtual app.
     */
    public boolean clearAppCache(String packageName, int userId) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return false;
        }
        try {
            FileUtils.deleteDir(BEnvironment.getDataCacheDir(packageName, userId));
            FileUtils.deleteDir(new File(BEnvironment.getDataDir(packageName, userId), "code_cache"));
            FileUtils.deleteDir(BEnvironment.getExternalDataCacheDir(packageName));
            FileUtils.mkdirs(BEnvironment.getDataCacheDir(packageName, userId));
            FileUtils.mkdirs(BEnvironment.getExternalDataCacheDir(packageName));
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public long getAppStorageSize(String packageName) {
        return getAppStorageSize(packageName, 0);
    }

    /**
     * Calculates total storage used (in bytes) by a virtual app (APK + internal data + external data + OBB).
     */
    public long getAppStorageSize(String packageName, int userId) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return 0L;
        }
        long total = 0L;
        total += computeDirSize(BEnvironment.getAppDir(packageName));
        total += computeDirSize(BEnvironment.getDataDir(packageName, userId));
        total += computeDirSize(BEnvironment.getDeDataDir(packageName, userId));
        total += computeDirSize(BEnvironment.getExternalDataDir(packageName));
        total += computeDirSize(BEnvironment.getExternalObbDir(packageName));
        return total;
    }

    private static long computeDirSize(File file) {
        if (file == null || !file.exists()) {
            return 0L;
        }
        if (file.isFile()) {
            return file.length();
        }
        long sum = 0L;
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                sum += computeDirSize(child);
            }
        }
        return sum;
    }

    /**
     * Backs up a virtual app's internal and external data (excluding cache, code_cache, and native lib)
     * into a single ZIP file so guest accounts and game settings can be saved and restored.
     */
    public boolean backupAppData(String packageName, int userId, File destZipFile) {
        if (packageName == null || packageName.trim().isEmpty() || destZipFile == null) {
            return false;
        }
        try {
            FileUtils.mkdirs(destZipFile.getParentFile());
            File internalDir = BEnvironment.getDataDir(packageName, userId);
            File externalDir = BEnvironment.getExternalDataDir(packageName);
            java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(
                    new java.io.BufferedOutputStream(new java.io.FileOutputStream(destZipFile)));
            try {
                if (internalDir.isDirectory()) {
                    zipDirectoryRecursive(internalDir, internalDir, "internal/", zos, true);
                }
                if (externalDir.isDirectory()) {
                    zipDirectoryRecursive(externalDir, externalDir, "external/", zos, true);
                }
            } finally {
                zos.close();
            }
            return destZipFile.isFile() && destZipFile.length() > 0;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void zipDirectoryRecursive(
            File rootDir, File currentDir, String prefix, java.util.zip.ZipOutputStream zos, boolean skipCaches) throws java.io.IOException {
        File[] files = currentDir.listFiles();
        if (files == null) return;
        byte[] buf = new byte[16384];
        for (File f : files) {
            String name = f.getName();
            if (skipCaches && currentDir.equals(rootDir)
                    && ("cache".equals(name) || "code_cache".equals(name) || "lib".equals(name))) {
                continue;
            }
            String relPath = prefix + rootDir.toURI().relativize(f.toURI()).getPath();
            if (f.isDirectory()) {
                zipDirectoryRecursive(rootDir, f, prefix, zos, false);
            } else if (f.isFile()) {
                java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(relPath);
                entry.setTime(f.lastModified());
                zos.putNextEntry(entry);
                java.io.FileInputStream fis = new java.io.FileInputStream(f);
                try {
                    int n;
                    while ((n = fis.read(buf)) > 0) {
                        zos.write(buf, 0, n);
                    }
                } finally {
                    fis.close();
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * Restores a virtual app's internal and external data from a backup ZIP created by backupAppData().
     */
    public boolean restoreAppData(String packageName, int userId, File srcZipFile) {
        if (packageName == null || packageName.trim().isEmpty() || srcZipFile == null || !srcZipFile.isFile()) {
            return false;
        }
        try {
            stopPackage(packageName, userId);
            File internalDir = BEnvironment.getDataDir(packageName, userId);
            File externalDir = BEnvironment.getExternalDataDir(packageName);
            FileUtils.mkdirs(internalDir);
            FileUtils.mkdirs(externalDir);
            String internalCanon = internalDir.getCanonicalPath() + File.separator;
            String externalCanon = externalDir.getCanonicalPath() + File.separator;

            java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(
                    new java.io.BufferedInputStream(new java.io.FileInputStream(srcZipFile)));
            try {
                byte[] buf = new byte[16384];
                java.util.zip.ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    String name = entry.getName();
                    File outFile = null;
                    if (name.startsWith("internal/")) {
                        String sub = name.substring("internal/".length());
                        if (!sub.isEmpty()) {
                            File candidate = new File(internalDir, sub);
                            if (candidate.getCanonicalPath().startsWith(internalCanon)) {
                                outFile = candidate;
                            }
                        }
                    } else if (name.startsWith("external/")) {
                        String sub = name.substring("external/".length());
                        if (!sub.isEmpty()) {
                            File candidate = new File(externalDir, sub);
                            if (candidate.getCanonicalPath().startsWith(externalCanon)) {
                                outFile = candidate;
                            }
                        }
                    }
                    if (outFile != null) {
                        if (entry.isDirectory()) {
                            FileUtils.mkdirs(outFile);
                        } else {
                            FileUtils.mkdirs(outFile.getParentFile());
                            java.io.FileOutputStream fos = new java.io.FileOutputStream(outFile);
                            try {
                                int n;
                                while ((n = zis.read(buf)) > 0) {
                                    fos.write(buf, 0, n);
                                }
                            } finally {
                                fos.close();
                            }
                        }
                    }
                    zis.closeEntry();
                }
            } finally {
                zis.close();
            }
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * Applies a built-in 90/120 FPS flagship gaming phone preset across virtual apps.
     */
    public boolean setDevicePreset(String presetName) {
        return com.teamdark.core.utils.compat.DeviceProfileCompat.setPreset(presetName);
    }

    /**
     * Applies a custom device model profile across virtual apps (both Java Build.* and C++ SystemProperties).
     */
    public boolean setCustomDeviceProfile(
            String manufacturer, String brand, String model, String device, String product, String board) {
        return com.teamdark.core.utils.compat.DeviceProfileCompat.setCustomProfile(
                manufacturer, brand, model, device, product, board);
    }

    /**
     * Installs an APK or a multi-part .xapk / .apks / .apkm bundle (containing base.apk + split APKs + optional .obb files).
     */
    public InstallResult installBundleAsUser(File bundleOrApkFile, int userId) {
        ensureInitialized(sContext);
        if (bundleOrApkFile == null || !bundleOrApkFile.isFile()) {
            return new InstallResult().installError("Bundle file does not exist");
        }
        String lowerName = bundleOrApkFile.getName().toLowerCase(java.util.Locale.US);
        boolean isBundleExt = lowerName.endsWith(".xapk")
                || lowerName.endsWith(".apks")
                || lowerName.endsWith(".apkm")
                || lowerName.endsWith(".zip");
        if (!isBundleExt) {
            try (java.util.zip.ZipFile zf = new java.util.zip.ZipFile(bundleOrApkFile)) {
                if (zf.getEntry("AndroidManifest.xml") != null && zf.getEntry("classes.dex") != null) {
                    return installPackageAsUser(bundleOrApkFile, userId);
                }
            } catch (Throwable ignored) {
            }
        }

        File stagingDir = new File(BEnvironment.getCacheDir(), "bundle_" + System.currentTimeMillis());
        FileUtils.mkdirs(stagingDir);
        try {
            java.util.List<File> extractedApks = new java.util.ArrayList<>();
            java.util.List<File> extractedObbs = new java.util.ArrayList<>();
            String stagingCanon = stagingDir.getCanonicalPath() + File.separator;

            try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(
                    new java.io.BufferedInputStream(new java.io.FileInputStream(bundleOrApkFile)))) {
                byte[] buf = new byte[16384];
                java.util.zip.ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        String lowerEntry = entryName.toLowerCase(java.util.Locale.US);
                        if (lowerEntry.endsWith(".apk") || lowerEntry.endsWith(".obb")) {
                            String flatName = new File(entryName).getName();
                            File outFile = new File(stagingDir, flatName);
                            if (outFile.getCanonicalPath().startsWith(stagingCanon)) {
                                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outFile)) {
                                    int n;
                                    while ((n = zis.read(buf)) > 0) {
                                        fos.write(buf, 0, n);
                                    }
                                }
                                if (lowerEntry.endsWith(".apk")) {
                                    extractedApks.add(outFile);
                                } else {
                                    extractedObbs.add(outFile);
                                }
                            }
                        }
                    }
                    zis.closeEntry();
                }
            }

            if (extractedApks.isEmpty()) {
                return installPackageAsUser(bundleOrApkFile, userId);
            }

            File baseApk = null;
            for (File f : extractedApks) {
                String n = f.getName().toLowerCase(java.util.Locale.US);
                if ("base.apk".equals(n) || "base-master.apk".equals(n)) {
                    baseApk = f;
                    break;
                }
            }
            if (baseApk == null) {
                long maxLen = -1L;
                for (File f : extractedApks) {
                    String n = f.getName().toLowerCase(java.util.Locale.US);
                    if (!n.startsWith("config.") && !n.startsWith("split_config.") && f.length() > maxLen) {
                        maxLen = f.length();
                        baseApk = f;
                    }
                }
            }
            if (baseApk == null) {
                baseApk = extractedApks.get(0);
            }

            InstallResult res = installPackageAsUser(baseApk, userId);
            if (!res.success || res.packageName == null) {
                return res;
            }

            String pkg = res.packageName;
            File appDir = BEnvironment.getAppDir(pkg);
            File appLibDir = BEnvironment.getAppLibDir(pkg);
            FileUtils.mkdirs(appDir);
            FileUtils.mkdirs(appLibDir);

            for (File splitApk : extractedApks) {
                if (splitApk.equals(baseApk)) continue;
                String splitName = splitApk.getName();
                if (!splitName.startsWith("split_")) {
                    splitName = "split_" + splitName;
                }
                File dstSplit = new File(appDir, splitName);
                FileUtils.copyFile(splitApk, dstSplit);
                try {
                    com.teamdark.core.utils.NativeUtils.copyNativeLib(dstSplit, appLibDir);
                } catch (Throwable ignored) {
                }
            }

            if (!extractedObbs.isEmpty()) {
                File virtualObbDir = BEnvironment.getExternalObbDir(pkg);
                FileUtils.mkdirs(virtualObbDir);
                for (File obb : extractedObbs) {
                    File dstObb = new File(virtualObbDir, obb.getName());
                    FileUtils.copyFile(obb, dstObb);
                }
            }
            return res;
        } catch (Throwable t) {
            return new InstallResult().installError("Bundle install failed: " + t.getMessage());
        } finally {
            FileUtils.deleteDir(stagingDir);
        }
    }

    public boolean isRuntimeSecure() {
        try {
            return com.teamdark.core.core.RNative.isRuntimeSecure();
        } catch (Throwable ignored) {
            return true;
        }
    }

    public boolean lockHostSignatureSha256(String expectedSha256Hex) {
        if (expectedSha256Hex == null || expectedSha256Hex.trim().isEmpty()) {
            return true;
        }
        try {
            return com.teamdark.core.core.RNative.lockHostSignature(
                    getContext(), expectedSha256Hex.trim());
        } catch (Throwable ignored) {
            return false;
        }
    }

    public boolean isNetworkEnvironmentSafe() {
        return com.teamdark.core.utils.compat.NetworkSecurityGuard.isNetworkEnvironmentSafe(getContext());
    }

    public String getDetectedSecurityThreat() {
        return com.teamdark.core.utils.compat.NetworkSecurityGuard.getDetectedSecurityThreat(getContext());
    }

    /**
     * Static & 0-arg helper matching FEATURESTICBOX_GUIDE.md:
     * TeamDarkCoreEngine.resetVirtualDeviceIdentity()
     */
    public static String resetVirtualDeviceIdentity() {
        return Md5Utils.resetVirtualDeviceIdentity(0);
    }

    public String resetVirtualDeviceIdentity(int userId) {
        return Md5Utils.resetVirtualDeviceIdentity(userId);
    }

    /**
     * Static helper matching FEATURESTICBOX_GUIDE.md:
     * TeamDarkCoreEngine.loadCustomLibrary("libMyPlugin.so")
     */
    public static boolean loadCustomLibrary(String pathOrName) {
        return RNative.loadCustomLibrary(pathOrName);
    }

    public void setDirectAppLoginEnabled(boolean enabled) {
        com.teamdark.core.fake.service.IFacebookWebPackageManagerProxy
                .setDirectFacebookAppLoginEnabled(enabled);
    }

    public boolean isDirectAppLoginEnabled() {
        return com.teamdark.core.fake.service.IFacebookWebPackageManagerProxy
                .isDirectFacebookAppLoginEnabled();
    }

    public void setLowNetworkOptimizationEnabled(boolean enabled) {
        com.teamdark.core.utils.compat.NetworkLowLatencyOptimizer.setEnabled(enabled);
    }

    public boolean isLowNetworkOptimizationEnabled() {
        return com.teamdark.core.utils.compat.NetworkLowLatencyOptimizer.isEnabled();
    }

    public boolean switchAccountSlot(String packageName, int targetUserId, boolean autoLaunch) {
        return com.teamdark.core.TeamDarkAccountSwitcher.switchAccountSlot(packageName, targetUserId, autoLaunch);
    }

    public int getActiveAccountSlot(String packageName) {
        return com.teamdark.core.TeamDarkAccountSwitcher.getActiveAccountSlot(packageName);
    }

    public boolean saveAccountSessionSnapshot(String packageName, int userId, String snapshotTag) {
        return com.teamdark.core.TeamDarkAccountSwitcher.saveSessionSnapshot(packageName, userId, snapshotTag);
    }

    public boolean restoreAccountSessionSnapshot(String packageName, int userId, String snapshotTag, boolean autoLaunch) {
        return com.teamdark.core.TeamDarkAccountSwitcher.restoreSessionSnapshot(packageName, userId, snapshotTag, autoLaunch);
    }

    public void setTargetGameFps(int targetFps) {
        com.teamdark.core.utils.compat.ThermalAndFrameGuard.setTargetFps(targetFps);
    }

    public int getTargetGameFps() {
        return com.teamdark.core.utils.compat.ThermalAndFrameGuard.getTargetFps();
    }

    public void setThermalGuardEnabled(boolean enabled) {
        com.teamdark.core.utils.compat.ThermalAndFrameGuard.setThermalGuardEnabled(enabled);
    }

    public int healGameResources(String packageName, int userId) {
        return com.teamdark.core.utils.compat.CrashShieldAndResourceHealer.healGameResources(
                getContext(), packageName, userId);
    }

    public void setAudioLowLatencyEnabled(boolean enabled) {
        com.teamdark.core.utils.compat.AudioLowLatencyCompat.setEnabled(enabled);
    }

    public long trimGameJunkAndLogs(String packageName, int userId) {
        return com.teamdark.core.utils.compat.StorageDietOptimizer.trimJunkNow(packageName, userId);
    }

    public String exportDiagnosticReport() {
        return com.teamdark.core.TeamDarkDiagnostics.buildDiagnosticReport(getContext());
    }

    public boolean copyDiagnosticReportToClipboard() {
        return com.teamdark.core.TeamDarkDiagnostics.copyDiagnosticReportToClipboard(getContext());
    }

    public void setIpadViewMode(int mode) {
        com.teamdark.core.utils.compat.TeamDarkIpadViewEngine.setMode(mode);
    }

    public int getIpadViewMode() {
        return com.teamdark.core.utils.compat.TeamDarkIpadViewEngine.getCurrentMode();
    }

    public void clearWebLoginSessions() {
        com.teamdark.core.utils.compat.WebViewCompat.clearAllWebLoginSessions(getContext(), null);
    }

    public void clearWebLoginSessions(String packageName, int userId) {
        com.teamdark.core.utils.compat.WebViewCompat.clearAllWebLoginSessions(getContext(), null);
        com.teamdark.core.utils.compat.WebViewCompat.clearSavedOAuthFilesForPackage(packageName, userId);
    }

    public boolean resetGuestAccountKeepResources(String packageName, int userId, boolean autoRelaunch) {
        return com.teamdark.core.GuestAccountManager.resetGuestAccountKeepResources(
                packageName, userId, autoRelaunch);
    }

    public void setAutopilotGamingEngineEnabled(boolean enabled) {
        com.teamdark.core.utils.compat.TeamDarkAutopilotEngine.setAutopilotEnabled(enabled);
    }

    public void requestBatteryOptimizationWhitelistIfNeeded(Activity hostActivity) {
        com.teamdark.core.utils.compat.TeamDarkBatteryAndOemGuard
                .requestIgnoreBatteryOptimizationsIfNeeded(hostActivity);
    }

    public void waitForReady() {
        ensureInitialized(sContext);
    }

    public static boolean startGame(Context context, String packageName) {
        return startGame(context, packageName, 0, null);
    }

    public static boolean startGame(Context context, String packageName, int userId) {
        return startGame(context, packageName, userId, null);
    }

    public static boolean startGame(Context context, String packageName, String soPathOrName) {
        return startGame(context, packageName, 0, soPathOrName);
    }

    public static boolean startGame(Context context, String packageName, int userId, String soPathOrName) {
        if (!com.teamdark.core.security.SdkLicenseGuard.isLicensed(context)) {
            Slog.e(TAG, "Virtual Engine startup blocked: unlicensed TeamDarkCore SDK. Please configure a valid SDK Key.");
            return false;
        }
        if (context != null) {
            get().ensureInitialized(context);
        }
        if (soPathOrName != null && !soPathOrName.trim().isEmpty()) {
            com.teamdark.core.core.RNative.stagePendingGameLibrary(context, soPathOrName.trim());
        }
        return get().launchApk(packageName, userId);
    }

    public boolean launchGame(Context context, String packageName) {
        return startGame(context, packageName, 0, null);
    }

    public boolean launchGame(Context context, String packageName, int userId) {
        return startGame(context, packageName, userId, null);
    }

    public static File getExternalObbDir(String packageName) {
        return BEnvironment.getExternalObbDir(packageName);
    }

    public static File getExternalDataDir(String packageName) {
        return BEnvironment.getExternalDataDir(packageName);
    }
}
