package com.allvideo.hdplayer.AdsIntegration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allvideo.hdplayer.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.Mrec;
import com.startapp.sdk.adsbase.StartAppAd;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.facebook.ads.AdSize.BANNER_HEIGHT_50;

public class AdUtils {
    public static int fullscreenCount = 0;
    private static int intAdType = 2;
    private static int nativeAdType = 1;
    private static int miniNativeAdType = 1;
    private static int bannerAdType = 0;
    private static int totalAdCount = 3;

    public static void showBanner(final Activity mContext, final RelativeLayout adContainer) {

        switch (bannerAdType) {
            case 0:
                if (AppUtility.getBoolean(mContext, Constant.IS_STARTAPP_AD, false)) {
                    Banner startAppBanner = new Banner(mContext);
                    adContainer.addView(startAppBanner);
                    adContainer.setGravity(Gravity.CENTER);
                    adBannerCount();
                    break;
                }
                adBannerCount();
            case 1:
                if (AppUtility.getBoolean(mContext, Constant.IS_GOOGLE_AD, false)) {
                    if (!AppUtility.getString(mContext, Constant.GOOGLE_BANNER, "").trim().isEmpty()) {

                        final com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(mContext);
                        mAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
                        mAdView.setAdUnitId(AppUtility.getString(mContext, Constant.GOOGLE_BANNER, ""));
                        AdRequest adRequest = new AdRequest.Builder().build();

                        mAdView.setAdListener(new AdListener() {

                            @Override
                            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                Log.e("onAdFailedToLoad", String.valueOf(loadAdError));
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                if (adContainer != null) {
                                    adContainer.removeAllViews();
                                    adContainer.addView(mAdView);
                                    adContainer.setGravity(Gravity.CENTER);
                                }
                            }
                        });
                        mAdView.loadAd(adRequest);
                    }
                    adBannerCount();
                    break;
                }
                adBannerCount();
            case 2:
                if (AppUtility.getBoolean(mContext, Constant.IS_FACEBOOK_AD, false)) {
                    if (!AppUtility.getString(mContext, Constant.FACEBOOK_BANNER, "").trim().isEmpty()) {
                        AdView adView = new AdView(mContext, AppUtility.getString(mContext, Constant.FACEBOOK_BANNER, ""), BANNER_HEIGHT_50);
                        adContainer.setPadding(1, 1, 1, 1);
                        adContainer.addView(adView);
                        adContainer.setGravity(Gravity.CENTER);
                        adView.loadAd();
                    }
                    adBannerCount();
                    break;
                }
                adBannerCount();
            case 3:
                if (AppUtility.getBoolean(mContext, Constant.IS_APP_AD_LINK, false)) {
                    View adView = mContext.getLayoutInflater().inflate(R.layout.ad_qureka, null);
                    ViewPager viewPager = adView.findViewById(R.id.ad_pager);
                    viewPager.getLayoutParams().height = 100;

                    if (AppUtility.getBoolean(mContext, Constant.IS_APP_AD_LINK, false)) {
                        viewPager.setVisibility(View.VISIBLE);
                    } else {
                        viewPager.setVisibility(View.GONE);
                    }
                    int[] mImageIds = new int[]{R.drawable.qbanner1,
                            R.drawable.qbanner2,
                            R.drawable.qbanner3,
                            R.drawable.qbanner4,
                            R.drawable.qbanner5,
                            R.drawable.qbanner6};

                    viewPager.setAdapter(new PagerAdapter() {
                        @Override
                        public int getCount() {
                            return mImageIds.length;
                        }

                        @Override
                        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                            return view == object;
                        }

                        @Override
                        public Object instantiateItem(ViewGroup container, int position) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setImageResource(mImageIds[position]);
                            container.addView(imageView, 0);
                            imageView.setOnClickListener(view -> mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtility.getString(mContext, Constant.APP_ADS_LINK, "http://play.google.com/store/apps/details?id=" + mContext.getPackageName())))));

