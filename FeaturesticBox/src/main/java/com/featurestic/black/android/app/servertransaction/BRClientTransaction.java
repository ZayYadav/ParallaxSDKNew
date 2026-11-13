package com.featurestic.black.android.app.servertransaction;

import com.featurestic.blackreflection.BlackReflection;

public class BRClientTransaction {
    public static ClientTransactionStatic getWithException() {
        return BlackReflection.create(ClientTransactionStatic.class, null, true);
    }
    public static ClientTransactionStatic get() {
        return BlackReflection.create(ClientTransactionStatic.class, null, false);
    }
    public static ClientTransactionContext getWithException(final Object caller) {
        return BlackReflection.create(ClientTransactionContext.class, caller, true);
    }
    public static ClientTransactionContext get(final Object caller) {
        return BlackReflection.create(ClientTransactionContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ClientTransactionContext.class);
    }
}
