package com.featurestic.black.android.media.session;

import com.featurestic.blackreflection.BlackReflection;

public class BRISessionManagerStub {
    public static ISessionManagerStubStatic getWithException() {
        return BlackReflection.create(ISessionManagerStubStatic.class, null, true);
    }
    public static ISessionManagerStubStatic get() {
        return BlackReflection.create(ISessionManagerStubStatic.class, null, false);
    }
    public static ISessionManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(ISessionManagerStubContext.class, caller, true);
    }
    public static ISessionManagerStubContext get(final Object caller) {
        return BlackReflection.create(ISessionManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ISessionManagerStubContext.class);
    }
}
