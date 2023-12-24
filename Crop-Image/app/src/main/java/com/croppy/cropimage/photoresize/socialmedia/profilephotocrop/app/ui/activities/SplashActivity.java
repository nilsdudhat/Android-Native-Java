package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.MyApplication;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adsintegration.AdUtils;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.databinding.ActivitySplashBinding;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.CacheUtils;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Utils;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends PermissionManagerActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.setBottomNavigationColor(SplashActivity.this, getColor(R.color.black));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRunTimePermission();
            }
        }, 1000);
    }

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

    private void nextActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showInterstitialAd(SplashActivity.this, new Intent(SplashActivity.this, MainActivity.class), null);
            }
        }, 1000);
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{
                Manifest.permission.CAMERA,
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
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
}
