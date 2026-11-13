package com.featurestic.black.android.os.storage;

import com.featurestic.blackreflection.BlackReflection;

public class BRStorageVolume {
    public static StorageVolumeStatic getWithException() {
        return BlackReflection.create(StorageVolumeStatic.class, null, true);
    }
    public static StorageVolumeStatic get() {
        return BlackReflection.create(StorageVolumeStatic.class, null, false);
    }
    public static StorageVolumeContext getWithException(final Object caller) {
        return BlackReflection.create(StorageVolumeContext.class, caller, true);
    }
    public static StorageVolumeContext get(final Object caller) {
        return BlackReflection.create(StorageVolumeContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(StorageVolumeContext.class);
    }
}
