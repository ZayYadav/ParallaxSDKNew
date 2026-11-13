package com.teamdark.black.android.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRIPowerManager {
    public static IPowerManagerStatic getWithException() {
        return BlackReflection.create(IPowerManagerStatic.class, null, true);
    }
    public static IPowerManagerStatic get() {
        return BlackReflection.create(IPowerManagerStatic.class, null, false);
    }
    public static IPowerManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IPowerManagerContext.class, caller, true);
    }
    public static IPowerManagerContext get(final Object caller) {
        return BlackReflection.create(IPowerManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IPowerManagerContext.class);
    }
}
