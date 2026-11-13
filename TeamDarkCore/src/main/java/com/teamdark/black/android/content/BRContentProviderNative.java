package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRContentProviderNative {
    public static ContentProviderNativeStatic getWithException() {
        return BlackReflection.create(ContentProviderNativeStatic.class, null, true);
    }
    public static ContentProviderNativeStatic get() {
        return BlackReflection.create(ContentProviderNativeStatic.class, null, false);
    }
    public static ContentProviderNativeContext getWithException(final Object caller) {
        return BlackReflection.create(ContentProviderNativeContext.class, caller, true);
    }
    public static ContentProviderNativeContext get(final Object caller) {
        return BlackReflection.create(ContentProviderNativeContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ContentProviderNativeContext.class);
    }
}
