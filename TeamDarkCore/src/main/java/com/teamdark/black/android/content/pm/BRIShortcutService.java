package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRIShortcutService {
    public static IShortcutServiceStatic getWithException() {
        return BlackReflection.create(IShortcutServiceStatic.class, null, true);
    }
    public static IShortcutServiceStatic get() {
        return BlackReflection.create(IShortcutServiceStatic.class, null, false);
    }
    public static IShortcutServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IShortcutServiceContext.class, caller, true);
    }
    public static IShortcutServiceContext get(final Object caller) {
        return BlackReflection.create(IShortcutServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IShortcutServiceContext.class);
    }
}
