package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRAppOpsManager {
    public static AppOpsManagerStatic getWithException() {
        return BlackReflection.create(AppOpsManagerStatic.class, null, true);
    }
    public static AppOpsManagerStatic get() {
        return BlackReflection.create(AppOpsManagerStatic.class, null, false);
    }
    public static AppOpsManagerContext getWithException(final Object caller) {
        return BlackReflection.create(AppOpsManagerContext.class, caller, true);
    }
    public static AppOpsManagerContext get(final Object caller) {
        return BlackReflection.create(AppOpsManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(AppOpsManagerContext.class);
    }
}
