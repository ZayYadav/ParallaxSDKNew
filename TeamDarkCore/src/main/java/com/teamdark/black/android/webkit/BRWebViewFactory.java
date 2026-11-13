package com.teamdark.black.android.webkit;

import com.teamdark.blackreflection.BlackReflection;

public class BRWebViewFactory {
    public static WebViewFactoryStatic getWithException() {
        return BlackReflection.create(WebViewFactoryStatic.class, null, true);
    }
    public static WebViewFactoryStatic get() {
        return BlackReflection.create(WebViewFactoryStatic.class, null, false);
    }
    public static WebViewFactoryContext getWithException(final Object caller) {
        return BlackReflection.create(WebViewFactoryContext.class, caller, true);
    }
    public static WebViewFactoryContext get(final Object caller) {
        return BlackReflection.create(WebViewFactoryContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(WebViewFactoryContext.class);
    }
}
