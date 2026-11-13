package com.featurestic.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.SurfaceControl")
public interface SurfaceControlStatic {
    @BMethodCheckNotProcess
    Method _check_screnshot(int int0, int int1);
    android.graphics.Bitmap screnshot(int int0, int int1);
}
