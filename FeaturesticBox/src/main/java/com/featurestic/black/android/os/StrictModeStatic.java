package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.StrictMode")
public interface StrictModeStatic {
    @BMethodCheckNotProcess
    Method _check_disableDeathOnFileUriExposure();
    java.lang.Void disableDeathOnFileUriExposure();
    @BFieldSetNotProcess
    void _set_DETECT_VM_FILE_URI_EXPOSURE(final Object value);
    @BFieldCheckNotProcess
    Field _check_DETECT_VM_FILE_URI_EXPOSURE();
    @BFieldNotProcess
    java.lang.Integer DETECT_VM_FILE_URI_EXPOSURE();
    @BFieldSetNotProcess
    void _set_PENALTY_DEATH_ON_FILE_URI_EXPOSURE(final Object value);
    @BFieldCheckNotProcess
    Field _check_PENALTY_DEATH_ON_FILE_URI_EXPOSURE();
    @BFieldNotProcess
    java.lang.Integer PENALTY_DEATH_ON_FILE_URI_EXPOSURE();
    @BFieldSetNotProcess
    void _set_sVmPolicyMask(final Object value);
    @BFieldCheckNotProcess
    Field _check_sVmPolicyMask();
    @BFieldNotProcess
    java.lang.Integer sVmPolicyMask();
}
