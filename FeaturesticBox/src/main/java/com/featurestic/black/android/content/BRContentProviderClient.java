package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRContentProviderClient {
    public static ContentProviderClientStatic getWithException() {
        return BlackReflection.create(ContentProviderClientStatic.class, null, true);
    }
    public static ContentProviderClientStatic get() {
        return BlackReflection.create(ContentProviderClientStatic.class, null, false);
    }
    public static ContentProviderClientContext getWithException(final Object caller) {
        return BlackReflection.create(ContentProviderClientContext.class, caller, true);
    }
    public static ContentProviderClientContext get(final Object caller) {
        return BlackReflection.create(ContentProviderClientContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ContentProviderClientContext.class);
    }
}
