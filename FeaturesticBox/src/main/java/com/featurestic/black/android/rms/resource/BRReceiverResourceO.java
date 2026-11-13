package com.featurestic.black.android.rms.resource;

import com.featurestic.blackreflection.BlackReflection;

public class BRReceiverResourceO {
    public static ReceiverResourceOStatic getWithException() {
        return BlackReflection.create(ReceiverResourceOStatic.class, null, true);
    }
    public static ReceiverResourceOStatic get() {
        return BlackReflection.create(ReceiverResourceOStatic.class, null, false);
    }
    public static ReceiverResourceOContext getWithException(final Object caller) {
        return BlackReflection.create(ReceiverResourceOContext.class, caller, true);
    }
    public static ReceiverResourceOContext get(final Object caller) {
        return BlackReflection.create(ReceiverResourceOContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ReceiverResourceOContext.class);
    }
}
