package com.teamdark.black.java.io;

import com.teamdark.blackreflection.BlackReflection;

public class BRFile {
    public static FileStatic getWithException() {
        return BlackReflection.create(FileStatic.class, null, true);
    }
    public static FileStatic get() {
        return BlackReflection.create(FileStatic.class, null, false);
    }
    public static FileContext getWithException(final Object caller) {
        return BlackReflection.create(FileContext.class, caller, true);
    }
    public static FileContext get(final Object caller) {
        return BlackReflection.create(FileContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(FileContext.class);
    }
}
