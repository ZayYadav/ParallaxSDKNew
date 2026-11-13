package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Instrumentation")
public interface InstrumentationContext {
    @BMethodCheckNotProcess
    Method _check_execStartActivity(android.content.Context Context0, android.os.IBinder IBinder1, android.os.IBinder IBinder2, com.featurestic.black.android.app.Activity Activity3, android.content.Intent Intent4, int int5, android.os.Bundle Bundle6);
    android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context Context0, android.os.IBinder IBinder1, android.os.IBinder IBinder2, com.featurestic.black.android.app.Activity Activity3, android.content.Intent Intent4, int int5, android.os.Bundle Bundle6);
}
