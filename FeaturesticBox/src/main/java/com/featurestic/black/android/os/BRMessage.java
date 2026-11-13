package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRMessage {
    public static MessageStatic getWithException() {
        return BlackReflection.create(MessageStatic.class, null, true);
    }
    public static MessageStatic get() {
        return BlackReflection.create(MessageStatic.class, null, false);
    }
    public static MessageContext getWithException(final Object caller) {
        return BlackReflection.create(MessageContext.class, caller, true);
    }
    public static MessageContext get(final Object caller) {
        return BlackReflection.create(MessageContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(MessageContext.class);
    }
}
