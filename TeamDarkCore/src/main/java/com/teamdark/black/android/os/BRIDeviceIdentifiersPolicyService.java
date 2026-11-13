package com.teamdark.black.android.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRIDeviceIdentifiersPolicyService {
    public static IDeviceIdentifiersPolicyServiceStatic getWithException() {
        return BlackReflection.create(IDeviceIdentifiersPolicyServiceStatic.class, null, true);
    }
    public static IDeviceIdentifiersPolicyServiceStatic get() {
        return BlackReflection.create(IDeviceIdentifiersPolicyServiceStatic.class, null, false);
    }
    public static IDeviceIdentifiersPolicyServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IDeviceIdentifiersPolicyServiceContext.class, caller, true);
    }
    public static IDeviceIdentifiersPolicyServiceContext get(final Object caller) {
        return BlackReflection.create(IDeviceIdentifiersPolicyServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IDeviceIdentifiersPolicyServiceContext.class);
    }
}
