package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser")
public interface PackageParserTiramisuContext {
    @BMethodCheckNotProcess
    Method _check_parsePackage(java.io.File File0, int flags);
    android.content.pm.PackageParser.Package parsePackage(java.io.File File0, int flags);
}
