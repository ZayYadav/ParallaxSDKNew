package com.teamdark.black.com.android.internal.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.content.NativeLibraryHelper$Handle")
public interface NativeLibraryHelperHandleStatic {
    @BMethodCheckNotProcess
    Method _check_create(java.io.File File0);
    java.lang.Object create(java.io.File File0);
}
