package com.teamdark.core.fake.provider;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;

import java.io.File;
import java.util.List;

import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.utils.compat.BuildCompat;

public class FileProviderHandler {

    public static Uri convertFileUri(Context context, Uri uri) {
        if (uri == null) return null;
        if (BuildCompat.isN()) {
            File file = convertFile(context, uri);
            if (file == null)
                return null;
            return TeamDarkCoreEngine.getBStorageManager().getUriForFile(file.getAbsolutePath());
        }
        return uri;
    }

    public static File convertFile(Context context, Uri uri) {
        if (uri == null) return null;
        if ("file".equalsIgnoreCase(uri.getScheme()) && uri.getPath() != null) {
            File direct = new File(uri.getPath());
            if (direct.exists()) return direct;
        }
        List<ProviderInfo> providers = BActivityThread.getProviders();
        for (ProviderInfo provider : providers) {
            try {
                File fileForUri = FileProvider.getFileForUri(context, provider.authority, uri);
                if (fileForUri != null && fileForUri.exists()) {
                    return fileForUri;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
