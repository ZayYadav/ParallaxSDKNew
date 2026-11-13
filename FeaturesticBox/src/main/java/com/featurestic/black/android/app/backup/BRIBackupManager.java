package com.featurestic.black.android.app.backup;

import com.featurestic.blackreflection.BlackReflection;

public class BRIBackupManager {
    public static IBackupManagerStatic getWithException() {
        return BlackReflection.create(IBackupManagerStatic.class, null, true);
    }
    public static IBackupManagerStatic get() {
        return BlackReflection.create(IBackupManagerStatic.class, null, false);
    }
    public static IBackupManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IBackupManagerContext.class, caller, true);
    }
    public static IBackupManagerContext get(final Object caller) {
        return BlackReflection.create(IBackupManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IBackupManagerContext.class);
    }
}
