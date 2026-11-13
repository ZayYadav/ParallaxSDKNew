package com.featurestic.black.model.vivo;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

/**
 * @author gm
 * @function
 * @date :2024/4/23 16:56
 **/
@BClassName("vivo.app.security.IVivoPermissionService")
public interface IVivoPermissionService {
    @BClassName("vivo.app.security.IVivoPermissionService$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}
