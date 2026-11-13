package com.teamdark.black.android.service.notification;

import com.teamdark.blackreflection.BlackReflection;

public class BRStatusBarNotification {
    public static StatusBarNotificationStatic getWithException() {
        return BlackReflection.create(StatusBarNotificationStatic.class, null, true);
    }
    public static StatusBarNotificationStatic get() {
        return BlackReflection.create(StatusBarNotificationStatic.class, null, false);
    }
    public static StatusBarNotificationContext getWithException(final Object caller) {
        return BlackReflection.create(StatusBarNotificationContext.class, caller, true);
    }
    public static StatusBarNotificationContext get(final Object caller) {
        return BlackReflection.create(StatusBarNotificationContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(StatusBarNotificationContext.class);
    }
}
