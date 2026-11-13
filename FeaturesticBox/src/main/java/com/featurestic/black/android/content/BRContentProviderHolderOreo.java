package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRContentProviderHolderOreo {
    public static ContentProviderHolderOreoStatic getWithException() {
        return BlackReflection.create(ContentProviderHolderOreoStatic.class, null, true);
    }
    public static ContentProviderHolderOreoStatic get() {
        return BlackReflection.create(ContentProviderHolderOreoStatic.class, null, false);
    }
    public static ContentProviderHolderOreoContext getWithException(final Object caller) {
        return BlackReflection.create(ContentProviderHolderOreoContext.class, caller, true);
    }
    public static ContentProviderHolderOreoContext get(final Object caller) {
        return BlackReflection.create(ContentProviderHolderOreoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ContentProviderHolderOreoContext.class);
    }
}
