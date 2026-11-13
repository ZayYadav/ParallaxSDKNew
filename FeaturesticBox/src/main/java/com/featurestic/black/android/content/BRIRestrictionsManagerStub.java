package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRIRestrictionsManagerStub {
    public static IRestrictionsManagerStubStatic getWithException() {
        return BlackReflection.create(IRestrictionsManagerStubStatic.class, null, true);
    }
    public static IRestrictionsManagerStubStatic get() {
        return BlackReflection.create(IRestrictionsManagerStubStatic.class, null, false);
    }
    public static IRestrictionsManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IRestrictionsManagerStubContext.class, caller, true);
    }
    public static IRestrictionsManagerStubContext get(final Object caller) {
        return BlackReflection.create(IRestrictionsManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IRestrictionsManagerStubContext.class);
    }
}
