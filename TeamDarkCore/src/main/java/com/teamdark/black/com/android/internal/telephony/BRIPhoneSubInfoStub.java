package com.teamdark.black.com.android.internal.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRIPhoneSubInfoStub {
    public static IPhoneSubInfoStubStatic getWithException() {
        return BlackReflection.create(IPhoneSubInfoStubStatic.class, null, true);
    }
    public static IPhoneSubInfoStubStatic get() {
        return BlackReflection.create(IPhoneSubInfoStubStatic.class, null, false);
    }
    public static IPhoneSubInfoStubContext getWithException(final Object caller) {
        return BlackReflection.create(IPhoneSubInfoStubContext.class, caller, true);
    }
    public static IPhoneSubInfoStubContext get(final Object caller) {
        return BlackReflection.create(IPhoneSubInfoStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IPhoneSubInfoStubContext.class);
    }
}
