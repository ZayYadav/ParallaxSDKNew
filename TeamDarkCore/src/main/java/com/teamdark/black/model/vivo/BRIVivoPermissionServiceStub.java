package com.teamdark.black.model.vivo;

import com.teamdark.blackreflection.BlackReflection;

public class BRIVivoPermissionServiceStub {
    public static IVivoPermissionServiceStubStatic getWithException() {
        return BlackReflection.create(IVivoPermissionServiceStubStatic.class, null, true);
    }
    public static IVivoPermissionServiceStubStatic get() {
        return BlackReflection.create(IVivoPermissionServiceStubStatic.class, null, false);
    }
    public static IVivoPermissionServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(IVivoPermissionServiceStubContext.class, caller, true);
    }
    public static IVivoPermissionServiceStubContext get(final Object caller) {
        return BlackReflection.create(IVivoPermissionServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IVivoPermissionServiceStubContext.class);
    }
}
