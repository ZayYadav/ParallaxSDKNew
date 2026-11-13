package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.IClipboard$Stub")
public interface IClipboardStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder iBinder);
    android.os.IInterface asInterface(android.os.IBinder iBinder);
}
