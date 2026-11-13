package com.teamdark.black.android.webkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.webkit.WebViewFactory")
public interface WebViewFactoryStatic {
    @BMethodCheckNotProcess
    Method _check_getUpdateService();
    java.lang.Object getUpdateService();
    @BFieldSetNotProcess
    void _set_sWebViewSupported(final Object value);
    @BFieldCheckNotProcess
    Field _check_sWebViewSupported();
    @BFieldNotProcess
    java.lang.Boolean sWebViewSupported();
}
