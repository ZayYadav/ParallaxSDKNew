package com.teamdark.core.core.system.pm.installer;

import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.core.system.pm.BPackageSettings;
import com.teamdark.core.entity.pm.InstallOption;
import com.teamdark.core.utils.FileUtils;

public class RemoveAppExecutor implements Executor {
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        FileUtils.deleteDir(BEnvironment.getAppDir(ps.pkg.packageName));
        return 0;
    }
}
