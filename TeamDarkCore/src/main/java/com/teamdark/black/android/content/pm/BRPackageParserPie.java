package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRPackageParserPie {
    public static PackageParserPieStatic getWithException() {
        return BlackReflection.create(PackageParserPieStatic.class, null, true);
    }
    public static PackageParserPieStatic get() {
        return BlackReflection.create(PackageParserPieStatic.class, null, false);
    }
    public static PackageParserPieContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserPieContext.class, caller, true);
    }
    public static PackageParserPieContext get(final Object caller) {
        return BlackReflection.create(PackageParserPieContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PackageParserPieContext.class);
    }
}
