package com.teamdark.black.android.dalvik.system;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BMethod;
import com.teamdark.blackreflection.annotation.BStaticField;

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
