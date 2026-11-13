# FeaturesticBox SDK — Clean & Universal Edition

Self-initializing Android 7–16 (API 24–36) Virtualization & Game Loader SDK supporting **32-bit (`armeabi-v7a`, `x86`)**, **64-bit (`arm64-v8a`, `x86_64`)**, **0ms Zero-Copy OBB Redirection**, and **1-Line Drop-In Loader Integration**.

## 1. Adding `.aar` or Module to Any Loader

### Option A — Using `FeaturesticBox-release.aar`:
Place `FeaturesticBox-release.aar` inside `app/libs/` and add in `app/build.gradle`:
```groovy
dependencies {
    implementation files('libs/FeaturesticBox-release.aar')
}
```
*(All required reflection libraries `FreeReflection` and `BlackReflection-core` are embedded directly inside the `.aar` under `/libs/`, so no extra dependencies or JitPack setup are needed in your Loader!)*

### Option B — Using Source Module:
Copy the `FeaturesticBox` module folder into your Loader root, add `include ':app', ':FeaturesticBox'` in `settings.gradle`, and add in `app/build.gradle`:
```groovy
dependencies {
    implementation project(':FeaturesticBox')
}
```

## 2. 1-Line Launch in Any Loader Activity

Because `FeaturesticBox` automatically initializes itself across `:main`, `:core`, and `:p0..:p24` via its built-in ContentProviders, you can launch any game with **1 line** inside any button click:

```java
import com.featurestic.box.FeaturesticLoader;

// Auto-initializes SDK, auto-clones game from phone if not yet installed, links OBB in 0ms, and launches:
FeaturesticLoader.launchGame(this, "com.pubg.imobile");
```

## 3. Built-In Compatibility Bridges (Works With Existing Loader Imports)

- **FeaturesticBox:** `com.featurestic.box.FeaturesticLoader`, `com.featurestic.box.FeaturesticBoxCore`
- **Mundo / Meta (`net_62v.external.*`):** `MetaCore`, `MetaApplicationInstaller`, `MetaActivityManager`, `MetaPackageManager`, `MetaActivationManager`, `MetaStorageManager`
- **BlackBox (`top.niunaijun.blackbox.*`):** `BlackBoxCore`, `ClientConfiguration`, `AppLifecycleCallback`
- **VCore (`com.vcore.*`):** `BlackBoxCore`, `ClientConfiguration`, `AppLifecycleCallback`

## 4. Building `FeaturesticBox-release.aar`

### Local (Android Studio / Terminal):
```bash
./gradlew :FeaturesticBox:assembleRelease
```
Output: `FeaturesticBox/build/outputs/aar/FeaturesticBox-release.aar`

### Cloud (GitHub Actions — Zero PC Needed):
Upload this folder to a GitHub repository and run the **Android CI** workflow in the **Actions** tab to download `FeaturesticBox-release-aar`.
