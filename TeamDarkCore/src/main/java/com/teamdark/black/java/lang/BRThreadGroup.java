package com.teamdark.black.java.lang;

import com.teamdark.blackreflection.BlackReflection;

public class BRThreadGroup {
    public static ThreadGroupStatic getWithException() {
        return BlackReflection.create(ThreadGroupStatic.class, null, true);
    }
    public static ThreadGroupStatic get() {
        return BlackReflection.create(ThreadGroupStatic.class, null, false);
    }
    public static ThreadGroupContext getWithException(final Object caller) {
        return BlackReflection.create(ThreadGroupContext.class, caller, true);
    }
    public static ThreadGroupContext get(final Object caller) {
        return BlackReflection.create(ThreadGroupContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ThreadGroupContext.class);
    }
}
