package com.teamdark.black.com.android.internal.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRNativeLibraryHelperHandle {
    public static NativeLibraryHelperHandleStatic getWithException() {
        return BlackReflection.create(NativeLibraryHelperHandleStatic.class, null, true);
    }
    public static NativeLibraryHelperHandleStatic get() {
        return BlackReflection.create(NativeLibraryHelperHandleStatic.class, null, false);
    }
    public static NativeLibraryHelperHandleContext getWithException(final Object caller) {
        return BlackReflection.create(NativeLibraryHelperHandleContext.class, caller, true);
    }
    public static NativeLibraryHelperHandleContext get(final Object caller) {
        return BlackReflection.create(NativeLibraryHelperHandleContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(NativeLibraryHelperHandleContext.class);
    }
}
