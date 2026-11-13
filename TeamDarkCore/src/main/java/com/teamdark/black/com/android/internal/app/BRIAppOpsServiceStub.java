package com.teamdark.black.com.android.internal.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRIAppOpsServiceStub {
    public static IAppOpsServiceStubStatic getWithException() {
        return BlackReflection.create(IAppOpsServiceStubStatic.class, null, true);
    }
    public static IAppOpsServiceStubStatic get() {
        return BlackReflection.create(IAppOpsServiceStubStatic.class, null, false);
    }
    public static IAppOpsServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(IAppOpsServiceStubContext.class, caller, true);
    }
    public static IAppOpsServiceStubContext get(final Object caller) {
        return BlackReflection.create(IAppOpsServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IAppOpsServiceStubContext.class);
    }
}
