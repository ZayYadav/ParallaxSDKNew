package com.featurestic.black.android.media;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.IAudioService$Stub")
public interface IAudioServiceStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder binder);
    android.os.IInterface asInterface(android.os.IBinder binder);
}
