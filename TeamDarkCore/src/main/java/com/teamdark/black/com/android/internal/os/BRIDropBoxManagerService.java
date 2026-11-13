package com.teamdark.black.com.android.internal.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRIDropBoxManagerService {
    public static IDropBoxManagerServiceStatic getWithException() {
        return BlackReflection.create(IDropBoxManagerServiceStatic.class, null, true);
    }
    public static IDropBoxManagerServiceStatic get() {
        return BlackReflection.create(IDropBoxManagerServiceStatic.class, null, false);
    }
    public static IDropBoxManagerServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IDropBoxManagerServiceContext.class, caller, true);
    }
    public static IDropBoxManagerServiceContext get(final Object caller) {
        return BlackReflection.create(IDropBoxManagerServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IDropBoxManagerServiceContext.class);
    }
}
