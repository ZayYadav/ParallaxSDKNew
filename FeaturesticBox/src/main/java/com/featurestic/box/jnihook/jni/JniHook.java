package com.featurestic.box.jnihook.jni;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.box.jnihook.JHook;

public final class JniHook {
    public static final int NATIVE_OFFSET = 0;
    public static final int NATIVE_OFFSET_2 = 0;

    public static void nativeOffset() {
        try {
            JHook.nativeOffset();
        } catch (Throwable ignored) {
        }
    }

    public static void nativeOffset2() {
        try {
            JHook.nativeOffset2();
        } catch (Throwable ignored) {
        }
    }

    public static void setAccessible(Class<?> clazz, Method method) {
        try {
            JHook.setAccessible(clazz, method);
        } catch (Throwable ignored) {
            if (method != null) {
                try {
                    method.setAccessible(true);
                } catch (Throwable ignored2) {
                }
            }
        }
    }

    public static void setAccessible(Class<?> clazz, Field field) {
        try {
            JHook.setAccessible(clazz, field);
        } catch (Throwable ignored) {
            if (field != null) {
                try {
                    field.setAccessible(true);
                } catch (Throwable ignored2) {
                }
            }
        }
    }
}
