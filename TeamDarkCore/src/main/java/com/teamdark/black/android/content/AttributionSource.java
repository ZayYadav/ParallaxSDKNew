package com.teamdark.black.android.content;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BConstructor;
import com.teamdark.blackreflection.annotation.BField;
import com.teamdark.blackreflection.annotation.BMethod;

/**
 * Created by TeamDarkCore on 2022/2/20.
 */
@BClassName("android.content.AttributionSource")
public interface AttributionSource {
    @BConstructor
    Object _new(int i,int i2,String str,String str2);

    @BConstructor
    Object _new(int i,String str,String str2);

    @BField
    Object mAttributionSourceState();

    @BMethod
    Object getNext();
}
