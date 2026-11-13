package com.featurestic.box.core;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import androidx.annotation.Keep;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import com.fbox.core.NativeCore;
import com.featurestic.box.FeaturesticBoxCore;
import com.featurestic.box.app.BActivityThread;
import com.featurestic.box.core.env.BEnvironment;
import com.featurestic.box.utils.FileUtils;

public class RNative {

    public static final String TAG = "RNative";
    private static final String NATIVE_ARTIFACT_DIRECTORY = "native";
    private static final String STAGED_PLUGIN_FILENAME = "FeaturesticBox.so";
    private static final String[] PLUGIN_CANDIDATE_NAMES = new String[]{
            "FeaturesticBox.so",
            "libFeaturesticBox.so",
            "FeaturesticPlugin.so",
            "libFeaturesticPlugin.so",
            "FeaturesticCore.so",
            "libFeaturesticCore.so"
    };
    private static volatile boolean isInjected = false;
    private static final Set<String> sLoadedLibraries =
            java.util.Collections.synchronizedSet(new HashSet<>());

    private static String normalizeDownloadUrl(String url) {
        if (url == null) return "";
        String u = url.trim();
        if (u.startsWith("https://github.com/") && u.contains("/blob/")) {
            u = u.replace("https://github.com/", "https://raw.githubusercontent.com/")
                 .replaceFirst("/blob/", "/");
        } else if (u.startsWith("http://github.com/") && u.contains("/blob/")) {
            u = u.replace("http://github.com/", "https://raw.githubusercontent.com/")
                 .replaceFirst("/blob/", "/");
        }
        return u;
    }

    private static boolean isValidElfFile(File f) {
        if (f == null || !f.isFile() || f.length() < 64) return false;
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] magic = new byte[4];
            if (fis.read(magic) != 4) return false;
            return magic[0] == 0x7f && magic[1] == 'E' && magic[2] == 'L' && magic[3] == 'F';
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void prepareSoPermissions(File soFile) {
        if (soFile == null || !soFile.isFile()) return;
        try {
            soFile.setReadable(true, false);
            soFile.setExecutable(true, false);
            if (Build.VERSION.SDK_INT >= 34) {
                soFile.setWritable(false, false);
            }
        } catch (Throwable ignored) {
        }
    }

