package com.teamdark.black.android.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRINetworkManagementService {
    public static INetworkManagementServiceStatic getWithException() {
        return BlackReflection.create(INetworkManagementServiceStatic.class, null, true);
    }
    public static INetworkManagementServiceStatic get() {
        return BlackReflection.create(INetworkManagementServiceStatic.class, null, false);
    }
    public static INetworkManagementServiceContext getWithException(final Object caller) {
        return BlackReflection.create(INetworkManagementServiceContext.class, caller, true);
    }
    public static INetworkManagementServiceContext get(final Object caller) {
        return BlackReflection.create(INetworkManagementServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(INetworkManagementServiceContext.class);
    }
}
