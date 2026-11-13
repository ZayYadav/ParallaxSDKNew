package com.teamdark.black.android.media;

import android.content.Context;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;
import com.teamdark.blackreflection.annotation.BStaticField;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.media.MediaRouter")
public interface MediaRouter {
    @BStaticField
    Object sStatic();

    @BClassName("android.media.MediaRouter$Static")
    interface StaticKitkat {
        @BField
        IInterface mMediaRouterService();
    }

    @BClassName("android.media.MediaRouter$Static")
    interface Static {
        @BField
        IInterface mAudioService();
    }

    // Add a method to get the MediaRouter instance dynamically for Android 10+
    @BStaticMethod
    Object getInstance(Context context);
}
