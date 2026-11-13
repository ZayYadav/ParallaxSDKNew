package com.featurestic.black.com.android.internal.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRUserManagerStub {
    public static UserManagerStubStatic getWithException() {
        return BlackReflection.create(UserManagerStubStatic.class, null, true);
    }
    public static UserManagerStubStatic get() {
        return BlackReflection.create(UserManagerStubStatic.class, null, false);
    }
    public static UserManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(UserManagerStubContext.class, caller, true);
    }
    public static UserManagerStubContext get(final Object caller) {
        return BlackReflection.create(UserManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(UserManagerStubContext.class);
    }
}
