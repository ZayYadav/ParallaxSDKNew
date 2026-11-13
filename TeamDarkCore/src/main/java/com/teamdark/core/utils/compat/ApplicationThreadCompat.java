package com.teamdark.core.utils.compat;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.black.android.app.BRApplicationThreadNative;
import com.teamdark.black.android.app.BRIApplicationThreadOreoStub;

public class ApplicationThreadCompat {

    public static IInterface asInterface(IBinder binder) {
        if (BuildCompat.isOreo()) {
            return BRIApplicationThreadOreoStub.get().asInterface(binder);
        }
        return BRApplicationThreadNative.get().asInterface(binder);
    }
}
