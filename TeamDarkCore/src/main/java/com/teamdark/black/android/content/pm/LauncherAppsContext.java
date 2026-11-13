package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.LauncherApps")
public interface LauncherAppsContext {
    @BFieldSetNotProcess
    void _set_mPm(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPm();
    @BFieldNotProcess
    android.content.pm.PackageManager mPm();
    @BFieldSetNotProcess
    void _set_mService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mService();
    @BFieldNotProcess
    android.os.IInterface mService();
    @BFieldSetNotProcess
    void _set_mUserManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_mUserManager();
    @BFieldNotProcess
    android.os.UserManager mUserManager();
}
