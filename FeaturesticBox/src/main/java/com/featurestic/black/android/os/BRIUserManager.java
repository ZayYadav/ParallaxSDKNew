package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRIUserManager {
    public static IUserManagerStatic getWithException() {
        return BlackReflection.create(IUserManagerStatic.class, null, true);
    }
    public static IUserManagerStatic get() {
        return BlackReflection.create(IUserManagerStatic.class, null, false);
    }
    public static IUserManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IUserManagerContext.class, caller, true);
    }
    public static IUserManagerContext get(final Object caller) {
        return BlackReflection.create(IUserManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IUserManagerContext.class);
    }
}
