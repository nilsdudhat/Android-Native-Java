package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AdUtils;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AppOpenAdManager;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.AlbumListActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.GalleryListActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.HomeActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.MyAlbumActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities.PreviewImageActivity;

public class MyApplication extends Application
        implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        /* For Ads Integration */
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdUtils.displayAds = false;
        AdUtils.displayBannerAds = false;
        AdUtils.displayMiniNativeAds = false;
        AdUtils.displayNativeAds = false;
        AdUtils.displayInterstitialAds = false;
        AdUtils.displayOpenAppAds = false;

        AdUtils.showInterstitialOnCount = 5;

        if (AdUtils.isNetworkAvailable(this)) {
            if (AdUtils.displayAds) {
                if (AdUtils.displayOpenAppAds) {
                    this.registerActivityLifecycleCallbacks(this);
                    appOpenAdManager = new AppOpenAdManager();
                    ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            appOpenAdManager.loadAd(currentActivity);
                            handler.postDelayed(this, 60 * 60 * 1000);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        if (AdUtils.isNetworkAvailable(currentActivity)) {
            if (AdUtils.displayAds) {
                if (AdUtils.displayOpenAppAds) {
                    if (!(currentActivity instanceof HomeActivity) &&
                            !(currentActivity instanceof GalleryListActivity) &&
                            !(currentActivity instanceof AlbumListActivity) &&
                            !(currentActivity instanceof MyAlbumActivity) &&
                            !(currentActivity instanceof PreviewImageActivity)) {
                        appOpenAdManager.onResumedShowOpenAd(currentActivity);
                    }
                }
            }
        }
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        currentActivity = activity;
        appOpenAdManager.loadAd(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showOpenAdInSplashAvailable(activity, onShowAdCompleteListener);
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }
}