package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRPackageParserSigningDetails {
    public static PackageParserSigningDetailsStatic getWithException() {
        return BlackReflection.create(PackageParserSigningDetailsStatic.class, null, true);
    }
    public static PackageParserSigningDetailsStatic get() {
        return BlackReflection.create(PackageParserSigningDetailsStatic.class, null, false);
    }
    public static PackageParserSigningDetailsContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserSigningDetailsContext.class, caller, true);
    }
    public static PackageParserSigningDetailsContext get(final Object caller) {
        return BlackReflection.create(PackageParserSigningDetailsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PackageParserSigningDetailsContext.class);
    }
}
