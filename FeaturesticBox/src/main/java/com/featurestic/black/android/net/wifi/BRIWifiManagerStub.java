package com.featurestic.black.android.net.wifi;

import com.featurestic.blackreflection.BlackReflection;

public class BRIWifiManagerStub {
    public static IWifiManagerStubStatic getWithException() {
        return BlackReflection.create(IWifiManagerStubStatic.class, null, true);
    }
    public static IWifiManagerStubStatic get() {
        return BlackReflection.create(IWifiManagerStubStatic.class, null, false);
    }
    public static IWifiManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IWifiManagerStubContext.class, caller, true);
    }
    public static IWifiManagerStubContext get(final Object caller) {
        return BlackReflection.create(IWifiManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IWifiManagerStubContext.class);
    }
}
