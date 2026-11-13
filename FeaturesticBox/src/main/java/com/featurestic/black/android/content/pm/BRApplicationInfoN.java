package com.featurestic.black.android.content.pm;

import com.featurestic.blackreflection.BlackReflection;

public class BRApplicationInfoN {
    public static ApplicationInfoNStatic getWithException() {
        return BlackReflection.create(ApplicationInfoNStatic.class, null, true);
    }
    public static ApplicationInfoNStatic get() {
        return BlackReflection.create(ApplicationInfoNStatic.class, null, false);
    }
    public static ApplicationInfoNContext getWithException(final Object caller) {
        return BlackReflection.create(ApplicationInfoNContext.class, caller, true);
    }
    public static ApplicationInfoNContext get(final Object caller) {
        return BlackReflection.create(ApplicationInfoNContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ApplicationInfoNContext.class);
    }
}
