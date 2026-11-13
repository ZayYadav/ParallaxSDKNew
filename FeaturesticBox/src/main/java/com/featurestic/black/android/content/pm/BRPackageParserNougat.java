package com.featurestic.black.android.content.pm;

import com.featurestic.blackreflection.BlackReflection;

public class BRPackageParserNougat {
    public static PackageParserNougatStatic getWithException() {
        return BlackReflection.create(PackageParserNougatStatic.class, null, true);
    }
    public static PackageParserNougatStatic get() {
        return BlackReflection.create(PackageParserNougatStatic.class, null, false);
    }
    public static PackageParserNougatContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserNougatContext.class, caller, true);
    }
    public static PackageParserNougatContext get(final Object caller) {
        return BlackReflection.create(PackageParserNougatContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(PackageParserNougatContext.class);
    }
}
