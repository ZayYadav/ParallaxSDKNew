package com.teamdark.black.com.android.internal.appwidget;

import com.teamdark.blackreflection.BlackReflection;

public class BRIAppWidgetService {
    public static IAppWidgetServiceStatic getWithException() {
        return BlackReflection.create(IAppWidgetServiceStatic.class, null, true);
    }
    public static IAppWidgetServiceStatic get() {
        return BlackReflection.create(IAppWidgetServiceStatic.class, null, false);
    }
    public static IAppWidgetServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IAppWidgetServiceContext.class, caller, true);
    }
    public static IAppWidgetServiceContext get(final Object caller) {
        return BlackReflection.create(IAppWidgetServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IAppWidgetServiceContext.class);
    }
}
