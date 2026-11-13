package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRIActivityManagerContentProviderHolder {
    public static IActivityManagerContentProviderHolderStatic getWithException() {
        return BlackReflection.create(IActivityManagerContentProviderHolderStatic.class, null, true);
    }
    public static IActivityManagerContentProviderHolderStatic get() {
        return BlackReflection.create(IActivityManagerContentProviderHolderStatic.class, null, false);
    }
    public static IActivityManagerContentProviderHolderContext getWithException(final Object caller) {
        return BlackReflection.create(IActivityManagerContentProviderHolderContext.class, caller, true);
    }
    public static IActivityManagerContentProviderHolderContext get(final Object caller) {
        return BlackReflection.create(IActivityManagerContentProviderHolderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IActivityManagerContentProviderHolderContext.class);
    }
}
