package com.featurestic.black.android.content.pm;

import com.featurestic.blackreflection.BlackReflection;

public class BRPackageParserService {
    public static PackageParserServiceStatic getWithException() {
        return BlackReflection.create(PackageParserServiceStatic.class, null, true);
    }
    public static PackageParserServiceStatic get() {
        return BlackReflection.create(PackageParserServiceStatic.class, null, false);
    }
    public static PackageParserServiceContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserServiceContext.class, caller, true);
    }
    public static PackageParserServiceContext get(final Object caller) {
        return BlackReflection.create(PackageParserServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(PackageParserServiceContext.class);
    }
}
