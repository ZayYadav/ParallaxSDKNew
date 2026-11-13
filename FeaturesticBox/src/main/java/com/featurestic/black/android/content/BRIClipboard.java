package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRIClipboard {
    public static IClipboardStatic getWithException() {
        return BlackReflection.create(IClipboardStatic.class, null, true);
    }
    public static IClipboardStatic get() {
        return BlackReflection.create(IClipboardStatic.class, null, false);
    }
    public static IClipboardContext getWithException(final Object caller) {
        return BlackReflection.create(IClipboardContext.class, caller, true);
    }
    public static IClipboardContext get(final Object caller) {
        return BlackReflection.create(IClipboardContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IClipboardContext.class);
    }
}
