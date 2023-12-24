package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AdUtils;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.MyApplication;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFoldersViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media.MediaLoader;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends PermissionManagerActivity {

    VideoViewModel videoViewModel;
    VideoFoldersViewModel videoFoldersViewModel;

    SharedPreferences mPrefs;
    boolean isFirstTime;

    ConstraintLayout constraint_start;
    TextView txt_start;

    ImageView img_logo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mPrefs = getApplicationContext().getSharedPreferences("app_preferences", MODE_PRIVATE);

        boolean isDark = mPrefs.getBoolean("theme", false);
        isFirstTime = mPrefs.getBoolean("first_time", true);
        mPrefs.edit().putBoolean("floating_btn", false).apply(); // disabling floating button
        mPrefs.edit().putBoolean("theme", false).apply(); // disabling theme button
        Log.d("--theme--", "isDark: " + isDark);


        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Utils.bottomNavigationBlackColor(SplashActivity.this);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Utils.bottomNavigationWhiteColor(SplashActivity.this);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        constraint_start = findViewById(R.id.constraint_start);
        txt_start = findViewById(R.id.txt_start);
        img_logo = findViewById(R.id.img_logo);


        txt_start.setOnClickListener(v -> {
            mPrefs.edit().putBoolean("first_time", false).apply();
            nextActivity();

        });

        videoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VideoViewModel.class);
        videoFoldersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VideoFoldersViewModel.class);

        int padding = getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._75sdp);

        if (isFirstTime) {
            constraint_start.setVisibility(View.INVISIBLE);
        } else {
            constraint_start.setVisibility(View.GONE);
        }
        img_logo.setPadding(padding, padding, padding, padding);

        setPermissionDeniedMessage(getString(R.string.permission_external_storage_denied));

        checkRunTimePermission();

    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

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
            getVideoData();
        }
    }


    private void getVideoData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                videoViewModel.deleteAllVideos();
                videoFoldersViewModel.deleteAllVideoFolders();

                MediaLoader.storeVideoFilesInDatabase(SplashActivity.this, videoViewModel, videoFoldersViewModel);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        videoViewModel.getAllVideos().observe(SplashActivity.this, new Observer<List<VideoModel>>() {
                            @Override
                            public void onChanged(List<VideoModel> videoModels) {
                                if (!videoModels.isEmpty()) {
                                    Utils.videoModelArrayList = videoModels;
                                    Log.d("--list--", "videoFolderModelArrayList: " + Utils.videoFolderModelArrayList.toString());
                                }
                            }
                        });

                        videoFoldersViewModel.getAllVideoFolders().observe(SplashActivity.this, new Observer<List<VideoFolderModel>>() {
                            @Override
                            public void onChanged(List<VideoFolderModel> videoFolderModels) {
                                if (!videoFolderModels.isEmpty()) {
                                    Utils.videoFolderModelArrayList = videoFolderModels;
                                    Log.d("--list--", "videoModelArrayList: " + Utils.videoModelArrayList.toString());
                                }
                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {

                                        callOpenAppAd();

                                        Log.d("--size--", "onPostExecute: " + Utils.videoFolderModelArrayList.size());
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        }).start();
    }

    private void callOpenAppAd() {
        if (AdUtils.isNetworkAvailable(SplashActivity.this)) {
            if (AdUtils.displayAds) {
                if (AdUtils.displayOpenAppAds) {

                    Application application = getApplication();

                    // If the application is not an instance of MyApplication, log an error message and
                    // start the MainActivity without showing the app open ad.
                    if (!(application instanceof MyApplication)) {
                        Log.e("--open_ad--", "Failed to cast application to MyApplication.");
                        isFirstTime();
                        return;
                    }

                    // Show the app open ad.
                    ((MyApplication) application)
                            .showAdIfAvailable(
                                    SplashActivity.this,
                                    new MyApplication.OnShowAdCompleteListener() {
                                        @Override
                                        public void onShowAdComplete() {
                                            isFirstTime();
                                        }
                                    });
                } else {
                    isFirstTime();
                }
            } else {
                isFirstTime();
            }
        } else {
            isFirstTime();
        }
    }

    private void isFirstTime() {
        if (isFirstTime) {
            final Animation animationFadeIn = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_fade_in);
            constraint_start.startAnimation(animationFadeIn);
            constraint_start.setVisibility(View.VISIBLE);
        } else {
            nextActivity();
        }
    }

    private void nextActivity() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onHasPermissionsChanged(boolean hasPermissions) {
        super.onHasPermissionsChanged(hasPermissions);

        getVideoData();
    }

    @Nullable
    @Override
    protected String[] getPermissionsToRequest() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
}