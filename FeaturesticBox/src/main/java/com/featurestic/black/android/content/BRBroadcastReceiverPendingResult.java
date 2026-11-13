package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRBroadcastReceiverPendingResult {
    public static BroadcastReceiverPendingResultStatic getWithException() {
        return BlackReflection.create(BroadcastReceiverPendingResultStatic.class, null, true);
    }
    public static BroadcastReceiverPendingResultStatic get() {
        return BlackReflection.create(BroadcastReceiverPendingResultStatic.class, null, false);
    }
    public static BroadcastReceiverPendingResultContext getWithException(final Object caller) {
        return BlackReflection.create(BroadcastReceiverPendingResultContext.class, caller, true);
    }
    public static BroadcastReceiverPendingResultContext get(final Object caller) {
        return BlackReflection.create(BroadcastReceiverPendingResultContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(BroadcastReceiverPendingResultContext.class);
    }
}
