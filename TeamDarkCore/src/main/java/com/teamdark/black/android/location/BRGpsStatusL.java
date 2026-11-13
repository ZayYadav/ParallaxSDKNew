package com.teamdark.black.android.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRGpsStatusL {
    public static GpsStatusLStatic getWithException() {
        return BlackReflection.create(GpsStatusLStatic.class, null, true);
    }
    public static GpsStatusLStatic get() {
        return BlackReflection.create(GpsStatusLStatic.class, null, false);
    }
    public static GpsStatusLContext getWithException(final Object caller) {
        return BlackReflection.create(GpsStatusLContext.class, caller, true);
    }
    public static GpsStatusLContext get(final Object caller) {
        return BlackReflection.create(GpsStatusLContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(GpsStatusLContext.class);
    }
}
