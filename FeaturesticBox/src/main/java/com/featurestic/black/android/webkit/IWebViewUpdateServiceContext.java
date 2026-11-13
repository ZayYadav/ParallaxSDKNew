package com.featurestic.black.android.webkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.webkit.IWebViewUpdateService")
public interface IWebViewUpdateServiceContext {
    @BMethodCheckNotProcess
    Method _check_getCurrentWebViewPackageName();
    java.lang.String getCurrentWebViewPackageName();
    @BMethodCheckNotProcess
    Method _check_waitForAndGetProvider();
    java.lang.Object waitForAndGetProvider();
}
