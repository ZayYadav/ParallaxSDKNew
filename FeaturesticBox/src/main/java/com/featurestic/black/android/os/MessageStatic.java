package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.Message")
public interface MessageStatic {
    @BMethodCheckNotProcess
    Method _check_updateCheckRecycle(int int0);
    java.lang.Void updateCheckRecycle(int int0);
}
