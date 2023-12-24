package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kabouzeid.appthemehelper.ThemeStore;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdUtils;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.MyApplication;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.base.AbsBaseActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AbsBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                int accentColorPref = ThemeStore.accentColor(SplashActivity.this);
                int accent_color = ContextCompat.getColor(SplashActivity.this, R.color.color_accent);

                if (accentColorPref != accent_color) {
                    ThemeStore.editTheme(SplashActivity.this)
                            .accentColor(ContextCompat.getColor(SplashActivity.this, R.color.color_accent))
                            .commit();
                }

                callOpenAd();
            }
        }, 1000);
    }

    private void nextActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    private void callOpenAd() {
        if (AdUtils.isNetworkAvailable(SplashActivity.this)) {
            if (AdUtils.displayAds) {
                if (AdUtils.displayOpenAppAds) {

                    Application application = getApplication();

                    // If the application is not an instance of MyApplication, log an error message and
                    // start the MainActivity without showing the app open ad.
                    if (!(application instanceof MyApplication)) {
                        Log.e("--open_ad--", "Failed to cast application to MyApplication.");
                        nextActivity();
                        return;
                    }

                    // Show the app open ad.
                    ((MyApplication) application)
                            .showAdIfAvailable(
                                    SplashActivity.this,
                                    new MyApplication.OnShowAdCompleteListener() {
                                        @Override
                                        public void onShowAdComplete() {
                                            nextActivity();
                                        }
                                    });
                } else {
                    nextActivity();
                }
            } else {
                nextActivity();
            }
        } else {
            nextActivity();
        }
    }
}
