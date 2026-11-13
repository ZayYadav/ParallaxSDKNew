package com.teamdark.black.android.app.admin;

import com.teamdark.blackreflection.BlackReflection;

public class BRIDevicePolicyManager {
    public static IDevicePolicyManagerStatic getWithException() {
        return BlackReflection.create(IDevicePolicyManagerStatic.class, null, true);
    }
    public static IDevicePolicyManagerStatic get() {
        return BlackReflection.create(IDevicePolicyManagerStatic.class, null, false);
    }
    public static IDevicePolicyManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IDevicePolicyManagerContext.class, caller, true);
    }
    public static IDevicePolicyManagerContext get(final Object caller) {
        return BlackReflection.create(IDevicePolicyManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IDevicePolicyManagerContext.class);
    }
}
