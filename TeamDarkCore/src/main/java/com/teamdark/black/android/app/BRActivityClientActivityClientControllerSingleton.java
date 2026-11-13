package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityClientActivityClientControllerSingleton {
    public static ActivityClientActivityClientControllerSingletonStatic getWithException() {
        return BlackReflection.create(ActivityClientActivityClientControllerSingletonStatic.class, null, true);
    }
    public static ActivityClientActivityClientControllerSingletonStatic get() {
        return BlackReflection.create(ActivityClientActivityClientControllerSingletonStatic.class, null, false);
    }
    public static ActivityClientActivityClientControllerSingletonContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityClientActivityClientControllerSingletonContext.class, caller, true);
    }
    public static ActivityClientActivityClientControllerSingletonContext get(final Object caller) {
        return BlackReflection.create(ActivityClientActivityClientControllerSingletonContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityClientActivityClientControllerSingletonContext.class);
    }
}
