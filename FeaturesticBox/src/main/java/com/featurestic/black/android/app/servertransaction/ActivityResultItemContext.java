package com.featurestic.black.android.app.servertransaction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.servertransaction.ActivityResultItem")
public interface ActivityResultItemContext {
    @BFieldSetNotProcess
    void _set_mResultInfoList(final Object value);
    @BFieldCheckNotProcess
    Field _check_mResultInfoList();
    @BFieldNotProcess
    java.util.List mResultInfoList();
}
