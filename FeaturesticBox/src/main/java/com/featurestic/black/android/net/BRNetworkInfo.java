package com.featurestic.black.android.net;

import com.featurestic.blackreflection.BlackReflection;

public class BRNetworkInfo {
    public static NetworkInfoStatic getWithException() {
        return BlackReflection.create(NetworkInfoStatic.class, null, true);
    }
    public static NetworkInfoStatic get() {
        return BlackReflection.create(NetworkInfoStatic.class, null, false);
    }
    public static NetworkInfoContext getWithException(final Object caller) {
        return BlackReflection.create(NetworkInfoContext.class, caller, true);
    }
    public static NetworkInfoContext get(final Object caller) {
        return BlackReflection.create(NetworkInfoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(NetworkInfoContext.class);
    }
}
