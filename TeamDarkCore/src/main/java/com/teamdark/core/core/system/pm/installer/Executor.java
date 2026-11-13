package com.teamdark.core.core.system.pm.installer;

import com.teamdark.core.core.system.pm.BPackageSettings;
import com.teamdark.core.entity.pm.InstallOption;

public interface Executor {
    public static final String TAG = "InstallExecutor";

    int exec(BPackageSettings bPackageSettings, InstallOption installOption, int i);
}
