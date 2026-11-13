package com.teamdark.black.android.media;

import com.teamdark.blackreflection.BlackReflection;

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
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(MediaRouterStaticContext.class);
    }
}
