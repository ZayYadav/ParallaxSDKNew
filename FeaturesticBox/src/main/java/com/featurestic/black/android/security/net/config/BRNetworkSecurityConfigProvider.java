package com.featurestic.black.android.security.net.config;

import com.featurestic.blackreflection.BlackReflection;

public class BRNetworkSecurityConfigProvider {
    public static NetworkSecurityConfigProviderStatic getWithException() {
        return BlackReflection.create(NetworkSecurityConfigProviderStatic.class, null, true);
    }
    public static NetworkSecurityConfigProviderStatic get() {
        return BlackReflection.create(NetworkSecurityConfigProviderStatic.class, null, false);
    }
    public static NetworkSecurityConfigProviderContext getWithException(final Object caller) {
        return BlackReflection.create(NetworkSecurityConfigProviderContext.class, caller, true);
    }
    public static NetworkSecurityConfigProviderContext get(final Object caller) {
        return BlackReflection.create(NetworkSecurityConfigProviderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(NetworkSecurityConfigProviderContext.class);
    }
}
