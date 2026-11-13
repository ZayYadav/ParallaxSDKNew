package com.featurestic.black.android.bluetooth;

import com.featurestic.blackreflection.BlackReflection;

public class BRIBluetoothManager {
    public static IBluetoothManagerStatic getWithException() {
        return BlackReflection.create(IBluetoothManagerStatic.class, null, true);
    }
    public static IBluetoothManagerStatic get() {
        return BlackReflection.create(IBluetoothManagerStatic.class, null, false);
    }
    public static IBluetoothManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IBluetoothManagerContext.class, caller, true);
    }
    public static IBluetoothManagerContext get(final Object caller) {
        return BlackReflection.create(IBluetoothManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IBluetoothManagerContext.class);
    }
}
