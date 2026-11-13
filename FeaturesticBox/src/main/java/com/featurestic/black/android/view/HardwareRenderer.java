package com.featurestic.black.android.view;

import java.io.File;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.view.HardwareRenderer")
public interface HardwareRenderer {
    @BStaticMethod
    void setupDiskCache(File File0);
}
