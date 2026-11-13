package com.teamdark.black.android.hardware.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRIContextHubService {
    public static IContextHubServiceStatic getWithException() {
        return BlackReflection.create(IContextHubServiceStatic.class, null, true);
    }
    public static IContextHubServiceStatic get() {
        return BlackReflection.create(IContextHubServiceStatic.class, null, false);
    }
    public static IContextHubServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IContextHubServiceContext.class, caller, true);
    }
    public static IContextHubServiceContext get(final Object caller) {
        return BlackReflection.create(IContextHubServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IContextHubServiceContext.class);
    }
}
