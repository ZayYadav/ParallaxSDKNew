package com.teamdark.core.utils.compat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageParser.Package;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.teamdark.black.android.content.pm.BRPackageParser;
import com.teamdark.black.android.content.pm.BRPackageParserLollipop;
import com.teamdark.black.android.content.pm.BRPackageParserLollipop22;
import com.teamdark.black.android.content.pm.BRPackageParserMarshmallow;
import com.teamdark.black.android.content.pm.BRPackageParserNougat;
import com.teamdark.black.android.content.pm.BRPackageParserPie;
import com.teamdark.core.TeamDarkCoreEngine;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.N;

public class PackageParserCompat {

    public static final int[] GIDS = new int[]{};
    private static final int API_LEVEL = Build.VERSION.SDK_INT;
    private static final int myUserId = 0;

    public static PackageParser createParser(File packageFile) {
        try {
            HiddenApiReflection.exemptAll();
        } catch (Throwable ignored) {
        }
        try {
            if (API_LEVEL >= M) {
                PackageParser p = BRPackageParserMarshmallow.get()._new();
                if (p != null) return p;
            } else if (API_LEVEL >= LOLLIPOP_MR1) {
                PackageParser p = BRPackageParserLollipop22.get()._new();
                if (p != null) return p;
            } else if (API_LEVEL >= LOLLIPOP) {
                PackageParser p = BRPackageParserLollipop.get()._new();
                if (p != null) return p;
            }
        } catch (Throwable ignored) {
        }
        try {
            return new PackageParser();
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static Package parsePackage(PackageParser parser, File packageFile, int flags) throws Throwable {
        Package pkg = null;
        try {
            if (parser != null) {
                if (API_LEVEL >= M) {
                    pkg = BRPackageParserMarshmallow.getWithException(parser).parsePackage(packageFile, flags);
                } else if (API_LEVEL >= LOLLIPOP_MR1) {
                    pkg = BRPackageParserLollipop22.getWithException(parser).parsePackage(packageFile, flags);
                } else if (API_LEVEL >= LOLLIPOP) {
                    pkg = BRPackageParserLollipop.getWithException(parser).parsePackage(packageFile, flags);
                } else {
                    pkg = BRPackageParser.getWithException(parser).parsePackage(packageFile, null, new DisplayMetrics(), flags);
                }
            }
        } catch (Throwable ignored) {
        }
        if (pkg == null && parser != null) {
            try {
                pkg = parser.parsePackage(packageFile, flags);
            } catch (Throwable ignored) {
            }
        }
        if (pkg == null) {
            pkg = parsePackageFallback(packageFile);
        } else {
            ensureMainLauncherIntent(pkg, packageFile);
        }
        return pkg;
    }

    public static void collectCertificates(PackageParser parser, Package p, int flags) throws Throwable {
        if (p == null) return;
        try {
            if (BuildCompat.isPie()) {
                BRPackageParserPie.getWithException().collectCertificates(p, true/*skipVerify*/);
            } else if (API_LEVEL >= N) {
                BRPackageParserNougat.getWithException().collectCertificates(p, flags);
            } else if (API_LEVEL >= M) {
                BRPackageParserMarshmallow.getWithException(parser).collectCertificates(p, flags);
            } else if (API_LEVEL >= LOLLIPOP_MR1) {
                BRPackageParserLollipop22.getWithException(parser).collectCertificates(p, flags);
            } else if (API_LEVEL >= LOLLIPOP) {
                BRPackageParserLollipop.getWithException(parser).collectCertificates(p, flags);
            } else {
                BRPackageParser.get(parser).collectCertificates(p, flags);
            }
        } catch (Throwable ignored) {
            // Never abort APK installation if hidden collectCertificates fails on Android 13-16
        }
    }

    private static Object allocateUnsafe(Class<?> clazz) throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field f = unsafeClass.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Object unsafe = f.get(null);
        Method alloc = unsafeClass.getMethod("allocateInstance", Class.class);
        return alloc.invoke(unsafe, clazz);
    }

    private static void setFieldSafe(Class<?> clazz, Object target, String fieldName, Object value) {
        Class<?> curr = clazz;
        while (curr != null && curr != Object.class) {
            try {
                Field f = curr.getDeclaredField(fieldName);
                f.setAccessible(true);
                f.set(target, value);
                return;
            } catch (Throwable ignored) {
                curr = curr.getSuperclass();
            }
        }
    }

    private static <T extends PackageParser.Component<?>> T buildComponent(
            Class<T> compClass, Package owner, ComponentInfo info) {
        try {
            @SuppressWarnings("unchecked")
            T comp = (T) allocateUnsafe(compClass);
            setFieldSafe(compClass, comp, "owner", owner);
            setFieldSafe(compClass, comp, "info", info);
            setFieldSafe(compClass, comp, "className", info.name);
            setFieldSafe(compClass, comp, "metaData", info.metaData);
            setFieldSafe(compClass, comp, "intents", new ArrayList<>());
            return comp;
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * 100% Public-API PackageParser.Package Builder Fallback.
     * Used if hidden PackageParser reflection ever fails on strict Android 14–16 ROMs.
     */
    public static Package parsePackageFallback(File packageFile) {
        if (packageFile == null || !packageFile.exists()) return null;
        try {
            Context ctx = TeamDarkCoreEngine.getContext();
            PackageManager pm = TeamDarkCoreEngine.getPackageManager();
            if (pm == null && ctx != null) {
                pm = ctx.getPackageManager();
            }
            if (pm == null) return null;

            String archivePath = packageFile.getAbsolutePath();
            int pmFlags = PackageManager.GET_ACTIVITIES
                    | PackageManager.GET_SERVICES
                    | PackageManager.GET_PROVIDERS
                    | PackageManager.GET_RECEIVERS
                    | PackageManager.GET_PERMISSIONS
                    | PackageManager.GET_META_DATA;

            PackageInfo pi = null;
            try {
                pi = pm.getPackageArchiveInfo(archivePath, pmFlags);
            } catch (Throwable ignored) {
            }
            if (pi == null) {
                try {
                    pi = pm.getPackageArchiveInfo(archivePath, 0);
                } catch (Throwable ignored) {
                }
            }
            if (pi == null || pi.packageName == null) {
                return null;
            }

            PackageInfo hostPi = null;
            try {
                hostPi = pm.getPackageInfo(pi.packageName, pmFlags);
            } catch (Throwable ignored) {
                try {
                    hostPi = TeamDarkCoreEngine.findHostPackageInfo(ctx, pi.packageName);
                } catch (Throwable ignored2) {
                }
            }

            ApplicationInfo appInfo = (hostPi != null && hostPi.applicationInfo != null)
                    ? hostPi.applicationInfo
                    : (pi.applicationInfo != null ? pi.applicationInfo : new ApplicationInfo());
            appInfo.packageName = pi.packageName;
            if (appInfo.sourceDir == null) appInfo.sourceDir = archivePath;
            if (appInfo.publicSourceDir == null) appInfo.publicSourceDir = archivePath;
            if (appInfo.processName == null) appInfo.processName = pi.packageName;

            Package pkg;
            try {
                Constructor<Package> ctor = Package.class.getDeclaredConstructor(String.class);
                ctor.setAccessible(true);
                pkg = ctor.newInstance(pi.packageName);
            } catch (Throwable ignored) {
                pkg = (Package) allocateUnsafe(Package.class);
                setFieldSafe(Package.class, pkg, "activities", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "receivers", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "providers", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "services", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "instrumentation", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "permissions", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "permissionGroups", new ArrayList<>());
                setFieldSafe(Package.class, pkg, "requestedPermissions", new ArrayList<>());
            }

            pkg.packageName = pi.packageName;
            pkg.applicationInfo = appInfo;
            pkg.baseCodePath = archivePath;
            pkg.codePath = archivePath;
            pkg.mVersionCode = pi.versionCode;
            pkg.mVersionName = pi.versionName;
            pkg.mSharedUserId = pi.sharedUserId;
            pkg.mSharedUserLabel = pi.sharedUserLabel;
            pkg.mAppMetaData = appInfo.metaData;
            if (pi.signatures != null) {
                pkg.mSignatures = pi.signatures;
            } else if (hostPi != null && hostPi.signatures != null) {
                pkg.mSignatures = hostPi.signatures;
            }

            String[] reqPerms = (pi.requestedPermissions != null)
                    ? pi.requestedPermissions
                    : (hostPi != null ? hostPi.requestedPermissions : null);
            if (reqPerms != null) {
                pkg.requestedPermissions.addAll(Arrays.asList(reqPerms));
            }

            ActivityInfo[] acts = (pi.activities != null && pi.activities.length > 0)
                    ? pi.activities
                    : (hostPi != null ? hostPi.activities : null);
            if (acts != null) {
                for (ActivityInfo ai : acts) {
                    if (ai == null) continue;
                    ai.applicationInfo = appInfo;
                    if (ai.packageName == null) ai.packageName = pi.packageName;
                    if (ai.processName == null) ai.processName = appInfo.processName;
                    PackageParser.Activity a = buildComponent(PackageParser.Activity.class, pkg, ai);
                    if (a != null) pkg.activities.add(a);
                }
            }

            ActivityInfo[] recs = (pi.receivers != null && pi.receivers.length > 0)
                    ? pi.receivers
                    : (hostPi != null ? hostPi.receivers : null);
            if (recs != null) {
                for (ActivityInfo ri : recs) {
                    if (ri == null) continue;
                    ri.applicationInfo = appInfo;
                    if (ri.packageName == null) ri.packageName = pi.packageName;
                    if (ri.processName == null) ri.processName = appInfo.processName;
                    PackageParser.Activity r = buildComponent(PackageParser.Activity.class, pkg, ri);
                    if (r != null) pkg.receivers.add(r);
                }
            }

            ServiceInfo[] srvs = (pi.services != null && pi.services.length > 0)
                    ? pi.services
                    : (hostPi != null ? hostPi.services : null);
            if (srvs != null) {
                for (ServiceInfo si : srvs) {
                    if (si == null) continue;
                    si.applicationInfo = appInfo;
                    if (si.packageName == null) si.packageName = pi.packageName;
                    if (si.processName == null) si.processName = appInfo.processName;
                    PackageParser.Service s = buildComponent(PackageParser.Service.class, pkg, si);
                    if (s != null) pkg.services.add(s);
                }
            }

            ProviderInfo[] provs = (pi.providers != null && pi.providers.length > 0)
                    ? pi.providers
                    : (hostPi != null ? hostPi.providers : null);
            if (provs != null) {
                for (ProviderInfo pri : provs) {
                    if (pri == null) continue;
                    pri.applicationInfo = appInfo;
                    if (pri.packageName == null) pri.packageName = pi.packageName;
                    if (pri.processName == null) pri.processName = appInfo.processName;
                    PackageParser.Provider p = buildComponent(PackageParser.Provider.class, pkg, pri);
                    if (p != null) pkg.providers.add(p);
                }
            }

            ensureMainLauncherIntent(pkg, packageFile);
            return pkg;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    /**
     * Ensures the parsed Package has at least one Activity with ACTION_MAIN + CATEGORY_LAUNCHER
     * so BPackageManager.getLaunchIntentForPackage() never returns null.
     */
    public static void ensureMainLauncherIntent(Package pkg, File packageFile) {
        if (pkg == null || pkg.packageName == null) return;
        try {
            boolean hasLauncher = false;
            if (pkg.activities != null) {
                for (PackageParser.Activity act : pkg.activities) {
                    if (act != null && act.intents != null) {
                        for (PackageParser.ActivityIntentInfo ii : act.intents) {
                            if (ii != null && ii.hasAction(Intent.ACTION_MAIN)
                                    && (ii.hasCategory(Intent.CATEGORY_LAUNCHER) || ii.hasCategory(Intent.CATEGORY_INFO))) {
                                hasLauncher = true;
                                break;
                            }
                        }
                    }
                    if (hasLauncher) break;
                }
            }
            if (hasLauncher) return;

            PackageManager pm = TeamDarkCoreEngine.getPackageManager();
            if (pm == null) return;

            ActivityInfo launcherAi = null;
            try {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                mainIntent.setPackage(pkg.packageName);
                List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
                if (list != null && !list.isEmpty() && list.get(0).activityInfo != null) {
                    launcherAi = list.get(0).activityInfo;
                }
            } catch (Throwable ignored) {
            }
            if (launcherAi == null) {
                try {
                    Intent launchIntent = pm.getLaunchIntentForPackage(pkg.packageName);
                    if (launchIntent != null && launchIntent.getComponent() != null) {
                        launcherAi = pm.getActivityInfo(launchIntent.getComponent(), 0);
                    }
                } catch (Throwable ignored) {
                }
            }
            if (launcherAi == null && pkg.activities != null && !pkg.activities.isEmpty()) {
                launcherAi = pkg.activities.get(0).info;
            }
            if (launcherAi == null) return;

            PackageParser.Activity targetAct = null;
            if (pkg.activities != null) {
                for (PackageParser.Activity act : pkg.activities) {
                    if (act != null && act.info != null && launcherAi.name != null && launcherAi.name.equals(act.info.name)) {
                        targetAct = act;
                        break;
                    }
                }
            }
            if (targetAct == null) {
                if (launcherAi.applicationInfo == null) {
                    launcherAi.applicationInfo = pkg.applicationInfo;
                }
                targetAct = buildComponent(PackageParser.Activity.class, pkg, launcherAi);
                if (targetAct != null) {
                    pkg.activities.add(0, targetAct);
                }
            }
            if (targetAct == null) return;
            if (targetAct.intents == null) {
                setFieldSafe(PackageParser.Activity.class, targetAct, "intents", new ArrayList<>());
            }
            PackageParser.ActivityIntentInfo mainFilter = new PackageParser.ActivityIntentInfo(targetAct);
            mainFilter.addAction(Intent.ACTION_MAIN);
            mainFilter.addCategory(Intent.CATEGORY_LAUNCHER);
            mainFilter.addCategory(Intent.CATEGORY_DEFAULT);
            targetAct.intents.add(mainFilter);
        } catch (Throwable ignored) {
        }
    }
}
