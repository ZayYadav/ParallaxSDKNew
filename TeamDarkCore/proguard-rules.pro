-renamesourcefileattribute TeamDarkGuard
-repackageclasses 'f.b'
-allowaccessmodification

-keep class com.teamdark.core.core.system.api.MetaActivationManager { *; }
-keep class net_62v.external.** { *; }
-keep class com.teamdark.core.** { *; }
-keep class top.niunaijun.blackbox.** { *; }
-keep class com.vcore.** { *; }
-keep class top.niunaijun.jnihook.** { *; }
-keep class black.** { *; }
-keep class android.** { *; }
-keep class com.android.** { *; }

-keepclassmembers class com.teamdark.core.TeamDarkCoreEngine {
    public *;
    public native *;
}

-keepclassmembers class com.teamdark.core.core.RNative {
    *;
    native <methods>;
}

-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
