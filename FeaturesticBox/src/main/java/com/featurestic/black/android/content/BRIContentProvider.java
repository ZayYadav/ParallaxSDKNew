package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRIContentProvider {
    public static IContentProviderStatic getWithException() {
        return BlackReflection.create(IContentProviderStatic.class, null, true);
    }
    public static IContentProviderStatic get() {
        return BlackReflection.create(IContentProviderStatic.class, null, false);
    }
    public static IContentProviderContext getWithException(final Object caller) {
        return BlackReflection.create(IContentProviderContext.class, caller, true);
    }
    public static IContentProviderContext get(final Object caller) {
        return BlackReflection.create(IContentProviderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IContentProviderContext.class);
    }
}
