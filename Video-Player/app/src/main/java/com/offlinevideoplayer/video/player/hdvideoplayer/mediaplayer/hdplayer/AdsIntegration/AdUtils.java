package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;

import java.io.IOException;
import java.util.Objects;

public class AdUtils {
    public static boolean displayAds = false;
    public static boolean displayInterstitialAds = false;
    public static boolean displayBannerAds = false;
    public static boolean displayNativeAds = false;
    public static boolean displayMiniNativeAds = false;
    public static boolean displayOpenAppAds = false;
    public static int interstitialCount = 0;
    public static int showInterstitialOnCount = 5;
    static InterstitialAd interstitialAd;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        switch (activeNetwork.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_MOBILE:
                if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetwork.getState() == NetworkInfo.State.CONNECTING) &&
                        isInternet())
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    private static boolean isInternet() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * For Admob Ads
     **/
    public static void showAdmobBannerAd(Activity activity, RelativeLayout relativeLayout) {
        AdSize adSize = getAdSize(activity);
        final com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(activity);
        mAdView.setAdSize(adSize);
        mAdView.setAdUnitId(activity.getString(R.string.admob_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        relativeLayout.getLayoutParams().height = adSize.getHeightInPixels(activity);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("--admob_banner--", "showAdmobBannerAd onAdFailedToLoad:" + loadAdError.getCause());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                relativeLayout.removeAllViews();
                relativeLayout.addView(mAdView);
                relativeLayout.setGravity(Gravity.CENTER);
                Log.d("--admob_banner--", "showAdmobBannerAd onAdLoaded:");
            }
        });
        mAdView.loadAd(adRequest);
    }

    public static AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static void loadInterstitialAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                context,
                context.getString(R.string.admob_interstitial),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        AdUtils.interstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d("--admob_interstitial--", "showAdmobInterstitialAd onAdFailedToLoad:" + loadAdError.getCause());
                    }
                }
        );
    }

    public static void showInterstitialAd(Activity activity, Intent intent) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
            interstitialAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Log.d("--admob_interstitial--", "showAdmobInterstitialAd onAdFailedToShowFullScreenContent:");
                            activity.startActivity(intent);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                            Log.d("--admob_interstitial--", "showAdmobInterstitialAd onAdFailedToShowFullScreenContent:" + adError.getCause());
                            interstitialCount--;
                            activity.startActivity(intent);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d("--admob_interstitial--", "showAdmobInterstitialAd onAdShowedFullScreenContent:");
                            loadInterstitialAd(activity);
                        }
                    });
        } else {
            AdUtils.interstitialCount--;
            activity.startActivity(intent);
        }
    }

    public static void showAdmobNativeAd(Activity activity, RelativeLayout relativeLayout) {
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).setVideoOptions(videoOptions).build();
        AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getString(R.string.admob_native));
        builder.forNativeAd(unifiedNativeAd -> {
            NativeAdView unifiedNativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.ad_admob_native, null);
            populateNativeAdWithMediaView(activity, unifiedNativeAd, unifiedNativeAdView);
            relativeLayout.removeAllViews();
            relativeLayout.addView(unifiedNativeAdView);
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).setVideoOptions(new VideoOptions.Builder().build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("--admob_native--", "showAdmobNativeAd onAdFailedToLoad:" + loadAdError.getCause());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("--admob_native--", "showAdmobNativeAd onAdLoaded:");
            }
        }).withNativeAdOptions(adOptions).build().loadAd(new AdRequest.Builder().build());
    }

    private static void populateNativeAdWithMediaView(Activity activity, com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView nativeAdView) {
        MediaView mediaView = nativeAdView.findViewById(R.id.ad_media);
        // Set the media view.
        nativeAdView.setMediaView(mediaView);
        // Set other ad assets.
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) Objects.requireNonNull(nativeAdView.getHeadlineView())).setText(nativeAd.getHeadline());
        Objects.requireNonNull(nativeAdView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));
        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(nativeAdView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(nativeAdView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(nativeAdView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(nativeAdView.getIconView())).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(nativeAdView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(nativeAdView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(nativeAdView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(nativeAdView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(nativeAdView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(nativeAdView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) { // Images
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                } else { // Videos
                    float scale = activity.getResources().getDisplayMetrics().density;
                    int maxHeightPixels = 175;
                    int maxHeightDp = (int) (maxHeightPixels * scale + 0.5f);
                    Log.d("--height--", "onChildViewAdded: " + maxHeightDp);
                    ViewGroup.LayoutParams params = child.getLayoutParams();
                    params.height = maxHeightDp;
                    child.setLayoutParams(params);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        nativeAdView.setNativeAd(nativeAd);
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();
        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    Log.d("--admob_native--", "populateNativeAdWithMediaView setVideoLifecycleCallbacks:");
                    // Publishers should allow native ads to complete video playback before
                }
            });
        }
    }

    public static void showAdmobMiniNativeAd(Activity activity, RelativeLayout relativeLayout) {
        relativeLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.ad_bg));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.height = (int) activity.getResources().getDimension(com.intuit.sdp.R.dimen._95sdp);
        int margin = (int) activity.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp);
        layoutParams.setMargins(margin, 0, margin, 0);
        relativeLayout.setLayoutParams(layoutParams);
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getString(R.string.admob_native));
        builder.forNativeAd(unifiedNativeAd -> {
            NativeAdView unifiedNativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.ad_admob_mini_native, null);
            populateSmallNativeAdWithMediaView(unifiedNativeAd, unifiedNativeAdView);
            relativeLayout.removeAllViews();
            relativeLayout.addView(unifiedNativeAdView);
        });
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("--admob_native--", "showAdmobNativeAd onAdFailedToLoad:" + loadAdError.getCause());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("--admob_native--", "showAdmobNativeAd onAdLoaded:");
            }
        }).withNativeAdOptions(adOptions).build().loadAd(new AdRequest.Builder().build());
    }

    private static void populateSmallNativeAdWithMediaView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView nativeAdView) {
        // Set other ad assets.
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) Objects.requireNonNull(nativeAdView.getHeadlineView())).setText(nativeAd.getHeadline());
        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(nativeAdView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(nativeAdView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(nativeAdView.getIconView())).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(nativeAdView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(nativeAdView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(nativeAdView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(nativeAdView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(nativeAdView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(nativeAdView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        nativeAdView.setNativeAd(nativeAd);
    }
}

