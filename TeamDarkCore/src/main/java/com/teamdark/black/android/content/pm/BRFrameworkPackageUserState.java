package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRFrameworkPackageUserState {
    public static FrameworkPackageUserStateStatic getWithException() {
        return BlackReflection.create(FrameworkPackageUserStateStatic.class, null, true);
    }
    public static FrameworkPackageUserStateStatic get() {
        return BlackReflection.create(FrameworkPackageUserStateStatic.class, null, false);
    }
    public static FrameworkPackageUserStateContext getWithException(final Object caller) {
        return BlackReflection.create(FrameworkPackageUserStateContext.class, caller, true);
    }
    public static FrameworkPackageUserStateContext get(final Object caller) {
        return BlackReflection.create(FrameworkPackageUserStateContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(FrameworkPackageUserStateContext.class);
    }
}
