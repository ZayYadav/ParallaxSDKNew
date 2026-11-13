package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityThreadAppBindData {
    public static ActivityThreadAppBindDataStatic getWithException() {
        return BlackReflection.create(ActivityThreadAppBindDataStatic.class, null, true);
    }
    public static ActivityThreadAppBindDataStatic get() {
        return BlackReflection.create(ActivityThreadAppBindDataStatic.class, null, false);
    }
    public static ActivityThreadAppBindDataContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityThreadAppBindDataContext.class, caller, true);
    }
    public static ActivityThreadAppBindDataContext get(final Object caller) {
        return BlackReflection.create(ActivityThreadAppBindDataContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityThreadAppBindDataContext.class);
    }
}
