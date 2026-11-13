package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRContentProviderClientQ {
    public static ContentProviderClientQStatic getWithException() {
        return BlackReflection.create(ContentProviderClientQStatic.class, null, true);
    }
    public static ContentProviderClientQStatic get() {
        return BlackReflection.create(ContentProviderClientQStatic.class, null, false);
    }
    public static ContentProviderClientQContext getWithException(final Object caller) {
        return BlackReflection.create(ContentProviderClientQContext.class, caller, true);
    }
    public static ContentProviderClientQContext get(final Object caller) {
        return BlackReflection.create(ContentProviderClientQContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ContentProviderClientQContext.class);
    }
}
