package com.teamdark.core;

import android.content.Context;
import androidx.annotation.Keep;
import com.teamdark.sdk.TeamDarkCore;

@Keep
public final class TeamDarkCoreLoader {
    private TeamDarkCoreLoader() {}

    public static void init(Context context) {
        TeamDarkCore.init(context);
    }

    public static boolean launchGame(Context context, String packageName) {
        return TeamDarkCore.startGame(context, packageName, 0, null);
    }

    public static boolean launchGame(Context context, String packageName, int userId) {
        return TeamDarkCore.startGame(context, packageName, userId, null);
    }
}
