package com.teamdark.black.android.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRCompatibilityInfoHolder {
    public static CompatibilityInfoHolderStatic getWithException() {
        return BlackReflection.create(CompatibilityInfoHolderStatic.class, null, true);
    }
    public static CompatibilityInfoHolderStatic get() {
        return BlackReflection.create(CompatibilityInfoHolderStatic.class, null, false);
    }
    public static CompatibilityInfoHolderContext getWithException(final Object caller) {
        return BlackReflection.create(CompatibilityInfoHolderContext.class, caller, true);
    }
    public static CompatibilityInfoHolderContext get(final Object caller) {
        return BlackReflection.create(CompatibilityInfoHolderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(CompatibilityInfoHolderContext.class);
    }
}
