package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.ApplicationInfo")
public interface ApplicationInfoLContext {
    @BFieldSetNotProcess
    void _set_primaryCpuAbi(final Object value);
    @BFieldCheckNotProcess
    Field _check_primaryCpuAbi();
    @BFieldNotProcess
    java.lang.String primaryCpuAbi();
    @BFieldSetNotProcess
    void _set_privateFlags(final Object value);
    @BFieldCheckNotProcess
    Field _check_privateFlags();
    @BFieldNotProcess
    java.lang.Integer privateFlags();
    @BFieldSetNotProcess
    void _set_scanPublicSourceDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_scanPublicSourceDir();
    @BFieldNotProcess
    java.lang.String scanPublicSourceDir();
    @BFieldSetNotProcess
    void _set_scanSourceDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_scanSourceDir();
    @BFieldNotProcess
    java.lang.String scanSourceDir();
    @BFieldSetNotProcess
    void _set_secondaryCpuAbi(final Object value);
    @BFieldCheckNotProcess
    Field _check_secondaryCpuAbi();
    @BFieldNotProcess
    java.lang.String secondaryCpuAbi();
    @BFieldSetNotProcess
    void _set_secondaryNativeLibraryDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_secondaryNativeLibraryDir();
    @BFieldNotProcess
    java.lang.String secondaryNativeLibraryDir();
    @BFieldSetNotProcess
    void _set_splitPublicSourceDirs(final Object value);
    @BFieldCheckNotProcess
    Field _check_splitPublicSourceDirs();
    @BFieldNotProcess
    java.lang.String[] splitPublicSourceDirs();
    @BFieldSetNotProcess
    void _set_splitSourceDirs(final Object value);
    @BFieldCheckNotProcess
    Field _check_splitSourceDirs();
    @BFieldNotProcess
    java.lang.String[] splitSourceDirs();
}
