package com.teamdark.black.android.media;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.MediaRouter$Static")
public interface MediaRouterStaticKitkatContext {
    @BFieldSetNotProcess
    void _set_mMediaRouterService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mMediaRouterService();
    @BFieldNotProcess
    android.os.IInterface mMediaRouterService();
}
