package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRPeriodicSync {
    public static PeriodicSyncStatic getWithException() {
        return BlackReflection.create(PeriodicSyncStatic.class, null, true);
    }
    public static PeriodicSyncStatic get() {
        return BlackReflection.create(PeriodicSyncStatic.class, null, false);
    }
    public static PeriodicSyncContext getWithException(final Object caller) {
        return BlackReflection.create(PeriodicSyncContext.class, caller, true);
    }
    public static PeriodicSyncContext get(final Object caller) {
        return BlackReflection.create(PeriodicSyncContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PeriodicSyncContext.class);
    }
}
