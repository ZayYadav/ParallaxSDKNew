package com.teamdark.black.android.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRLocationManagerGpsStatusListenerTransport {
    public static LocationManagerGpsStatusListenerTransportStatic getWithException() {
        return BlackReflection.create(LocationManagerGpsStatusListenerTransportStatic.class, null, true);
    }
    public static LocationManagerGpsStatusListenerTransportStatic get() {
        return BlackReflection.create(LocationManagerGpsStatusListenerTransportStatic.class, null, false);
    }
    public static LocationManagerGpsStatusListenerTransportContext getWithException(final Object caller) {
        return BlackReflection.create(LocationManagerGpsStatusListenerTransportContext.class, caller, true);
    }
    public static LocationManagerGpsStatusListenerTransportContext get(final Object caller) {
        return BlackReflection.create(LocationManagerGpsStatusListenerTransportContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LocationManagerGpsStatusListenerTransportContext.class);
    }
}
