package com.featurestic.black.android.nfc;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.nfc.INfcAdapter")
public interface INfcAdapter {
    @BClassName("android.nfc.INfcAdapter$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}
