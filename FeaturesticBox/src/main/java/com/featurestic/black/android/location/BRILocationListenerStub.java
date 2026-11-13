package com.featurestic.black.android.location;

import com.featurestic.blackreflection.BlackReflection;

public class BRILocationListenerStub {
    public static ILocationListenerStubStatic getWithException() {
        return BlackReflection.create(ILocationListenerStubStatic.class, null, true);
    }
    public static ILocationListenerStubStatic get() {
        return BlackReflection.create(ILocationListenerStubStatic.class, null, false);
    }
    public static ILocationListenerStubContext getWithException(final Object caller) {
        return BlackReflection.create(ILocationListenerStubContext.class, caller, true);
    }
    public static ILocationListenerStubContext get(final Object caller) {
        return BlackReflection.create(ILocationListenerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ILocationListenerStubContext.class);
    }
}
