package com.featurestic.black.android.dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("dalvik.system.DexFile")
public interface DexFileStatic {
    @BFieldSetNotProcess
    void _set_mCookie(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCookie();
    @BFieldNotProcess
    java.lang.Object mCookie();
}
