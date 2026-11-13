package com.teamdark.black.android.media;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.AudioManager")
public interface AudioManagerStatic {
    @BMethodCheckNotProcess
    Method _check_getService(android.content.Context context);
    java.lang.Object getService(android.content.Context context);
    @BMethodCheckNotProcess
    Method _check_isStreamMute(int streamType);
    java.lang.Boolean isStreamMute(int streamType);
    @BFieldSetNotProcess
    void _set_sService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sService();
    @BFieldNotProcess
    android.os.IInterface sService();
}
