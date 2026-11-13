package com.teamdark.core.core.system;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.env.AppSystemEnv;
import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.core.system.accounts.BAccountManagerService;
import com.teamdark.core.core.system.am.BActivityManagerService;
import com.teamdark.core.core.system.am.BJobManagerService;
import com.teamdark.core.core.system.location.BLocationManagerService;
import com.teamdark.core.core.system.notification.BNotificationManagerService;
import com.teamdark.core.core.system.os.BStorageManagerService;
import com.teamdark.core.core.system.pm.BPackageInstallerService;
import com.teamdark.core.core.system.pm.BPackageManagerService;
import com.teamdark.core.core.system.pm.BXposedManagerService;
import com.teamdark.core.core.system.user.BUserHandle;
import com.teamdark.core.core.system.user.BUserManagerService;
import com.teamdark.core.entity.pm.InstallOption;
import com.teamdark.core.utils.FileUtils;

public class TeamDarkCoreSystem {

    private static volatile TeamDarkCoreSystem sTeamDarkCoreSystem;
    private final List<ISystemService> mServices = new ArrayList<>();
    private static final AtomicBoolean isStartup = new AtomicBoolean(false);

    private TeamDarkCoreSystem() { }

    public static TeamDarkCoreSystem getSystem() {
        if (sTeamDarkCoreSystem == null) {
            synchronized (TeamDarkCoreSystem.class) {
                if (sTeamDarkCoreSystem == null) {
                    sTeamDarkCoreSystem = new TeamDarkCoreSystem();
                }
            }
        }
        return sTeamDarkCoreSystem;
    }

    public void startup() {
        if (isStartup.getAndSet(true)) {
            return;
        }
        // Load virtual environment
        BEnvironment.load();
        // Register core system services
        mServices.add(BPackageManagerService.get());
        mServices.add(BUserManagerService.get());
        mServices.add(BActivityManagerService.get());
        mServices.add(BJobManagerService.get());
        mServices.add(BStorageManagerService.get());
        mServices.add(BPackageInstallerService.get());
        mServices.add(BXposedManagerService.get());
        mServices.add(BProcessManagerService.get());
        mServices.add(BAccountManagerService.get());
        mServices.add(BLocationManagerService.get());
        mServices.add(BNotificationManagerService.get());
        // Notify system ready
        for (ISystemService service : mServices) {
            try {
                service.systemReady();
            } catch (Throwable ignored) {
                // Never break virtual startup
            }
        }

        // Pre-install system apps asynchronously so Main UI startup is never blocked
        final List<String> preInstallPackages = AppSystemEnv.getPreInstallPackages();
        if (preInstallPackages != null && !preInstallPackages.isEmpty()) {
            new Thread(() -> {
                for (String pkg : preInstallPackages) {
                    try {
                        if (!BPackageManagerService.get().isInstalled(pkg, BUserHandle.USER_ALL)) {
                            PackageInfo info = TeamDarkCoreEngine.getPackageManager().getPackageInfo(pkg, 0);
                            BPackageManagerService.get().installPackageAsUser(info.applicationInfo.sourceDir, InstallOption.installBySystem(), BUserHandle.USER_ALL);
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }, "TeamDark-PreInstall").start();
        }
        // Init jar environment (SAFE)
        //initJarEnv();
    }
    
    private void initJarEnv() {
        // OPTIONAL: junit.jar (ignore if missing)
        try {
            InputStream junit = TeamDarkCoreEngine.getContext().getAssets().open("junit.jar");
            FileUtils.copyFile(junit,BEnvironment.JUNIT_JAR);
        } catch (Throwable ignored) {
            // junit.jar not present → safe to ignore
        }

        // REQUIRED: empty.jar
        try {
            InputStream empty = TeamDarkCoreEngine.getContext().getAssets().open("empty.jar");
            FileUtils.copyFile(empty,BEnvironment.EMPTY_JAR);
        } catch (Throwable e) {
            // empty.jar missing is a REAL problem
            e.printStackTrace();
        }
    }
}