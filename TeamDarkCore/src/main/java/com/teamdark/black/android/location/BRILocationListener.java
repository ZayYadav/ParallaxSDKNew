package com.teamdark.black.android.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRILocationListener {
    public static ILocationListenerStatic getWithException() {
        return BlackReflection.create(ILocationListenerStatic.class, null, true);
    }
    public static ILocationListenerStatic get() {
        return BlackReflection.create(ILocationListenerStatic.class, null, false);
    }
    public static ILocationListenerContext getWithException(final Object caller) {
        return BlackReflection.create(ILocationListenerContext.class, caller, true);
    }
    public static ILocationListenerContext get(final Object caller) {
        return BlackReflection.create(ILocationListenerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ILocationListenerContext.class);
    }
}
