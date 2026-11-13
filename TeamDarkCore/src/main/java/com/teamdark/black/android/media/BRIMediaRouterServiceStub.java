package com.teamdark.black.android.media;

import com.teamdark.blackreflection.BlackReflection;

public class BRIMediaRouterServiceStub {
    public static IMediaRouterServiceStubStatic getWithException() {
        return BlackReflection.create(IMediaRouterServiceStubStatic.class, null, true);
    }
    public static IMediaRouterServiceStubStatic get() {
        return BlackReflection.create(IMediaRouterServiceStubStatic.class, null, false);
    }
    public static IMediaRouterServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(IMediaRouterServiceStubContext.class, caller, true);
    }
    public static IMediaRouterServiceStubContext get(final Object caller) {
        return BlackReflection.create(IMediaRouterServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IMediaRouterServiceStubContext.class);
    }
}
