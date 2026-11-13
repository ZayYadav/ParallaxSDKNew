package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRPendingIntentO {
    public static PendingIntentOStatic getWithException() {
        return BlackReflection.create(PendingIntentOStatic.class, null, true);
    }
    public static PendingIntentOStatic get() {
        return BlackReflection.create(PendingIntentOStatic.class, null, false);
    }
    public static PendingIntentOContext getWithException(final Object caller) {
        return BlackReflection.create(PendingIntentOContext.class, caller, true);
    }
    public static PendingIntentOContext get(final Object caller) {
        return BlackReflection.create(PendingIntentOContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PendingIntentOContext.class);
    }
}
