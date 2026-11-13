package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRIActivityManagerContentProviderHolderMIUI {
    public static IActivityManagerContentProviderHolderMIUIStatic getWithException() {
        return BlackReflection.create(IActivityManagerContentProviderHolderMIUIStatic.class, null, true);
    }
    public static IActivityManagerContentProviderHolderMIUIStatic get() {
        return BlackReflection.create(IActivityManagerContentProviderHolderMIUIStatic.class, null, false);
    }
    public static IActivityManagerContentProviderHolderMIUIContext getWithException(final Object caller) {
        return BlackReflection.create(IActivityManagerContentProviderHolderMIUIContext.class, caller, true);
    }
    public static IActivityManagerContentProviderHolderMIUIContext get(final Object caller) {
        return BlackReflection.create(IActivityManagerContentProviderHolderMIUIContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IActivityManagerContentProviderHolderMIUIContext.class);
    }
}
