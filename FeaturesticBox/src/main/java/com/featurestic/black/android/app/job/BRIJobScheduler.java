package com.featurestic.black.android.app.job;

import com.featurestic.blackreflection.BlackReflection;

public class BRIJobScheduler {
    public static IJobSchedulerStatic getWithException() {
        return BlackReflection.create(IJobSchedulerStatic.class, null, true);
    }
    public static IJobSchedulerStatic get() {
        return BlackReflection.create(IJobSchedulerStatic.class, null, false);
    }
    public static IJobSchedulerContext getWithException(final Object caller) {
        return BlackReflection.create(IJobSchedulerContext.class, caller, true);
    }
    public static IJobSchedulerContext get(final Object caller) {
        return BlackReflection.create(IJobSchedulerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IJobSchedulerContext.class);
    }
}
