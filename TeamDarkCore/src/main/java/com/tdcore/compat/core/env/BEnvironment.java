package com.tdcore.compat.core.env;

import androidx.annotation.Keep;
import java.io.File;
import com.teamdark.core.core.env.TeamDarkStorageManager;

@Keep
public class BEnvironment extends com.teamdark.core.core.env.BEnvironment {
    public static File getExternalObbDir(String packageName) {
        File dir = com.teamdark.core.core.env.BEnvironment.getExternalObbDir(packageName, 0);
        try {
            if (!TeamDarkStorageManager.hasVirtualObbFiles(packageName)) {
                TeamDarkStorageManager.syncObbToVirtualIfNeeded(packageName);
            }
        } catch (Throwable ignored) {}
        return dir;
    }
}
