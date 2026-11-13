package com.featurestic.box.fake.service;

import com.featurestic.black.android.os.BRServiceManager;
import com.featurestic.black.com.android.internal.telephony.BRISubStub;
import com.featurestic.box.fake.hook.BinderInvocationStub;
import com.featurestic.box.fake.service.base.ValueMethodProxy;

public class ISubProxy extends BinderInvocationStub {
    public static final String TAG = "ISubProxy";

    public ISubProxy() {
        super(BRServiceManager.get().getService("isub"));
    }

    @Override
    protected Object getWho() {
        return BRISubStub.get().asInterface(BRServiceManager.get().getService("isub"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("isub");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new ValueMethodProxy("getAllSubInfoList", null));
        addMethodHook(new ValueMethodProxy("getActiveSubscriptionInfo", null));
        addMethodHook(new ValueMethodProxy("getActiveSubscriptionInfoForIccId", null));
        addMethodHook(new ValueMethodProxy("getActiveSubscriptionInfoForSimSlotIndex", null));
        addMethodHook(new ValueMethodProxy("getActiveSubscriptionInfoList", null));
        addMethodHook(new ValueMethodProxy("getAvailableSubscriptionInfoList", null));
        addMethodHook(new ValueMethodProxy("getAccessibleSubscriptionInfoList", null));
        addMethodHook(new ValueMethodProxy("getAllSubInfoCount", -1));
        addMethodHook(new ValueMethodProxy("getActiveSubInfoCount", -1));
        addMethodHook(new ValueMethodProxy("getActiveSubInfoCountMax", -1));
        addMethodHook(new ValueMethodProxy("addSubInfoRecord", -1));
        addMethodHook(new ValueMethodProxy("addSubInfo", -1));
        addMethodHook(new ValueMethodProxy("removeSubInfo", -1));
    }
}
