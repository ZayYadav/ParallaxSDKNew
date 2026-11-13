package com.teamdark.black.android.content;

import com.teamdark.blackreflection.BlackReflection;

public class BRClipboardManager {
    public static ClipboardManagerStatic getWithException() {
        return BlackReflection.create(ClipboardManagerStatic.class, null, true);
    }
    public static ClipboardManagerStatic get() {
        return BlackReflection.create(ClipboardManagerStatic.class, null, false);
    }
    public static ClipboardManagerContext getWithException(final Object caller) {
        return BlackReflection.create(ClipboardManagerContext.class, caller, true);
    }
    public static ClipboardManagerContext get(final Object caller) {
        return BlackReflection.create(ClipboardManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ClipboardManagerContext.class);
    }
}
