package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRIMms {
    public static IMmsStatic getWithException() {
        return BlackReflection.create(IMmsStatic.class, null, true);
    }
    public static IMmsStatic get() {
        return BlackReflection.create(IMmsStatic.class, null, false);
    }
    public static IMmsContext getWithException(final Object caller) {
        return BlackReflection.create(IMmsContext.class, caller, true);
    }
    public static IMmsContext get(final Object caller) {
        return BlackReflection.create(IMmsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IMmsContext.class);
    }
}
