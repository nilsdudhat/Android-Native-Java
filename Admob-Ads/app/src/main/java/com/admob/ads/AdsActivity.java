package com.admob.ads;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        loadAds();
    }

    private void loadAds() {
        loadBannerAd(findViewById(R.id.rl_banner));
    }

    private void loadBannerAd(RelativeLayout rl_banner) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(AdsActivity.this, adWidth);

        AdView adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setAdSize(AdSize.BANNER);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d("--ads--", "BANNER-- onAdClicked: ");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d("--ads--", "BANNER-- onAdClosed: ");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("--ads--", "BANNER-- onAdFailedToLoad: " + adError.getMessage());
                rl_banner.getLayoutParams().height = 0;
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.d("--ads--", "BANNER-- onAdImpression: ");
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("--ads--", "BANNER-- onAdLoaded: ");

                rl_banner.getLayoutParams().height = adSize.getHeightInPixels(AdsActivity.this);

                rl_banner.removeAllViews();
                rl_banner.addView(adView);
                rl_banner.setGravity(Gravity.CENTER);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("--ads--", "BANNER-- onAdOpened: ");
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}