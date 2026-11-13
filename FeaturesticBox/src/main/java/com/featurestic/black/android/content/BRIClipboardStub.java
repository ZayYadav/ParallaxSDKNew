package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRIClipboardStub {
    public static IClipboardStubStatic getWithException() {
        return BlackReflection.create(IClipboardStubStatic.class, null, true);
    }
    public static IClipboardStubStatic get() {
        return BlackReflection.create(IClipboardStubStatic.class, null, false);
    }
    public static IClipboardStubContext getWithException(final Object caller) {
        return BlackReflection.create(IClipboardStubContext.class, caller, true);
    }
    public static IClipboardStubContext get(final Object caller) {
        return BlackReflection.create(IClipboardStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IClipboardStubContext.class);
    }
}
