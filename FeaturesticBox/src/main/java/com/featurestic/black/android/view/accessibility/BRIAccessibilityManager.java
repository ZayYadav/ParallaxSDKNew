package com.featurestic.black.android.view.accessibility;

import com.featurestic.blackreflection.BlackReflection;

public class BRIAccessibilityManager {
    public static IAccessibilityManagerStatic getWithException() {
        return BlackReflection.create(IAccessibilityManagerStatic.class, null, true);
    }
    public static IAccessibilityManagerStatic get() {
        return BlackReflection.create(IAccessibilityManagerStatic.class, null, false);
    }
    public static IAccessibilityManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IAccessibilityManagerContext.class, caller, true);
    }
    public static IAccessibilityManagerContext get(final Object caller) {
        return BlackReflection.create(IAccessibilityManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IAccessibilityManagerContext.class);
    }
}
