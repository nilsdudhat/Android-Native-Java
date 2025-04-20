package com.allvideo.hdplayer.AdsIntegration;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, initializationStatus -> {
        });
        AudienceNetworkAds.initialize(this);
        AudienceNetworkAds.buildInitSettings(this).initialize();

        StartAppSDK.init(this, "210441667", false);
        StartAppAd.disableAutoInterstitial();
        StartAppAd.disableSplash();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
