package com.teamdark.black.android.widget;

import com.teamdark.blackreflection.BlackReflection;

public class BRRemoteViews {
    public static RemoteViewsStatic getWithException() {
        return BlackReflection.create(RemoteViewsStatic.class, null, true);
    }
    public static RemoteViewsStatic get() {
        return BlackReflection.create(RemoteViewsStatic.class, null, false);
    }
    public static RemoteViewsContext getWithException(final Object caller) {
        return BlackReflection.create(RemoteViewsContext.class, caller, true);
    }
    public static RemoteViewsContext get(final Object caller) {
        return BlackReflection.create(RemoteViewsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(RemoteViewsContext.class);
    }
}
