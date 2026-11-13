package com.teamdark.black.android.content;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

/**
 * Created by TeamDarkCore on 2022/2/20.
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
