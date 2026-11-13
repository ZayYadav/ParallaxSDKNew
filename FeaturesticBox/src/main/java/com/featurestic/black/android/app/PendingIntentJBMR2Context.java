package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.PendingIntent")
public interface PendingIntentJBMR2Context {
    @BMethodCheckNotProcess
    Method _check_getIntent();
    android.content.Intent getIntent();
}
