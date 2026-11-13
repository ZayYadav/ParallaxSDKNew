package com.teamdark.black.android.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.NetworkInfo")
public interface NetworkInfoStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.net.NetworkInfo _new(int type);
    @BConstructorNotProcess
    com.teamdark.black.android.net.NetworkInfo _new(int type, int subType, java.lang.String typeName, java.lang.String subTypeName);
}
