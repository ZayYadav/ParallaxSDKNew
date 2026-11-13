package com.featurestic.black.android.permission;

import com.featurestic.blackreflection.BlackReflection;

public class BRIPermissionManager {
    public static IPermissionManagerStatic getWithException() {
        return BlackReflection.create(IPermissionManagerStatic.class, null, true);
    }
    public static IPermissionManagerStatic get() {
        return BlackReflection.create(IPermissionManagerStatic.class, null, false);
    }
    public static IPermissionManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IPermissionManagerContext.class, caller, true);
    }
    public static IPermissionManagerContext get(final Object caller) {
        return BlackReflection.create(IPermissionManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IPermissionManagerContext.class);
    }
}
