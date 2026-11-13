package com.teamdark.core.core.env;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.utils.FileUtils;
import com.teamdark.core.utils.ShellUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Multi-Layer OBB Discovery & Copy Engine for TeamDarkCore (Android 10–17).
 * Copies host OBBs from /storage/emulated/0/Android/obb/<pkg> into
 * /storage/emulated/0/SdCard/Android/obb/<pkg> (alongside teamdark.lic).
 */
public final class TeamDarkStorageManager {

    private static final long MIN_VALID_OBB_BYTES = 1024L * 1024L; // 1 MB minimum for valid .obb

    public interface CopyProgressListener {
        void onProgress(int percent, long copiedBytes, long totalBytes, String fileName);
    }

    private static final String[] HOST_STORAGE_ROOTS = new String[]{
            "/storage/emulated/0",
            "/sdcard",
            "/mnt/pass_through/0/emulated/0",
            "/storage/self/primary",
            "/mnt/sdcard",
            "/mnt/user/0/emulated/0",
            "/mnt/installer/0/emulated/0",
            "/mnt/runtime/full/emulated/0",
            "/mnt/runtime/write/emulated/0",
            "/mnt/runtime/read/emulated/0",
            "/data/media/0"
    };

    private TeamDarkStorageManager() {}

    public static File obtainAppExternalStorageDir() {
        File containerPath = BEnvironment.getExternalStorageDirectory();
        FileUtils.mkdirs(containerPath);
        return containerPath;
    }

    public static File getObbContainerPath(String packageName) {
        File dir = BEnvironment.getExternalObbDir(packageName);
        FileUtils.mkdirs(dir);
        return dir;
    }

    public static File getDataContainerPath(String packageName) {
        File dir = BEnvironment.getExternalDataDir(packageName);
        FileUtils.mkdirs(dir);
        FileUtils.mkdirs(new File(dir, "files"));
        FileUtils.mkdirs(new File(dir, "cache"));
        return dir;
    }

