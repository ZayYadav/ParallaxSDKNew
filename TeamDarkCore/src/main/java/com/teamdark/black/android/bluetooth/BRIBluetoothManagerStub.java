package com.teamdark.black.android.bluetooth;

import com.teamdark.blackreflection.BlackReflection;

public class BRIBluetoothManagerStub {
    public static IBluetoothManagerStubStatic getWithException() {
        return BlackReflection.create(IBluetoothManagerStubStatic.class, null, true);
    }
    public static IBluetoothManagerStubStatic get() {
        return BlackReflection.create(IBluetoothManagerStubStatic.class, null, false);
    }
    public static IBluetoothManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IBluetoothManagerStubContext.class, caller, true);
    }
    public static IBluetoothManagerStubContext get(final Object caller) {
        return BlackReflection.create(IBluetoothManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IBluetoothManagerStubContext.class);
    }
}
