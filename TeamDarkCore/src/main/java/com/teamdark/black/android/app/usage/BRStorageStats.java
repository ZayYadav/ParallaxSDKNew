package com.teamdark.black.android.app.usage;

import com.teamdark.blackreflection.BlackReflection;

public class BRStorageStats {
    public static StorageStatsStatic getWithException() {
        return BlackReflection.create(StorageStatsStatic.class, null, true);
    }
    public static StorageStatsStatic get() {
        return BlackReflection.create(StorageStatsStatic.class, null, false);
    }
    public static StorageStatsContext getWithException(final Object caller) {
        return BlackReflection.create(StorageStatsContext.class, caller, true);
    }
    public static StorageStatsContext get(final Object caller) {
        return BlackReflection.create(StorageStatsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(StorageStatsContext.class);
    }
}
