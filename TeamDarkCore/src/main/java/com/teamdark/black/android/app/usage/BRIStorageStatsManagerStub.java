package com.teamdark.black.android.app.usage;

import com.teamdark.blackreflection.BlackReflection;

public class BRIStorageStatsManagerStub {
    public static IStorageStatsManagerStubStatic getWithException() {
        return BlackReflection.create(IStorageStatsManagerStubStatic.class, null, true);
    }
    public static IStorageStatsManagerStubStatic get() {
        return BlackReflection.create(IStorageStatsManagerStubStatic.class, null, false);
    }
    public static IStorageStatsManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IStorageStatsManagerStubContext.class, caller, true);
    }
    public static IStorageStatsManagerStubContext get(final Object caller) {
        return BlackReflection.create(IStorageStatsManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IStorageStatsManagerStubContext.class);
    }
}