    /**
     * Checks if valid (>= 1 MB) .obb files are present inside /storage/emulated/0/SdCard/Android/obb/<pkg>.
     * Ignores small files like teamdark.lic and cleans up 0-byte/truncated .obb files.
     */
    public static boolean hasVirtualObbFiles(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) return false;
        String pkg = packageName.trim();
        try {
            File vObbDir = BEnvironment.getExternalObbDir(pkg);
            // Migrate any .obb files from legacy /SdCard/0/Android/obb/<pkg> into /SdCard/Android/obb/<pkg>
            List<File> strayDirs = new ArrayList<>();
            strayDirs.add(new File(BEnvironment.getExternalStorageDirectory(), "0/Android/obb/" + pkg));
            try {
                Context ctx = TeamDarkCoreEngine.getContext();
                if (ctx != null && ctx.getExternalFilesDir(null) != null) {
                    strayDirs.add(new File(ctx.getExternalFilesDir(null), "SdCard/0/Android/obb/" + pkg));
                    strayDirs.add(new File(ctx.getExternalFilesDir(null), "SdCard/Android/obb/" + pkg));
                }
            } catch (Throwable ignored) {
            }
            for (File legacyDir : strayDirs) {
                if (legacyDir != null && legacyDir.exists() && !legacyDir.getAbsolutePath().equals(vObbDir.getAbsolutePath())) {
                    File[] legFiles = legacyDir.listFiles();
                    if (legFiles != null) {
                        for (File lf : legFiles) {
                            if (lf != null && lf.isFile() && lf.getName().endsWith(".obb") && lf.length() >= MIN_VALID_OBB_BYTES) {
                                FileUtils.mkdirs(vObbDir);
                                File dst = new File(vObbDir, lf.getName());
                                if (!dst.exists() || dst.length() != lf.length()) {
                                    if (!lf.renameTo(dst)) {
                                        FileUtils.copyFile(lf, dst);
                                        lf.delete();
                                    }
                                } else {
                                    lf.delete();
                                }
                            }
                        }
                    }
                }
            }

            File[] files = vObbDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f != null && f.isFile() && f.getName().endsWith(".obb")) {
                        if (f.length() >= MIN_VALID_OBB_BYTES) {
                            return true;
                        } else if (f.length() == 0L) {
                            // Remove 0-byte failed copy remnants
                            f.delete();
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    /**
     * Discovers all readable host .obb files for packageName across Android 10–17:
     *   Layer 1: Standard listFiles() across all mount roots + fallback folders (/Download, /Android/obb, /OBB)
     *   Layer 2: Direct filename probing ("main.<versionCode>.<pkg>.obb" & "patch.<versionCode>.<pkg>.obb")
     *            using host PackageInfo.versionCode AND full BGMI/PUBG version sweep (bypasses Android 13-17 opendir block!)
     *   Layer 3: Shell `ls` discovery (unprivileged `sh` and rooted `su`)
     */
    public static List<File> discoverHostObbFiles(Context context, String packageName) {
        Map<String, File> foundByName = new LinkedHashMap<>();
        if (packageName == null || packageName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String pkg = packageName.trim();
        File virtualObbDir = BEnvironment.getExternalObbDir(pkg);
        String virtualCanon;
        try {
            virtualCanon = virtualObbDir.getCanonicalPath();
        } catch (Throwable ignored) {
            virtualCanon = virtualObbDir.getAbsolutePath();
        }

        // Layer 1: Standard directory scan across all storage roots
        for (String root : HOST_STORAGE_ROOTS) {
            try {
                File dir = new File(root + "/Android/obb", pkg);
                try {
                    if (dir.getCanonicalPath().equals(virtualCanon)) continue;
                } catch (Throwable ignored) {}
                File[] list = dir.listFiles();
                if (list != null) {
                    for (File f : list) {
                        if (f != null && f.isFile() && f.getName().endsWith(".obb") && f.length() >= MIN_VALID_OBB_BYTES) {
                            if (!foundByName.containsKey(f.getName())) {
                                foundByName.put(f.getName(), f);
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        // Also check common fallback folders where users or downloaders place .obb files
        String[] extraSearchDirs = new String[]{
                "/storage/emulated/0/Android/obb",
                "/storage/emulated/0/Download",
                "/storage/emulated/0/OBB",
                "/storage/emulated/0"
        };
        for (String extra : extraSearchDirs) {
            try {
                File dir = new File(extra);
                File[] list = dir.listFiles();
                if (list != null) {
                    for (File f : list) {
                        if (f != null && f.isFile()
                                && f.getName().endsWith("." + pkg + ".obb")
                                && f.length() >= MIN_VALID_OBB_BYTES) {
                            if (!foundByName.containsKey(f.getName())) {
                                foundByName.put(f.getName(), f);
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        boolean isWorkerThread = (Looper.myLooper() != Looper.getMainLooper());

        // Layer 2: Direct filename probing using host PackageInfo.versionCode (Bypasses Android 13–17 listFiles() == null!)
        if (foundByName.isEmpty()) {
            int hostVersionCode = resolveHostVersionCode(context, pkg);
            if (hostVersionCode > 0) {
                int minDelta = isWorkerThread ? -1500 : -60;
                for (String root : HOST_STORAGE_ROOTS) {
                    File dir = new File(root + "/Android/obb", pkg);
                    for (int delta = 25; delta >= minDelta; delta--) {
                        int vc = hostVersionCode + delta;
                        if (vc <= 0) continue;
                        probeObbCandidate(dir, "main." + vc + "." + pkg + ".obb", foundByName);
                        probeObbCandidate(dir, "patch." + vc + "." + pkg + ".obb", foundByName);
                    }
                    // Also check vc / 10 and vc / 100 in case versionCode has architecture suffix digits
                    for (int div : new int[]{10, 100}) {
                        int baseVc = hostVersionCode / div;
                        if (baseVc > 1000) {
                            for (int d = 20; d >= -200; d--) {
                                int vc = baseVc + d;
                                probeObbCandidate(dir, "main." + vc + "." + pkg + ".obb", foundByName);
                            }
                        }
                    }
                    if (!foundByName.isEmpty()) {
                        break;
                    }
                }
            }
            // Fast sweep of standard 5-digit BGMI/PUBG OBB version numbers (multiples of 5 from 14000 to 25000)
            if (foundByName.isEmpty() && isWorkerThread) {
                File primaryDir = new File("/storage/emulated/0/Android/obb", pkg);
                for (int vc = 24000; vc >= 14000; vc -= 5) {
                    probeObbCandidate(primaryDir, "main." + vc + "." + pkg + ".obb", foundByName);
                    if (!foundByName.isEmpty()) break;
                }
            }
        }

        // Layer 3: Shell `ls` discovery on worker thread (tries unprivileged sh first, then su)
        if (foundByName.isEmpty() && isWorkerThread) {
            for (boolean useRoot : new boolean[]{false, true}) {
                try {
                    File hostObbDir = new File("/storage/emulated/0/Android/obb", pkg);
                    ShellUtils.CommandResult res = ShellUtils.execCommand("ls -1 " + hostObbDir.getAbsolutePath(), useRoot);
                    if (res != null && res.successMsg != null && !res.successMsg.isEmpty()) {
                        String[] lines = res.successMsg.split("\\r?\\n");
                        for (String line : lines) {
                            String name = line != null ? line.trim() : "";
                            if (name.endsWith(".obb")) {
                                File candidate = new File(hostObbDir, name);
                                foundByName.put(name, candidate);
                            }
                        }
                    }
                    if (!foundByName.isEmpty()) break;
                } catch (Throwable ignored) {
                }
            }
        }

        return new ArrayList<>(foundByName.values());
    }

    private static void probeObbCandidate(File dir, String fileName, Map<String, File> foundByName) {
        if (foundByName.containsKey(fileName)) return;
        File f = new File(dir, fileName);
        if (f.isFile() && f.length() >= MIN_VALID_OBB_BYTES) {
            foundByName.put(fileName, f);
        }
    }

    private static int resolveHostVersionCode(Context context, String packageName) {
        Context ctx = context != null ? context : TeamDarkCoreEngine.getContext();
        if (ctx != null) {
            try {
                PackageManager pm = ctx.getPackageManager();
                if (pm != null) {
                    PackageInfo pi = pm.getPackageInfo(packageName, 0);
                    if (pi != null && pi.versionCode > 0) {
                        return pi.versionCode;
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        try {
            PackageInfo pi = TeamDarkCoreEngine.findHostPackageInfo(ctx, packageName);
            if (pi != null) {
                if (pi.versionCode > 0) {
                    return pi.versionCode;
                }
                if (pi.applicationInfo != null && pi.applicationInfo.sourceDir != null && ctx != null) {
                    PackageInfo archivePi = ctx.getPackageManager().getPackageArchiveInfo(pi.applicationInfo.sourceDir, 0);
                    if (archivePi != null && archivePi.versionCode > 0) {
                        return archivePi.versionCode;
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return -1;
    }

    /**
     * Copies host OBB files into /storage/emulated/0/SdCard/Android/obb/<pkg>
     * with real-time progress reporting, atomic temp-file rename, and shell/root fallback.
     */
    public static boolean copyOrSyncObbWithProgress(Context context, String packageName, CopyProgressListener listener) {
        if (packageName == null || packageName.trim().isEmpty()) return false;
        String pkg = packageName.trim();
        try {
            File destDir = getObbContainerPath(pkg);
            if (!destDir.exists()) {
                FileUtils.mkdirs(destDir);
            }

            // Migrate any existing /SdCard/0/Android/obb/<pkg> files first
            hasVirtualObbFiles(pkg);

            List<File> sourceFiles = discoverHostObbFiles(context, pkg);
            if (sourceFiles.isEmpty()) {
                // Try direct shell/root copy from /storage/emulated/0/Android/obb/<pkg>/*.obb
                if (tryShellOrRootCopyObb(pkg, destDir, listener)) {
                    return true;
                }
                if (hasVirtualObbFiles(pkg)) {
                    if (listener != null) {
                        listener.onProgress(100, 1L, 1L, "OBB ready in /SdCard/Android/obb/" + pkg);
                    }
                    return true;
                }
                return false;
            }

            long totalBytes = 0L;
            for (File f : sourceFiles) {
                long len = f.length();
                totalBytes += (len > 0L ? len : (700L * 1024L * 1024L));
            }
            if (totalBytes <= 0L) totalBytes = 1L;

            long copiedBytes = 0L;
            byte[] buffer = new byte[524288]; // 512 KB high-speed I/O buffer

            for (File srcFile : sourceFiles) {
                File dstFile = new File(destDir, srcFile.getName());
                long srcLen = srcFile.length();

                // Fast check: if destination file already exists with exact same byte size (>= 1MB), skip re-copying!
                if (dstFile.isFile() && srcLen >= MIN_VALID_OBB_BYTES && dstFile.length() == srcLen) {
                    copiedBytes += srcLen;
                    int pct = (int) Math.min(100L, (copiedBytes * 100L) / totalBytes);
                    if (listener != null) {
                        listener.onProgress(pct, copiedBytes, totalBytes, srcFile.getName());
                    }
                    continue;
                }

                File tmpFile = new File(destDir, srcFile.getName() + ".copying");
                if (tmpFile.exists()) tmpFile.delete();

                boolean fileCopied = false;
                try (FileInputStream fis = new FileInputStream(srcFile);
                     FileOutputStream fos = new FileOutputStream(tmpFile)) {
                    int read;
                    long lastReportTime = 0L;
                    while ((read = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                        copiedBytes += read;
                        long now = System.currentTimeMillis();
                        if (listener != null && (now - lastReportTime >= 60L || copiedBytes >= totalBytes)) {
                            lastReportTime = now;
                            int pct = (int) Math.min(100L, (copiedBytes * 100L) / totalBytes);
                            listener.onProgress(pct, copiedBytes, totalBytes, srcFile.getName());
                        }
                    }
                    fos.flush();
                    fos.getFD().sync();
                    if (tmpFile.length() >= MIN_VALID_OBB_BYTES) {
                        if (dstFile.exists()) dstFile.delete();
                        if (!tmpFile.renameTo(dstFile)) {
                            FileUtils.copyFile(tmpFile, dstFile);
                            tmpFile.delete();
                        }
                        fileCopied = dstFile.isFile() && dstFile.length() >= MIN_VALID_OBB_BYTES;
                    } else {
                        tmpFile.delete();
                    }
                } catch (Throwable copyErr) {
                    if (tmpFile.exists()) tmpFile.delete();
                }

                if (!fileCopied) {
                    // Fallback to shell/root cp if Java FileInputStream was blocked
                    if (tryShellOrRootCopyObb(pkg, destDir, listener)) {
                        return true;
                    }
                }
            }
            return hasVirtualObbFiles(pkg);
        } catch (Throwable ignored) {
            return hasVirtualObbFiles(pkg);
        }
    }

    private static boolean tryShellOrRootCopyObb(String pkg, File destDir, CopyProgressListener listener) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return false;
        }
        String dstPath = destDir.getAbsolutePath();
        String[] srcDirs = new String[]{
                "/storage/emulated/0/Android/obb/" + pkg,
                "/data/media/0/Android/obb/" + pkg,
                "/mnt/pass_through/0/emulated/0/Android/obb/" + pkg,
                "/sdcard/Android/obb/" + pkg
        };
        for (boolean useRoot : new boolean[]{false, true}) {
            for (String srcDir : srcDirs) {
                try {
                    String cmd = "mkdir -p \"" + dstPath + "\" && cp -f \"" + srcDir + "/\"*.obb \"" + dstPath + "/\" && chmod 666 \"" + dstPath + "/\"*.obb";
                    ShellUtils.execCommand(cmd, useRoot);
                    if (hasVirtualObbFiles(pkg)) {
                        if (listener != null) {
                            listener.onProgress(100, 1L, 1L, "OBB copied to /SdCard/Android/obb/" + pkg);
                        }
                        return true;
                    }
                } catch (Throwable ignored) {
                }
            }
        }
        return false;
    }

    public static boolean syncObbToVirtualIfNeeded(String packageName) {
        if (hasVirtualObbFiles(packageName)) {
            return true;
        }
        return copyOrSyncObbWithProgress(TeamDarkCoreEngine.getContext(), packageName, null);
    }

    public static boolean syncDataToVirtualIfNeeded(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) return false;
        try {
            getDataContainerPath(packageName);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean syncAllToVirtual(String packageName) {
        boolean obb = syncObbToVirtualIfNeeded(packageName);
        boolean data = syncDataToVirtualIfNeeded(packageName);
        return obb || data;
    }

    public static boolean cleanVirtualAppCache(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) return false;
        try {
            File internalData = BEnvironment.getDataDir(packageName, 0);
            FileUtils.deleteDir(new File(internalData, "cache"));
            FileUtils.deleteDir(new File(internalData, "code_cache"));
            File externalCache = BEnvironment.getExternalDataCacheDir(packageName);
            FileUtils.deleteDir(externalCache);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
