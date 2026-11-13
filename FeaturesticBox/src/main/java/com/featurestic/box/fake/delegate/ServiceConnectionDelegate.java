package com.featurestic.box.fake.delegate;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceConnectionDelegate extends IServiceConnection.Stub {
    private static final Map<IBinder, ServiceConnectionDelegate> sServiceConnectDelegate = new ConcurrentHashMap<>();
    private final IServiceConnection mConn;
    private final ComponentName mComponentName;

    private ServiceConnectionDelegate(IServiceConnection mConn, ComponentName targetComponent) {
        this.mConn = mConn;
        this.mComponentName = targetComponent;
    }

    public static ServiceConnectionDelegate getDelegate(IBinder iBinder) {
        return iBinder != null ? sServiceConnectDelegate.get(iBinder) : null;
    }

    public static IServiceConnection createProxy(IServiceConnection base, Intent intent) {
        if (base == null || base instanceof ServiceConnectionDelegate) {
            return base;
        }
        final IBinder iBinder = base.asBinder();
        ServiceConnectionDelegate delegate = sServiceConnectDelegate.get(iBinder);
        if (delegate == null) {
            try {
                iBinder.linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        sServiceConnectDelegate.remove(iBinder);
                        try {
                            iBinder.unlinkToDeath(this, 0);
                        } catch (Throwable ignored) {
                        }
                    }
                }, 0);
            } catch (RemoteException ignored) {
            }
            ComponentName target = intent != null ? intent.getComponent() : null;
            delegate = new ServiceConnectionDelegate(base, target);
            sServiceConnectDelegate.put(iBinder, delegate);
        }
        return delegate;
    }

    public void connected(ComponentName name, IBinder service) throws RemoteException {
        connected(name, service, (Object) null, false);
    }

    public void connected(ComponentName name, IBinder service, boolean dead) throws RemoteException {
        connected(name, service, (Object) null, dead);
    }

    public void connected(ComponentName name, IBinder service, IBinder session, boolean dead) throws RemoteException {
        connected(name, service, (Object) session, dead);
    }

    private void connected(ComponentName name, IBinder service, Object session, boolean dead) throws RemoteException {
        ComponentName target = mComponentName != null ? mComponentName : name;
        SCDispatchHelper.dispatch(mConn, target, service, session, dead);
    }

    public static void dispatchConnected(IServiceConnection conn, ComponentName target, IBinder service, Object session, boolean dead) throws RemoteException {
        SCDispatchHelper.dispatch(conn, target, service, session, dead);
    }
}
