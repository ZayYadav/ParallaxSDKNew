package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.ApplicationInfo")
public interface ApplicationInfoNContext {
    @BFieldSetNotProcess
    void _set_credentialEncryptedDataDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_credentialEncryptedDataDir();
    @BFieldNotProcess
    java.lang.String credentialEncryptedDataDir();
    @BFieldSetNotProcess
    void _set_credentialProtectedDataDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_credentialProtectedDataDir();
    @BFieldNotProcess
    java.lang.String credentialProtectedDataDir();
    @BFieldSetNotProcess
    void _set_deviceEncryptedDataDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_deviceEncryptedDataDir();
    @BFieldNotProcess
    java.lang.String deviceEncryptedDataDir();
    @BFieldSetNotProcess
    void _set_deviceProtectedDataDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_deviceProtectedDataDir();
    @BFieldNotProcess
    java.lang.String deviceProtectedDataDir();
}
