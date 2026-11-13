package com.teamdark.black.android.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRDropBoxManager {
    public static DropBoxManagerStatic getWithException() {
        return BlackReflection.create(DropBoxManagerStatic.class, null, true);
    }
    public static DropBoxManagerStatic get() {
        return BlackReflection.create(DropBoxManagerStatic.class, null, false);
    }
    public static DropBoxManagerContext getWithException(final Object caller) {
        return BlackReflection.create(DropBoxManagerContext.class, caller, true);
    }
    public static DropBoxManagerContext get(final Object caller) {
        return BlackReflection.create(DropBoxManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(DropBoxManagerContext.class);
    }
}
