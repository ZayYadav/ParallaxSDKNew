package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRIContentService {
    public static IContentServiceStatic getWithException() {
        return BlackReflection.create(IContentServiceStatic.class, null, true);
    }
    public static IContentServiceStatic get() {
        return BlackReflection.create(IContentServiceStatic.class, null, false);
    }
    public static IContentServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IContentServiceContext.class, caller, true);
    }
    public static IContentServiceContext get(final Object caller) {
        return BlackReflection.create(IContentServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IContentServiceContext.class);
    }
}
