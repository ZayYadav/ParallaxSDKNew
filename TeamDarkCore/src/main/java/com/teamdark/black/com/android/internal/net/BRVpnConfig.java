package com.teamdark.black.com.android.internal.net;

import com.teamdark.blackreflection.BlackReflection;

public class BRVpnConfig {
    public static VpnConfigStatic getWithException() {
        return BlackReflection.create(VpnConfigStatic.class, null, true);
    }
    public static VpnConfigStatic get() {
        return BlackReflection.create(VpnConfigStatic.class, null, false);
    }
    public static VpnConfigContext getWithException(final Object caller) {
        return BlackReflection.create(VpnConfigContext.class, caller, true);
    }
    public static VpnConfigContext get(final Object caller) {
        return BlackReflection.create(VpnConfigContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(VpnConfigContext.class);
    }
}
