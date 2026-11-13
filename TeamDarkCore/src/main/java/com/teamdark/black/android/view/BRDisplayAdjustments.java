package com.teamdark.black.android.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRDisplayAdjustments {
    public static DisplayAdjustmentsStatic getWithException() {
        return BlackReflection.create(DisplayAdjustmentsStatic.class, null, true);
    }
    public static DisplayAdjustmentsStatic get() {
        return BlackReflection.create(DisplayAdjustmentsStatic.class, null, false);
    }
    public static DisplayAdjustmentsContext getWithException(final Object caller) {
        return BlackReflection.create(DisplayAdjustmentsContext.class, caller, true);
    }
    public static DisplayAdjustmentsContext get(final Object caller) {
        return BlackReflection.create(DisplayAdjustmentsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(DisplayAdjustmentsContext.class);
    }
}
