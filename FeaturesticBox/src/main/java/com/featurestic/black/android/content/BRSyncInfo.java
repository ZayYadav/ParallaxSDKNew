package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRSyncInfo {
    public static SyncInfoStatic getWithException() {
        return BlackReflection.create(SyncInfoStatic.class, null, true);
    }
    public static SyncInfoStatic get() {
        return BlackReflection.create(SyncInfoStatic.class, null, false);
    }
    public static SyncInfoContext getWithException(final Object caller) {
        return BlackReflection.create(SyncInfoContext.class, caller, true);
    }
    public static SyncInfoContext get(final Object caller) {
        return BlackReflection.create(SyncInfoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SyncInfoContext.class);
    }
}
