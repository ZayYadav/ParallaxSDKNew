package com.sbox.core.env;

import androidx.annotation.Keep;
import java.io.File;
import com.featurestic.box.core.env.FeaturesticStorageManager;

@Keep
public class BEnvironment extends com.featurestic.box.core.env.BEnvironment {
    public static File getExternalObbDir(String packageName) {
        File dir = com.featurestic.box.core.env.BEnvironment.getExternalObbDir(packageName, 0);
        try {
            if (!FeaturesticStorageManager.hasVirtualObbFiles(packageName)) {
                FeaturesticStorageManager.syncObbToVirtualIfNeeded(packageName);
            }
        } catch (Throwable ignored) {}
        return dir;
    }
}