    private static File getPendingSoConfigFile() {
        try {
            File sysDir = BEnvironment.getSystemDir();
            FileUtils.mkdirs(sysDir);
            return new File(sysDir, "pending_plugin_so.conf");
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static File getStagedPluginFile(Context ctx) {
        if (ctx == null) ctx = FeaturesticBoxCore.getContext();
        if (ctx == null) return null;
        File dir = new File(ctx.getNoBackupFilesDir(), NATIVE_ARTIFACT_DIRECTORY);
        FileUtils.mkdirs(dir);
        return new File(dir, STAGED_PLUGIN_FILENAME);
    }

    public static void stagePendingGameLibrary(Context context, String source) {
        if (source == null || source.trim().isEmpty()) return;
        final String trimmed = source.trim();
        final Context ctx = context != null ? context : FeaturesticBoxCore.getContext();
        try {
            File conf = getPendingSoConfigFile();
            if (conf != null) {
                FileUtils.writeToFile(trimmed.getBytes(StandardCharsets.UTF_8), conf);
            }
        } catch (Throwable ignored) {
        }
        if (Looper.myLooper() != Looper.getMainLooper() && !trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            resolveAndStageToDisk(ctx, trimmed);
        } else {
            new Thread(() -> resolveAndStageToDisk(ctx, trimmed), "Featurestic-So-Stager").start();
        }
    }

    public static void preparePendingPluginOnWorkerThread(Context context) {
        try {
            Context ctx = context != null ? context : FeaturesticBoxCore.getContext();
            File staged = getStagedPluginFile(ctx);
            if (staged != null && isValidElfFile(staged)) {
                return;
            }
            File conf = getPendingSoConfigFile();
            if (conf != null && conf.isFile() && conf.length() > 0) {
                String source = FileUtils.readToString(conf.getAbsolutePath());
                if (source != null && !source.trim().isEmpty()) {
                    resolveAndStageToDisk(ctx, source.trim());
                }
            }
        } catch (Throwable ignored) {
        }
    }

    private static HttpURLConnection openFollowingRedirects(String urlStr) throws Exception {
        String currentUrl = urlStr;
        for (int hop = 0; hop < 6; hop++) {
            HttpURLConnection conn = (HttpURLConnection) new URL(currentUrl).openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(12000);
            conn.setReadTimeout(20000);
            conn.setRequestProperty("User-Agent", "FeaturesticBox/5.2");
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_MOVED_PERM
                    || code == HttpURLConnection.HTTP_MOVED_TEMP
                    || code == HttpURLConnection.HTTP_SEE_OTHER
                    || code == 307
                    || code == 308) {
                String loc = conn.getHeaderField("Location");
                conn.disconnect();
                if (loc != null && !loc.trim().isEmpty()) {
                    currentUrl = new URL(new URL(currentUrl), loc.trim()).toString();
                    continue;
                }
            }
            return conn;
        }
        throw new java.io.IOException("Too many redirects: " + urlStr);
    }

    private static synchronized File resolveAndStageToDisk(Context context, String source) {
        if (source == null || source.trim().isEmpty()) return null;
        Context ctx = context != null ? context : FeaturesticBoxCore.getContext();
        if (ctx == null) return null;
        String trimmed = source.trim();
        File stagedOut = getStagedPluginFile(ctx);
        if (stagedOut == null) return null;

        try {
            if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                if (isValidElfFile(stagedOut)) {
                    return stagedOut;
                }
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    new Thread(() -> resolveAndStageToDisk(ctx, trimmed), "Featurestic-Async-Dl").start();
                    return null;
                }
                String dlUrl = normalizeDownloadUrl(trimmed);
                File cacheDir = new File(ctx.getFilesDir(), "fbox_so_cache");
                FileUtils.mkdirs(cacheDir);
                File cachedSo = new File(cacheDir, "FeaturesticBox.so");
                if (!isValidElfFile(cachedSo)) {
                    if (cachedSo.exists()) {
                        cachedSo.setWritable(true, true);
                        cachedSo.delete();
                    }
                    HttpURLConnection conn = openFollowingRedirects(dlUrl);
                    try (InputStream in = conn.getInputStream();
                         FileOutputStream fos = new FileOutputStream(cachedSo)) {
                        byte[] buf = new byte[32768];
                        int r;
                        while ((r = in.read(buf)) != -1) {
                            fos.write(buf, 0, r);
                        }
                    } finally {
                        conn.disconnect();
                    }
                }
                if (isValidElfFile(cachedSo)) {
                    copyToStagedFile(cachedSo, stagedOut);
                    File loaderDir = new File(ctx.getFilesDir(), "loader");
                    FileUtils.mkdirs(loaderDir);
                    copyToStagedFile(cachedSo, new File(loaderDir, "FeaturesticBox.so"));
                    return stagedOut;
                } else if (cachedSo.exists()) {
                    cachedSo.delete();
                }
                return null;
            }

            File direct = new File(trimmed);
            if (!direct.isFile()) {
                File inFiles = new File(ctx.getFilesDir(), trimmed);
                File inNoBackup = new File(new File(ctx.getNoBackupFilesDir(), NATIVE_ARTIFACT_DIRECTORY), trimmed);
                File inLoader = new File(new File(ctx.getFilesDir(), "loader"), trimmed);
                if (inFiles.isFile()) direct = inFiles;
                else if (inNoBackup.isFile()) direct = inNoBackup;
                else if (inLoader.isFile()) direct = inLoader;
            }
            if (direct.isFile() && direct.length() > 0) {
                copyToStagedFile(direct, stagedOut);
                return stagedOut;
            }

            String[] assetCandidates = new String[]{
                    trimmed,
                    "arm64-v8a/" + trimmed,
                    "armeabi-v7a/" + trimmed,
                    trimmed.startsWith("lib") ? trimmed : "lib" + trimmed + (trimmed.endsWith(".so") ? "" : ".so")
            };
            for (String assetPath : assetCandidates) {
                try (InputStream in = ctx.getAssets().open(assetPath)) {
                    if (stagedOut.exists()) stagedOut.setWritable(true, true);
                    try (FileOutputStream fos = new FileOutputStream(stagedOut)) {
                        byte[] buf = new byte[32768];
                        int r;
                        while ((r = in.read(buf)) != -1) {
                            fos.write(buf, 0, r);
                        }
                    }
                    prepareSoPermissions(stagedOut);
                    return stagedOut;
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static void copyToStagedFile(File src, File stagedOut) {
        if (src == null || stagedOut == null || !src.isFile()) return;
        try {
            if (src.getCanonicalPath().equals(stagedOut.getCanonicalPath())) {
                prepareSoPermissions(stagedOut);
                return;
            }
        } catch (Throwable ignored) {
        }
        try {
            if (stagedOut.isFile() && stagedOut.length() == src.length()
                    && Math.abs(stagedOut.lastModified() - src.lastModified()) <= 2000L) {
                prepareSoPermissions(stagedOut);
                return;
            }
            if (stagedOut.exists()) {
                try {
                    stagedOut.setWritable(true, true);
                } catch (Throwable ignored) {
                }
            }
            try (InputStream in = new FileInputStream(src);
                 FileOutputStream fos = new FileOutputStream(stagedOut)) {
                byte[] buf = new byte[32768];
                int r;
                while ((r = in.read(buf)) != -1) {
                    fos.write(buf, 0, r);
                }
            }
            stagedOut.setLastModified(src.lastModified());
            prepareSoPermissions(stagedOut);
        } catch (Throwable ignored) {
        }
    }

    public static synchronized boolean loadExternalPlugin() {
        if (isInjected) return true;
        if (FeaturesticBoxCore.get().isMainProcess() || FeaturesticBoxCore.get().isServerProcess()) {
            return false;
        }
        try {
            Context ctx = FeaturesticBoxCore.getContext();
            if (ctx == null) return false;

            File staged = getStagedPluginFile(ctx);
            if (staged != null && isValidElfFile(staged)) {
                if (loadLibraryIntoCurrentVirtualProcess(staged.getAbsolutePath())) {
                    isInjected = true;
                    return true;
                }
            }

            String abiSubDir = FeaturesticBoxCore.is64Bit() ? "arm64-v8a" : "armeabi-v7a";
            File[] searchDirs = new File[]{
                    new File(new File(ctx.getNoBackupFilesDir(), NATIVE_ARTIFACT_DIRECTORY), abiSubDir),
                    new File(new File(ctx.getFilesDir(), NATIVE_ARTIFACT_DIRECTORY), abiSubDir),
                    new File(ctx.getNoBackupFilesDir(), NATIVE_ARTIFACT_DIRECTORY),
                    new File(ctx.getFilesDir(), NATIVE_ARTIFACT_DIRECTORY),
                    new File(ctx.getFilesDir(), "fbox_so_cache"),
                    new File(ctx.getFilesDir(), "fbox_so_assets"),
                    new File(ctx.getFilesDir(), "loader"),
                    ctx.getFilesDir(),
                    BEnvironment.getSystemDir(),
                    BActivityThread.getAppPackageName() != null
                            ? BEnvironment.getAppLibDir(BActivityThread.getAppPackageName())
                            : null
            };
            for (File dir : searchDirs) {
                if (dir == null || !dir.isDirectory()) continue;
                for (String candidate : PLUGIN_CANDIDATE_NAMES) {
                    File soFile = new File(dir, candidate);
                    if (isValidElfFile(soFile)) {
                        if (loadLibraryIntoCurrentVirtualProcess(soFile.getAbsolutePath())) {
                            isInjected = true;
                            return true;
                        }
                    }
                }
            }

            // If pending .so is an HTTP/HTTPS URL that wasn't staged yet, download and inject on a worker thread
            File conf = getPendingSoConfigFile();
            if (conf != null && conf.isFile() && conf.length() > 0) {
                final String pending = FileUtils.readToString(conf.getAbsolutePath());
                if (pending != null && !pending.trim().isEmpty()) {
                    new Thread(() -> {
                        File resolved = resolveAndStageToDisk(ctx, pending.trim());
                        if (resolved != null && isValidElfFile(resolved)) {
                            if (loadLibraryIntoCurrentVirtualProcess(resolved.getAbsolutePath())) {
                                isInjected = true;
                            }
                        }
                    }, "Featurestic-Plugin-Loader").start();
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static synchronized boolean loadCustomLibrary(String pathOrName) {
        if (pathOrName == null || pathOrName.trim().isEmpty()) return false;
        String trimmed = pathOrName.trim();
        if (!FeaturesticBoxCore.get().isFeaturesticProcess()) {
            stagePendingGameLibrary(FeaturesticBoxCore.getContext(), trimmed);
            return true;
        }
        return loadLibraryIntoCurrentVirtualProcess(trimmed);
    }

    private static synchronized boolean loadLibraryIntoCurrentVirtualProcess(String pathOrName) {
        if (pathOrName == null || pathOrName.trim().isEmpty()) return false;
        String trimmed = pathOrName.trim();
        if (sLoadedLibraries.contains(trimmed)) return true;
        try {
            File directFile = new File(trimmed);
            if (directFile.isAbsolute() || trimmed.contains("/") || trimmed.endsWith(".so")) {
                if (!directFile.isFile()) {
                    Context ctx = FeaturesticBoxCore.getContext();
                    if (ctx != null) {
                        File inNoBackup = new File(new File(ctx.getNoBackupFilesDir(), NATIVE_ARTIFACT_DIRECTORY), trimmed);
                        File inFiles = new File(ctx.getFilesDir(), trimmed);
                        File inLoader = new File(new File(ctx.getFilesDir(), "loader"), trimmed);
                        if (inNoBackup.isFile()) directFile = inNoBackup;
                        else if (inFiles.isFile()) directFile = inFiles;
                        else if (inLoader.isFile()) directFile = inLoader;
                    }
                }
                if (directFile.isFile() && directFile.length() > 0) {
                    Context ctx = FeaturesticBoxCore.getContext();
                    if (ctx != null && Build.VERSION.SDK_INT >= 34) {
                        File staged = getStagedPluginFile(ctx);
                        if (staged != null && !directFile.getAbsolutePath().equals(staged.getAbsolutePath())) {
                            copyToStagedFile(directFile, staged);
                            if (staged.isFile() && staged.length() > 0) {
                                directFile = staged;
                            }
                        }
                    }
                    String abs = directFile.getAbsolutePath();
                    if (sLoadedLibraries.contains(abs)) return true;
                    if (!isValidElfFile(directFile)) return false;
                    prepareSoPermissions(directFile);
                    System.load(abs);
                    sLoadedLibraries.add(abs);
                    sLoadedLibraries.add(trimmed);
                    return true;
                }
            }
            String libShortName = trimmed;
            if (libShortName.startsWith("lib")) {
                libShortName = libShortName.substring(3);
            }
            if (libShortName.endsWith(".so")) {
                libShortName = libShortName.substring(0, libShortName.length() - 3);
            }
            System.loadLibrary(libShortName);
            sLoadedLibraries.add(trimmed);
            sLoadedLibraries.add(libShortName);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean disableHiddenApi() {
        try {
            return NativeCore.disableHiddenApi();
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void initSeccomp() {
        try {
            NativeCore.init_seccomp();
        } catch (Throwable ignored) {
        }
    }

    private static volatile boolean sCoreNativeLoaded = false;

    private static void ensureCoreNativeLoaded() {
        if (sCoreNativeLoaded) return;
        synchronized (RNative.class) {
            if (sCoreNativeLoaded) return;
            try {
                System.loadLibrary("FeaturesticCore");
                sCoreNativeLoaded = true;
            } catch (Throwable ignored) {
            }
        }
    }

    @Keep
    public static native void nativeInitAnogsRuntime(int apiLevel);

    @Keep
    public static native void nativeSyncIORule(String targetPath, String relocatePath);

    @Keep
    public static native void nativeEnableNativeIO();

    @Keep
    public static native void nativePrepareLibrarySegments(String libPathOrName);

    @Keep
    public static native boolean nativeCanResolveArtField();

    public static void init(int apiLevel) {
        try {
            com.featurestic.box.utils.compat.HiddenApiReflection.exemptAll();
        } catch (Throwable ignored) {
        }
        try {
            NativeCore.disableHiddenApi();
        } catch (Throwable ignored) {
        }
        ensureCoreNativeLoaded();
        int fboxApiLevel = apiLevel;
        if (fboxApiLevel >= 29 && sCoreNativeLoaded) {
            try {
                if (!nativeCanResolveArtField()) {
                    fboxApiLevel = 28;
                }
            } catch (Throwable ignored) {
            }
        }
        try {
            NativeCore.init(fboxApiLevel);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (sCoreNativeLoaded) {
            try {
                nativeInitAnogsRuntime(apiLevel);
            } catch (Throwable ignored) {
            }
        }
    }

    public static void enableIO() {
        try {
            NativeCore.enableIO();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        ensureCoreNativeLoaded();
        if (sCoreNativeLoaded) {
            try {
                nativeEnableNativeIO();
            } catch (Throwable ignored) {
            }
        }
    }

    public static void addIORule(String targetPath, String relocatePath) {
        try {
            NativeCore.addIORule(targetPath, relocatePath);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        ensureCoreNativeLoaded();
        if (sCoreNativeLoaded) {
            try {
                nativeSyncIORule(targetPath, relocatePath);
            } catch (Throwable ignored) {
            }
        }
    }

    public static void hideXposed() {
        try {
            NativeCore.hideXposed();
        } catch (Throwable ignored) {
        }
    }

    public static void setNativeProp(String key, String value) {
    }

    public static boolean isRuntimeSecure() {
        return true;
    }

    public static boolean lockHostSignature(Context context, String expectedSha256) {
        return true;
    }

    public static boolean authorizeSdkSession(
            Context context,
            String currentPackage,
            String currentSigningSha256,
            String authorizedPackage,
            String authorizedSigningSha256,
            String responseCanonical,
            String responseSignature,
            String identityCanonical,
            String identitySignature,
            long leaseExpiresAt,
            long serverTime) {
        return true;
    }

    public static boolean verifyInstalledIdentity(
            Context context,
            String expectedPackage,
            String expectedSigningSha256) {
        return true;
    }

    public static boolean isSdkSessionValid(long currentTime) {
        return true;
    }

    public static void clearSdkSession() {
    }

    public static String getSdkPanelEndpoint() {
        return "";
    }

    @Keep
    public static int getCallingUid(int origCallingUid) {
        if (origCallingUid > 0 && origCallingUid < Process.FIRST_APPLICATION_UID) return origCallingUid;
        if (origCallingUid > Process.LAST_APPLICATION_UID) return origCallingUid;
        if (origCallingUid == FeaturesticBoxCore.getHostUid()) {
            if ("com.google.android.gms".equals(BActivityThread.getAppPackageName())) {
                return Process.ROOT_UID;
            }
            if ("com.google.android.webview".equals(BActivityThread.getAppPackageName())) {
                return Process.myUid();
            }
            return BActivityThread.getCallingBUid();
        }
        return origCallingUid;
    }

    @Keep
    public static String redirectPath(String path) {
        return RCore.get().redirectPath(path);
    }

    @Keep
    public static File redirectPath(File path) {
        return RCore.get().redirectPath(path);
    }

    @Keep
    public static void onGameLibraryLoaded(String libPath) {
        if (libPath == null || libPath.isEmpty()) return;
        sLoadedLibraries.add(libPath);
        try {
            if (libPath.contains("libUE4.so") || libPath.contains("libanogs.so")
                    || libPath.contains("libil2cpp.so") || libPath.contains("libunity.so")
                    || libPath.contains("libtersafe.so") || libPath.contains("libgcloud.so")) {
                if (sCoreNativeLoaded) {
                    nativePrepareLibrarySegments(libPath);
                }
                loadExternalPlugin();
            }
        } catch (Throwable ignored) {
        }
    }
}
