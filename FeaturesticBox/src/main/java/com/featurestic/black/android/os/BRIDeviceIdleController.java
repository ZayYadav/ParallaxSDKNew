package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRIDeviceIdleController {
    public static IDeviceIdleControllerStatic getWithException() {
        return BlackReflection.create(IDeviceIdleControllerStatic.class, null, true);
    }
    public static IDeviceIdleControllerStatic get() {
        return BlackReflection.create(IDeviceIdleControllerStatic.class, null, false);
    }
    public static IDeviceIdleControllerContext getWithException(final Object caller) {
        return BlackReflection.create(IDeviceIdleControllerContext.class, caller, true);
    }
    public static IDeviceIdleControllerContext get(final Object caller) {
        return BlackReflection.create(IDeviceIdleControllerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IDeviceIdleControllerContext.class);
    }
}
