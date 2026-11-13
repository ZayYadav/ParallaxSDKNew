package com.teamdark.black.android.credentials;

import com.teamdark.blackreflection.BlackReflection;

public class BRICredentialManagerStub {
    public static ICredentialManagerStubStatic getWithException() {
        return BlackReflection.create(ICredentialManagerStubStatic.class, null, true);
    }
    public static ICredentialManagerStubStatic get() {
        return BlackReflection.create(ICredentialManagerStubStatic.class, null, false);
    }
    public static ICredentialManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(ICredentialManagerStubContext.class, caller, true);
    }
    public static ICredentialManagerStubContext get(final Object caller) {
        return BlackReflection.create(ICredentialManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ICredentialManagerStubContext.class);
    }
}
