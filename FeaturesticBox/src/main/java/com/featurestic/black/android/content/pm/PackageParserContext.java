package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser")
public interface PackageParserContext {
    @BMethodCheckNotProcess
    Method _check_collectCertificates(android.content.pm.PackageParser.Package p, int flags);
    java.lang.Void collectCertificates(android.content.pm.PackageParser.Package p, int flags);
    @BMethodCheckNotProcess
    Method _check_parsePackage(java.io.File File0, java.lang.String String1, android.util.DisplayMetrics DisplayMetrics2, int int3);
    android.content.pm.PackageParser.Package parsePackage(java.io.File File0, java.lang.String String1, android.util.DisplayMetrics DisplayMetrics2, int int3);
}
