package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser")
public interface PackageParserLollipop22Context {
    @BMethodCheckNotProcess
    Method _check_collectCertificates(android.content.pm.PackageParser.Package p, int flags);
    java.lang.Void collectCertificates(android.content.pm.PackageParser.Package p, int flags);
    @BMethodCheckNotProcess
    Method _check_parsePackage(java.io.File File0, int int1);
    android.content.pm.PackageParser.Package parsePackage(java.io.File File0, int int1);
}
