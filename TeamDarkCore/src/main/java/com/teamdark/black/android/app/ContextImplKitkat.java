package com.teamdark.black.android.app;

import java.io.File;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.app.ContextImpl")
public interface ContextImplKitkat {
    @BField
    Object mDisplayAdjustments();

    @BField
    File[] mExternalCacheDirs();

    @BField
    File[] mExternalFilesDirs();

    @BField
    String mOpPackageName();
}
