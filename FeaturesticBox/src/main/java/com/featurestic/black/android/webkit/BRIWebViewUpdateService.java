package com.featurestic.black.android.webkit;

import com.featurestic.blackreflection.BlackReflection;

public class BRIWebViewUpdateService {
    public static IWebViewUpdateServiceStatic getWithException() {
        return BlackReflection.create(IWebViewUpdateServiceStatic.class, null, true);
    }
    public static IWebViewUpdateServiceStatic get() {
        return BlackReflection.create(IWebViewUpdateServiceStatic.class, null, false);
    }
    public static IWebViewUpdateServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IWebViewUpdateServiceContext.class, caller, true);
    }
    public static IWebViewUpdateServiceContext get(final Object caller) {
        return BlackReflection.create(IWebViewUpdateServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IWebViewUpdateServiceContext.class);
    }
}
