package com.teamdark.black.android.os.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.storage.StorageManager")
public interface StorageManagerStatic {
    @BMethodCheckNotProcess
    Method _check_getVolumeList(int int0, int int1);
    android.os.storage.StorageVolume[] getVolumeList(int int0, int int1);
}
