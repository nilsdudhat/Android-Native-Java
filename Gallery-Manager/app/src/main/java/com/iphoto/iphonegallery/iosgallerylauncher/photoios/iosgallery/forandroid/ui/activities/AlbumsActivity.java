package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AlbumAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityAlbumBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.AlbumClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AlbumsActivity extends BaseActivity implements AlbumClickListener {

    ActivityAlbumBinding binding;

    String albumType;

    HashMap<String, ArrayList<FileModel>> albumHashMap = new HashMap<>();

    AlbumAdapter albumAdapter;

    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(AlbumsActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        showBannerAd(AlbumsActivity.this, binding.adBanner);
        loadInterstitialAd(AlbumsActivity.this);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("album_type")) {
            albumType = getIntent().getStringExtra("album_type");
            binding.txtTitle.setText(albumType);
        }

        if (!Constant.INTENT_ALBUM_MODEL_HASHMAP.isEmpty()) {
            albumHashMap = Constant.INTENT_ALBUM_MODEL_HASHMAP;
        }

        Log.d("--albumMap--", "onCreate: " + albumHashMap.size());

        if ((albumHashMap == null) || albumType.isEmpty()) {
            finish();
        } else {
            if (albumHashMap.isEmpty()) {
                finish();
            } else {
                setUpRecyclerView();

                allMediaViewModel.getAllMediaData().observe(AlbumsActivity.this, new Observer<List<MediaModel>>() {
                    @Override
                    public void onChanged(List<MediaModel> mediaModelList) {
                        if (!isFirstTime) {
                            refreshData(mediaModelList);
                        } else {
                            isFirstTime = false;
                        }
                    }
                });
            }
        }
    }

    private void refreshData(List<MediaModel> mediaModelList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaModelList.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModelList);
                    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

                    for (int i = 0; i < mediaModelArrayList.size(); i++) {
                        MediaModel mediaModel = mediaModelArrayList.get(i);

                        FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
                        fileModelArrayList.add(fileModel);
                    }

                    Collections.sort(fileModelArrayList, new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(rhs.getDateModified())).compareTo(f.parse(lhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });

                    HashMap<String, ArrayList<FileModel>> tempAlbumHashMap = new HashMap<>(MediaLoader.getAllAlbumHashMap(fileModelArrayList));

                    Log.d("--albums--", "tempAlbumHashMap: " + tempAlbumHashMap.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            albumsViewModel.getAllFavorites().observe(AlbumsActivity.this, new Observer<List<AlbumDBModel.FavoriteDBModel>>() {
                                @Override
                                public void onChanged(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<FileModel> favoritesList = new ArrayList<>();
                                            ArrayList<AlbumDBModel.FavoriteDBModel> tempFavoritesList = new ArrayList<>(favoriteDBModels);

                                            for (int i = 0; i < tempFavoritesList.size(); i++) {
                                                AlbumDBModel.FavoriteDBModel favoriteDBModel = tempFavoritesList.get(i);

                                                for (int j = 0; j < fileModelArrayList.size(); j++) {
                                                    FileModel fileModel = fileModelArrayList.get(i);

                                                    if (fileModel.getPath().equals(favoriteDBModel.getPath())) {
                                                        favoritesList.add(fileModel);
                                                    }
                                                }
                                            }

                                            if (!favoritesList.isEmpty()) {
                                                tempAlbumHashMap.put("Favorites", favoritesList);
                                            }

                                            if (!fileModelArrayList.isEmpty()) {
                                                tempAlbumHashMap.put("Recents", fileModelArrayList);
                                            }

                                            ArrayList<FileModel> cameraList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Camera/", fileModelArrayList));
                                            if (!cameraList.isEmpty()) {
                                                tempAlbumHashMap.put("Camera", cameraList);
                                            }

                                            ArrayList<FileModel> videoList = new ArrayList<>(MediaLoader.getVideosList(fileModelArrayList));
                                            if (!videoList.isEmpty()) {
                                                tempAlbumHashMap.put("Videos", videoList);
                                            }

                                            ArrayList<FileModel> downloadList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Download/", fileModelArrayList));
                                            if (!downloadList.isEmpty()) {
                                                tempAlbumHashMap.put("Download", downloadList);
                                            }

                                            ArrayList<FileModel> screenshotList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Screenshots/", fileModelArrayList));
                                            if (!screenshotList.isEmpty()) {
                                                tempAlbumHashMap.put("Screenshots", screenshotList);
                                            }

                                            albumHashMap = new HashMap<>(tempAlbumHashMap);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    albumAdapter.swapList(albumType, albumHashMap);
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }

    private void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AlbumsActivity.this, 2);
        binding.rvAlbumGrid.setLayoutManager(gridLayoutManager);

        albumAdapter = new AlbumAdapter(AlbumsActivity.this, "AlbumsActivity", this);
        binding.rvAlbumGrid.setAdapter(albumAdapter);

        albumAdapter.swapList(albumType, albumHashMap);
    }

    @Override
    public void onAlbumClick(String albumName, ArrayList<FileModel> fileModelList) {
        Intent intent = new Intent(AlbumsActivity.this, AlbumMediaActivity.class);
        intent.putExtra("albumName", albumName);
//        intent.putExtra("fileModelArrayList", fileModelList);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelList);
//        startActivity(intent);
        showInterstitialAd(AlbumsActivity.this, intent, null);
    }
}
