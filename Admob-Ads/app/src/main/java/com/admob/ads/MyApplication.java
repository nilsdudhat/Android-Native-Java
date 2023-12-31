package com.admob.ads;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("BE93F08B3737533CCA5399A6B776B60F")).build());

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, initializationStatus -> {
            Log.d("--ads--", "onInitializationComplete: " + initializationStatus.getAdapterStatusMap());
        });
    }
}
