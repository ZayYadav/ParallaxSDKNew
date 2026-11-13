package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRISubStub {
    public static ISubStubStatic getWithException() {
        return BlackReflection.create(ISubStubStatic.class, null, true);
    }
    public static ISubStubStatic get() {
        return BlackReflection.create(ISubStubStatic.class, null, false);
    }
    public static ISubStubContext getWithException(final Object caller) {
        return BlackReflection.create(ISubStubContext.class, caller, true);
    }
    public static ISubStubContext get(final Object caller) {
        return BlackReflection.create(ISubStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ISubStubContext.class);
    }
}
