package com.featurestic.black.android.content.pm;

import com.featurestic.blackreflection.BlackReflection;

public class BRPackageParserPermission {
    public static PackageParserPermissionStatic getWithException() {
        return BlackReflection.create(PackageParserPermissionStatic.class, null, true);
    }
    public static PackageParserPermissionStatic get() {
        return BlackReflection.create(PackageParserPermissionStatic.class, null, false);
    }
    public static PackageParserPermissionContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserPermissionContext.class, caller, true);
    }
    public static PackageParserPermissionContext get(final Object caller) {
        return BlackReflection.create(PackageParserPermissionContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(PackageParserPermissionContext.class);
    }
}
