package com.featurestic.box.core.env;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.featurestic.black.android.app.BRActivityThread;
import com.featurestic.box.FeaturesticBoxCore;
import com.featurestic.box.app.BActivityThread;
import com.featurestic.box.utils.FileUtils;
import com.featurestic.box.utils.compat.BuildCompat;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Virtual Environment Paths for FeaturesticBox.
 * Uses /storage/emulated/0/SdCard/Android/obb/<pkg> and /storage/emulated/0/SdCard/Android/data/<pkg>
 * matching BLACK-OPNE / RIYAZ-VIP and libfbox.so C++ IO::redirectPath ("/SdCard").
 */
public class BEnvironment {
    public static boolean sHideRoot = true;
    public static boolean sHideXposed = true;
    public static boolean sEnableDaemonService = false;
    public static File JUNIT_JAR;
    public static File EMPTY_JAR;

    private static volatile File sVBoxRoot;
    private static volatile File sExternalVBoxRoot;

    private static Context resolveContext() {
        Context ctx = FeaturesticBoxCore.getContext();
        if (ctx != null) {
            return ctx;
        }
        try {
            Object mainThread = FeaturesticBoxCore.mainThread();
            if (mainThread != null) {
                Application app = BRActivityThread.get(mainThread).mInitialApplication();
                if (app != null) {
                    return app;
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static File resolveVBoxRoot() {
        if (sVBoxRoot != null) {
            return sVBoxRoot;
        }
        synchronized (BEnvironment.class) {
            if (sVBoxRoot != null) {
                return sVBoxRoot;
            }
            Context ctx = resolveContext();
            if (ctx != null) {
                File filesDir = ctx.getFilesDir();
                if (filesDir != null) {
                    sVBoxRoot = filesDir;
                    return sVBoxRoot;
                }
                File cacheDir = ctx.getCacheDir();
                if (cacheDir != null && cacheDir.getParentFile() != null) {
                    sVBoxRoot = new File(cacheDir.getParentFile(), "files");
                    return sVBoxRoot;
                }
            }
            String hostPkg = FeaturesticBoxCore.getHostPkg();
            if (hostPkg != null && !hostPkg.isEmpty()) {
                sVBoxRoot = new File("/data/user/0/" + hostPkg + "/files");
                return sVBoxRoot;
            }
            return new File("/data/local/tmp/fbox");
        }
    }

    private static File resolveHostStorageRoot() {
        try {
            File ext = Environment.getExternalStorageDirectory();
            if (ext != null && ext.exists()) {
                return ext;
            }
        } catch (Throwable ignored) {
        }
        return new File("/storage/emulated/0");
    }

    private static File resolveExternalSdCardRoot() {
        if (sExternalVBoxRoot != null) {
            return sExternalVBoxRoot;
        }
        File primarySdCard = new File(resolveHostStorageRoot(), "SdCard");
        try {
            FileUtils.mkdirs(primarySdCard);
            FileUtils.mkdirs(new File(primarySdCard, "Android/obb"));
            FileUtils.mkdirs(new File(primarySdCard, "Android/data"));
        } catch (Throwable ignored) {
        }
        sExternalVBoxRoot = primarySdCard;
        return primarySdCard;
    }

    public static void load() {
        File vboxRoot = getVBoxRoot();
        FileUtils.mkdirs(vboxRoot);
        FileUtils.mkdirs(getExternalStorageDirectory());
        JUNIT_JAR = new File(getCacheDir(), "junit.jar");
        EMPTY_JAR = new File(getCacheDir(), "empty.jar");
        FileUtils.mkdirs(getSystemDir());
        FileUtils.mkdirs(getCacheDir());
        FileUtils.mkdirs(getProcDir());
        FileUtils.mkdirs(getAppRootDir());
    }

    public static File getVBoxRoot() {
        return resolveVBoxRoot();
    }

    public static File getVirtualRoot() {
        return getVBoxRoot();
    }

    public static File getInternalDirectory() {
        return getVBoxRoot();
    }

    public static File getExternalStorageDirectory() {
        return resolveExternalSdCardRoot();
    }

    public static File getExternalVirtualRoot() {
        return getExternalStorageDirectory();
    }

    public static File getExternalDirectory() {
        return getExternalStorageDirectory();
    }

    public static File getExternalUserDir(int userId) {
        if (userId <= 0) {
            return getExternalStorageDirectory();
        }
        return new File(getExternalStorageDirectory(), String.format(Locale.US, "%d", userId));
    }

    public static File getHostExternalDataDir(String packageName) {
        return new File(resolveHostStorageRoot(), String.format(Locale.US, "Android/data/%s", packageName));
    }

    public static File getHostExternalObbDir(String packageName) {
        return new File(resolveHostStorageRoot(), String.format(Locale.US, "Android/obb/%s", packageName));
    }

    public static File ObbDirectory(int userId) {
        return getExternalUserDir(userId);
    }

    public static File ObbDirectory() {
        return getExternalStorageDirectory();
    }

    public static File getObbDir() {
        return new File(getExternalStorageDirectory(), "Android/obb");
    }

    public static File getExternalObbDir(String packageName) {
        return new File(getExternalStorageDirectory(), String.format(Locale.US, "Android/obb/%s", packageName));
    }

    public static File getExternalObbDir(String packageName, int userId) {
        return new File(getExternalUserDir(userId), String.format(Locale.US, "Android/obb/%s", packageName));
    }

    public static File getExternalUserAppObbDir(int userId, String packageName) {
        return getExternalObbDir(packageName, userId);
    }

    public static File getObbDir(String packageName) {
        return getExternalObbDir(packageName, 0);
    }

    public static File getObbDir(String packageName, int userId) {
        return getExternalObbDir(packageName, userId);
    }

    public static File getExternalDataDir(String packageName) {
        return new File(getExternalStorageDirectory(), String.format(Locale.US, "Android/data/%s", packageName));
    }

    public static File getExternalDataDir(String packageName, int userId) {
        return new File(getExternalUserDir(userId), String.format(Locale.US, "Android/data/%s", packageName));
    }

    public static File getExternalUserAppDataDir(int userId, String packageName) {
        return getExternalDataDir(packageName, userId);
    }

    public static File getExternalDataFilesDir(String packageName) {
        return new File(getExternalDataDir(packageName), "files");
    }

    public static File getExternalDataFilesDir(String packageName, int userId) {
        return new File(getExternalDataDir(packageName, userId), "files");
    }

    public static File getExternalDataCacheDir(String packageName) {
        return new File(getExternalDataDir(packageName), "cache");
    }

    public static File getExternalDataCacheDir(String packageName, int userId) {
        return new File(getExternalDataDir(packageName, userId), "cache");
    }

    public static ArrayList<String> getAllDex(String str) {
        File appDir = getAppDir(str);
        ArrayList<String> arrayList = new ArrayList<>();
        File[] listFiles = appDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name != null && name.endsWith(".apk");
            }
        });
        if (listFiles != null) {
            for (File file : listFiles) {
                if (BuildCompat.isUpsideDownCake()) {
                    try {
                        file.setReadOnly();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                arrayList.add(file.getAbsolutePath());
            }
        }
        return arrayList;
    }

    public static File getSystemDir() {
        return new File(getVBoxRoot(), "system");
    }

    public static File getProcDir() {
        return new File(getVBoxRoot(), "proc");
    }

    public static File getProcDir(int pid) {
        File file = new File(getProcDir(), String.format(Locale.US, "%d", pid));
        FileUtils.mkdirs(file);
        return file;
    }

    public static File getCacheDir() {
        return new File(getVBoxRoot(), "cache");
    }

    public static File getUserDir(int userId) {
        return new File(getVBoxRoot(), String.format(Locale.US, "data/user/%d", userId));
    }

    public static File getDeDataDir(String packageName) {
        return getDeDataDir(packageName, 0);
    }

    public static File getDeDataDir(String packageName, int userId) {
        return new File(getVBoxRoot(), String.format(Locale.US, "data/user_de/%d/%s", userId, packageName));
    }

    public static File getDataDir(String packageName) {
        return getDataDir(packageName, 0);
    }

    public static File getDataDir(String packageName, int userId) {
        return new File(getVBoxRoot(), String.format(Locale.US, "data/user/%d/%s", userId, packageName));
    }

    public static File getDataFilesDir(String packageName) {
        return getDataFilesDir(packageName, 0);
    }

    public static File getDataFilesDir(String packageName, int userId) {
        return new File(getDataDir(packageName, userId), "files");
    }

    public static File getDataCacheDir(String packageName) {
        return getDataCacheDir(packageName, 0);
    }

    public static File getDataCacheDir(String packageName, int userId) {
        return new File(getDataDir(packageName, userId), "cache");
    }

    public static File getDataLibDir(String packageName) {
        return getDataLibDir(packageName, 0);
    }

    public static File getDataLibDir(String packageName, int userId) {
        return new File(getDataDir(packageName, userId), "lib");
    }

    public static File getDataDatabasesDir(String packageName) {
        return getDataDatabasesDir(packageName, 0);
    }

    public static File getDataDatabasesDir(String packageName, int userId) {
        return new File(getDataDir(packageName, userId), "databases");
    }

    public static File getAppRootDir() {
        return getAppDir("");
    }

    public static File getAppDir(String packageName) {
        return new File(getVBoxRoot(), "data/app/" + packageName);
    }

    public static File getBaseApkDir(String packageName) {
        return new File(getVBoxRoot(), "data/app/" + packageName + "/base.apk");
    }

    public static File getAppLibDir(String packageName) {
        return new File(getAppDir(packageName), "lib");
    }

    public static File getUserInfoConf() {
        return new File(getSystemDir(), "user.conf");
    }

    public static File getAccountsConf() {
        return new File(getSystemDir(), "accounts.conf");
    }

    public static File getUidConf() {
        return new File(getSystemDir(), "uid.conf");
    }

    public static File getSharedUserConf() {
        return new File(getSystemDir(), "shared-user.conf");
    }

    public static File getXPModuleConf() {
        return new File(getSystemDir(), "xposed-module.conf");
    }

    public static File getFakeLocationConf() {
        return new File(getSystemDir(), "fake-location.conf");
    }

    public static File getFakeDeviceConf() {
        return new File(getSystemDir(), "fake-device.conf");
    }

    public static File getPackageConf(String packageName) {
        return new File(getAppDir(packageName), "package.conf");
    }

    public static File getXSharedPreferences(String packageName, String prefFileName) {
        return new File(getDataDir(packageName, BActivityThread.getUserId()), "shared_prefs/" + prefFileName + ".xml");
    }
}
