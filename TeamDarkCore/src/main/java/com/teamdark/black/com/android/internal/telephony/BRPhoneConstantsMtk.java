package com.teamdark.black.com.android.internal.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRPhoneConstantsMtk {
    public static PhoneConstantsMtkStatic getWithException() {
        return BlackReflection.create(PhoneConstantsMtkStatic.class, null, true);
    }
    public static PhoneConstantsMtkStatic get() {
        return BlackReflection.create(PhoneConstantsMtkStatic.class, null, false);
    }
    public static PhoneConstantsMtkContext getWithException(final Object caller) {
        return BlackReflection.create(PhoneConstantsMtkContext.class, caller, true);
    }
    public static PhoneConstantsMtkContext get(final Object caller) {
        return BlackReflection.create(PhoneConstantsMtkContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PhoneConstantsMtkContext.class);
    }
}
