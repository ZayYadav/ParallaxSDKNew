package com.featurestic.box;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Keep;
import com.featurestic.box.FeaturesticBoxCore;

/**
 * Zero-Config Auto-Initializer ContentProvider with opt-out support:
 * Add <meta-data android:name="featurestic.auto_init" android:value="false" />
 * to <application> if you prefer manual initialization only.
 */
@Keep
public class FeaturesticAutoInitProvider extends ContentProvider {
    private static boolean isAutoInitEnabled(Context context) {
        if (context == null) return true;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null && ai.metaData.containsKey("featurestic.auto_init")) {
                return ai.metaData.getBoolean("featurestic.auto_init", true);
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        if (context != null && isAutoInitEnabled(context)) {
            try {
                FeaturesticBoxCore.get().ensureAttachedBaseContext(context);
            } catch (Throwable ignored) {
            }
        }
        super.attachInfo(context, info);
    }

    @Override
    public boolean onCreate() {
        try {
            Context ctx = getContext();
            if (ctx != null && isAutoInitEnabled(ctx)) {
                FeaturesticBoxCore.get().ensureAttachedBaseContext(ctx);
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
