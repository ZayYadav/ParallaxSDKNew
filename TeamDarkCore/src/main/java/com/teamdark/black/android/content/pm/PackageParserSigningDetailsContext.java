package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser$SigningDetails")
public interface PackageParserSigningDetailsContext {
    @BMethodCheckNotProcess
    Method _check_hasPastSigningCertificates();
    java.lang.Boolean hasPastSigningCertificates();
    @BMethodCheckNotProcess
    Method _check_hasSignatures();
    java.lang.Boolean hasSignatures();
    @BFieldSetNotProcess
    void _set_pastSigningCertificates(final Object value);
    @BFieldCheckNotProcess
    Field _check_pastSigningCertificates();
    @BFieldNotProcess
    android.content.pm.Signature[] pastSigningCertificates();
    @BFieldSetNotProcess
    void _set_signatures(final Object value);
    @BFieldCheckNotProcess
    Field _check_signatures();
    @BFieldNotProcess
    android.content.pm.Signature[] signatures();
}
