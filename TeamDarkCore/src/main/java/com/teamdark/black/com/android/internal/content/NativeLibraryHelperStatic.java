package com.teamdark.black.com.android.internal.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.content.NativeLibraryHelper")
public interface NativeLibraryHelperStatic {
    @BMethodCheckNotProcess
    Method _check_copyNativeBinaries(com.teamdark.black.com.android.internal.content.NativeLibraryHelper.Handle Handle0, java.io.File File1, java.lang.String String2);
    java.lang.Integer copyNativeBinaries(com.teamdark.black.com.android.internal.content.NativeLibraryHelper.Handle Handle0, java.io.File File1, java.lang.String String2);
    @BMethodCheckNotProcess
    Method _check_findSupportedAbi(com.teamdark.black.com.android.internal.content.NativeLibraryHelper.Handle Handle0, @BParamClassName("[Ljava.lang.String;") java.lang.String[] strings);
    java.lang.Integer findSupportedAbi(com.teamdark.black.com.android.internal.content.NativeLibraryHelper.Handle Handle0, @BParamClassName("[Ljava.lang.String;") java.lang.String[] strings);
}
