package com.teamdark.black.android.view;

import java.io.File;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.renderscript.RenderScript")
public interface RenderScript {
    @BStaticMethod
    void setupDiskCache(File File0);
}
