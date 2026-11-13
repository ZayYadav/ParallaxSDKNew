package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRApplicationInfoP {
    public static ApplicationInfoPStatic getWithException() {
        return BlackReflection.create(ApplicationInfoPStatic.class, null, true);
    }
    public static ApplicationInfoPStatic get() {
        return BlackReflection.create(ApplicationInfoPStatic.class, null, false);
    }
    public static ApplicationInfoPContext getWithException(final Object caller) {
        return BlackReflection.create(ApplicationInfoPContext.class, caller, true);
    }
    public static ApplicationInfoPContext get(final Object caller) {
        return BlackReflection.create(ApplicationInfoPContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ApplicationInfoPContext.class);
    }
}
