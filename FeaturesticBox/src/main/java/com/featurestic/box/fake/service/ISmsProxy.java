package com.featurestic.box.fake.service;

import java.lang.reflect.Method;

import com.featurestic.black.android.os.BRServiceManager;
import com.featurestic.black.com.android.internal.telephony.BRISmsStub;
import com.featurestic.box.FeaturesticBoxCore;
import com.featurestic.box.fake.hook.BinderInvocationStub;
import com.featurestic.box.fake.hook.MethodHook;
import com.featurestic.box.fake.hook.ProxyMethod;
import com.featurestic.box.utils.MethodParameterUtils;

public class ISmsProxy extends BinderInvocationStub {
    public ISmsProxy() {
        super(BRServiceManager.get().getService("isms"));
    }

    @Override
    protected Object getWho() {
        return BRISmsStub.get().asInterface(BRServiceManager.get().getService("isms"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("isms");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    private static void replacePkgArg(Object[] args, int index) {
        if (args != null && index >= 0 && index < args.length && args[index] instanceof String) {
            args[index] = FeaturesticBoxCore.getHostPkg();
        }
    }

    @ProxyMethod("sendText")
    public static class SendText extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("sendTextForSubscriber")
    public static class SendTextForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            replacePkgArg(args, 1);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("sendData")
    public static class SendData extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("sendDataForSubscriber")
    public static class SendDataForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            replacePkgArg(args, 1);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("sendMultipartText")
    public static class SendMultipartText extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("sendMultipartTextForSubscriber")
    public static class SendMultipartTextForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            replacePkgArg(args, 1);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getAllMessagesFromIccEf")
    public static class GetAllMessagesFromIccEf extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getAllMessagesFromIccEfForSubscriber")
    public static class GetAllMessagesFromIccEfForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            replacePkgArg(args, 1);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("copyMessageToIccEf")
    public static class CopyMessageToIccEf extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("copyMessageToIccEfForSubscriber")
    public static class CopyMessageToIccEfForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            replacePkgArg(args, 1);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("updateMessageOnIccEf")
    public static class UpdateMessageOnIccEf extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("updateMessageOnIccEfForSubscriber")
    public static class UpdateMessageOnIccEfForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            replacePkgArg(args, 1);
            return method.invoke(who, args);
        }
    }
}
