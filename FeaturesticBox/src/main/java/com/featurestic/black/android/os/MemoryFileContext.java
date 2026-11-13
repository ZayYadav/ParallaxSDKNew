package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.MemoryFile")
public interface MemoryFileContext {
    @BMethodCheckNotProcess
    Method _check_getFileDescriptor();
    java.io.FileDescriptor getFileDescriptor();
}
