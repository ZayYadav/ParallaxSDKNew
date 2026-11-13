package com.teamdark.black.android.location;

import android.util.ArrayMap;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.location.LocationManager")
public interface LocationManagerQ {
    @BField
    ArrayMap mGnssNmeaListeners();

    @BField
    ArrayMap mGnssStatusListeners();

    @BField
    ArrayMap mGpsNmeaListeners();

    @BField
    ArrayMap mGpsStatusListeners();

    @BField
    ArrayMap mListeners();
}
