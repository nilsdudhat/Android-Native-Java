package com.cartoon2021.photo.editor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class MyApplication extends Application {

    static SharedPreferences.Editor prefEditor;
    static SharedPreferences preferences;
    public static String timeString;

    public void onCreate() {
        super.onCreate();
        timeString = "";
        SharedPreferences sharedPreferences = getSharedPreferences("myGamePreferences", 0);
        preferences = sharedPreferences;
        prefEditor = sharedPreferences.edit();

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
