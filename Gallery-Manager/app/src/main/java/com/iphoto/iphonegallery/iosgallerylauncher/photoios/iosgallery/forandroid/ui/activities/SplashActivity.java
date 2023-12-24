package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.MyApplication;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adsintegration.AdUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivitySplashBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaCursor;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends PermissionManagerActivity {

    AllMediaViewModel allMediaViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.setTheme(SplashActivity.this); // providing theme for activity before @onCreate
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater()); // inflating binding
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide(); // removing Action Bar

        allMediaViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AllMediaViewModel.class);

        // Handler to manage animation of logo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation rotation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_rotate);
                binding.imgLogo.startAnimation(rotation);
            }
        }, 200);

        // Handler to check permissions
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

        // removing cache files
        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheUtils.deleteCache(SplashActivity.this);
            }
        }).start();
    }

    protected void checkRunTimePermission() {
        /* permissions */
        String[] permissionArrays = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionArrays = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
            };
        } else {
            permissionArrays = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

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
                    loadMediaFiles();
                }
            }).start();
        }
    }

    private void callOpenAd() {
        if (AdUtils.isNetworkAvailable(SplashActivity.this)) { // check for internet connection
            if (AdUtils.displayAds) { // check for ads is enabled or not
                if (AdUtils.displayOpenAppAds) { // check for open ads enabled or not

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

    private void loadMediaFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = MediaCursor.makeMediaCursor(SplashActivity.this, null, null);

                int video_count = 0;
                int image_count = 0;

                try {
                    if (cursor != null && cursor.moveToFirst()) {

                        int column_id = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                        int column_path = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                        int column_date_modified = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED);
                        int column_file_format = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE);
                        int column_file_size = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

                        do {
                            String id = cursor.getString(column_id);
                            String path = cursor.getString(column_path);
                            String date_modified = DateUtils.getFullDateFromLong(Long.parseLong(cursor.getString(column_date_modified)));
                            String file_format = cursor.getString(column_file_format);
                            String size = cursor.getString(column_file_size);

                            MediaModel mediaModel = new MediaModel(id, path, date_modified, file_format, "", size);

                            if (!allMediaViewModel.isMediaExist(path)) {
                                allMediaViewModel.insertMedia(mediaModel);
                            }

                            if (file_format.startsWith("image")) {
                                image_count++;
                            } else if (file_format.startsWith("video")) {
                                video_count++;
                            }

                            Log.d("--path--", "getMedias: " + mediaModel.getPath());
                        } while (cursor.moveToNext());

                        PreferenceUtils.setInteger(SplashActivity.this, PreferenceUtils.VIDEO_COUNT, video_count);
                        PreferenceUtils.setInteger(SplashActivity.this, PreferenceUtils.IMAGE_COUNT, image_count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null)
                        cursor.close();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callOpenAd();
                    }
                });
            }
        }).start();
    }

    private void nextActivity() {
        // going for next activity to load media files
        showInterstitialAd(SplashActivity.this, new Intent(SplashActivity.this, MainActivity.class), null);
    }

    /**
     * permission change callback
     */
    @Override
    protected void onHasPermissionsChanged(boolean hasPermissions) {
        super.onHasPermissionsChanged(hasPermissions);

        if (hasPermissions) { // if permission changed
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMediaFiles();
                }
            }, 1000);
        }
    }

    @Nullable
    @Override
    protected String[] getPermissionsToRequest() {
        // asking runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION};
        } else {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }
}
