package com.featurestic.black.libcore.io;

import com.featurestic.blackreflection.BlackReflection;

public class BROs {
    public static OsStatic getWithException() {
        return BlackReflection.create(OsStatic.class, null, true);
    }
    public static OsStatic get() {
        return BlackReflection.create(OsStatic.class, null, false);
    }
    public static OsContext getWithException(final Object caller) {
        return BlackReflection.create(OsContext.class, caller, true);
    }
    public static OsContext get(final Object caller) {
        return BlackReflection.create(OsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(OsContext.class);
    }
}
