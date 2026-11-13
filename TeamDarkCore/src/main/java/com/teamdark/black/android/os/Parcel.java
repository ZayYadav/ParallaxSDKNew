package com.teamdark.black.android.os;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;

@BClassName("android.os.Parcel")
public interface Parcel {
    @BStaticField
    int VAL_PARCELABLE();

    @BStaticField
    int VAL_PARCELABLEARRAY();
}
