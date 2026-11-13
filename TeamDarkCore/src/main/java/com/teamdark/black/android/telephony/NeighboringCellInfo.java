package com.teamdark.black.android.telephony;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.telephony.NeighboringCellInfo")
public interface NeighboringCellInfo {
    @BField
    int mCid();

    @BField
    int mLac();

    @BField
    int mRssi();
}
