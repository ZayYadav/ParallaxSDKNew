package com.teamdark.black.android.app;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.app.LoadedApk")
public interface LoadedApkICS {
    @BField
    Object mCompatibilityInfo();
}
