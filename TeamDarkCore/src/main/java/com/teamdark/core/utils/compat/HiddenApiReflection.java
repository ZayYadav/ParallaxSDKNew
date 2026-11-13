package com.teamdark.core.utils.compat;

import android.content.Context;
import android.os.Build;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 4-Layer Hidden API Unsealer for Android 9–16:
 *   1. C++ JNI Unseal via libtdcore.so (com.tdcore.legacy.NativeCore.disableHiddenApi() -> libart.so VMRuntime_setHiddenApiExemptions)
 *   2. Double Meta-Reflection (Class.class.getDeclaredMethod("getDeclaredMethod", ...))
 *   3. LSPosed HiddenApiBypass (org.lsposed.hiddenapibypass.HiddenApiBypass)
 *   4. FreeReflection (me.weishu.reflection.Reflection)
 */
public final class HiddenApiReflection {
    private static final AtomicBoolean sNativeUnsealed = new AtomicBoolean(false);

    private HiddenApiReflection() {}

    private static boolean unsealViaNativeCore() {
        try {
            if (sNativeUnsealed.compareAndSet(false, true)) {
                return com.tdcore.legacy.NativeCore.disableHiddenApi();
            }
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static int unseal(Context context) {
        boolean ok = unsealViaNativeCore();
        if (exemptAll()) {
            ok = true;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Class<?> hab = Class.forName("org.lsposed.hiddenapibypass.HiddenApiBypass");
                Method m = hab.getDeclaredMethod("addHiddenApiExemptions", String[].class);
                m.setAccessible(true);
                m.invoke(null, (Object) new String[]{""});
                ok = true;
            }
        } catch (Throwable ignored) {
        }
        try {
            if (context != null) {
                Class<?> freeRef = Class.forName("me.weishu.reflection.Reflection");
                Method m = freeRef.getDeclaredMethod("unseal", Context.class);
                m.setAccessible(true);
                Object r = m.invoke(null, context);
                if (r instanceof Integer && ((Integer) r) == 0) {
                    ok = true;
                }
            }
        } catch (Throwable ignored) {
        }
        return ok ? 0 : -1;
    }

    public static boolean exempt(String member) {
        return exempt(new String[]{member});
    }

    public static boolean exempt(String... methods) {
        boolean ok = unsealViaNativeCore();
        // Layer 1: Double Meta-Reflection (bypasses Java reflection caller frame filter)
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(
                    vmRuntimeClass, "setHiddenApiExemptions", (Object) new Class[]{String[].class});
            getRuntime.setAccessible(true);
            setHiddenApiExemptions.setAccessible(true);
            Object vmRuntime = getRuntime.invoke(null);
            setHiddenApiExemptions.invoke(vmRuntime, new Object[]{methods});
            ok = true;
        } catch (Throwable ignored) {
        }
        // Layer 2: LSPosed HiddenApiBypass if present
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Class<?> hab = Class.forName("org.lsposed.hiddenapibypass.HiddenApiBypass");
                Method m = hab.getDeclaredMethod("addHiddenApiExemptions", String[].class);
                m.setAccessible(true);
                m.invoke(null, (Object) methods);
                ok = true;
            }
        } catch (Throwable ignored) {
        }
        return ok;
    }

    public static boolean exemptAll() {
        return exempt(new String[]{"", "L"});
    }
}
