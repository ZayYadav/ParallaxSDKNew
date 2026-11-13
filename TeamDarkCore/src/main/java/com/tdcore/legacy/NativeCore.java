package com.tdcore.legacy;

import androidx.annotation.Keep;
import java.io.File;
import com.teamdark.core.core.RNative;

@Keep
public class NativeCore {
    static {
        try {
            System.loadLibrary("TDCore");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static native void init(int apiLevel);
    public static native void enableIO();
    public static native void addIORule(String targetPath, String relocatePath);
    public static native void hideXposed();
    public static native boolean disableHiddenApi();
    public static native void init_seccomp();
    public static native String[] getPanelConfig();

    @Keep
    public static int getCallingUid(int origCallingUid) {
        return RNative.getCallingUid(origCallingUid);
    }

    @Keep
    public static String redirectPath(String path) {
        return RNative.redirectPath(path);
    }

    @Keep
    public static File redirectPath(File path) {
        return RNative.redirectPath(path);
    }

    @Keep
    public static long[] loadEmptyDex() {
        return new long[0];
    }
}
