package com.featurestic.black.android.ddm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.ddm.DdmHandleAppName")
public interface DdmHandleAppNameStatic {
    @BMethodCheckNotProcess
    Method _check_setAppName(java.lang.String String0, int i);
    java.lang.Void setAppName(java.lang.String String0, int i);
}
