package com.teamdark.black.android.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRIGraphicsStats {
    public static IGraphicsStatsStatic getWithException() {
        return BlackReflection.create(IGraphicsStatsStatic.class, null, true);
    }
    public static IGraphicsStatsStatic get() {
        return BlackReflection.create(IGraphicsStatsStatic.class, null, false);
    }
    public static IGraphicsStatsContext getWithException(final Object caller) {
        return BlackReflection.create(IGraphicsStatsContext.class, caller, true);
    }
    public static IGraphicsStatsContext get(final Object caller) {
        return BlackReflection.create(IGraphicsStatsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IGraphicsStatsContext.class);
    }
}
