package com.featurestic.black.android.os.storage;

import com.featurestic.blackreflection.BlackReflection;

public class BRIStorageManager {
    public static IStorageManagerStatic getWithException() {
        return BlackReflection.create(IStorageManagerStatic.class, null, true);
    }
    public static IStorageManagerStatic get() {
        return BlackReflection.create(IStorageManagerStatic.class, null, false);
    }
    public static IStorageManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IStorageManagerContext.class, caller, true);
    }
    public static IStorageManagerContext get(final Object caller) {
        return BlackReflection.create(IStorageManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IStorageManagerContext.class);
    }
}
