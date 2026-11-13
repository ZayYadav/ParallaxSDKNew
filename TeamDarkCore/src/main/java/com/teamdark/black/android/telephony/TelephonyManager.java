package com.teamdark.black.android.telephony;

import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;
import com.teamdark.blackreflection.annotation.BStaticMethod;

/**
 * Created by TeamDarkCore on 2022/2/26.
 */
@BClassName("android.telephony.TelephonyManager")
public interface TelephonyManager {

    @BStaticMethod
    Object getSubscriberInfoService();

    @BStaticField
    boolean sServiceHandleCacheEnabled();

    @BStaticField
    IInterface sIPhoneSubInfo();
}
