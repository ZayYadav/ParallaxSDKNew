package com.teamdark.black.android.content.pm;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.content.pm.ApplicationInfo")
public interface ApplicationInfoL {
    @BField
    String primaryCpuAbi();

    @BField
    Integer privateFlags();

    @BField
    String scanPublicSourceDir();

    @BField
    String scanSourceDir();

    @BField
    String secondaryCpuAbi();

    @BField
    String secondaryNativeLibraryDir();

    @BField
    String[] splitPublicSourceDirs();

    @BField
    String[] splitSourceDirs();
}
