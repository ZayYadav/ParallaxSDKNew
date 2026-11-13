package com.teamdark.black.android.content.pm;


import android.content.pm.PackageParser;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.content.pm.PackageParser")
public interface PackageParserPie {
    @BStaticMethod
    void collectCertificates(PackageParser.Package p, boolean skipVerify);
}
