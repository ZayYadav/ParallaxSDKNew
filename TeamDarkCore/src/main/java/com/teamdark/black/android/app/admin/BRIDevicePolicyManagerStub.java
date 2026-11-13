package com.teamdark.black.android.app.admin;

import com.teamdark.blackreflection.BlackReflection;

public class BRIDevicePolicyManagerStub {
    public static IDevicePolicyManagerStubStatic getWithException() {
        return BlackReflection.create(IDevicePolicyManagerStubStatic.class, null, true);
    }
    public static IDevicePolicyManagerStubStatic get() {
        return BlackReflection.create(IDevicePolicyManagerStubStatic.class, null, false);
    }
    public static IDevicePolicyManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IDevicePolicyManagerStubContext.class, caller, true);
    }
    public static IDevicePolicyManagerStubContext get(final Object caller) {
        return BlackReflection.create(IDevicePolicyManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IDevicePolicyManagerStubContext.class);
    }
}
