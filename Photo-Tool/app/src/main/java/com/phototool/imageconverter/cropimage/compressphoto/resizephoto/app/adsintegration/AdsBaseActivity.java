package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdsBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showInterstitialAd(Activity activity, Intent intent, Bundle bundle) {
        if (AdUtils.displayAds) {
            if (AdUtils.displayInterstitialAds) {
                Log.d("--show_interstitial--", "AdUtils.displayAds: if");

                if (AdUtils.isNetworkAvailable(activity)) {
                    Log.d("--show_interstitial--", "AdUtils.isNetworkAvailable(activity): if");

                    if (AdUtils.interstitialCount >= AdUtils.showInterstitialOnCount) {
                        Log.d("--show_interstitial--", "interstitialCount matched: " + AdUtils.interstitialCount);
                        AdUtils.interstitialCount = 0;
                        AdUtils.showInterstitialAd(activity, intent);

                        Log.d("--show_interstitial--", "interstitialCount 0: " + AdUtils.interstitialCount);
                    } else {
                        Log.d("--show_interstitial--", "interstitialCount not matched: " + AdUtils.interstitialCount);
                        if (bundle != null) {
                            activity.startActivity(intent, bundle);
                        } else {
                            activity.startActivity(intent);
                        }
                        AdUtils.interstitialCount++;
                    }
                } else {
                    Log.d("--show_interstitial--", "AdUtils.isNetworkAvailable(activity): " + AdUtils.interstitialCount);

                    if (bundle != null) {
                        activity.startActivity(intent, bundle);
                    } else {
                        activity.startActivity(intent);
                    }
                    if (AdUtils.interstitialCount >= AdUtils.showInterstitialOnCount) {
                        AdUtils.interstitialCount = 0;
                    } else {
                        AdUtils.interstitialCount++;
                    }
                }
            } else {
                if (bundle != null) {
                    activity.startActivity(intent, bundle);
                } else {
                    activity.startActivity(intent);
                }
            }
        } else {
            Log.d("--show_interstitial--", "AdUtils.displayAds: else");

            activity.startActivity(intent, bundle);
        }
    }

    public void loadInterstitialAd(Activity activity) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                AdUtils.loadInterstitialAd(activity);

                handler.postDelayed( this, 60 * 1000 );
            }
        });
    }

    public void showBannerAd(Activity activity, RelativeLayout relativeLayout) {
        if (AdUtils.displayAds) {
            if (AdUtils.displayBannerAds) {
                if (AdUtils.isNetworkAvailable(activity)) {
                    AdUtils.showAdmobBannerAd(activity, relativeLayout);
                } else {
                    relativeLayout.getLayoutParams().height = 0;
                }
            } else {
                relativeLayout.getLayoutParams().height = 0;
            }
        } else {
            relativeLayout.getLayoutParams().height = 0;
        }
    }

    public void showNativeAd(Activity activity, RelativeLayout relativeLayout) {
        if (AdUtils.displayAds) {
            if (AdUtils.displayNativeAds) {
                if (AdUtils.isNetworkAvailable(activity)) {
                    AdUtils.showAdmobNativeAd(activity, relativeLayout);
                } else {
                    relativeLayout.getLayoutParams().height = 0;
                }
            } else {
                relativeLayout.getLayoutParams().height = 0;
            }
        } else {
            relativeLayout.getLayoutParams().height = 0;
        }
    }

    public void showSmallNativeAd(Activity activity, RelativeLayout relativeLayout) {
        if (AdUtils.displayAds) {
            if (AdUtils.displayMiniNativeAds) {
                if (AdUtils.isNetworkAvailable(activity)) {
                    AdUtils.showAdmobMiniNativeAd(activity, relativeLayout);
                } else {
                    relativeLayout.getLayoutParams().height = 0;
                }
            } else {
                relativeLayout.getLayoutParams().height = 0;
            }
        } else {
            relativeLayout.getLayoutParams().height = 0;
        }
    }
}