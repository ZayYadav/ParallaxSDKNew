package com.teamdark.black.android.content.integrity;

import com.teamdark.blackreflection.BlackReflection;

public class BRIAppIntegrityManager {
    public static IAppIntegrityManagerStatic getWithException() {
        return BlackReflection.create(IAppIntegrityManagerStatic.class, null, true);
    }
    public static IAppIntegrityManagerStatic get() {
        return BlackReflection.create(IAppIntegrityManagerStatic.class, null, false);
    }
    public static IAppIntegrityManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IAppIntegrityManagerContext.class, caller, true);
    }
    public static IAppIntegrityManagerContext get(final Object caller) {
        return BlackReflection.create(IAppIntegrityManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IAppIntegrityManagerContext.class);
    }
}