                            return imageView;
                        }

                        @Override
                        public void destroyItem(ViewGroup container, int position, Object object) {
                            container.removeView((ImageView) object);
                        }
                    });

                    new CountDownTimer(2000, 2000) {
                        public void onTick(long millisUntilFinished) {
                            viewPager.post(() -> viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % 6));
                        }

                        public void onFinish() {
                            start();
                        }
                    }.start();

                    adContainer.addView(adView);
                    adContainer.setGravity(Gravity.CENTER);
                    adBannerCount();
                    break;
                }
                adBannerCount();
                if (AppUtility.getBoolean(mContext, Constant.IS_STARTAPP_AD, false) || AppUtility.getBoolean(mContext, Constant.IS_GOOGLE_AD, false) || AppUtility.getBoolean(mContext, Constant.IS_FACEBOOK_AD, false))
                    showBanner(mContext, adContainer);
        }
    }

    private static void adBannerCount() {
        if (bannerAdType == totalAdCount) {
            bannerAdType = 0;
        } else {
            bannerAdType = bannerAdType + 1;
        }
    }

    public static void showInterstitial(final Activity activity) {
        if (AppUtility.getString(activity, Constant.COUNTER, "").trim().isEmpty())
            AppUtility.saveString(activity, Constant.COUNTER, "5");
        if (!AppUtility.getString(activity, Constant.COUNTER, "5").equalsIgnoreCase("")) {
            if (fullscreenCount < Integer.parseInt(AppUtility.getString(activity, Constant.COUNTER, "5"))) {
                fullscreenCount += 1;
                return;
            }
        }
        switch (intAdType) {
            case 0:
                if (AppUtility.getBoolean(activity, Constant.IS_STARTAPP_AD, false)) {
                    StartAppAd startAppAd = new StartAppAd(activity);
                    startAppAd.showAd();
                    fullscreenCount = 0;
                    adIntCount();
                    break;
                }
                adIntCount();
            case 1:
                if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
                    if (!AppUtility.getString(activity, Constant.GOOGLE_INT, "").trim().isEmpty()) {

                        AdRequest adRequest = new AdRequest.Builder().build();
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                                activity,
                                AppUtility.getString(activity, Constant.GOOGLE_INT, ""),
                                adRequest,
                                new InterstitialAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                        interstitialAd.show(activity);
                                        interstitialAd.setFullScreenContentCallback(
                                                new FullScreenContentCallback() {
                                                    @Override
                                                    public void onAdDismissedFullScreenContent() {

                                                    }

                                                    @Override
                                                    public void onAdFailedToShowFullScreenContent(@NotNull com.google.android.gms.ads.AdError adError) {

                                                    }

                                                    @Override
                                                    public void onAdShowedFullScreenContent() {
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                                    }
                                }
                        );
                    }
                    fullscreenCount = 0;
                    adIntCount();
                    break;
                }
                adIntCount();
            case 2:
                if (AppUtility.getBoolean(activity, Constant.IS_FACEBOOK_AD, false)) {
                    if (!AppUtility.getString(activity, Constant.FACEBOOK_INT, "").trim().isEmpty()) {
                        final InterstitialAd interstitialAd = new InterstitialAd(activity, AppUtility.getString(activity, Constant.FACEBOOK_INT, ""));
                        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                                Log.e("FB Ins", "Interstitial ad displayed.");
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                Log.e("FB Ins", "Interstitial ad dismissed.");
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                Log.e("FB Ins", "Interstitial ad failed to load: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                Log.d("FB Ins", "Interstitial ad is loaded and ready to be displayed!");
                                interstitialAd.show();
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                Log.d("FB Ins", "Interstitial ad clicked!");
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                Log.d("FB Ins", "Interstitial ad impression logged!");
                            }
                        }).build());
                    }
                    fullscreenCount = 0;
                    adIntCount();
                    break;
                }
                adIntCount();
            case 3:
                if (AppUtility.getBoolean(activity, Constant.IS_APP_AD_LINK, false)) {
                    showQureka(activity);
                    fullscreenCount = 0;
                    adIntCount();
                    break;
                }
                adIntCount();
                if (AppUtility.getBoolean(activity, Constant.IS_STARTAPP_AD, false) || AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false) || AppUtility.getBoolean(activity, Constant.IS_FACEBOOK_AD, false))
                    showInterstitial(activity);
        }
    }

    public static void showQureka(Context mContext) {
        Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.activity_qads);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        TextView tvTimeCount = dialog.findViewById(R.id.tvTimeCount);
        ProgressBar mProgressBar = dialog.findViewById(R.id.progressBar);
        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        ImageView img_ad = dialog.findViewById(R.id.img_ad);
        img_ad.setOnClickListener(view -> mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtility.getString(mContext, Constant.APP_ADS_LINK, "http://play.google.com/store/apps/details?id=" + mContext.getPackageName())))));
        final int min = 1;
        final int max = 7;
        final int random = new Random().nextInt((max - min) + 1) + min;
        switch (random) {
            case 1:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint1));
                break;
            case 2:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint2));
                break;
            case 3:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint3));
                break;
            case 4:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint4));
                break;
            case 5:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint5));
                break;
            case 6:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint6));
                break;
            case 7:
                img_ad.setImageDrawable(mContext.getDrawable(R.drawable.qint7));
                break;
        }

        btnClose.setOnClickListener(view -> dialog.dismiss());
        mProgressBar.setMax(10);

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimeCount.setText("" + millisUntilFinished / 1000);
                int progress = (int) (millisUntilFinished / 1000);
                mProgressBar.setProgress(mProgressBar.getMax() - progress);
            }

            public void onFinish() {
                mProgressBar.setProgress(100);
                mProgressBar.setVisibility(View.GONE);
                tvTimeCount.setVisibility(View.GONE);
                btnClose.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private static void adIntCount() {
        if (intAdType == totalAdCount) {
            intAdType = 0;
        } else {
            intAdType = intAdType + 1;
        }
    }

    public static void showNativeAds(final Activity mContext, RelativeLayout rlLayout) {
        switch (nativeAdType) {
            case 0:
                if (AppUtility.getBoolean(mContext, Constant.IS_STARTAPP_AD, false)) {
                    setUpStartAppNative(mContext, rlLayout);
                    adNativeCount();
                    break;
                }
                adNativeCount();
            case 1:
                if (AppUtility.getBoolean(mContext, Constant.IS_GOOGLE_AD, false)) {
                    if (!AppUtility.getString(mContext, Constant.GOOGLE_NATIVE, "").trim().isEmpty())
                        setUpGoogleNative(mContext, rlLayout);
                    adNativeCount();
                    break;
                }
                adNativeCount();
            case 2:
                if (AppUtility.getBoolean(mContext, Constant.IS_FACEBOOK_AD, false)) {
                    if (!AppUtility.getString(mContext, Constant.FACEBOOK_NATIVE, "").trim().isEmpty())
                        setUpFbNative(mContext, rlLayout, R.layout.fb_native_ad_layout);
                    adNativeCount();
                    break;
                }
                adNativeCount();
            case 3:
                if (AppUtility.getBoolean(mContext, Constant.IS_APP_AD_LINK, false)) {
                    View adView = mContext.getLayoutInflater().inflate(R.layout.ad_qureka, null);
                    ViewPager viewPager = adView.findViewById(R.id.ad_pager);
                    viewPager.getLayoutParams().height = 500;

                    if (AppUtility.getBoolean(mContext, Constant.IS_APP_AD_LINK, false)) {
                        viewPager.setVisibility(View.VISIBLE);
                    } else {
                        viewPager.setVisibility(View.GONE);
                    }
                    int[] mImageIds = new int[]{R.drawable.qnative1,
                            R.drawable.qnative2,
                            R.drawable.qnative3,
                            R.drawable.qnative4,
                            R.drawable.qnative5,
                            R.drawable.qnative6};

                    viewPager.setAdapter(new PagerAdapter() {
                        @Override
                        public int getCount() {
                            return mImageIds.length;
                        }

                        @Override
                        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                            return view == object;
                        }

                        @Override
                        public Object instantiateItem(ViewGroup container, int position) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setImageResource(mImageIds[position]);
                            container.addView(imageView, 0);
                            imageView.setOnClickListener(view -> mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtility.getString(mContext, Constant.APP_ADS_LINK, "http://play.google.com/store/apps/details?id=" + mContext.getPackageName())))));

                            return imageView;
                        }

                        @Override
                        public void destroyItem(ViewGroup container, int position, Object object) {
                            container.removeView((ImageView) object);
                        }
                    });

                    new CountDownTimer(2000, 2000) {
                        public void onTick(long millisUntilFinished) {
                            viewPager.post(() -> viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % 6));
                        }

                        public void onFinish() {
                            start();
                        }
                    }.start();

                    rlLayout.addView(adView);
                    rlLayout.setGravity(Gravity.CENTER);
                    adNativeCount();
                    break;
                }
                adNativeCount();
                if (AppUtility.getBoolean(mContext, Constant.IS_STARTAPP_AD, false) || AppUtility.getBoolean(mContext, Constant.IS_GOOGLE_AD, false) || AppUtility.getBoolean(mContext, Constant.IS_FACEBOOK_AD, false))
                    showNativeAds(mContext, rlLayout);
        }
    }

    private static void adNativeCount() {
        if (nativeAdType == totalAdCount) {
            nativeAdType = 0;
        } else {
            nativeAdType = nativeAdType + 1;
        }
    }

    private static void setUpFbNative(Context mContext, RelativeLayout rlLayout, int adLayout) {
        NativeAd nativeAd = new NativeAd(mContext, AppUtility.getString(mContext, Constant.FACEBOOK_NATIVE, ""));

        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.e("FbAd", ad.getPlacementId());
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("FbAd", adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                nativeAd.unregisterView();

                RelativeLayout.LayoutParams mrecParameters =
                        new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                mrecParameters.addRule(RelativeLayout.CENTER_IN_PARENT);

                NativeAdLayout nativeAdLayout = new NativeAdLayout(mContext);
                nativeAdLayout.setLayoutParams(mrecParameters);
                LayoutInflater inflater = LayoutInflater.from(mContext);

                LinearLayout adView = (LinearLayout) inflater.inflate(adLayout, nativeAdLayout, false);
                nativeAdLayout.addView(adView);

                LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
                AdOptionsView adOptionsView = new AdOptionsView(mContext, nativeAd, nativeAdLayout);
                adChoicesContainer.removeAllViews();
                adChoicesContainer.addView(adOptionsView, 0);

                TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
                TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

                nativeAdTitle.setText(nativeAd.getAdvertiserName());
                nativeAdBody.setText(nativeAd.getAdBodyText());
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);

                nativeAd.registerViewForInteraction(
                        adView,
                        nativeAdMedia,
                        clickableViews);
                rlLayout.setPadding(1, 1, 1, 1);
                rlLayout.addView(nativeAdLayout);
                rlLayout.setGravity(Gravity.CENTER);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        }).build());
    }

    private static void setUpStartAppNative(Context mContext, RelativeLayout rlLayout) {
        Mrec startAppMrec = new Mrec(mContext);
        RelativeLayout.LayoutParams mrecParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        mrecParameters.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlLayout.addView(startAppMrec, mrecParameters);
        rlLayout.setGravity(Gravity.CENTER);
    }

    private static void setUpGoogleNative(Context mContext, RelativeLayout rlLayout) {

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        AdLoader.Builder builder = new AdLoader.Builder(mContext, AppUtility.getString(mContext, Constant.GOOGLE_NATIVE, ""));
        builder.forNativeAd(unifiedNativeAd -> {
            NativeAdView unifiedNativeAdView = (NativeAdView) LayoutInflater.from(mContext).inflate(R.layout.admob_native, null);
            populateNativeAdView(unifiedNativeAd, unifiedNativeAdView);
            rlLayout.removeAllViews();
            rlLayout.addView(unifiedNativeAdView);
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).withNativeAdOptions(adOptions).build().loadAd(new AdRequest.Builder().build());
    }

    private static void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView nativeAdView) {

        com.google.android.gms.ads.nativead.MediaView mediaView = nativeAdView.findViewById(R.id.ad_media);
        nativeAdView.setMediaView(mediaView);
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));

        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());

        if (nativeAd.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            nativeAdView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            nativeAdView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);

            RatingBar ratingBar = (RatingBar) nativeAdView.findViewById(R.id.ad_stars);
            Drawable drawable = ratingBar.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#FF3C00"),PorterDuff.Mode.SRC_ATOP);
        }
        if (nativeAd.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        nativeAdView.setNativeAd(nativeAd);
    }

    public static void showMiniNativeAds(final Activity mContext, RelativeLayout rlLayout) {
        switch (miniNativeAdType) {
            case 0:
                if (AppUtility.getBoolean(mContext, Constant.IS_STARTAPP_AD, false)) {
                    Banner startAppBanner = new Banner(mContext);
                    rlLayout.addView(startAppBanner);
                    adMiniNativeCount();
                    break;
                }
                adMiniNativeCount();
            case 1:
                if (AppUtility.getBoolean(mContext, Constant.IS_GOOGLE_AD, false)) {
                    if (!AppUtility.getString(mContext, Constant.GOOGLE_NATIVE, "").trim().isEmpty())
                        setUpGoogleMiniNative(mContext, rlLayout);
                    adMiniNativeCount();
                    break;
                }
                adMiniNativeCount();
            case 2:
                if (AppUtility.getBoolean(mContext, Constant.IS_FACEBOOK_AD, false)) {
                    if (!AppUtility.getString(mContext, Constant.FACEBOOK_NATIVE, "").trim().isEmpty())
                        setUpFbNative(mContext, rlLayout, R.layout.fb_mini_native_layout);
                    adMiniNativeCount();
                    break;
                }
                adMiniNativeCount();
            case 3:
                if (AppUtility.getBoolean(mContext, Constant.IS_APP_AD_LINK, false)) {
                    View adView = mContext.getLayoutInflater().inflate(R.layout.ad_qureka, null);
                    ViewPager viewPager = adView.findViewById(R.id.ad_pager);
                    viewPager.getLayoutParams().height = 220;

                    if (AppUtility.getBoolean(mContext, Constant.IS_APP_AD_LINK, false)) {
                        viewPager.setVisibility(View.VISIBLE);
                    } else {
                        viewPager.setVisibility(View.GONE);
                    }
                    int[] mImageIds = new int[]{R.drawable.banner_one,
                            R.drawable.banner_two,
                            R.drawable.banner_three,
                            R.drawable.banner_five,
                            R.drawable.banner_four,
                            R.drawable.banner_six};

                    viewPager.setAdapter(new PagerAdapter() {
                        @Override
                        public int getCount() {
                            return mImageIds.length;
                        }

                        @Override
                        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                            return view == object;
                        }

                        @NotNull
                        @Override
                        public Object instantiateItem(@NotNull ViewGroup container, int position) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setImageResource(mImageIds[position]);
                            container.addView(imageView, 0);
                            imageView.setOnClickListener(view -> mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtility.getString(mContext, Constant.APP_ADS_LINK, "http://play.google.com/store/apps/details?id=" + mContext.getPackageName())))));

                            return imageView;
                        }

                        @Override
                        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
                            container.removeView((ImageView) object);
                        }
                    });

                    new CountDownTimer(2000, 2000) {
                        public void onTick(long millisUntilFinished) {
                            viewPager.post(() -> viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % 6));
                        }

                        public void onFinish() {
                            start();
                        }
                    }.start();

                    rlLayout.addView(adView);
                    rlLayout.setGravity(Gravity.CENTER);
                    adMiniNativeCount();
                    break;
                }
                adMiniNativeCount();
                if (AppUtility.getBoolean(mContext, Constant.IS_STARTAPP_AD, false) || AppUtility.getBoolean(mContext, Constant.IS_GOOGLE_AD, false) || AppUtility.getBoolean(mContext, Constant.IS_FACEBOOK_AD, false))
                    showMiniNativeAds(mContext, rlLayout);
        }
    }

    private static void adMiniNativeCount() {
        if (miniNativeAdType == totalAdCount) {
            miniNativeAdType = 0;
        } else {
            miniNativeAdType = miniNativeAdType + 1;
        }
    }

    private static void setUpGoogleMiniNative(Context mContext, RelativeLayout rlLayout) {

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();
        AdLoader.Builder builder = new AdLoader.Builder(mContext, AppUtility.getString(mContext, Constant.GOOGLE_NATIVE, ""));
        builder.forNativeAd(unifiedNativeAd -> {

            NativeAdView unifiedNativeAdView = (NativeAdView) LayoutInflater.from(mContext).inflate(R.layout.ad_mini_unified, null);
            populateNativeAdView(unifiedNativeAd, unifiedNativeAdView);
            rlLayout.removeAllViews();
            rlLayout.addView(unifiedNativeAdView);
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).withNativeAdOptions(adOptions).build().loadAd(new AdRequest.Builder().build());
    }
}