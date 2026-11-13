package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRSyncRequest {
    public static SyncRequestStatic getWithException() {
        return BlackReflection.create(SyncRequestStatic.class, null, true);
    }
    public static SyncRequestStatic get() {
        return BlackReflection.create(SyncRequestStatic.class, null, false);
    }
    public static SyncRequestContext getWithException(final Object caller) {
        return BlackReflection.create(SyncRequestContext.class, caller, true);
    }
    public static SyncRequestContext get(final Object caller) {
        return BlackReflection.create(SyncRequestContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SyncRequestContext.class);
    }
}
