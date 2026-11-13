package com.featurestic.black.android.media.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.session.ISessionManager")
public interface ISessionManagerStatic {
    @BMethodCheckNotProcess
    Method _check_getService(android.content.Context context);
    android.os.IInterface getService(android.content.Context context);
}
