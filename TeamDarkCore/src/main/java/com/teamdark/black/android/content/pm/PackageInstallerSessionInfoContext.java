package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageInstaller$SessionInfo")
public interface PackageInstallerSessionInfoContext {
    @BFieldSetNotProcess
    void _set_active(final Object value);
    @BFieldCheckNotProcess
    Field _check_active();
    @BFieldNotProcess
    java.lang.Boolean active();
    @BFieldSetNotProcess
    void _set_appIcon(final Object value);
    @BFieldCheckNotProcess
    Field _check_appIcon();
    @BFieldNotProcess
    android.graphics.Bitmap appIcon();
    @BFieldSetNotProcess
    void _set_appLabel(final Object value);
    @BFieldCheckNotProcess
    Field _check_appLabel();
    @BFieldNotProcess
    java.lang.CharSequence appLabel();
    @BFieldSetNotProcess
    void _set_appPackageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_appPackageName();
    @BFieldNotProcess
    java.lang.String appPackageName();
    @BFieldSetNotProcess
    void _set_installerPackageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_installerPackageName();
    @BFieldNotProcess
    java.lang.String installerPackageName();
    @BFieldSetNotProcess
    void _set_mode(final Object value);
    @BFieldCheckNotProcess
    Field _check_mode();
    @BFieldNotProcess
    java.lang.Integer mode();
    @BFieldSetNotProcess
    void _set_progress(final Object value);
    @BFieldCheckNotProcess
    Field _check_progress();
    @BFieldNotProcess
    java.lang.Float progress();
    @BFieldSetNotProcess
    void _set_resolvedBaseCodePath(final Object value);
    @BFieldCheckNotProcess
    Field _check_resolvedBaseCodePath();
    @BFieldNotProcess
    java.lang.String resolvedBaseCodePath();
    @BFieldSetNotProcess
    void _set_sealed(final Object value);
    @BFieldCheckNotProcess
    Field _check_sealed();
    @BFieldNotProcess
    java.lang.Boolean sealed();
    @BFieldSetNotProcess
    void _set_sessionId(final Object value);
    @BFieldCheckNotProcess
    Field _check_sessionId();
    @BFieldNotProcess
    java.lang.Integer sessionId();
    @BFieldSetNotProcess
    void _set_sizeBytes(final Object value);
    @BFieldCheckNotProcess
    Field _check_sizeBytes();
    @BFieldNotProcess
    java.lang.Long sizeBytes();
}
