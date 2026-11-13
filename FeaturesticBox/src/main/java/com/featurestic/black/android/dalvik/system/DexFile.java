package com.featurestic.black.android.dalvik.system;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BMethod;
import com.featurestic.blackreflection.annotation.BStaticField;

@BClassName("dalvik.system.DexFile")
public interface DexFile {

    @BMethod
    Object openDexFileNative(String sourceName, String outputName, int flags);

    @BMethod
    Object openDexFile(String sourceName, String outputName, int flags);

    @BMethod
    Object loadDex(String sourceName, String outputName, int flags);

    @BStaticField
    Object mCookie();
}
