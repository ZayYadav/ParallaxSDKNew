package com.teamdark.black.android.app.servertransaction;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityLifecycleItem {
    public static ActivityLifecycleItemStatic getWithException() {
        return BlackReflection.create(ActivityLifecycleItemStatic.class, null, true);
    }
    public static ActivityLifecycleItemStatic get() {
        return BlackReflection.create(ActivityLifecycleItemStatic.class, null, false);
    }
    public static ActivityLifecycleItemContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityLifecycleItemContext.class, caller, true);
    }
    public static ActivityLifecycleItemContext get(final Object caller) {
        return BlackReflection.create(ActivityLifecycleItemContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityLifecycleItemContext.class);
    }
}
