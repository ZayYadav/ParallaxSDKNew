package com.featurestic.black.android.content;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BField;

/**
 * Created by FeaturesticBox on 2022/2/20.
 */
@BClassName("android.content.AttributionSourceState")
public interface AttributionSourceState {
    @BField
    String packageName();

    @BField
    int pid();

    @BField
    int uid();
}
