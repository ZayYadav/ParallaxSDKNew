package com.teamdark.black.android.os.System;

import com.teamdark.blackreflection.BlackReflection;

public class BRISystemUpdateStub {
    public static ISystemUpdateStubStatic getWithException() {
        return BlackReflection.create(ISystemUpdateStubStatic.class, null, true);
    }
    public static ISystemUpdateStubStatic get() {
        return BlackReflection.create(ISystemUpdateStubStatic.class, null, false);
    }
    public static ISystemUpdateStubContext getWithException(final Object caller) {
        return BlackReflection.create(ISystemUpdateStubContext.class, caller, true);
    }
    public static ISystemUpdateStubContext get(final Object caller) {
        return BlackReflection.create(ISystemUpdateStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ISystemUpdateStubContext.class);
    }
}
