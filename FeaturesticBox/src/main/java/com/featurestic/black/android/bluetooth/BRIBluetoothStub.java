package com.featurestic.black.android.bluetooth;

import com.featurestic.blackreflection.BlackReflection;

public class BRIBluetoothStub {
    public static IBluetoothStubStatic getWithException() {
        return BlackReflection.create(IBluetoothStubStatic.class, null, true);
    }
    public static IBluetoothStubStatic get() {
        return BlackReflection.create(IBluetoothStubStatic.class, null, false);
    }
    public static IBluetoothStubContext getWithException(final Object caller) {
        return BlackReflection.create(IBluetoothStubContext.class, caller, true);
    }
    public static IBluetoothStubContext get(final Object caller) {
        return BlackReflection.create(IBluetoothStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IBluetoothStubContext.class);
    }
}
