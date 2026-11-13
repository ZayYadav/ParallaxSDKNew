package com.teamdark.core.core.system.pm.installer;

import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.core.system.pm.BPackageSettings;
import com.teamdark.core.entity.pm.InstallOption;
import com.teamdark.core.utils.FileUtils;

public class RemoveUserExecutor implements Executor {
    @Override
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        String packageName = ps.pkg.packageName;
        FileUtils.deleteDir(BEnvironment.getDataDir(packageName, userId));
        FileUtils.deleteDir(BEnvironment.getDeDataDir(packageName, userId));
        return 0;
    }
}
