package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRSharedPreferencesImpl {
    public static SharedPreferencesImplStatic getWithException() {
        return BlackReflection.create(SharedPreferencesImplStatic.class, null, true);
    }
    public static SharedPreferencesImplStatic get() {
        return BlackReflection.create(SharedPreferencesImplStatic.class, null, false);
    }
    public static SharedPreferencesImplContext getWithException(final Object caller) {
        return BlackReflection.create(SharedPreferencesImplContext.class, caller, true);
    }
    public static SharedPreferencesImplContext get(final Object caller) {
        return BlackReflection.create(SharedPreferencesImplContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(SharedPreferencesImplContext.class);
    }
}
