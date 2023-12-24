package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AlbumMediaListAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityAlbumMediaBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DeleteMediaManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AlbumMediaActivity extends BaseActivity implements MediaAdapterClickListener {

    ActivityAlbumMediaBinding binding;

    AlbumMediaListAdapter albumMediaListAdapter;

    String selectedAlbumName;
    ArrayList<FileModel> albumMediaList = new ArrayList<>();

    boolean isFirstTime = true;

    ArrayList<Integer> selectedItems = new ArrayList<>();
    boolean isMultipleSelection = false;
    boolean isAlreadyFavorites = true;

    List<File> fileList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.setTheme(AlbumMediaActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        showBannerAd(AlbumMediaActivity.this, binding.adBanner);
        loadInterstitialAd(AlbumMediaActivity.this);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        selectedAlbumName = getIntent().getStringExtra("albumName");
        binding.txtMediaTitle.setText("Album: " + selectedAlbumName);
        Log.d("--name--", "onCreate: " + selectedAlbumName);

        if (!Constant.INTENT_FILE_MODEL_ARRAY_LIST.isEmpty()) {
            albumMediaList = new ArrayList<>(Constant.INTENT_FILE_MODEL_ARRAY_LIST);
        }

        if (albumMediaList == null) {
            finish();
        } else {
            if (albumMediaList.isEmpty()) {
                finish();
            } else {
                setUpRecyclerView();

                binding.imgFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToFavorites();
                    }
                });

                binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMediaFiles();
                    }
                });

                binding.frameSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isMultipleSelection) {
                            isMultipleSelection = false;

                            selectedItems = new ArrayList<>();
                            fileList = new ArrayList<>();

                            binding.txtSelect.setText("Select");
                            binding.llMain.setVisibility(View.GONE);
                        } else {
                            isMultipleSelection = true;

                            binding.txtSelect.setText("Cancel");
                            binding.llMain.setVisibility(View.VISIBLE);
                        }
                        albumMediaListAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                        albumMediaListAdapter.notifyItemRangeChanged(0, albumMediaList.size());
                    }
                });

                allMediaViewModel.getAllMediaData().observe(AlbumMediaActivity.this, new Observer<List<MediaModel>>() {
                    @Override
                    public void onChanged(List<MediaModel> mediaModelList) {
                        refreshData(mediaModelList);
                    }
                });
            }
        }
    }

    private void refreshData(List<MediaModel> mediaModelList) {
        final long[] startTime = {System.currentTimeMillis()};
        final long[] pauseTime = {0};

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaModelList.isEmpty()) {
                    if ((System.currentTimeMillis() - startTime[0]) >= 1000) {
                        pauseTime[0] = 0;
                    } else {
                        pauseTime[0] = 500;
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, pauseTime[0]);
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
                            albumsViewModel.getAllFavorites().observe(AlbumMediaActivity.this, new Observer<List<AlbumDBModel.FavoriteDBModel>>() {
                                @Override
                                public void onChanged(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {
                                    if ((System.currentTimeMillis() - startTime[0]) >= 2000) {
                                        startTime[0] = System.currentTimeMillis();
                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<FileModel> favoritesList = new ArrayList<>();
                                            ArrayList<AlbumDBModel.FavoriteDBModel> tempFavoritesList = new ArrayList<>(favoriteDBModels);

                                            for (int i = 0; i < tempFavoritesList.size(); i++) {
                                                AlbumDBModel.FavoriteDBModel favoriteDBModel = tempFavoritesList.get(i);

                                                for (int j = 0; j < fileModelArrayList.size(); j++) {
                                                    FileModel fileModel = fileModelArrayList.get(j);

                                                    if (fileModel.getPath().equals(favoriteDBModel.getPath())) {
                                                        favoritesList.add(fileModel);
                                                    }
                                                }
                                            }

                                            if (!favoritesList.isEmpty()) {
                                                tempAlbumHashMap.put("Favorites", favoritesList);
                                            } else {
                                                tempAlbumHashMap.put("Favorites", new ArrayList<>());
                                            }

                                            if (!fileModelArrayList.isEmpty()) {
                                                tempAlbumHashMap.put("Recents", fileModelArrayList);
                                            } else {
                                                tempAlbumHashMap.put("Recents", new ArrayList<>());
                                            }

                                            ArrayList<FileModel> cameraList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Camera/", fileModelArrayList));
                                            if (!cameraList.isEmpty()) {
                                                tempAlbumHashMap.put("Camera", cameraList);
                                            } else {
                                                tempAlbumHashMap.put("Camera", new ArrayList<>());
                                            }

                                            ArrayList<FileModel> videoList = new ArrayList<>(MediaLoader.getVideosList(fileModelArrayList));
                                            if (!videoList.isEmpty()) {
                                                tempAlbumHashMap.put("Videos", videoList);
                                            } else {
                                                tempAlbumHashMap.put("Videos", new ArrayList<>());
                                            }

                                            ArrayList<FileModel> downloadList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Download/", fileModelArrayList));
                                            if (!downloadList.isEmpty()) {
                                                tempAlbumHashMap.put("Download", downloadList);
                                            } else {
                                                tempAlbumHashMap.put("Download", new ArrayList<>());
                                            }

                                            ArrayList<FileModel> screenshotList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Screenshots/", fileModelArrayList));
                                            if (!screenshotList.isEmpty()) {
                                                tempAlbumHashMap.put("Screenshots", screenshotList);
                                            } else {
                                                tempAlbumHashMap.put("Screenshots", new ArrayList<>());
                                            }

                                            albumMediaList = new ArrayList<>();
                                            albumMediaList = tempAlbumHashMap.get(selectedAlbumName);

                                            if (!isFirstTime) {
                                                if ((System.currentTimeMillis() - startTime[0]) >= 1000) {
                                                    pauseTime[0] = 0;
                                                } else {
                                                    pauseTime[0] = 500;
                                                }
                                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (albumMediaList == null) {
                                                            finish();
                                                        } else {
                                                            if (albumMediaList.isEmpty()) {
                                                                finish();
                                                            } else {
                                                                albumMediaListAdapter.swapList(albumMediaList);
                                                            }
                                                        }
                                                    }
                                                }, pauseTime[0]);
                                            } else {
                                                isFirstTime = false;
                                            }
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
        WrapperGridLayoutManager gridLayoutManager = new WrapperGridLayoutManager(AlbumMediaActivity.this, 3);
        binding.rvAlbumGrid.setLayoutManager(gridLayoutManager);

        albumMediaListAdapter = new AlbumMediaListAdapter(AlbumMediaActivity.this, selectedAlbumName, this);
        binding.rvAlbumGrid.setAdapter(albumMediaListAdapter);

        albumMediaListAdapter.swapList(albumMediaList);
    }

    private void deleteMediaFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!selectedItems.isEmpty()) {
                    fileList = new ArrayList<>();

                    /* sorting array in ascending order */
                    Collections.sort(selectedItems);

                    /* reverse array (descending order) */
                    Collections.reverse(selectedItems);

                    Log.d("--selection--", "selectedItems: " + Arrays.asList(selectedItems.toArray()));

                    for (int i = 0; i < selectedItems.size(); i++) {
                        int position = selectedItems.get(i);

                        FileModel fileModel = albumMediaList.get(position);
                        fileList.add(new File(fileModel.getPath()));
                    }

                    Log.d("--selection--", "fileList: " + Arrays.asList(fileList.toArray()));
                    Log.d("--selection--", "albumFileList: " + albumMediaList.size());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        try {
                            DeleteMediaManager.deleteFilesAbove10(fileList, 2021, AlbumMediaActivity.this, new Intent());
                        } catch (Exception e) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AlbumMediaActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                                    isMultipleSelection = false;

                                    selectedItems = new ArrayList<>();
                                    fileList = new ArrayList<>();

                                    albumMediaListAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                                    albumMediaListAdapter.notifyItemRangeChanged(0, albumMediaList.size());
                                }
                            });
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialog dialog = new Dialog(AlbumMediaActivity.this, R.style.CustomDialog);
                                dialog.setCancelable(true);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.setContentView(R.layout.dialog_delete);
                                dialog.getWindow().setGravity(Gravity.BOTTOM);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                TextView txt_title = dialog.findViewById(R.id.txt_title);
                                TextView txt_cancel = dialog.findViewById(R.id.txt_create);
                                TextView txt_ok = dialog.findViewById(R.id.txt_ok);

                                txt_title.setText("Do you want to delete selected media files?");

                                txt_cancel.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                });

                                txt_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                        ProgressDialog progressDialog = new ProgressDialog(AlbumMediaActivity.this);
                                        progressDialog.setMessage("Deleting Files");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        for (int i = 0; i < fileList.size(); i++) {
                                            File file = fileList.get(i);

                                            if (file.exists()) {
                                                if (file.delete()) {
                                                    MediaScannerConnection.scanFile(AlbumMediaActivity.this,
                                                            new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {

                                                            });
                                                    isFavorite(file.getAbsolutePath(), selectedItems.get(i));
                                                } else {
                                                    Toast.makeText(AlbumMediaActivity.this, "Could not delete this file.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        selectedItems = new ArrayList<>();
                                        isMultipleSelection = false;

                                        binding.llMain.setVisibility(View.GONE);
                                        binding.txtSelect.setText("Select");
                                        albumMediaListAdapter.swapSelectedList(selectedItems, isMultipleSelection);

                                        fileList = new ArrayList<>();
                                        progressDialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlbumMediaActivity.this, "No Media files are selected.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void isFavorite(String path, int position) {
        Log.d("--selection--", "isFavorite: " + path);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (albumsViewModel.isFavoriteExist(path)) {
                    Log.d("--selection--", "isFavorite: " + path);
                    AlbumDBModel.FavoriteDBModel albumDBModel = albumsViewModel.getFavoriteByPath(path);
                    albumsViewModel.deleteFavorite(albumDBModel);
                }
            }
        }).start();
    }

    private void addToFavorites() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!selectedItems.isEmpty()) {
                    ArrayList<AlbumDBModel.FavoriteDBModel> selectedFavoriteArrayList = new ArrayList<>();
                    Log.d("--check--", "selectedItems: " + Arrays.asList(selectedItems.toArray()));

                    for (int i = 0; i < selectedItems.size(); i++) {
                        int position = selectedItems.get(i);

                        Log.d("--check--", "position: " + position);
                        Log.d("--check--", "albumFileList: " + albumMediaList.size());

                        FileModel selectedFileModel = albumMediaList.get(position);

                        if (!albumsViewModel.isFavoriteExist(selectedFileModel.getPath())) {
                            AlbumDBModel.FavoriteDBModel albumDBModel = new AlbumDBModel.FavoriteDBModel(
                                    selectedFileModel.getId(),
                                    selectedFileModel.getPath(),
                                    selectedFileModel.getDateModified(),
                                    selectedFileModel.getFileFormat(),
                                    selectedFileModel.getDuration(),
                                    selectedFileModel.getSize());

                            selectedFavoriteArrayList.add(albumDBModel);
                        }
                    }

                    for (int i = 0; i < selectedFavoriteArrayList.size(); i++) {
                        AlbumDBModel.FavoriteDBModel albumDBModel = selectedFavoriteArrayList.get(i);

                        if (!albumsViewModel.isFavoriteExist(albumDBModel.getPath())) {
                            albumsViewModel.insertFavorite(albumDBModel);

                            isAlreadyFavorites = false;
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isAlreadyFavorites) {
                                isAlreadyFavorites = true;
                                Toast.makeText(AlbumMediaActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();

                                isMultipleSelection = false;
                                selectedItems = new ArrayList<>();

                                binding.llMain.setVisibility(View.GONE);
                                binding.txtSelect.setText("Select");

                                albumMediaListAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                                albumMediaListAdapter.notifyItemRangeChanged(0, albumMediaList.size());
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AlbumMediaActivity.this, "Already in Favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlbumMediaActivity.this, "No Media files are selected.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == 2021) {
                        try {

                            Log.d("--check--", "onActivityResult: " + "Success");
                            Log.d("--check--", "selectedItems: " + selectedItems.size());

                            for (int i = 0; i < selectedItems.size(); i++) {
                                int position = selectedItems.get(i);
                                Log.d("--check--", "position: " + position);
                                Log.d("--check--", "albumFileList: " + albumMediaList.size());

                                FileModel fileModel = albumMediaList.get(position);

                                isFavorite(fileModel.getPath(), position);
                            }
                            isMultipleSelection = false;

                            selectedItems = new ArrayList<>();
                            fileList = new ArrayList<>();

                            Log.d("--selection--", "favorites checked and deleted: " + "Success");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.txtSelect.setText("Select");
                                    binding.llMain.setVisibility(View.GONE);

                                    albumMediaListAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                                    albumMediaListAdapter.notifyItemRangeChanged(0, albumMediaList.size());

                                    fileList = new ArrayList<>();

                                    Log.d("--selection--", "All Done: ");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view, ArrayList<FileModel> fileModelArrayList, int position, String mediaType, String subMediaType) {
        if (isMultipleSelection) {
            if (selectedItems.contains(position)) {
                selectedItems.remove((Integer) position);
            } else {
                selectedItems.add(position);
            }

            albumMediaListAdapter.notifySingleSelection(selectedItems, position);
        } else {
            Intent intent = new Intent(AlbumMediaActivity.this, FullScreenMediaActivity.class);

//            intent.putParcelableArrayListExtra("fileModelArrayList", fileModelArrayList);
            Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
            intent.putExtra("position", position);
            intent.putExtra("mediaType", mediaType);
            intent.putExtra("subMediaType", subMediaType);

//        startActivity(intent);
            showInterstitialAd(AlbumMediaActivity.this, intent, null);
        }
    }
}
