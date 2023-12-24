package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adsintegration.AdsBaseActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityMediaLoadingBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaCursor;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaLoadingActivity extends AdsBaseActivity {

    ActivityMediaLoadingBinding binding;

    AllMediaViewModel allMediaViewModel;

    boolean isLoaded = false;

    boolean isAdded = false;
    boolean isDeleted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.setTheme(MediaLoadingActivity.this); // providing theme for activity before @onCreate
        super.onCreate(savedInstanceState);
        binding = ActivityMediaLoadingBinding.inflate(getLayoutInflater()); // inflating binding
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide(); // removing Action Bar

        loadInterstitialAd(MediaLoadingActivity.this); // loading interstitial ad

        allMediaViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AllMediaViewModel.class);

        allMediaViewModel.getAllMediaData().observe(MediaLoadingActivity.this, new Observer<List<MediaModel>>() {
            @Override
            public void onChanged(List<MediaModel> mediaModels) {
                if (!isLoaded) {
                    isLoaded = true;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

                            for (int i = 0; i < mediaModelArrayList.size(); i++) {
                                MediaModel mediaModel = mediaModelArrayList.get(i);
                                String path = mediaModel.getPath();

                                if (!new File(path).exists()) {
                                    allMediaViewModel.deleteMedia(mediaModel);
                                }
                            }

                            isDeleted = true;

                            if (isAdded) { // checking if files added to database or not
                                nextActivity();
                            }
                        }
                    }).start();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<FileModel> fileModelArrayList = new ArrayList<>(MediaCursor.getAllMedia(MediaLoadingActivity.this));

                for (int i = 0; i < fileModelArrayList.size(); i++) {
                    FileModel fileModel = fileModelArrayList.get(i);
                    String path = fileModel.getPath();

                    if (!allMediaViewModel.isMediaExist(path)) {
                        MediaModel mediaModel = new MediaModel(
                                fileModel.getId(),
                                fileModel.getPath(),
                                fileModel.getDateModified(),
                                fileModel.getFileFormat(),
                                fileModel.getDuration(),
                                fileModel.getSize());
                        allMediaViewModel.insertMedia(mediaModel);
                    }
                }

                isAdded = true;

                if (isDeleted) { // checking if non existing files deleted in database or not
                    nextActivity();
                }
            }
        }).start();
    }

    private void nextActivity() {
        Intent intent = new Intent(MediaLoadingActivity.this, MainActivity.class);
        showInterstitialAd(MediaLoadingActivity.this, intent, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // thread to remove cache files
        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheUtils.deleteCache(MediaLoadingActivity.this);
            }
        }).start();
    }
}
