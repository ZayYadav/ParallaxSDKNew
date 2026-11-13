package com.featurestic.black.android.app.job;

import com.featurestic.blackreflection.BlackReflection;

public class BRJobInfo {
    public static JobInfoStatic getWithException() {
        return BlackReflection.create(JobInfoStatic.class, null, true);
    }
    public static JobInfoStatic get() {
        return BlackReflection.create(JobInfoStatic.class, null, false);
    }
    public static JobInfoContext getWithException(final Object caller) {
        return BlackReflection.create(JobInfoContext.class, caller, true);
    }
    public static JobInfoContext get(final Object caller) {
        return BlackReflection.create(JobInfoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(JobInfoContext.class);
    }
}
