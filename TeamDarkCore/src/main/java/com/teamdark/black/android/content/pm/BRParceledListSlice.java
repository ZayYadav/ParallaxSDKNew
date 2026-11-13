package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRParceledListSlice {
    public static ParceledListSliceStatic getWithException() {
        return BlackReflection.create(ParceledListSliceStatic.class, null, true);
    }
    public static ParceledListSliceStatic get() {
        return BlackReflection.create(ParceledListSliceStatic.class, null, false);
    }
    public static ParceledListSliceContext getWithException(final Object caller) {
        return BlackReflection.create(ParceledListSliceContext.class, caller, true);
    }
    public static ParceledListSliceContext get(final Object caller) {
        return BlackReflection.create(ParceledListSliceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ParceledListSliceContext.class);
    }
}
