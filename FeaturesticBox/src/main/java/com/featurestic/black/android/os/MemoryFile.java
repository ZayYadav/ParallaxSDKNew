package com.featurestic.black.android.os;

import java.io.FileDescriptor;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BMethod;

@BClassName("android.os.MemoryFile")
public interface MemoryFile {
    @BMethod
    FileDescriptor getFileDescriptor();
}
