package com.teamdark.core.fake.service;

import android.os.Build;
import android.os.IInterface;
import android.os.storage.StorageVolume;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.android.os.mount.BRIMountServiceStub;
import com.teamdark.black.android.os.storage.BRIStorageManagerStub;
import com.teamdark.black.android.os.storage.BRStorageVolume;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.RCore;
import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.FileUtils;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.compat.BuildCompat;

public class IStorageManagerProxy extends BinderInvocationStub {

    public IStorageManagerProxy() {
        super(BRServiceManager.get().getService("mount"));
    }

    @Override
    protected Object getWho() {
        IInterface mount;
        if (BuildCompat.isOreo()) {
            mount = BRIStorageManagerStub.get().asInterface(BRServiceManager.get().getService("mount"));
        } else {
            mount = BRIMountServiceStub.get().asInterface(BRServiceManager.get().getService("mount"));
        }
        return mount;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("mount");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method != null && !"getVolumeList".equals(method.getName())) {
            MethodParameterUtils.replaceAllAppPkg(args);
            MethodParameterUtils.replaceFirstUid(args);
            MethodParameterUtils.replaceLastUid(args);
        }
        try {
            return super.invoke(proxy, method, args);
        } catch (SecurityException ignored) {
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException) return null;
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @ProxyMethod("getVolumeList")
    public static class GetVolumeList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Object[] callArgs = args != null ? args.clone() : null;
            if (callArgs != null) {
                if (callArgs.length >= 2 && callArgs[0] instanceof Integer && callArgs[1] instanceof String) {
                    // Android 13-16 (API 33+): getVolumeList(int userId, String callingPackage, int flags)
                    // Android 6-12 (API 23-32): getVolumeList(int uid, String packageName, int flags)
                    callArgs[0] = Build.VERSION.SDK_INT >= 33
                            ? TeamDarkCoreEngine.getHostUserId()
                            : TeamDarkCoreEngine.getHostUid();
                    callArgs[1] = TeamDarkCoreEngine.getHostPkg();
                } else {
                    MethodParameterUtils.replaceAllAppPkg(callArgs);
                    MethodParameterUtils.replaceFirstUid(callArgs);
                }
            }
            Object raw;
            try {
                raw = method.invoke(who, callArgs);
            } catch (InvocationTargetException e) {
                // Fallback: try opposite first-arg convention (uid vs userId) if OEM signature differs
                if (callArgs != null && callArgs.length >= 2 && callArgs[0] instanceof Integer) {
                    callArgs[0] = Build.VERSION.SDK_INT >= 33
                            ? TeamDarkCoreEngine.getHostUid()
                            : TeamDarkCoreEngine.getHostUserId();
                    raw = method.invoke(who, callArgs);
                } else {
                    throw e.getTargetException() != null ? e.getTargetException() : e;
                }
            }
            if (raw instanceof StorageVolume[]) {
                StorageVolume[] volumes = (StorageVolume[]) raw;
                File virtualExt = BEnvironment.getExternalStorageDirectory();
                FileUtils.mkdirs(virtualExt);
                for (StorageVolume volume : volumes) {
                    if (volume == null) continue;
                    try {
                        BRStorageVolume.get(volume)._set_mPath(virtualExt);
                    } catch (Throwable ignored) {
                    }
                    if (BuildCompat.isPie()) {
                        try {
                            BRStorageVolume.get(volume)._set_mInternalPath(virtualExt);
                        } catch (Throwable ignored) {
                        }
                    }
                }
                return volumes;
            }
            return raw;
        }
    }

    @ProxyMethod("mkdirs")
    public static class mkdirs extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            try {
                if (args != null) {
                    for (Object arg : args) {
                        if (arg instanceof String) {
                            String p = (String) arg;
                            if (p.startsWith("/")) {
                                String redirected = RCore.get().redirectPath(p);
                                FileUtils.mkdirs(redirected);
                            }
                        } else if (arg instanceof File) {
                            File redirected = RCore.get().redirectPath((File) arg);
                            FileUtils.mkdirs(redirected);
                        }
                    }
                }
                return 0;
            } catch (Throwable t) {
                return 0;
            }
        }
    }

    @ProxyMethod("getVolumePaths")
    public static class GetVolumePaths extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            try {
                return new String[]{BEnvironment.getExternalStorageDirectory().getAbsolutePath()};
            } catch (Throwable t) {
                return new String[0];
            }
        }
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        if (BuildCompat.isS()) {
            addMethodHook(new GetVolumePaths());
        }
    }
}
