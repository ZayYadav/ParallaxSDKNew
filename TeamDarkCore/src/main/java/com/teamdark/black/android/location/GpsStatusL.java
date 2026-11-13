package com.teamdark.black.android.location;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BMethod;

@BClassName("android.location.GpsStatus")
public interface GpsStatusL {
    @BMethod
    void setStatus(int int0, int[] ints1, float[] floats2, float[] floats3, float[] floats4, int[] ints5, int[] ints6, int[] ints7);
}
