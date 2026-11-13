package com.teamdark.black.android.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRDisplay {
    public static DisplayStatic getWithException() {
        return BlackReflection.create(DisplayStatic.class, null, true);
    }
    public static DisplayStatic get() {
        return BlackReflection.create(DisplayStatic.class, null, false);
    }
    public static DisplayContext getWithException(final Object caller) {
        return BlackReflection.create(DisplayContext.class, caller, true);
    }
    public static DisplayContext get(final Object caller) {
        return BlackReflection.create(DisplayContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(DisplayContext.class);
    }
}
