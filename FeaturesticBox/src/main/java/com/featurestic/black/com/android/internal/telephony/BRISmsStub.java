package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRISmsStub {
    public static ISmsStubStatic getWithException() {
        return BlackReflection.create(ISmsStubStatic.class, null, true);
    }
    public static ISmsStubStatic get() {
        return BlackReflection.create(ISmsStubStatic.class, null, false);
    }
    public static ISmsStubContext getWithException(final Object caller) {
        return BlackReflection.create(ISmsStubContext.class, caller, true);
    }
    public static ISmsStubContext get(final Object caller) {
        return BlackReflection.create(ISmsStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ISmsStubContext.class);
    }
}
