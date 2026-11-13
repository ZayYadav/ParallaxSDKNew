package com.teamdark.black.android.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRLocationManagerGnssStatusListenerTransport {
    public static LocationManagerGnssStatusListenerTransportStatic getWithException() {
        return BlackReflection.create(LocationManagerGnssStatusListenerTransportStatic.class, null, true);
    }
    public static LocationManagerGnssStatusListenerTransportStatic get() {
        return BlackReflection.create(LocationManagerGnssStatusListenerTransportStatic.class, null, false);
    }
    public static LocationManagerGnssStatusListenerTransportContext getWithException(final Object caller) {
        return BlackReflection.create(LocationManagerGnssStatusListenerTransportContext.class, caller, true);
    }
    public static LocationManagerGnssStatusListenerTransportContext get(final Object caller) {
        return BlackReflection.create(LocationManagerGnssStatusListenerTransportContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LocationManagerGnssStatusListenerTransportContext.class);
    }
}
