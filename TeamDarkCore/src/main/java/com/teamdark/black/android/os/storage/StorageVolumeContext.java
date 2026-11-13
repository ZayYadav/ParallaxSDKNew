package com.teamdark.black.android.os.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.storage.StorageVolume")
public interface StorageVolumeContext {
    @BFieldSetNotProcess
    void _set_mInternalPath(final Object value);
    @BFieldCheckNotProcess
    Field _check_mInternalPath();
    @BFieldNotProcess
    java.io.File mInternalPath();
    @BFieldSetNotProcess
    void _set_mPath(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPath();
    @BFieldNotProcess
    java.io.File mPath();
}
