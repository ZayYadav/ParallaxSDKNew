package com.teamdark.black.model.vivo;

import com.teamdark.blackreflection.BlackReflection;

public class BRIVivoPermissionService {
    public static IVivoPermissionServiceStatic getWithException() {
        return BlackReflection.create(IVivoPermissionServiceStatic.class, null, true);
    }
    public static IVivoPermissionServiceStatic get() {
        return BlackReflection.create(IVivoPermissionServiceStatic.class, null, false);
    }
    public static IVivoPermissionServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IVivoPermissionServiceContext.class, caller, true);
    }
    public static IVivoPermissionServiceContext get(final Object caller) {
        return BlackReflection.create(IVivoPermissionServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IVivoPermissionServiceContext.class);
    }
}
