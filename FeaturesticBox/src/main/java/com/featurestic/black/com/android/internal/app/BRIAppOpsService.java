package com.featurestic.black.com.android.internal.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRIAppOpsService {
    public static IAppOpsServiceStatic getWithException() {
        return BlackReflection.create(IAppOpsServiceStatic.class, null, true);
    }
    public static IAppOpsServiceStatic get() {
        return BlackReflection.create(IAppOpsServiceStatic.class, null, false);
    }
    public static IAppOpsServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IAppOpsServiceContext.class, caller, true);
    }
    public static IAppOpsServiceContext get(final Object caller) {
        return BlackReflection.create(IAppOpsServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IAppOpsServiceContext.class);
    }
}
