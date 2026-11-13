package com.featurestic.black.android.content;

import android.os.IBinder;
import android.os.IInterface;
import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.content.IClipboard")
public interface IClipboard {

    @BClassName("android.content.IClipboard$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder iBinder);
    }
}
