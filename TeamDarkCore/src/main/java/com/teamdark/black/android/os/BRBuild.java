package com.teamdark.black.android.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRBuild {
    public static BuildStatic getWithException() {
        return BlackReflection.create(BuildStatic.class, null, true);
    }
    public static BuildStatic get() {
        return BlackReflection.create(BuildStatic.class, null, false);
    }
    public static BuildContext getWithException(final Object caller) {
        return BlackReflection.create(BuildContext.class, caller, true);
    }
    public static BuildContext get(final Object caller) {
        return BlackReflection.create(BuildContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(BuildContext.class);
    }
}
