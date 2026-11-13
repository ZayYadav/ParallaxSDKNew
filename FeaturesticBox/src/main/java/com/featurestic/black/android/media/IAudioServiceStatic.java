package com.featurestic.black.android.media;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.IAudioService")
public interface IAudioServiceStatic {
    @BMethodCheckNotProcess
    Method _check_setMicrophoneMute(boolean mute, java.lang.String callingPackage, int userId);
    java.lang.Void setMicrophoneMute(boolean mute, java.lang.String callingPackage, int userId);
    @BMethodCheckNotProcess
    Method _check_isMicrophoneMute();
    java.lang.Boolean isMicrophoneMute();
    @BMethodCheckNotProcess
    Method _check_startBluetoothSco(java.lang.String callingPackage);
    java.lang.Void startBluetoothSco(java.lang.String callingPackage);
    @BMethodCheckNotProcess
    Method _check_stopBluetoothSco(java.lang.String callingPackage);
    java.lang.Void stopBluetoothSco(java.lang.String callingPackage);
}
