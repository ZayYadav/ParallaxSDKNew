package com.teamdark.core.fake.service.context.providers;

import android.database.MatrixCursor;
import android.net.Uri;
import android.os.IInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.GmsCore;
import com.teamdark.core.fake.hook.ClassInvocationStub;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Android 16 compatibility wrapper for the real GMS Dynamite provider.
 *
 * Virtual apps run under the host UID. The wrapper keeps the real provider
 * caller identity paired with that UID and suppresses only measurement-module
 * discovery. Other Dynamite modules are delegated unchanged so Google auth and
 * unrelated Play Services APIs remain available.
 */
public final class GmsDynamiteProviderStub extends ClassInvocationStub implements BContentProvider {
    private IInterface mBase;

    @Override
    public IInterface wrapper(IInterface contentProviderProxy, String appPkg) {
        mBase = contentProviderProxy;
        injectHook();
        return (IInterface) getProxyInvocation();
    }

    @Override
    protected Object getWho() {
        return mBase;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
    }

    @Override
    protected void onBindMethod() {
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("asBinder".equals(method.getName())) {
            return method.invoke(mBase, args);
        }

        normalizeRealCaller(args);

        if ("query".equals(method.getName())) {
            Uri uri = findUri(args);
            if (GmsCore.isMeasurementDynamiteUri(uri)) {
                String[] projection = findProjection(args);
                return new MatrixCursor(projection != null ? projection : new String[0], 0);
            }
        }

        try {
            return method.invoke(mBase, args);
        } catch (InvocationTargetException failure) {
            Throwable cause = failure.getTargetException();
            throw cause != null ? cause : failure;
        }
    }

    private static void normalizeRealCaller(Object[] args) {
        if (args == null || args.length == 0 || args[0] == null) {
            return;
        }
        Object first = args[0];
        if (first instanceof String) {
            args[0] = TeamDarkCoreEngine.getHostPkg();
            return;
        }
        Class<?> attributionClass = BRAttributionSource.getRealClass();
        if (attributionClass != null && attributionClass.isInstance(first)) {
            ContextCompat.fixAttributionSourceState(first, TeamDarkCoreEngine.getHostUid());
        }
    }

    private static Uri findUri(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof Uri) {
                return (Uri) arg;
            }
        }
        return null;
    }

    private static String[] findProjection(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof String[]) {
                return (String[]) arg;
            }
        }
        return null;
    }
}
