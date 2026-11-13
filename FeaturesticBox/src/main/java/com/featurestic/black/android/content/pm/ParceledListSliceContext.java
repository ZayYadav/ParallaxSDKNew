package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.ParceledListSlice")
public interface ParceledListSliceContext {
    @BMethodCheckNotProcess
    Method _check_append(java.lang.Object item);
    java.lang.Boolean append(java.lang.Object item);
    @BMethodCheckNotProcess
    Method _check_getList();
    java.util.List<?> getList();
    @BMethodCheckNotProcess
    Method _check_isLastSlice();
    java.lang.Boolean isLastSlice();
    @BMethodCheckNotProcess
    Method _check_populateList();
    android.os.Parcelable populateList();
    @BMethodCheckNotProcess
    Method _check_setLastSlice(boolean b);
    java.lang.Void setLastSlice(boolean b);
}
