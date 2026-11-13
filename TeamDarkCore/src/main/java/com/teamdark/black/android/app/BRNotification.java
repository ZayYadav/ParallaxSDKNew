package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRNotification {
    public static NotificationStatic getWithException() {
        return BlackReflection.create(NotificationStatic.class, null, true);
    }
    public static NotificationStatic get() {
        return BlackReflection.create(NotificationStatic.class, null, false);
    }
    public static NotificationContext getWithException(final Object caller) {
        return BlackReflection.create(NotificationContext.class, caller, true);
    }
    public static NotificationContext get(final Object caller) {
        return BlackReflection.create(NotificationContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(NotificationContext.class);
    }
}
