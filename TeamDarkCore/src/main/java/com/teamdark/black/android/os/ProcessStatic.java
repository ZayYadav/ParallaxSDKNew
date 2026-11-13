package com.teamdark.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.Process")
public interface ProcessStatic {
    @BMethodCheckNotProcess
    Method _check_setArgV0(java.lang.String String0);
    java.lang.Void setArgV0(java.lang.String String0);
}
