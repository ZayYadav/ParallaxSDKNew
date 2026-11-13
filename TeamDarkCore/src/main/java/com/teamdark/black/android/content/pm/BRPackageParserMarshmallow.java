package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRPackageParserMarshmallow {
    public static PackageParserMarshmallowStatic getWithException() {
        return BlackReflection.create(PackageParserMarshmallowStatic.class, null, true);
    }
    public static PackageParserMarshmallowStatic get() {
        return BlackReflection.create(PackageParserMarshmallowStatic.class, null, false);
    }
    public static PackageParserMarshmallowContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserMarshmallowContext.class, caller, true);
    }
    public static PackageParserMarshmallowContext get(final Object caller) {
        return BlackReflection.create(PackageParserMarshmallowContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PackageParserMarshmallowContext.class);
    }
}
