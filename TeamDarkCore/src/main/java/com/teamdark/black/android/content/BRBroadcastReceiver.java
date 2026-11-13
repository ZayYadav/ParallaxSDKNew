package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRBroadcastReceiver {
    public static BroadcastReceiverStatic getWithException() {
        return BlackReflection.create(BroadcastReceiverStatic.class, null, true);
    }
    public static BroadcastReceiverStatic get() {
        return BlackReflection.create(BroadcastReceiverStatic.class, null, false);
    }
    public static BroadcastReceiverContext getWithException(final Object caller) {
        return BlackReflection.create(BroadcastReceiverContext.class, caller, true);
    }
    public static BroadcastReceiverContext get(final Object caller) {
        return BlackReflection.create(BroadcastReceiverContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(BroadcastReceiverContext.class);
    }
}
