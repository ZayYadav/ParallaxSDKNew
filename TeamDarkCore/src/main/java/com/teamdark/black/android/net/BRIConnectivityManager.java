package com.teamdark.black.android.net;

import com.teamdark.blackreflection.BlackReflection;

public class BRIConnectivityManager {
    public static IConnectivityManagerStatic getWithException() {
        return BlackReflection.create(IConnectivityManagerStatic.class, null, true);
    }
    public static IConnectivityManagerStatic get() {
        return BlackReflection.create(IConnectivityManagerStatic.class, null, false);
    }
    public static IConnectivityManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IConnectivityManagerContext.class, caller, true);
    }
    public static IConnectivityManagerContext get(final Object caller) {
        return BlackReflection.create(IConnectivityManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IConnectivityManagerContext.class);
    }
}
