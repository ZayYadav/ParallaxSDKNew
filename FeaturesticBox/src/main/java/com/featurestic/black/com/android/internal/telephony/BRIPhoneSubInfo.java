package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRIPhoneSubInfo {
    public static IPhoneSubInfoStatic getWithException() {
        return BlackReflection.create(IPhoneSubInfoStatic.class, null, true);
    }
    public static IPhoneSubInfoStatic get() {
        return BlackReflection.create(IPhoneSubInfoStatic.class, null, false);
    }
    public static IPhoneSubInfoContext getWithException(final Object caller) {
        return BlackReflection.create(IPhoneSubInfoContext.class, caller, true);
    }
    public static IPhoneSubInfoContext get(final Object caller) {
        return BlackReflection.create(IPhoneSubInfoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IPhoneSubInfoContext.class);
    }
}
