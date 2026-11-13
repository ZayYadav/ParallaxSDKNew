package com.featurestic.black.android.telephony;


import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BConstructor;
import com.featurestic.blackreflection.annotation.BField;

@BClassName("android.telephony.CellIdentityGsm")
public interface CellIdentityGsm {
    @BConstructor
    CellIdentityGsm _new();

    @BField
    int mCid();

    @BField
    int mLac();

    @BField
    int mMcc();

    @BField
    int mMnc();
}
