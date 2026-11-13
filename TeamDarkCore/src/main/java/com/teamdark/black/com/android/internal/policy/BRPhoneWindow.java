package com.teamdark.black.com.android.internal.policy;

import com.teamdark.blackreflection.BlackReflection;

public class BRPhoneWindow {
    public static PhoneWindowStatic getWithException() {
        return BlackReflection.create(PhoneWindowStatic.class, null, true);
    }
    public static PhoneWindowStatic get() {
        return BlackReflection.create(PhoneWindowStatic.class, null, false);
    }
    public static PhoneWindowContext getWithException(final Object caller) {
        return BlackReflection.create(PhoneWindowContext.class, caller, true);
    }
    public static PhoneWindowContext get(final Object caller) {
        return BlackReflection.create(PhoneWindowContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PhoneWindowContext.class);
    }
}
