package com.teamdark.black.android.media;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.MediaRouter$Static")
public interface MediaRouterStaticContext {
    @BFieldSetNotProcess
    void _set_mAudioService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAudioService();
    @BFieldNotProcess
    android.os.IInterface mAudioService();
}
