# TeamDarkCore SDK — Clean & Universal Edition

Self-initializing Android 7–16 (API 24–36) Virtualization & Game Loader SDK supporting **32-bit (`armeabi-v7a`, `x86`)**, **64-bit (`arm64-v8a`, `x86_64`)**, **0ms Zero-Copy OBB Redirection**, and **1-Line Drop-In Loader Integration**.

## 1. Adding `.aar` or Module to Any Loader

### Option A — Using `TeamDarkCore-release.aar`:
Place `TeamDarkCore-release.aar` inside `app/libs/` and add in `app/build.gradle`:
```groovy
dependencies {
    implementation files('libs/TeamDarkCore-release.aar')
}
```
*(All required reflection libraries `FreeReflection` and `BlackReflection-core` are embedded directly inside the `.aar` under `/libs/`, so no extra dependencies or JitPack setup are needed in your Loader!)*

### Option B — Using Source Module:
Copy the `TeamDarkCore` module folder into your Loader root, add `include ':app', ':TeamDarkCore'` in `settings.gradle`, and add in `app/build.gradle`:
```groovy
dependencies {
    implementation project(':TeamDarkCore')
}
```

## 2. 1-Line Launch in Any Loader Activity

Because `TeamDarkCore` automatically initializes itself across `:main`, `:core`, and `:p0..:p24` via its built-in ContentProviders, you can launch any game with **1 line** inside any button click:

```java
import com.teamdark.core.TeamDarkCoreLoader;

// Auto-initializes SDK, auto-clones game from phone if not yet installed, links OBB in 0ms, and launches:
TeamDarkCoreLoader.launchGame(this, "com.pubg.imobile");
```

## 3. Built-In Compatibility Bridges (Works With Existing Loader Imports)

- **TeamDarkCore:** `com.teamdark.core.TeamDarkCoreLoader`, `com.teamdark.core.TeamDarkCoreEngine`
- **Mundo / Meta (`net_62v.external.*`):** `MetaCore`, `MetaApplicationInstaller`, `MetaActivityManager`, `MetaPackageManager`, `MetaActivationManager`, `MetaStorageManager`
- **BlackBox (`top.niunaijun.blackbox.*`):** `BlackBoxCore`, `ClientConfiguration`, `AppLifecycleCallback`
- **VCore (`com.vcore.*`):** `BlackBoxCore`, `ClientConfiguration`, `AppLifecycleCallback`

## 4. Building `TeamDarkCore-release.aar`

### Local (Android Studio / Terminal):
```bash
./gradlew :TeamDarkCore:assembleRelease
```
Output: `TeamDarkCore/build/outputs/aar/TeamDarkCore-release.aar`

### Cloud (GitHub Actions — Zero PC Needed):
Upload this folder to a GitHub repository and run the **Android CI** workflow in the **Actions** tab to download `TeamDarkCore-release-aar`.

## 5. PHP License Control Center (TDCore branch)

The active PHP/MySQL implementation is in [Pannel/README.md](Pannel/README.md).
Old Node/Neon sources are archived in `LegacyNodePanel/` outside the PHP
document root; do not deploy them as part of the live PHP panel.

The admin must provision the HTTPS panel endpoint, P-256 signing private key
(server only), its public X.509 DER Base64 value (SDK build property),
MySQL schema, and at least one valid license. Example SDK setup:

```java
import com.teamdark.core.TeamDarkCore;
TeamDarkCore.setSdkKey(this, "TD-ISSUED-LICENSE-KEY");
boolean allowed = TeamDarkCore.startGame(this, "your.authorized.package");
```

Set `tdLicenseUrl=https://YOUR-PANEL/connect` and
`tdLicenseP256PublicKey=BASE64_X509_P256_PUBLIC_KEY` in your local Gradle
properties before building the SDK. Without both, SDK authorization
**fails closed**; it will not use the old cached/offline grace mode.
Optional signed feature flags can be queried using
`SdkLicenseGuard.isFeatureAllowed(context, bitIndex)` after authorization.
Root / facade launch paths and the central `launchApk` path call online
authorization, but runtime testing on real Android devices is still required.

This is an ongoing migration from the old Node panel's much larger API.
The PHP admin/license API is implemented; historical client portal and
telemetry interfaces are not yet migrated. The new panel does not
automatically migrate old Neon production data.
