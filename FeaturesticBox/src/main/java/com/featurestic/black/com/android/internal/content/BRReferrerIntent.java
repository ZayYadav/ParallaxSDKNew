package com.featurestic.black.com.android.internal.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRReferrerIntent {
    public static ReferrerIntentStatic getWithException() {
        return BlackReflection.create(ReferrerIntentStatic.class, null, true);
    }
    public static ReferrerIntentStatic get() {
        return BlackReflection.create(ReferrerIntentStatic.class, null, false);
    }
    public static ReferrerIntentContext getWithException(final Object caller) {
        return BlackReflection.create(ReferrerIntentContext.class, caller, true);
    }
    public static ReferrerIntentContext get(final Object caller) {
        return BlackReflection.create(ReferrerIntentContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ReferrerIntentContext.class);
    }
}
