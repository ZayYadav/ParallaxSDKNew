package com.teamdark.black.android.app.role;

import com.teamdark.blackreflection.BlackReflection;

public class BRIRoleManager {
    public static IRoleManagerStatic getWithException() {
        return BlackReflection.create(IRoleManagerStatic.class, null, true);
    }
    public static IRoleManagerStatic get() {
        return BlackReflection.create(IRoleManagerStatic.class, null, false);
    }
    public static IRoleManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IRoleManagerContext.class, caller, true);
    }
    public static IRoleManagerContext get(final Object caller) {
        return BlackReflection.create(IRoleManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IRoleManagerContext.class);
    }
}
