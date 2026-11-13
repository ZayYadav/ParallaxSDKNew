package com.teamdark.black.android.rms;

import com.teamdark.blackreflection.BlackReflection;

public class BRHwSysResImplP {
    public static HwSysResImplPStatic getWithException() {
        return BlackReflection.create(HwSysResImplPStatic.class, null, true);
    }
    public static HwSysResImplPStatic get() {
        return BlackReflection.create(HwSysResImplPStatic.class, null, false);
    }
    public static HwSysResImplPContext getWithException(final Object caller) {
        return BlackReflection.create(HwSysResImplPContext.class, caller, true);
    }
    public static HwSysResImplPContext get(final Object caller) {
        return BlackReflection.create(HwSysResImplPContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(HwSysResImplPContext.class);
    }
}
