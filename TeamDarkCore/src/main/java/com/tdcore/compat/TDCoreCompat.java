package com.tdcore.compat;

import androidx.annotation.Keep;
import com.teamdark.core.TeamDarkCoreEngine;

@Keep
public class TDCoreCompat extends TeamDarkCoreEngine {
    private static final TDCoreCompat sInstance = new TDCoreCompat();
    public static TDCoreCompat get() {
        return sInstance;
    }
}
