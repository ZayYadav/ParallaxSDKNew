package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRIntentSender {
    public static IntentSenderStatic getWithException() {
        return BlackReflection.create(IntentSenderStatic.class, null, true);
    }
    public static IntentSenderStatic get() {
        return BlackReflection.create(IntentSenderStatic.class, null, false);
    }
    public static IntentSenderContext getWithException(final Object caller) {
        return BlackReflection.create(IntentSenderContext.class, caller, true);
    }
    public static IntentSenderContext get(final Object caller) {
        return BlackReflection.create(IntentSenderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IntentSenderContext.class);
    }
}
