package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

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
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(SyncAdapterTypeNContext.class);
    }
}
