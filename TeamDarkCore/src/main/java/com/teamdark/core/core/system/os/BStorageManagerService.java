package com.teamdark.core.core.system.os;

import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.storage.StorageVolume;

import java.io.File;

import com.teamdark.black.android.os.storage.BRStorageManager;
import com.teamdark.black.android.os.storage.BRStorageVolume;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.core.env.BEnvironment;
import com.teamdark.core.core.system.ISystemService;
import com.teamdark.core.core.system.user.BUserHandle;
import com.teamdark.core.fake.provider.FileProvider;
import com.teamdark.core.proxy.ProxyManifest;
import com.teamdark.core.utils.compat.BuildCompat;

public class BStorageManagerService extends IBStorageManagerService.Stub implements ISystemService {
    private static final BStorageManagerService sService = new BStorageManagerService();

    public static BStorageManagerService get() {
        return sService;
    }

    public BStorageManagerService() {
    }

    @Override
    public StorageVolume[] getVolumeList(int uid, String packageName, int flags, int userId) throws RemoteException {
        if (BRStorageManager.get().getVolumeList(0, 0) == null) {
            return null;
        }
        try {
            StorageVolume[] storageVolumes = BRStorageManager.get().getVolumeList(BUserHandle.getUserId(Process.myUid()), 0);
            if (storageVolumes == null)
                return null;
            for (StorageVolume storageVolume : storageVolumes) {
                BRStorageVolume.get(storageVolume)._set_mPath(BEnvironment.getExternalStorageDirectory());
                if (BuildCompat.isPie()) {
                    BRStorageVolume.get(storageVolume)._set_mInternalPath(BEnvironment.getExternalStorageDirectory());
                }
            }
            return storageVolumes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Uri getUriForFile(String file) throws RemoteException {
        return FileProvider.getUriForFile(TeamDarkCoreEngine.getContext(), ProxyManifest.getProxyFileProvider(), new File(file));
    }

    @Override
    public void systemReady() {

    }
}
