package com.teamdark.core.core.system;

import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.system.accounts.BAccountManagerService;
import com.teamdark.core.core.system.am.BActivityManagerService;
import com.teamdark.core.core.system.am.BJobManagerService;
import com.teamdark.core.core.system.location.BLocationManagerService;
import com.teamdark.core.core.system.notification.BNotificationManagerService;
import com.teamdark.core.core.system.os.BStorageManagerService;
import com.teamdark.core.core.system.pm.BPackageManagerService;
import com.teamdark.core.core.system.pm.BXposedManagerService;
import com.teamdark.core.core.system.user.BUserManagerService;

/**
 * Created by Milk on 3/31/21.
 */
public class ServiceManager {
    private static ServiceManager sServiceManager = null;
    public static final String ACTIVITY_MANAGER = "activity_manager";
    public static final String JOB_MANAGER = "job_manager";
    public static final String PACKAGE_MANAGER = "package_manager";
    public static final String STORAGE_MANAGER = "storage_manager";
    public static final String USER_MANAGER = "user_manager";
    public static final String XPOSED_MANAGER = "xposed_manager";
    public static final String ACCOUNT_MANAGER = "account_manager";
    public static final String LOCATION_MANAGER = "location_manager";
    public static final String NOTIFICATION_MANAGER = "notification_manager";

    private final Map<String, IBinder> mCaches = new HashMap<>();

    public static ServiceManager get() {
        if (sServiceManager == null) {
            synchronized (ServiceManager.class) {
                if (sServiceManager == null) {
                    sServiceManager = new ServiceManager();
                }
            }
        }
        return sServiceManager;
    }

    public static IBinder getService(String name) {
        return get().getServiceInternal(name);
    }

    private ServiceManager() {
        mCaches.put(ACTIVITY_MANAGER, BActivityManagerService.get());
        mCaches.put(JOB_MANAGER, BJobManagerService.get());
        mCaches.put(PACKAGE_MANAGER, BPackageManagerService.get());
        mCaches.put(STORAGE_MANAGER, BStorageManagerService.get());
        mCaches.put(USER_MANAGER, BUserManagerService.get());
        mCaches.put(XPOSED_MANAGER, BXposedManagerService.get());
        mCaches.put(ACCOUNT_MANAGER, BAccountManagerService.get());
        mCaches.put(LOCATION_MANAGER, BLocationManagerService.get());
        mCaches.put(NOTIFICATION_MANAGER, BNotificationManagerService.get());
    }

    public IBinder getServiceInternal(String name) {
        return mCaches.get(name);
    }

    /**
     * Warm virtual services only in virtual-app processes.
     *
     * <p>The host/loader main process does not need all nine Binder services just to draw its
     * splash/login UI. Eagerly resolving every service performs synchronous provider/Binder calls
     * during Application.onCreate() and can stall startup while the server process is coming up.
     * TeamDarkManager already resolves each service lazily when a loader action actually needs it.</p>
     */
    public static void initTeamDarkManager() {
        if (TeamDarkCoreEngine.get().isMainProcess()) {
            return;
        }

        TeamDarkCoreEngine.get().getService(ACTIVITY_MANAGER);
        TeamDarkCoreEngine.get().getService(JOB_MANAGER);
        TeamDarkCoreEngine.get().getService(PACKAGE_MANAGER);
        TeamDarkCoreEngine.get().getService(STORAGE_MANAGER);
        TeamDarkCoreEngine.get().getService(USER_MANAGER);
        TeamDarkCoreEngine.get().getService(XPOSED_MANAGER);
        TeamDarkCoreEngine.get().getService(ACCOUNT_MANAGER);
        TeamDarkCoreEngine.get().getService(LOCATION_MANAGER);
        TeamDarkCoreEngine.get().getService(NOTIFICATION_MANAGER);
    }
}
