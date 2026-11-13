package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.GpsStatus")
public interface GpsStatusContext {
    @BMethodCheckNotProcess
    Method _check_setStatus(int int0, int[] ints1, float[] floats2, float[] floats3, float[] floats4, int int5, int int6, int int7);
    java.lang.Void setStatus(int int0, int[] ints1, float[] floats2, float[] floats3, float[] floats4, int int5, int int6, int int7);
}
