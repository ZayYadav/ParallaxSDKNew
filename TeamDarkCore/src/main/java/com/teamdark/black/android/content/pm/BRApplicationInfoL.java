package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRApplicationInfoL {
    public static ApplicationInfoLStatic getWithException() {
        return BlackReflection.create(ApplicationInfoLStatic.class, null, true);
    }
    public static ApplicationInfoLStatic get() {
        return BlackReflection.create(ApplicationInfoLStatic.class, null, false);
    }
    public static ApplicationInfoLContext getWithException(final Object caller) {
        return BlackReflection.create(ApplicationInfoLContext.class, caller, true);
    }
    public static ApplicationInfoLContext get(final Object caller) {
        return BlackReflection.create(ApplicationInfoLContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ApplicationInfoLContext.class);
    }
}
