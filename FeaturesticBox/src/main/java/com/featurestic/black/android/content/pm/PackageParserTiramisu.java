package com.featurestic.black.android.content.pm;

import android.content.pm.PackageParser;

import java.io.File;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BConstructor;
import com.featurestic.blackreflection.annotation.BMethod;

/**
 * @author gm
 * @function
 * @date :2024/4/24 21:19
 **/
@BClassName("android.content.pm.PackageParser")
public interface PackageParserTiramisu {

    @BConstructor
    android.content.pm.PackageParser _new();

    @BMethod
    PackageParser.Package parsePackage(File File0, int flags);

}
