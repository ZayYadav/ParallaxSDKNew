package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityThread {
    public static ActivityThreadStatic getWithException() {
        return BlackReflection.create(ActivityThreadStatic.class, null, true);
    }
    public static ActivityThreadStatic get() {
        return BlackReflection.create(ActivityThreadStatic.class, null, false);
    }
    public static ActivityThreadContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityThreadContext.class, caller, true);
    }
    public static ActivityThreadContext get(final Object caller) {
        return BlackReflection.create(ActivityThreadContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityThreadContext.class);
    }
}
