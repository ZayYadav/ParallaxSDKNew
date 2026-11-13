package com.sbox;

import androidx.annotation.Keep;
import com.featurestic.box.FeaturesticBoxCore;

@Keep
public class SboxCore extends FeaturesticBoxCore {
    private static final SboxCore sInstance = new SboxCore();
    public static SboxCore get() {
        return sInstance;
    }
}
