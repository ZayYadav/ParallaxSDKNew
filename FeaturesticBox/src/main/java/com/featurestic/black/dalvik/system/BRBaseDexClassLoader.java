package com.featurestic.black.dalvik.system;

import com.featurestic.blackreflection.BlackReflection;

public class BRBaseDexClassLoader {
    public static BaseDexClassLoaderStatic getWithException() {
        return BlackReflection.create(BaseDexClassLoaderStatic.class, null, true);
    }
    public static BaseDexClassLoaderStatic get() {
        return BlackReflection.create(BaseDexClassLoaderStatic.class, null, false);
    }
    public static BaseDexClassLoaderContext getWithException(final Object caller) {
        return BlackReflection.create(BaseDexClassLoaderContext.class, caller, true);
    }
    public static BaseDexClassLoaderContext get(final Object caller) {
        return BlackReflection.create(BaseDexClassLoaderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(BaseDexClassLoaderContext.class);
    }
}
