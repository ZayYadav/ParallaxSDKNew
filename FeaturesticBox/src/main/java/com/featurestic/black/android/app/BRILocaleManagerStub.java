package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRILocaleManagerStub {
    public static ILocaleManagerStubStatic getWithException() {
        return BlackReflection.create(ILocaleManagerStubStatic.class, null, true);
    }
    public static ILocaleManagerStubStatic get() {
        return BlackReflection.create(ILocaleManagerStubStatic.class, null, false);
    }
    public static ILocaleManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(ILocaleManagerStubContext.class, caller, true);
    }
    public static ILocaleManagerStubContext get(final Object caller) {
        return BlackReflection.create(ILocaleManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ILocaleManagerStubContext.class);
    }
}
