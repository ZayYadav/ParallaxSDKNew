package com.teamdark.core.fake.service;

import android.content.Context;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.utils.compat.ContextCompat;

import com.teamdark.black.android.media.BRIAudioServiceStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.fake.frameworks.BLocationManager;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.fake.service.base.PkgMethodProxy;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * @author gm
 * @function
 * @date :2024/4/20 20:48
 **/
public class IAudioManagerProxy extends BinderInvocationStub {
    public IAudioManagerProxy() {
        super(BRServiceManager.get().getService(Context.AUDIO_SERVICE));
    }

    @Override
    protected Object getWho() {
        return BRIAudioServiceStub.get().asInterface(BRServiceManager.get().getService(Context.AUDIO_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceAllAppPkg(args);
        MethodParameterUtils.replaceFirstUid(args);
        MethodParameterUtils.replaceLastUid(args);
        if (args != null && BRAttributionSource.getRealClass() != null) {
            String attrClassName = BRAttributionSource.getRealClass().getName();
            for (Object arg : args) {
                if (arg != null && attrClassName.equals(arg.getClass().getName())) {
                    ContextCompat.fixAttributionSourceState(arg, TeamDarkCoreEngine.getHostUid());
                }
            }
        }
        try {
            return super.invoke(proxy, method, args);
        } catch (SecurityException | IllegalArgumentException ignored) {
            if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
            if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException || e.getTargetException() instanceof IllegalArgumentException) {
                if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
                return null;
            }
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @ProxyMethod("unregisterAudioFocusClient")
    public static class unregisterAudioFocusClient extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("registerRemoteControlClient")
    public static class registerRemoteControlClient extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("disableSafeMediaVolume")
    public static class disableSafeMediaVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("startBluetoothSco")
    public static class startBluetoothSco extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("stopBluetoothSco")
    public static class stopBluetoothSco extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setBluetoothScoOn")
    public static class setBluetoothScoOn extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setSpeakerphoneOn")
    public static class setSpeakerphoneOn extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setWiredDeviceConnectionState")
    public static class setWiredDeviceConnectionState extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("requestAudioFocus")
    public static class requestAudioFocus extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("abandonAudioFocus")
    public static class abandonAudioFocus extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("avrcpSupportsAbsoluteVolume")
    public static class avrcpSupportsAbsoluteVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setMode")
    public static class setMode extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setRingerModeInternal")
    public static class setRingerModeInternal extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setRingerModeExternal")
    public static class setRingerModeExternal extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setMasterVolume")
    public static class setMasterVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setStreamVolume")
    public static class setStreamVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("adjustMasterVolume")
    public static class adjustMasterVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("adjustStreamVolume")
    public static class adjustStreamVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("adjustSuggestedStreamVolume")
    public static class adjustSuggestedStreamVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("adjustLocalOrRemoteStreamVolume")
    public static class adjustLocalOrRemoteStreamVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("adjustVolume")
    public static class adjustVolume extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("isHardwareDetected")
    public static class isHardwareDetected extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setMicrophoneMute")
    public static class setMicrophoneMute extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastAppPkg(args);
            MethodParameterUtils.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }
}
