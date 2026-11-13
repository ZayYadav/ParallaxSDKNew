package com.featurestic.black.android.credentials;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.credentials.ICredentialManager$Stub")
public interface ICredentialManagerStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder binder);
    android.os.IInterface asInterface(android.os.IBinder binder);
}
