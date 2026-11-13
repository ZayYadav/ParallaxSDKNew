package com.teamdark.core.utils.compat;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.teamdark.lsparanoid.Obfuscate;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.system.ServiceManager;
import com.teamdark.core.proxy.ProxyManifest;
import com.teamdark.core.utils.provider.ProviderCall;

/**
 * 0.2s Instant Game Launch Pre-Warmer.
 *
 * Pre-warms the :teamdarkcore_daemon server Binder channel, PackageManager/ActivityManager/UserManager
 * proxies, and DNS caches in a background thread as soon as the Host Loader starts so pressing
 * "Launch Game" opens the game immediately with zero cold-start Binder latency.
 */
@Obfuscate
public final class TeamDarkProcessWarmer {
    private static final AtomicBoolean sWarmed = new AtomicBoolean(false);
    private static final ExecutorService WARM_POOL = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "TeamDark-PreWarmer");
        t.setDaemon(true);
        return t;
    });

    private TeamDarkProcessWarmer() {
    }

    public static void preWarmInBackground(final Context context) {
        if (!sWarmed.compareAndSet(false, true)) {
            return;
        }
        WARM_POOL.execute(() -> {
            try {
                // 1. Wake up :teamdarkcore_daemon ContentProvider & core Binder services
                ProviderCall.callSafely(ProxyManifest.getBindProvider(), "VM", null, null);
                TeamDarkCoreEngine.get().getService(ServiceManager.ACTIVITY_MANAGER);
                TeamDarkCoreEngine.get().getService(ServiceManager.PACKAGE_MANAGER);
                TeamDarkCoreEngine.get().getService(ServiceManager.USER_MANAGER);
                TeamDarkCoreEngine.get().getService(ServiceManager.STORAGE_MANAGER);
                TeamDarkCoreEngine.getBPackageManager().getInstalledApplications(0, 0);
            } catch (Throwable ignored) {
            }
            try {
                // 2. Pre-warm DNS & Low-Latency Network routes
                NetworkLowLatencyOptimizer.initForProcess(context);
            } catch (Throwable ignored) {
            }
        });
    }
}
