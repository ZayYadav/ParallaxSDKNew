package com.teamdark.core.core.system;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.utils.Slog;
import com.teamdark.core.utils.compat.BundleCompat;

public class SystemCallProvider extends ContentProvider {
    public static final String TAG = "SystemCallProvider";

    @Override
    public boolean onCreate() {
        return initSystem();
    }

    private boolean initSystem() {
        try {
            if (getContext() != null) {
                TeamDarkCoreEngine.get().ensureAttachedBaseContext(getContext());
            }
        } catch (Throwable ignored) {
        }
        try {
            TeamDarkCoreSystem.getSystem().startup();
        } catch (Throwable ignored) {
        }
        return true;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Slog.d(TAG, "call: " + method + ", " + extras);
        if ("VM".equals(method)) {
            Bundle bundle = new Bundle();
            if (extras != null) {
                String name = extras.getString("_G_|_server_name_");
                if (name == null) {
                    name = extras.getString("_B_|_server_name_");
                }
                IBinderBinderHelper.putBinderBothKeys(bundle, ServiceManager.getService(name));
            }
            return bundle;
        }
        return super.call(method, arg, extras);
    }

    private static final class IBinderBinderHelper {
        static void putBinderBothKeys(Bundle bundle, android.os.IBinder binder) {
            BundleCompat.putBinder(bundle, "_G_|_server_", binder);
            BundleCompat.putBinder(bundle, "_B_|_server_", binder);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
