package com.teamdark.black.com.android.internal.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRIInputMethodManagerStub {
    public static IInputMethodManagerStubStatic getWithException() {
        return BlackReflection.create(IInputMethodManagerStubStatic.class, null, true);
    }
    public static IInputMethodManagerStubStatic get() {
        return BlackReflection.create(IInputMethodManagerStubStatic.class, null, false);
    }
    public static IInputMethodManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IInputMethodManagerStubContext.class, caller, true);
    }
    public static IInputMethodManagerStubContext get(final Object caller) {
        return BlackReflection.create(IInputMethodManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IInputMethodManagerStubContext.class);
    }
}
