package com.featurestic.black.android.app.usage;

import com.featurestic.blackreflection.BlackReflection;

public class BRIStorageStatsManager {
    public static IStorageStatsManagerStatic getWithException() {
        return BlackReflection.create(IStorageStatsManagerStatic.class, null, true);
    }
    public static IStorageStatsManagerStatic get() {
        return BlackReflection.create(IStorageStatsManagerStatic.class, null, false);
    }
    public static IStorageStatsManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IStorageStatsManagerContext.class, caller, true);
    }
    public static IStorageStatsManagerContext get(final Object caller) {
        return BlackReflection.create(IStorageStatsManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IStorageStatsManagerContext.class);
    }
}
