package com.featurestic.black.android.bluetooth;

import com.featurestic.blackreflection.BlackReflection;

public class BRIBluetooth {
    public static IBluetoothStatic getWithException() {
        return BlackReflection.create(IBluetoothStatic.class, null, true);
    }
    public static IBluetoothStatic get() {
        return BlackReflection.create(IBluetoothStatic.class, null, false);
    }
    public static IBluetoothContext getWithException(final Object caller) {
        return BlackReflection.create(IBluetoothContext.class, caller, true);
    }
    public static IBluetoothContext get(final Object caller) {
        return BlackReflection.create(IBluetoothContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IBluetoothContext.class);
    }
}
