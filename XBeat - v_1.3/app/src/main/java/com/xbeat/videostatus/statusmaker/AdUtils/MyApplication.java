package com.xbeat.videostatus.statusmaker.AdUtils;

import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class MyApplication extends MultiDexApplication {

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
}
