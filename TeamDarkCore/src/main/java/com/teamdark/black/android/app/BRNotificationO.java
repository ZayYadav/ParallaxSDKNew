package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRNotificationO {
    public static NotificationOStatic getWithException() {
        return BlackReflection.create(NotificationOStatic.class, null, true);
    }
    public static NotificationOStatic get() {
        return BlackReflection.create(NotificationOStatic.class, null, false);
    }
    public static NotificationOContext getWithException(final Object caller) {
        return BlackReflection.create(NotificationOContext.class, caller, true);
    }
    public static NotificationOContext get(final Object caller) {
        return BlackReflection.create(NotificationOContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(NotificationOContext.class);
    }
}
