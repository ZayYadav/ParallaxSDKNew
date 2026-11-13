package com.featurestic.black.android.net;

import com.featurestic.blackreflection.BlackReflection;

public class BRIVpnManagerStub {
    public static IVpnManagerStubStatic getWithException() {
        return BlackReflection.create(IVpnManagerStubStatic.class, null, true);
    }
    public static IVpnManagerStubStatic get() {
        return BlackReflection.create(IVpnManagerStubStatic.class, null, false);
    }
    public static IVpnManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IVpnManagerStubContext.class, caller, true);
    }
    public static IVpnManagerStubContext get(final Object caller) {
        return BlackReflection.create(IVpnManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IVpnManagerStubContext.class);
    }
}
