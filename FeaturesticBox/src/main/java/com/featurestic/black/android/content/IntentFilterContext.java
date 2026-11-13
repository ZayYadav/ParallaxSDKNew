package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.IntentFilter")
public interface IntentFilterContext {
    @BFieldSetNotProcess
    void _set_mActions(final Object value);
    @BFieldCheckNotProcess
    Field _check_mActions();
    @BFieldNotProcess
    java.util.List<java.lang.String> mActions();
    @BFieldSetNotProcess
    void _set_mCategories(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCategories();
    @BFieldNotProcess
    java.util.List<java.lang.String> mCategories();
}
