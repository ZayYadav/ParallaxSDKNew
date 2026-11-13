package com.teamdark.black.android.media.session;

import com.teamdark.blackreflection.BlackReflection;

public class BRISessionManager {
    public static ISessionManagerStatic getWithException() {
        return BlackReflection.create(ISessionManagerStatic.class, null, true);
    }
    public static ISessionManagerStatic get() {
        return BlackReflection.create(ISessionManagerStatic.class, null, false);
    }
    public static ISessionManagerContext getWithException(final Object caller) {
        return BlackReflection.create(ISessionManagerContext.class, caller, true);
    }
    public static ISessionManagerContext get(final Object caller) {
        return BlackReflection.create(ISessionManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ISessionManagerContext.class);
    }
}
