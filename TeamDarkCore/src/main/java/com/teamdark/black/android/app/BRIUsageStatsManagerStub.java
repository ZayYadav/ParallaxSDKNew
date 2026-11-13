package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRIUsageStatsManagerStub {
    public static IUsageStatsManagerStubStatic getWithException() {
        return BlackReflection.create(IUsageStatsManagerStubStatic.class, null, true);
    }
    public static IUsageStatsManagerStubStatic get() {
        return BlackReflection.create(IUsageStatsManagerStubStatic.class, null, false);
    }
    public static IUsageStatsManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IUsageStatsManagerStubContext.class, caller, true);
    }
    public static IUsageStatsManagerStubContext get(final Object caller) {
        return BlackReflection.create(IUsageStatsManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IUsageStatsManagerStubContext.class);
    }
}
