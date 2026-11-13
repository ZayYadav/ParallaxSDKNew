package com.featurestic.black.android.content.pm;

import com.featurestic.blackreflection.BlackReflection;

public class BRPackageParserProvider {
    public static PackageParserProviderStatic getWithException() {
        return BlackReflection.create(PackageParserProviderStatic.class, null, true);
    }
    public static PackageParserProviderStatic get() {
        return BlackReflection.create(PackageParserProviderStatic.class, null, false);
    }
    public static PackageParserProviderContext getWithException(final Object caller) {
        return BlackReflection.create(PackageParserProviderContext.class, caller, true);
    }
    public static PackageParserProviderContext get(final Object caller) {
        return BlackReflection.create(PackageParserProviderContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(PackageParserProviderContext.class);
    }
}
