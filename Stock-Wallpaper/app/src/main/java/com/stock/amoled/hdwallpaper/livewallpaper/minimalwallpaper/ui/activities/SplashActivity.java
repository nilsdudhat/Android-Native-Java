package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.MyApplication;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adsintegration.AdUtils;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivitySplashBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.preferences.ThemePreferences;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.CacheUtils;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends PermissionManagerActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheUtils.deleteCache(SplashActivity.this);
            }
        }).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemePreferences.setTheme(); // setting up theme (Light/Dark)
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        sharedPreferences.edit().putString("last_fragment", "Home").apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRunTimePermission();
            }
        }, 1000);
    }

    private void nextActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        boolean isAllGranted = false;

        for (String permissionArray : permissionArrays) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    permissionArray) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                break;
            } else {
                isAllGranted = true;
            }
        }

        if (isAllGranted) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    callOpenAd();
                }
            }).start();
        }
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

    @Override
    protected void onHasPermissionsChanged(boolean hasPermissions) {
        super.onHasPermissionsChanged(hasPermissions);

        if (hasPermissions) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callOpenAd();
                }
            }, 1000);
        }
    }

    @Nullable
    @Override
    protected String[] getPermissionsToRequest() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
}
