package com.featurestic.black.android.media;

import com.featurestic.blackreflection.BlackReflection;

public class BRMediaRouterStatic {
    public static MediaRouterStaticStatic getWithException() {
        return BlackReflection.create(MediaRouterStaticStatic.class, null, true);
    }
    public static MediaRouterStaticStatic get() {
        return BlackReflection.create(MediaRouterStaticStatic.class, null, false);
    }
    public static MediaRouterStaticContext getWithException(final Object caller) {
        return BlackReflection.create(MediaRouterStaticContext.class, caller, true);
    }
    public static MediaRouterStaticContext get(final Object caller) {
        return BlackReflection.create(MediaRouterStaticContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(MediaRouterStaticContext.class);
    }
}
