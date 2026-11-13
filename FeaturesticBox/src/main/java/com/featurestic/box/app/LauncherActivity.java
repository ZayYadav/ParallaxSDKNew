package com.featurestic.box.app;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

import com.featurestic.box.FeaturesticBoxCore;
import com.featurestic.box.utils.Slog;

/**
 * Fast, Responsive FeaturesticBox Splash Screen
 * Transitions into the virtual game container as soon as ProxyActivity$P0 comes to the foreground,
 * without ever prematurely finishing back to the Loader's Start button.
 */
public class LauncherActivity extends Activity {
    public static final String TAG = "SplashScreen";

    public static final String KEY_INTENT = "launch_intent";
    public static final String KEY_PKG = "launch_pkg";
    public static final String KEY_USER_ID = "launch_user_id";

    private static final long SPLASH_DURATION_MS = 1800L;
    private static volatile java.lang.ref.WeakReference<Activity> sActiveLauncherRef;

    public static Activity getActiveLauncher() {
        java.lang.ref.WeakReference<Activity> ref = sActiveLauncherRef;
        Activity act = ref != null ? ref.get() : null;
        if (act != null && !act.isFinishing() && !act.isDestroyed()) {
            return act;
        }
        return null;
    }

    private volatile boolean isRunning = false;
    private volatile boolean mIsPaused = false;
    private volatile boolean mLaunchDispatched = false;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mDeferredFinishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning && !mIsPaused && !isFinishing()) {
                finish();
            }
        }
    };
    private ObjectAnimator mRingAnimator;
    private ValueAnimator mProgressAnimator;

    private TextView mLaunchStatusView;
    private TextView mCountdownBadgeView;
    private TextView mPercentBadgeView;
    private ProgressBar mProgressBar;

    public static void launch(Intent intent, int userId) {
        Intent splash = new Intent();
        splash.setClass(FeaturesticBoxCore.getContext(), LauncherActivity.class);
        splash.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String pkg = intent.getPackage();
        if ((pkg == null || pkg.isEmpty()) && intent.getComponent() != null) {
            pkg = intent.getComponent().getPackageName();
        }
        splash.putExtra(LauncherActivity.KEY_INTENT, intent);
        splash.putExtra(LauncherActivity.KEY_PKG, pkg);
        splash.putExtra(LauncherActivity.KEY_USER_ID, userId);
        FeaturesticBoxCore.getContext().startActivity(splash);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            FeaturesticBoxCore.get().ensureInitialized(this);
        } catch (Throwable ignored) {
        }
        super.onCreate(savedInstanceState);
        sActiveLauncherRef = new java.lang.ref.WeakReference<>(this);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Intent launchIntent = intent.getParcelableExtra(KEY_INTENT);
        String pkg = intent.getStringExtra(KEY_PKG);
        int userId = intent.getIntExtra(KEY_USER_ID, 0);
        if (launchIntent == null) {
            finish();
            return;
        }
        if ((pkg == null || pkg.isEmpty()) && launchIntent.getComponent() != null) {
            pkg = launchIntent.getComponent().getPackageName();
        }

        PackageInfo packageInfo = null;
        if (pkg != null) {
            try {
                packageInfo = FeaturesticBoxCore.getBPackageManager().getPackageInfo(pkg, 0, userId);
            } catch (Throwable ignored) {
            }
            if (packageInfo == null) {
                try {
                    packageInfo = FeaturesticBoxCore.getPackageManager().getPackageInfo(pkg, 0);
                } catch (Throwable ignored) {
                }
            }
        }
        Drawable icon = null;
        String appLabel = pkg != null ? pkg : "Virtual Target";
        if (packageInfo != null && packageInfo.applicationInfo != null) {
            try {
                icon = packageInfo.applicationInfo.loadIcon(FeaturesticBoxCore.getPackageManager());
            } catch (Throwable ignored) {
            }
            try {
                CharSequence labelSeq = packageInfo.applicationInfo.loadLabel(FeaturesticBoxCore.getPackageManager());
                if (labelSeq != null && labelSeq.length() > 0) {
                    appLabel = labelSeq.toString();
                }
            } catch (Throwable ignored) {
            }
        }

        buildFeaturesticSplashView(icon, appLabel);
        startLiveCountdownAnimation();

        scheduleLaunch(launchIntent, pkg, userId);
    }

    private void scheduleLaunch(final Intent launchIntent, final String targetPkg, final int userId) {
        mHandler.postDelayed(() -> new Thread(() -> {
            try {
                Thread dlThread = new Thread(() -> {
                    try {
                        com.featurestic.box.core.RNative.preparePendingPluginOnWorkerThread(LauncherActivity.this);
                    } catch (Throwable ignored) {
                    }
                }, "Featurestic-Splash-PluginPrep");
                dlThread.start();
                try {
                    dlThread.join(2500L);
                } catch (Throwable ignored) {
                }

                Intent finalIntent = launchIntent;
                if (targetPkg != null && !targetPkg.isEmpty()) {
                    try {
                        if (!FeaturesticBoxCore.get().isInstalled(targetPkg, userId)) {
                            FeaturesticBoxCore.get().installPackageAsUser(targetPkg, userId);
                        }
                        Intent vIntent = FeaturesticBoxCore.getBPackageManager().getLaunchIntentForPackage(targetPkg, userId);
                        if (vIntent != null) {
                            finalIntent = vIntent;
                        }
                    } catch (Throwable ignored) {
                    }
                }

                if (!com.featurestic.box.security.SdkLicenseGuard.isLicensed(this)) {
                    Slog.e(TAG, "Virtual Engine startup aborted: unlicensed FeaturesticBox SDK. Please configure a valid SDK Key.");
                    mHandler.post(this::finish);
                    return;
                }

                if (isLandscapeTarget(finalIntent, targetPkg, userId)) {
                    ensureLandscapeDisplayBeforeLaunch();
                }

                FeaturesticBoxCore.getBActivityManager().startActivity(finalIntent, userId);
                mLaunchDispatched = true;
            } catch (Throwable throwable) {
                Slog.e(TAG, "Unable to start isolated activity: " + throwable.getMessage());
                mHandler.post(this::finish);
            }
        }, "FeaturesticLaunch").start(), 260L);
    }

    private boolean isLandscapeTarget(Intent intent, String pkg, int userId) {
        if (pkg != null && (pkg.startsWith("com.pubg.")
                || "com.tencent.ig".equals(pkg)
                || "com.rekoo.pubgm".equals(pkg)
                || "com.vng.pubgmobile".equals(pkg))) {
            return true;
        }
        try {
            if (intent != null) {
                android.content.pm.ResolveInfo ri = FeaturesticBoxCore.getBPackageManager().resolveActivity(
                        intent, 0, null, userId);
                if (ri != null && ri.activityInfo != null) {
                    int o = ri.activityInfo.screenOrientation;
                    return o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            || o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                            || o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                            || o == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private void ensureLandscapeDisplayBeforeLaunch() {
        try {
            mHandler.post(() -> {
                try {
                    if (!isFinishing() && !isDestroyed()) {
                        setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                } catch (Throwable ignored) {
                }
            });
            Thread.sleep(360L);
            com.featurestic.box.fake.service.IDisplayManagerProxy.flushDisplayInfoCache();
        } catch (Throwable ignored) {
        }
    }

    private void startRingRotation(View ringView) {
        try {
            mRingAnimator = ObjectAnimator.ofFloat(ringView, "rotation", 0f, 360f);
            mRingAnimator.setDuration(4000L);
            mRingAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mRingAnimator.setInterpolator(new LinearInterpolator());
            mRingAnimator.start();
        } catch (Throwable ignored) {
        }
    }

    private void startLiveCountdownAnimation() {
        try {
            mProgressAnimator = ValueAnimator.ofInt(0, 1000);
            mProgressAnimator.setDuration(SPLASH_DURATION_MS);
            mProgressAnimator.setInterpolator(new LinearInterpolator());
            mProgressAnimator.addUpdateListener(animation -> {
                int progress1000 = (int) animation.getAnimatedValue();
                int percent = Math.min(100, Math.max(0, progress1000 / 10));
                float remainingSec = Math.max(0f, (SPLASH_DURATION_MS * (1000 - progress1000)) / 1000000f);

                if (mProgressBar != null) {
                    mProgressBar.setProgress(progress1000);
                }
                if (mPercentBadgeView != null) {
                    mPercentBadgeView.setText(percent + "%");
                }
                if (mCountdownBadgeView != null) {
                    mCountdownBadgeView.setText(String.format(Locale.US, "%.1fs", remainingSec));
                }
                if (mLaunchStatusView != null) {
                    if (percent < 35) {
                        mLaunchStatusView.setText("MOUNTING VIRTUAL SANDBOX & OBB...");
                    } else if (percent < 70) {
                        mLaunchStatusView.setText("LOCKING 120HZ ENGINE & MEMORY SHIELD...");
                    } else if (percent < 95) {
                        mLaunchStatusView.setText("INJECTING RUNTIME PERMISSIONS & PLUGIN...");
                    } else {
                        mLaunchStatusView.setText("READY  •  LAUNCHING VIRTUAL CONTAINER");
                    }
                }
            });
            mProgressAnimator.start();
        } catch (Throwable ignored) {
        }
    }

    private void buildFeaturesticSplashView(@Nullable Drawable appIcon, String appLabel) {
        FrameLayout rootFrame = new FrameLayout(this);
        GradientDrawable rootBg = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#040611"), Color.parseColor("#090D22"), Color.parseColor("#04050E")}
        );
        rootFrame.setBackground(rootBg);

        LinearLayout mainCol = new LinearLayout(this);
        mainCol.setOrientation(LinearLayout.VERTICAL);
        mainCol.setGravity(Gravity.CENTER_HORIZONTAL);
        mainCol.setPadding(dp(24), dp(36), dp(24), dp(28));

        LinearLayout topPill = new LinearLayout(this);
        topPill.setOrientation(LinearLayout.HORIZONTAL);
        topPill.setGravity(Gravity.CENTER_VERTICAL);
        topPill.setPadding(dp(14), dp(6), dp(15), dp(6));
        GradientDrawable pillBg = new GradientDrawable();
        pillBg.setColor(Color.parseColor("#141B33"));
        pillBg.setCornerRadius(dp(999));
        pillBg.setStroke(dp(1), Color.parseColor("#3300F0FF"));
        topPill.setBackground(pillBg);

        View statusDot = new View(this);
        GradientDrawable dotBg = new GradientDrawable();
        dotBg.setShape(GradientDrawable.OVAL);
        dotBg.setColor(Color.parseColor("#30D158"));
        statusDot.setBackground(dotBg);
        topPill.addView(statusDot, new LinearLayout.LayoutParams(dp(6), dp(6)));

        TextView pillText = new TextView(this);
        pillText.setText("FEATURESTIC ENGINE  •  FAST TURBO BOOT");
        pillText.setTextColor(Color.parseColor("#E2E8F0"));
        pillText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f);
        pillText.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        LinearLayout.LayoutParams pillTextLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pillTextLp.leftMargin = dp(8);
        topPill.addView(pillText, pillTextLp);
        mainCol.addView(topPill, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(32)));

        Space topSpace = new Space(this);
        mainCol.addView(topSpace, new LinearLayout.LayoutParams(dp(1), 0, 1f));

        FrameLayout centerEmblemFrame = new FrameLayout(this);
        View ringView = new View(this);
        GradientDrawable ringDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#5500F0FF"), Color.parseColor("#22A855F7"), Color.parseColor("#553B82F6")}
        );
        ringDrawable.setShape(GradientDrawable.OVAL);
        ringDrawable.setStroke(dp(2), Color.parseColor("#8800F0FF"));
        ringDrawable.setColor(Color.TRANSPARENT);
        ringView.setBackground(ringDrawable);
        FrameLayout.LayoutParams ringLp = new FrameLayout.LayoutParams(dp(220), dp(220), Gravity.CENTER);
        centerEmblemFrame.addView(ringView, ringLp);
        startRingRotation(ringView);

        FrameLayout brandCard = new FrameLayout(this);
        GradientDrawable brandCardBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#151B38"), Color.parseColor("#080B1A")}
        );
        brandCardBg.setCornerRadius(dp(42));
        brandCardBg.setStroke(dp(1), Color.parseColor("#4400F0FF"));
        brandCard.setBackground(brandCardBg);
        int cardPad = dp(12);
        brandCard.setPadding(cardPad, cardPad, cardPad, cardPad);

        FeaturesticRibbonFLogoView fLogoView = new FeaturesticRibbonFLogoView(this);
        brandCard.addView(fLogoView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        FrameLayout.LayoutParams brandCardLp = new FrameLayout.LayoutParams(dp(172), dp(172), Gravity.CENTER);
        centerEmblemFrame.addView(brandCard, brandCardLp);
        mainCol.addView(centerEmblemFrame, new LinearLayout.LayoutParams(dp(228), dp(228)));

        TextView brandTitle = new TextView(this);
        brandTitle.setText("FEATURESTIC");
        brandTitle.setTextColor(Color.WHITE);
        brandTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f);
        brandTitle.setTypeface(Typeface.create("sans-serif-black", Typeface.BOLD));
        brandTitle.setLetterSpacing(0.18f);
        brandTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams btLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btLp.topMargin = dp(16);
        mainCol.addView(brandTitle, btLp);

        TextView brandSub = new TextView(this);
        brandSub.setText("120Hz Display Engine  •  Anti-Crack Vault  •  Log Shield");
        brandSub.setTextColor(Color.parseColor("#94A3B8"));
        brandSub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.5f);
        brandSub.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        brandSub.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams bsLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bsLp.topMargin = dp(4);
        mainCol.addView(brandSub, bsLp);

        LinearLayout badgeRow = new LinearLayout(this);
        badgeRow.setOrientation(LinearLayout.HORIZONTAL);
        badgeRow.setGravity(Gravity.CENTER);
        badgeRow.addView(createBadgeChip("120 FPS LOCKED", "#00F0FF", 0));
        badgeRow.addView(createBadgeChip("PERMS GRANTED", "#30D158", dp(8)));
        badgeRow.addView(createBadgeChip("TURBO SANDBOX", "#A855F7", dp(8)));
        LinearLayout.LayoutParams brLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        brLp.topMargin = dp(12);
        mainCol.addView(badgeRow, brLp);

        Space bottomSpace = new Space(this);
        mainCol.addView(bottomSpace, new LinearLayout.LayoutParams(dp(1), 0, 1f));

        LinearLayout gameCard = new LinearLayout(this);
        gameCard.setOrientation(LinearLayout.HORIZONTAL);
        gameCard.setGravity(Gravity.CENTER_VERTICAL);
        gameCard.setPadding(dp(14), dp(12), dp(14), dp(12));
        GradientDrawable gameCardBg = new GradientDrawable();
        gameCardBg.setColor(Color.parseColor("#141B33"));
        gameCardBg.setCornerRadius(dp(18));
        gameCardBg.setStroke(dp(1), Color.parseColor("#2E3C56"));
        gameCard.setBackground(gameCardBg);

        ImageView ivIcon = new ImageView(this);
        ivIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (appIcon != null) {
            ivIcon.setImageDrawable(appIcon);
        }
        gameCard.addView(ivIcon, new LinearLayout.LayoutParams(dp(48), dp(48)));

        LinearLayout gameTextCol = new LinearLayout(this);
        gameTextCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gtcLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        gtcLp.leftMargin = dp(13);
        gameTextCol.setLayoutParams(gtcLp);

        TextView tvAppName = new TextView(this);
        tvAppName.setText(appLabel);
        tvAppName.setTextColor(Color.WHITE);
        tvAppName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        tvAppName.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        tvAppName.setSingleLine(true);
        tvAppName.setEllipsize(TextUtils.TruncateAt.END);
        gameTextCol.addView(tvAppName);

        TextView tvAppSub = new TextView(this);
        tvAppSub.setText("120Hz Hardware Mode  •  Isolated Container Ready");
        tvAppSub.setTextColor(Color.parseColor("#38BDF8"));
        tvAppSub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.5f);
        gameTextCol.addView(tvAppSub);
        gameCard.addView(gameTextCol);

        mCountdownBadgeView = createBadgeChip("1.8s", "#00F0FF", dp(6));
        gameCard.addView(mCountdownBadgeView);
        mainCol.addView(gameCard, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(74)));

        LinearLayout statusRow = new LinearLayout(this);
        statusRow.setOrientation(LinearLayout.HORIZONTAL);
        statusRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams srLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        srLp.topMargin = dp(14);
        statusRow.setLayoutParams(srLp);

        View pulseDot = new View(this);
        pulseDot.setBackground(dotBg);
        statusRow.addView(pulseDot, new LinearLayout.LayoutParams(dp(7), dp(7)));

        mLaunchStatusView = new TextView(this);
        mLaunchStatusView.setText("MOUNTING VIRTUAL SANDBOX & OBB...");
        mLaunchStatusView.setTextColor(Color.parseColor("#E2E8F0"));
        mLaunchStatusView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9.5f);
        mLaunchStatusView.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        LinearLayout.LayoutParams lsLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        lsLp.leftMargin = dp(9);
        statusRow.addView(mLaunchStatusView, lsLp);

        mPercentBadgeView = new TextView(this);
        mPercentBadgeView.setText("0%");
        mPercentBadgeView.setTextColor(Color.parseColor("#00F0FF"));
        mPercentBadgeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        mPercentBadgeView.setTypeface(Typeface.create("sans-serif-black", Typeface.BOLD));
        statusRow.addView(mPercentBadgeView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mainCol.addView(statusRow);

        mProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(1000);
        mProgressBar.setProgress(0);
        LinearLayout.LayoutParams pbLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(7));
        pbLp.topMargin = dp(8);
        mainCol.addView(mProgressBar, pbLp);

        rootFrame.addView(mainCol, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(rootFrame);
    }

    private TextView createBadgeChip(String text, String hexColor, int leftMarginPx) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Color.parseColor(hexColor));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.5f);
        tv.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dp(10), dp(3), dp(10), dp(3));
        GradientDrawable chipBg = new GradientDrawable();
        chipBg.setColor(Color.parseColor("#141B33"));
        chipBg.setCornerRadius(dp(999));
        chipBg.setStroke(dp(1), Color.parseColor("#2E3C56"));
        tv.setBackground(chipBg);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(22));
        lp.leftMargin = leftMarginPx;
        tv.setLayoutParams(lp);
        return tv;
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    protected void onPause() {
        if (mLaunchDispatched) {
            isRunning = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRunning) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mRingAnimator != null) {
            try {
                mRingAnimator.cancel();
            } catch (Throwable ignored) {
            }
            mRingAnimator = null;
        }
        if (mProgressAnimator != null) {
            try {
                mProgressAnimator.cancel();
            } catch (Throwable ignored) {
            }
            mProgressAnimator = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
