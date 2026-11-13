package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.SigningInfo")
public interface SigningInfoContext {
    @BFieldSetNotProcess
    void _set_mSigningDetails(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSigningDetails();
    @BFieldNotProcess
    android.content.pm.PackageParser.SigningDetails mSigningDetails();
}
