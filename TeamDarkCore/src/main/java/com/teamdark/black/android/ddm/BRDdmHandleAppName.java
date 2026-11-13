package com.teamdark.black.android.ddm;

import com.teamdark.blackreflection.BlackReflection;

public class BRDdmHandleAppName {
    public static DdmHandleAppNameStatic getWithException() {
        return BlackReflection.create(DdmHandleAppNameStatic.class, null, true);
    }
    public static DdmHandleAppNameStatic get() {
        return BlackReflection.create(DdmHandleAppNameStatic.class, null, false);
    }
    public static DdmHandleAppNameContext getWithException(final Object caller) {
        return BlackReflection.create(DdmHandleAppNameContext.class, caller, true);
    }
    public static DdmHandleAppNameContext get(final Object caller) {
        return BlackReflection.create(DdmHandleAppNameContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(DdmHandleAppNameContext.class);
    }
}
