package com.featurestic.black.android.security.net.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.security.net.config.NetworkSecurityConfigProvider")
public interface NetworkSecurityConfigProviderStatic {
    @BMethodCheckNotProcess
    Method _check_install(android.content.Context Context0);
    java.lang.Void install(android.content.Context Context0);
}
