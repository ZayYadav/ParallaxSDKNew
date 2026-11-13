package com.featurestic.black.android.security.net.config;

import android.content.Context;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.security.net.config.NetworkSecurityConfigProvider")
public interface NetworkSecurityConfigProvider {
    @BStaticMethod
    void install(Context Context0);
}
