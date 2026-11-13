package com.featurestic.black.android.telephony;

import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticField;
import com.featurestic.blackreflection.annotation.BStaticMethod;

/**
 * Created by FeaturesticBox on 2022/2/26.
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
