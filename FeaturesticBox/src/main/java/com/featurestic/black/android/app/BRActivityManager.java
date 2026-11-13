package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRActivityManager {
    public static ActivityManagerStatic getWithException() {
        return BlackReflection.create(ActivityManagerStatic.class, null, true);
    }
    public static ActivityManagerStatic get() {
        return BlackReflection.create(ActivityManagerStatic.class, null, false);
    }
    public static ActivityManagerContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityManagerContext.class, caller, true);
    }
    public static ActivityManagerContext get(final Object caller) {
        return BlackReflection.create(ActivityManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ActivityManagerContext.class);
    }
}
