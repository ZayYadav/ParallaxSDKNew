package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageInstaller$SessionInfo")
public interface PackageInstallerSessionInfoStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.content.pm.PackageInstaller.SessionInfo _new();
}
