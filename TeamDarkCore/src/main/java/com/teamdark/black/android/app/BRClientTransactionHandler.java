package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRClientTransactionHandler {
    public static ClientTransactionHandlerStatic getWithException() {
        return BlackReflection.create(ClientTransactionHandlerStatic.class, null, true);
    }
    public static ClientTransactionHandlerStatic get() {
        return BlackReflection.create(ClientTransactionHandlerStatic.class, null, false);
    }
    public static ClientTransactionHandlerContext getWithException(final Object caller) {
        return BlackReflection.create(ClientTransactionHandlerContext.class, caller, true);
    }
    public static ClientTransactionHandlerContext get(final Object caller) {
        return BlackReflection.create(ClientTransactionHandlerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ClientTransactionHandlerContext.class);
    }
}
