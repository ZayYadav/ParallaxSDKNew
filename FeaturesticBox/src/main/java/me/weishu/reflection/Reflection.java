package me.weishu.reflection;

import android.content.Context;
import java.lang.reflect.Method;

public class Reflection {
    public static int unseal(Context context) {
        return exemptAll() ? 0 : -1;
    }

    public static boolean exempt(String member) {
        return exempt(new String[]{member});
    }

    public static boolean exempt(String... methods) {
        try {
            Class<?> vmRuntimeClass = Class.forName("dalvik.system.VMRuntime");
            Method getRuntime = vmRuntimeClass.getDeclaredMethod("getRuntime");
            getRuntime.setAccessible(true);
            Object vmRuntime = getRuntime.invoke(null);
            Method setExemptions = vmRuntimeClass.getDeclaredMethod("setHiddenApiExemptions", String[].class);
            setExemptions.setAccessible(true);
            setExemptions.invoke(vmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean exemptAll() {
        return exempt(new String[]{"L"});
    }
}
