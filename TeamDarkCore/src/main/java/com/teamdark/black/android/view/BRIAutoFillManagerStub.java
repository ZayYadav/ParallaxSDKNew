package com.teamdark.black.android.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRIAutoFillManagerStub {
    public static IAutoFillManagerStubStatic getWithException() {
        return BlackReflection.create(IAutoFillManagerStubStatic.class, null, true);
    }
    public static IAutoFillManagerStubStatic get() {
        return BlackReflection.create(IAutoFillManagerStubStatic.class, null, false);
    }
    public static IAutoFillManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IAutoFillManagerStubContext.class, caller, true);
    }
    public static IAutoFillManagerStubContext get(final Object caller) {
        return BlackReflection.create(IAutoFillManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IAutoFillManagerStubContext.class);
    }
}
