package com.featurestic.black.android.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.NetworkInfo")
public interface NetworkInfoStatic {
    @BConstructorNotProcess
    com.featurestic.black.android.net.NetworkInfo _new(int type);
    @BConstructorNotProcess
    com.featurestic.black.android.net.NetworkInfo _new(int type, int subType, java.lang.String typeName, java.lang.String subTypeName);
}
