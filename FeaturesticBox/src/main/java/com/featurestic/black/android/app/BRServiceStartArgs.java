package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRServiceStartArgs {
    public static ServiceStartArgsStatic getWithException() {
        return BlackReflection.create(ServiceStartArgsStatic.class, null, true);
    }
    public static ServiceStartArgsStatic get() {
        return BlackReflection.create(ServiceStartArgsStatic.class, null, false);
    }
    public static ServiceStartArgsContext getWithException(final Object caller) {
        return BlackReflection.create(ServiceStartArgsContext.class, caller, true);
    }
    public static ServiceStartArgsContext get(final Object caller) {
        return BlackReflection.create(ServiceStartArgsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ServiceStartArgsContext.class);
    }
}
