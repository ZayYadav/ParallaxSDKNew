package com.teamdark.black.android.credentials;

import com.teamdark.blackreflection.BlackReflection;

public class BRICredentialManager {
    public static ICredentialManagerStatic getWithException() {
        return BlackReflection.create(ICredentialManagerStatic.class, null, true);
    }
    public static ICredentialManagerStatic get() {
        return BlackReflection.create(ICredentialManagerStatic.class, null, false);
    }
    public static ICredentialManagerContext getWithException(final Object caller) {
        return BlackReflection.create(ICredentialManagerContext.class, caller, true);
    }
    public static ICredentialManagerContext get(final Object caller) {
        return BlackReflection.create(ICredentialManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ICredentialManagerContext.class);
    }
}
