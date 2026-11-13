package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRIUsageStatsManager {
    public static IUsageStatsManagerStatic getWithException() {
        return BlackReflection.create(IUsageStatsManagerStatic.class, null, true);
    }
    public static IUsageStatsManagerStatic get() {
        return BlackReflection.create(IUsageStatsManagerStatic.class, null, false);
    }
    public static IUsageStatsManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IUsageStatsManagerContext.class, caller, true);
    }
    public static IUsageStatsManagerContext get(final Object caller) {
        return BlackReflection.create(IUsageStatsManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IUsageStatsManagerContext.class);
    }
}
