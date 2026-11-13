package com.featurestic.box;

import android.content.Context;
import androidx.annotation.Keep;
import com.featurestic.sdk.FeaturesticBox;

@Keep
public final class FeaturesticLoader {
    private FeaturesticLoader() {}

    public static void init(Context context) {
        FeaturesticBox.init(context);
    }

    public static boolean launchGame(Context context, String packageName) {
        return FeaturesticBox.startGame(context, packageName, 0, null);
    }

    public static boolean launchGame(Context context, String packageName, int userId) {
        return FeaturesticBox.startGame(context, packageName, userId, null);
    }
}
