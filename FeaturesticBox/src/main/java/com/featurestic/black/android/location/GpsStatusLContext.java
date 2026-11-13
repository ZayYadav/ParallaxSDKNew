package com.featurestic.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.GpsStatus")
public interface GpsStatusLContext {
    @BMethodCheckNotProcess
    Method _check_setStatus(int int0, int[] ints1, float[] floats2, float[] floats3, float[] floats4, int[] ints5, int[] ints6, int[] ints7);
    java.lang.Void setStatus(int int0, int[] ints1, float[] floats2, float[] floats3, float[] floats4, int[] ints5, int[] ints6, int[] ints7);
}
