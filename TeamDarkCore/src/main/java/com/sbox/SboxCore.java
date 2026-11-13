package com.sbox;

import androidx.annotation.Keep;
import com.teamdark.core.TeamDarkCoreEngine;

@Keep
public class SboxCore extends TeamDarkCoreEngine {
    private static final SboxCore sInstance = new SboxCore();
    public static SboxCore get() {
        return sInstance;
    }
}
