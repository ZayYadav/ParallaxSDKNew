package com.featurestic.black.android.accounts;

import com.featurestic.blackreflection.BlackReflection;

public class BRIAccountManagerStub {
    public static IAccountManagerStubStatic getWithException() {
        return BlackReflection.create(IAccountManagerStubStatic.class, null, true);
    }
    public static IAccountManagerStubStatic get() {
        return BlackReflection.create(IAccountManagerStubStatic.class, null, false);
    }
    public static IAccountManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IAccountManagerStubContext.class, caller, true);
    }
    public static IAccountManagerStubContext get(final Object caller) {
        return BlackReflection.create(IAccountManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IAccountManagerStubContext.class);
    }
}
