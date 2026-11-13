package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRSyncAdapterTypeN {
    public static SyncAdapterTypeNStatic getWithException() {
        return BlackReflection.create(SyncAdapterTypeNStatic.class, null, true);
    }
    public static SyncAdapterTypeNStatic get() {
        return BlackReflection.create(SyncAdapterTypeNStatic.class, null, false);
    }
    public static SyncAdapterTypeNContext getWithException(final Object caller) {
        return BlackReflection.create(SyncAdapterTypeNContext.class, caller, true);
    }
    public static SyncAdapterTypeNContext get(final Object caller) {
        return BlackReflection.create(SyncAdapterTypeNContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SyncAdapterTypeNContext.class);
    }
}
