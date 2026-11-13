package com.featurestic.black.android.dalvik.system;

import com.featurestic.blackreflection.BlackReflection;

public class BRDexFile {
    public static DexFileStatic getWithException() {
        return BlackReflection.create(DexFileStatic.class, null, true);
    }
    public static DexFileStatic get() {
        return BlackReflection.create(DexFileStatic.class, null, false);
    }
    public static DexFileContext getWithException(final Object caller) {
        return BlackReflection.create(DexFileContext.class, caller, true);
    }
    public static DexFileContext get(final Object caller) {
        return BlackReflection.create(DexFileContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(DexFileContext.class);
    }
}
