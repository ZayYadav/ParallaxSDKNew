package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRPackageInstallerSessionParamsMarshmallow {
    public static PackageInstallerSessionParamsMarshmallowStatic getWithException() {
        return BlackReflection.create(PackageInstallerSessionParamsMarshmallowStatic.class, null, true);
    }
    public static PackageInstallerSessionParamsMarshmallowStatic get() {
        return BlackReflection.create(PackageInstallerSessionParamsMarshmallowStatic.class, null, false);
    }
    public static PackageInstallerSessionParamsMarshmallowContext getWithException(final Object caller) {
        return BlackReflection.create(PackageInstallerSessionParamsMarshmallowContext.class, caller, true);
    }
    public static PackageInstallerSessionParamsMarshmallowContext get(final Object caller) {
        return BlackReflection.create(PackageInstallerSessionParamsMarshmallowContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PackageInstallerSessionParamsMarshmallowContext.class);
    }
}
