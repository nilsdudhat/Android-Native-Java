package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.MyApplication;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Inner class that loads and shows app open ads.
 */
public class AppOpenAdManager {

    private static final String LOG_TAG = "AppOpenAdManager";
    public boolean isShowingAd = false;
    boolean stop = false;
    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    /**
     * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
     */
    private long loadTime = 0;

    /**
     * Constructor.
     */
    public AppOpenAdManager() {
    }

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    public void loadAd(Context context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                context.getString(R.string.admob_open_ad),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();

                        Log.d(LOG_TAG, "onAdLoaded.");
//                            Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isLoadingAd = false;
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                            Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Check if ad exists and can be shown.
     */
    private boolean isAdAvailable() {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null;
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showOpenAdInSplashAvailable(
            @NonNull final Activity activity,
            @NonNull MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        Log.d(LOG_TAG, "Will show ad.");

        Date startingTime = Calendar.getInstance().getTime();
        Log.d("--loading_time--", "startingTime: " + startingTime.getTime());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Date loadingTime = Calendar.getInstance().getTime();

                if (!stop) {
                    Log.d("--loading_time--", "loadingTime: " + loadingTime.getTime());
                    if ((loadingTime.getTime() - startingTime.getTime()) < 1000) {
                        if (appOpenAd != null) {
                            stop = true;
                            isShowingAd = true;
                            appOpenAd.show(activity);

                            appOpenAd.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        /**
                                         * Called when full screen content is dismissed.
                                         */
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Set the reference to null so isAdAvailable() returns false.
                                            appOpenAd = null;
                                            isShowingAd = false;

                                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//                                            Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                                            onShowAdCompleteListener.onShowAdComplete();
                                        }

                                        /**
                                         * Called when fullscreen content failed to show.
                                         */
                                        @Override
                                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                            appOpenAd = null;
                                            isShowingAd = false;

                                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                                            Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();

                                            onShowAdCompleteListener.onShowAdComplete();
                                        }

                                        /**
                                         * Called when fullscreen content is shown.
                                         */
                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                                            Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        handler.postDelayed(this, 100);
                    } else {
                        stop = true;
                        onShowAdCompleteListener.onShowAdComplete();
                    }
                }
            }
        }, 100);
    }

    public void onResumedShowOpenAd(Activity currentActivity) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            loadAd(currentActivity);
            return;
        }

        Log.d(LOG_TAG, "Will show ad.");

        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null;
                        isShowingAd = false;

                        Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//                            Toast.makeText(currentActivity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                        loadAd(currentActivity);
                    }

                    /** Called when fullscreen content failed to show. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        appOpenAd = null;
                        isShowingAd = false;

                        Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                            Toast.makeText(currentActivity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();

                        loadAd(currentActivity);
                    }

                    /** Called when fullscreen content is shown. */
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                            Toast.makeText(currentActivity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                    }
                });

        isShowingAd = true;
        appOpenAd.show(currentActivity);
    }
}
