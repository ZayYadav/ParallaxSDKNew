package com.featurestic.black.android.app;


import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BField;

@BClassName("android.app.LoadedApk")
public interface LoadedApkICS {
    @BField
    Object mCompatibilityInfo();
}
