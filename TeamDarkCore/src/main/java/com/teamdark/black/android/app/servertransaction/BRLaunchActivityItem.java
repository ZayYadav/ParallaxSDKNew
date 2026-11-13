package com.teamdark.black.android.app.servertransaction;

import com.teamdark.blackreflection.BlackReflection;

public class BRLaunchActivityItem {
    public static LaunchActivityItemStatic getWithException() {
        return BlackReflection.create(LaunchActivityItemStatic.class, null, true);
    }
    public static LaunchActivityItemStatic get() {
        return BlackReflection.create(LaunchActivityItemStatic.class, null, false);
    }
    public static LaunchActivityItemContext getWithException(final Object caller) {
        return BlackReflection.create(LaunchActivityItemContext.class, caller, true);
    }
    public static LaunchActivityItemContext get(final Object caller) {
        return BlackReflection.create(LaunchActivityItemContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LaunchActivityItemContext.class);
    }
}
