package com.featurestic.black.android.service.persistentdata;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.service.persistentdata.IPersistentDataBlockService")
public interface IPersistentDataBlockService {
    @BClassName("android.service.persistentdata.IPersistentDataBlockService$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}
