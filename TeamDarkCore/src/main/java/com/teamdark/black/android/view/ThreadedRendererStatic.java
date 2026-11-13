package com.teamdark.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.ThreadedRenderer")
public interface ThreadedRendererStatic {
    @BMethodCheckNotProcess
    Method _check_setupDiskCache(java.io.File File0);
    java.lang.Void setupDiskCache(java.io.File File0);
}
