package com.featurestic.box.fake.service;

import android.util.Log;
import android.view.inputmethod.EditorInfo;

import java.lang.reflect.Method;

import com.featurestic.black.android.os.BRServiceManager;
import com.featurestic.black.com.android.internal.view.BRIInputMethodManagerStub;
import com.featurestic.box.FeaturesticBoxCore;
import com.featurestic.box.fake.hook.BinderInvocationStub;
import com.featurestic.box.fake.hook.MethodHook;
import com.featurestic.box.fake.hook.ProxyMethod;
import com.featurestic.box.utils.ArrayUtils;
import com.featurestic.box.utils.MethodParameterUtils;

public class IInputMethodManagerProxy extends BinderInvocationStub {
    public IInputMethodManagerProxy() {
        super(BRServiceManager.get().getService("input_method"));
    }

    @Override
    protected Object getWho() {
        return BRIInputMethodManagerStub.get().asInterface(BRServiceManager.get().getService("input_method"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("input_method");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("startInputOrWindowGainedFocus")
    public static class startInputOrWindowGainedFocus extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "startInputOrWindowGainedFocus called");
            int index = ArrayUtils.indexOfFirst(args, EditorInfo.class);
            if (index != -1) {
                EditorInfo attribute = (EditorInfo) args[index];
                attribute.packageName = FeaturesticBoxCore.getHostPkg();
                args[index] = attribute;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("startInput")
    public static class startInput extends startInputOrWindowGainedFocus {
    }

    @ProxyMethod("windowGainedFocus")
    public static class windowGainedFocus extends startInputOrWindowGainedFocus {
    }

    @ProxyMethod("getInputMethodList")
    public static class getInputMethodList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getEnabledInputMethodList")
    public static class getEnabledInputMethodList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }
}
