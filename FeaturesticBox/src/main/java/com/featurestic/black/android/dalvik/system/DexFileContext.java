package com.featurestic.black.android.dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("dalvik.system.DexFile")
public interface DexFileContext {
    @BMethodCheckNotProcess
    Method _check_openDexFileNative(java.lang.String sourceName, java.lang.String outputName, int flags);
    java.lang.Object openDexFileNative(java.lang.String sourceName, java.lang.String outputName, int flags);
    @BMethodCheckNotProcess
    Method _check_openDexFile(java.lang.String sourceName, java.lang.String outputName, int flags);
    java.lang.Object openDexFile(java.lang.String sourceName, java.lang.String outputName, int flags);
    @BMethodCheckNotProcess
    Method _check_loadDex(java.lang.String sourceName, java.lang.String outputName, int flags);
    java.lang.Object loadDex(java.lang.String sourceName, java.lang.String outputName, int flags);
}
