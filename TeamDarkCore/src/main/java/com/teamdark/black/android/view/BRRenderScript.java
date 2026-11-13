package com.teamdark.black.android.view;

import com.teamdark.blackreflection.BlackReflection;

public class BRRenderScript {
    public static RenderScriptStatic getWithException() {
        return BlackReflection.create(RenderScriptStatic.class, null, true);
    }
    public static RenderScriptStatic get() {
        return BlackReflection.create(RenderScriptStatic.class, null, false);
    }
    public static RenderScriptContext getWithException(final Object caller) {
        return BlackReflection.create(RenderScriptContext.class, caller, true);
    }
    public static RenderScriptContext get(final Object caller) {
        return BlackReflection.create(RenderScriptContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(RenderScriptContext.class);
    }
}
