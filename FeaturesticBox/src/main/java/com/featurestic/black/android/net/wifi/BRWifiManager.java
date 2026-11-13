package com.featurestic.black.android.net.wifi;

import com.featurestic.blackreflection.BlackReflection;

public class BRWifiManager {
    public static WifiManagerStatic getWithException() {
        return BlackReflection.create(WifiManagerStatic.class, null, true);
    }
    public static WifiManagerStatic get() {
        return BlackReflection.create(WifiManagerStatic.class, null, false);
    }
    public static WifiManagerContext getWithException(final Object caller) {
        return BlackReflection.create(WifiManagerContext.class, caller, true);
    }
    public static WifiManagerContext get(final Object caller) {
        return BlackReflection.create(WifiManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(WifiManagerContext.class);
    }
}
