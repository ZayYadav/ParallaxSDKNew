package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityClient {
    public static ActivityClientStatic getWithException() {
        return BlackReflection.create(ActivityClientStatic.class, null, true);
    }
    public static ActivityClientStatic get() {
        return BlackReflection.create(ActivityClientStatic.class, null, false);
    }
    public static ActivityClientContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityClientContext.class, caller, true);
    }
    public static ActivityClientContext get(final Object caller) {
        return BlackReflection.create(ActivityClientContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityClientContext.class);
    }
}
