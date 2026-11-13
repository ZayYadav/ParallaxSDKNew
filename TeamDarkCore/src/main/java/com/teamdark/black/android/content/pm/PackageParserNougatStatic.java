package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser")
public interface PackageParserNougatStatic {
    @BMethodCheckNotProcess
    Method _check_collectCertificates(android.content.pm.PackageParser.Package p, int flags);
    java.lang.Void collectCertificates(android.content.pm.PackageParser.Package p, int flags);
}
