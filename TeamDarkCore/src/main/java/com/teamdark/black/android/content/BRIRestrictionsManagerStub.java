package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

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
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IRestrictionsManagerStubContext.class);
    }
}
