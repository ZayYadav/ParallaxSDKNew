package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRIDeviceIdleControllerStub {
    public static IDeviceIdleControllerStubStatic getWithException() {
        return BlackReflection.create(IDeviceIdleControllerStubStatic.class, null, true);
    }
    public static IDeviceIdleControllerStubStatic get() {
        return BlackReflection.create(IDeviceIdleControllerStubStatic.class, null, false);
    }
    public static IDeviceIdleControllerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IDeviceIdleControllerStubContext.class, caller, true);
    }
    public static IDeviceIdleControllerStubContext get(final Object caller) {
        return BlackReflection.create(IDeviceIdleControllerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IDeviceIdleControllerStubContext.class);
    }
}
