package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRNotificationLBuilder {
    public static NotificationLBuilderStatic getWithException() {
        return BlackReflection.create(NotificationLBuilderStatic.class, null, true);
    }
    public static NotificationLBuilderStatic get() {
        return BlackReflection.create(NotificationLBuilderStatic.class, null, false);
    }
    public static NotificationLBuilderContext getWithException(final Object caller) {
        return BlackReflection.create(NotificationLBuilderContext.class, caller, true);
    }
    public static NotificationLBuilderContext get(final Object caller) {
        return BlackReflection.create(NotificationLBuilderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(NotificationLBuilderContext.class);
    }
}
