package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRPackageParserComponent {
    public static PackageParserComponentStatic getWithException() {
        return BlackReflection.create(PackageParserComponentStatic.class, null, true);
    }
    public static PackageParserComponentStatic get() {
        return BlackReflection.create(PackageParserComponentStatic.class, null, false);
    }
    public static PackageParserComponentContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserComponentContext.class, caller, true);
    }
    public static PackageParserComponentContext get(final Object caller) {
        return BlackReflection.create(PackageParserComponentContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(PackageParserComponentContext.class);
    }
}
