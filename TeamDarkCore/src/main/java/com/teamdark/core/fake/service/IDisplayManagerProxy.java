package com.teamdark.core.fake.service;

import android.os.Build;
import android.os.IInterface;
import android.util.SparseArray;
import android.view.Display;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.teamdark.black.android.hardware.display.BRDisplayManagerGlobal;
import com.teamdark.core.fake.hook.ClassInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.MethodParameterUtils;

public class IDisplayManagerProxy extends ClassInvocationStub {

    public IDisplayManagerProxy() {
    }

    @Override
    protected Object getWho() {
        return BRDisplayManagerGlobal.get(BRDisplayManagerGlobal.get().getInstance()).mDm();
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        Object dmg = BRDisplayManagerGlobal.get().getInstance();
        BRDisplayManagerGlobal.get(dmg)._set_mDm(getProxyInvocation());
        flushDisplayInfoCache();
    }

    @Override
    public boolean isBadEnv() {
        Object dmg = BRDisplayManagerGlobal.get().getInstance();
        IInterface mDm = BRDisplayManagerGlobal.get(dmg).mDm();
        return mDm != getProxyInvocation();
    }

    /**
     * Clears DisplayManagerGlobal's cached DisplayInfo so Display.getRefreshRate() and
     * Display.getMode() immediately fetch the 120Hz DisplayInfo from IDisplayManagerProxy.
     */
    public static void flushDisplayInfoCache() {
        try {
            Object dmg = BRDisplayManagerGlobal.get().getInstance();
            if (dmg == null) return;
            Field cacheField = findFieldRecursive(dmg.getClass(), "mDisplayInfoCache");
            if (cacheField != null) {
                cacheField.setAccessible(true);
                Object cache = cacheField.get(dmg);
                if (cache instanceof SparseArray) {
                    ((SparseArray<?>) cache).clear();
                }
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceFirstAppPkg(args);
        Object result = super.invoke(proxy, method, args);
        if (result != null && "getDisplayInfo".equals(method.getName())) {
            try {
                int targetFps = com.teamdark.core.utils.compat.ThermalAndFrameGuard.getTargetFps();
                if (targetFps >= 90) {
                    patchDisplayInfoForTargetHz(result, (float) targetFps);
                }
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    private static boolean isLandscapeContext() {
        try {
            String pkg = null;
            if (com.teamdark.core.app.BActivityThread.isThreadInit()) {
                pkg = com.teamdark.core.app.BActivityThread.getAppPackageName();
            }
            if (pkg != null && (pkg.startsWith("com.pubg.")
                    || "com.tencent.ig".equals(pkg)
                    || "com.rekoo.pubgm".equals(pkg)
                    || "com.vng.pubgmobile".equals(pkg))) {
                return true;
            }
            java.util.Map<android.os.IBinder, Object> map = 
                    com.teamdark.black.android.app.BRActivityThread.get(com.teamdark.core.TeamDarkCoreEngine.mainThread()).mActivities();
            if (map != null && !map.isEmpty()) {
                for (Object record : map.values()) {
                    if (record != null) {
                        android.app.Activity a = com.teamdark.black.android.app.BRActivityThreadActivityClientRecord.get(record).activity();
                        if (a != null) {
                            int o = a.getRequestedOrientation();
                            if (o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                    || o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                                    || o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                                    || o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static void patchDisplayInfoForTargetHz(Object displayInfo, float requestedFps) {
        if (displayInfo == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        Class<?> cls = displayInfo.getClass();
        float targetFps = requestedFps;

        try {
            int logicalW = getIntField(displayInfo, cls, "logicalWidth", 1080);
            int logicalH = getIntField(displayInfo, cls, "logicalHeight", 2400);

            boolean isLandscape = isLandscapeContext();
            int maxDim = Math.max(logicalW, logicalH);
            int minDim = Math.min(logicalW, logicalH);

            if (isLandscape) {
                setIntField(displayInfo, cls, "logicalWidth", maxDim);
                setIntField(displayInfo, cls, "logicalHeight", minDim);
                setIntField(displayInfo, cls, "appWidth", maxDim);
                setIntField(displayInfo, cls, "appHeight", minDim);
                setIntField(displayInfo, cls, "largestNominalAppWidth", maxDim);
                setIntField(displayInfo, cls, "largestNominalAppHeight", minDim);
                setIntField(displayInfo, cls, "smallestNominalAppWidth", minDim);
                setIntField(displayInfo, cls, "smallestNominalAppHeight", minDim);
                setIntField(displayInfo, cls, "rotation", 1); // Surface.ROTATION_90
            }

            Field modesField = findFieldRecursive(cls, "supportedModes");
            if (modesField != null) {
                modesField.setAccessible(true);
                Display.Mode[] modes = (Display.Mode[]) modesField.get(displayInfo);

                Display.Mode bestMode = null;
                float maxRate = 0f;
                if (modes != null && modes.length > 0) {
                    for (Display.Mode m : modes) {
                        if (m != null && m.getRefreshRate() > maxRate) {
                            maxRate = m.getRefreshRate();
                            bestMode = m;
                        }
                    }
                }

                if (maxRate > 120.0f) {
                    targetFps = maxRate;
                }

                int w = isLandscape ? maxDim : ((bestMode != null) ? bestMode.getPhysicalWidth() : logicalW);
                int h = isLandscape ? minDim : ((bestMode != null) ? bestMode.getPhysicalHeight() : logicalH);

                if (bestMode == null || bestMode.getRefreshRate() < 118.0f || (isLandscape && bestMode.getPhysicalWidth() < bestMode.getPhysicalHeight())) {
                    Display.Mode synth120 = createDisplayMode(999, w, h, (targetFps >= 90) ? targetFps : 120.0f);
                    if (synth120 != null) {
                        bestMode = synth120;
                        if (modes != null) {
                            Display.Mode[] expanded = new Display.Mode[modes.length + 1];
                            System.arraycopy(modes, 0, expanded, 0, modes.length);
                            expanded[modes.length] = synth120;
                            modesField.set(displayInfo, expanded);
                        } else {
                            modesField.set(displayInfo, new Display.Mode[]{synth120});
                        }
                    }
                }

                if (bestMode != null) {
                    setIntField(displayInfo, cls, "modeId", bestMode.getModeId());
                    setIntField(displayInfo, cls, "defaultModeId", bestMode.getModeId());

                    Field appsModesField = findFieldRecursive(cls, "appsSupportedModes");
                    if (appsModesField != null) {
                        appsModesField.setAccessible(true);
                        appsModesField.set(displayInfo, modesField.get(displayInfo));
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        if (targetFps >= 90) {
            setFloatField(displayInfo, cls, "refreshRateOverride", targetFps);
            setFloatField(displayInfo, cls, "renderFrameRate", targetFps);
            setLongField(displayInfo, cls, "presentationDeadlineNanos", 8333333L);
            setLongField(displayInfo, cls, "appVsyncOffsetNanos", 1000000L);
        }
    }

    private static Display.Mode createDisplayMode(int modeId, int width, int height, float refreshRate) {
        try {
            for (Constructor<?> ctor : Display.Mode.class.getDeclaredConstructors()) {
                ctor.setAccessible(true);
                Class<?>[] p = ctor.getParameterTypes();
                if (p.length == 4
                        && p[0] == int.class && p[1] == int.class
                        && p[2] == int.class && p[3] == float.class) {
                    return (Display.Mode) ctor.newInstance(modeId, width, height, refreshRate);
                }
                if (p.length == 6
                        && p[0] == int.class && p[1] == int.class
                        && p[2] == int.class && p[3] == float.class
                        && p[4] == float[].class && p[5] == int[].class) {
                    return (Display.Mode) ctor.newInstance(
                            modeId, width, height, refreshRate, new float[0], new int[0]);
                }
                if (p.length == 7
                        && p[0] == int.class && p[1] == int.class
                        && p[2] == int.class && p[3] == float.class
                        && p[4] == float.class
                        && p[5] == float[].class && p[6] == int[].class) {
                    return (Display.Mode) ctor.newInstance(
                            modeId, width, height, refreshRate, refreshRate, new float[0], new int[0]);
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static Field findFieldRecursive(Class<?> cls, String name) {
        Class<?> curr = cls;
        while (curr != null && curr != Object.class) {
            try {
                return curr.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                curr = curr.getSuperclass();
            }
        }
        return null;
    }

    private static int getIntField(Object obj, Class<?> cls, String name, int defVal) {
        try {
            Field f = findFieldRecursive(cls, name);
            if (f != null) {
                f.setAccessible(true);
                return f.getInt(obj);
            }
        } catch (Throwable ignored) {
        }
        return defVal;
    }

    private static void setIntField(Object obj, Class<?> cls, String name, int val) {
        try {
            Field f = findFieldRecursive(cls, name);
            if (f != null) {
                f.setAccessible(true);
                f.setInt(obj, val);
            }
        } catch (Throwable ignored) {
        }
    }

    private static void setFloatField(Object obj, Class<?> cls, String name, float val) {
        try {
            Field f = findFieldRecursive(cls, name);
            if (f != null) {
                f.setAccessible(true);
                f.setFloat(obj, val);
            }
        } catch (Throwable ignored) {
        }
    }

    private static void setLongField(Object obj, Class<?> cls, String name, long val) {
        try {
            Field f = findFieldRecursive(cls, name);
            if (f != null) {
                f.setAccessible(true);
                f.setLong(obj, val);
            }
        } catch (Throwable ignored) {
        }
    }

    @ProxyMethod("createVirtualDisplay")
    public static class CreateVirtualDisplay extends MethodHook {
        @Override
        protected String getMethodName() {
            return "createVirtualDisplay";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }
}
