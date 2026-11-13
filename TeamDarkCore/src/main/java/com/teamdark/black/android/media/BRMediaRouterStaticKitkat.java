package com.teamdark.black.android.media;

import com.teamdark.blackreflection.BlackReflection;

public class BRMediaRouterStaticKitkat {
    public static MediaRouterStaticKitkatStatic getWithException() {
        return BlackReflection.create(MediaRouterStaticKitkatStatic.class, null, true);
    }
    public static MediaRouterStaticKitkatStatic get() {
        return BlackReflection.create(MediaRouterStaticKitkatStatic.class, null, false);
    }
    public static MediaRouterStaticKitkatContext getWithException(final Object caller) {
        return BlackReflection.create(MediaRouterStaticKitkatContext.class, caller, true);
    }
    public static MediaRouterStaticKitkatContext get(final Object caller) {
        return BlackReflection.create(MediaRouterStaticKitkatContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(MediaRouterStaticKitkatContext.class);
    }
}
