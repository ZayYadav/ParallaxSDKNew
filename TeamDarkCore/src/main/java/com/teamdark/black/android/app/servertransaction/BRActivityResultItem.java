package com.teamdark.black.android.app.servertransaction;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityResultItem {
    public static ActivityResultItemStatic getWithException() {
        return BlackReflection.create(ActivityResultItemStatic.class, null, true);
    }
    public static ActivityResultItemStatic get() {
        return BlackReflection.create(ActivityResultItemStatic.class, null, false);
    }
    public static ActivityResultItemContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityResultItemContext.class, caller, true);
    }
    public static ActivityResultItemContext get(final Object caller) {
        return BlackReflection.create(ActivityResultItemContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityResultItemContext.class);
    }
}
