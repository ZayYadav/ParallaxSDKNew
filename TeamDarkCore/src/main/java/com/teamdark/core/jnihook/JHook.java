package com.teamdark.core.jnihook;

import androidx.annotation.Keep;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Keep
public final class JHook {
    public static final int NATIVE_OFFSET = 0;
    public static final int NATIVE_OFFSET_2 = 0;

    public static final native void nativeOffset();
    public static final native void nativeOffset2();
    public static native void setAccessible(Class<?> clazz, Method method);
    public static native void setAccessible(Class<?> clazz, Field field);
}
