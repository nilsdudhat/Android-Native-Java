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

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.PlacesMediaAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityPlacesDetailBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DeleteMediaManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PlacesMediaActivity extends BaseActivity implements MediaAdapterClickListener {

    ActivityPlacesDetailBinding binding;

    String address = "";

    PlacesMediaAdapter placesMediaAdapter;

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    boolean isFirstTime = true;

    ArrayList<Integer> selectedItems = new ArrayList<>();
    boolean isMultipleSelection = false;
    boolean isAlreadyFavorites = true;

    List<File> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(PlacesMediaActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityPlacesDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        loadInterstitialAd(PlacesMediaActivity.this);
        showBannerAd(PlacesMediaActivity.this, binding.adBanner);

        binding.imgBack.setOnClickListener(v -> onBackPressed());

        if (getIntent().hasExtra("address")) {
            address = getIntent().getStringExtra("address");

            binding.txtMediaTitle.setText(address);

            WrapperGridLayoutManager gridLayoutManager = new WrapperGridLayoutManager(PlacesMediaActivity.this, 3);
            binding.rvMedia.setLayoutManager(gridLayoutManager);

            placesMediaAdapter = new PlacesMediaAdapter(PlacesMediaActivity.this, address, this);
            binding.rvMedia.setAdapter(placesMediaAdapter);

            if (!Constant.INTENT_FILE_MODEL_ARRAY_LIST.isEmpty()) {
                fileModelArrayList = new ArrayList<>(Constant.INTENT_FILE_MODEL_ARRAY_LIST);
            }

            if (fileModelArrayList.isEmpty()) {
                finish();
            } else {
                placesMediaAdapter.swapList(fileModelArrayList);

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
                        placesMediaAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                        placesMediaAdapter.notifyItemRangeChanged(0, fileModelArrayList.size());
                    }
                });

                albumsViewModel.getAllAddressData().observe(PlacesMediaActivity.this, new Observer<List<AlbumDBModel.AddressDBModel>>() {
                    @Override
                    public void onChanged(List<AlbumDBModel.AddressDBModel> addressDBModels) {
                        if (!isFirstTime) {
                            refreshAddressData(addressDBModels);
                        } else {
                            isFirstTime = false;
                        }
                    }
                });
            }
        } else {
            finish();
        }
    }

    private void refreshAddressData(List<AlbumDBModel.AddressDBModel> addressDBModels) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<FileModel> tempFileList = new ArrayList<>();

                for (int i = 0; i < addressDBModels.size(); i++) {
                    AlbumDBModel.AddressDBModel addressDBModel = addressDBModels.get(i);

                    if (address.equals(addressDBModel.getAddress())) {
                        FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);
                        tempFileList.add(fileModel);
                    }
                }

                Collections.sort(tempFileList, new Comparator<FileModel>() {
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                    @Override
                    public int compare(FileModel lhs, FileModel rhs) {
                        try {
                            return Objects.requireNonNull(simpleDateFormat.parse(lhs.getDateModified())).compareTo(simpleDateFormat.parse(rhs.getDateModified()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });

                fileModelArrayList = new ArrayList<>(tempFileList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fileModelArrayList.isEmpty()) {
                            finish();
                        } else {
                            placesMediaAdapter.swapList(fileModelArrayList);
                        }
                    }
                });
            }
        }).start();
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

                        FileModel fileModel = fileModelArrayList.get(position);
                        fileList.add(new File(fileModel.getPath()));
                    }

                    Log.d("--selection--", "fileList: " + Arrays.asList(fileList.toArray()));
                    Log.d("--selection--", "albumFileList: " + fileModelArrayList.size());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        try {
                            DeleteMediaManager.deleteFilesAbove10(fileList, 2021, PlacesMediaActivity.this, new Intent());
                        } catch (Exception e) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PlacesMediaActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                                    isMultipleSelection = false;

                                    selectedItems = new ArrayList<>();
                                    fileList = new ArrayList<>();

                                    placesMediaAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                                    placesMediaAdapter.notifyItemRangeChanged(0, fileModelArrayList.size());
                                }
                            });
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialog dialog = new Dialog(PlacesMediaActivity.this, R.style.CustomDialog);
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

                                        ProgressDialog progressDialog = new ProgressDialog(PlacesMediaActivity.this);
                                        progressDialog.setMessage("Deleting Files");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        for (int i = 0; i < fileList.size(); i++) {
                                            File file = fileList.get(i);

                                            if (file.exists()) {
                                                if (file.delete()) {
                                                    MediaScannerConnection.scanFile(PlacesMediaActivity.this,
                                                            new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {

                                                            });
                                                    isFavorite(file.getAbsolutePath(), selectedItems.get(i));
                                                    removeFromPlaces(file.getAbsolutePath());
                                                } else {
                                                    Toast.makeText(PlacesMediaActivity.this, "Could not delete this file.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        selectedItems = new ArrayList<>();
                                        isMultipleSelection = false;

                                        binding.llMain.setVisibility(View.GONE);
                                        binding.txtSelect.setText("Select");
                                        placesMediaAdapter.swapSelectedList(selectedItems, isMultipleSelection);

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
                            Toast.makeText(PlacesMediaActivity.this, "No Media files are selected.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void removeFromPlaces(String path) {
        AlbumDBModel.AddressDBModel addressDBModel = albumsViewModel.getAddressByPath(path);
        albumsViewModel.deleteAddress(addressDBModel);
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
                        Log.d("--check--", "albumFileList: " + fileModelArrayList.size());

                        FileModel selectedFileModel = fileModelArrayList.get(position);

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
                                Toast.makeText(PlacesMediaActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();

                                isMultipleSelection = false;
                                selectedItems = new ArrayList<>();

                                binding.llMain.setVisibility(View.GONE);
                                binding.txtSelect.setText("Select");

                                placesMediaAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                                placesMediaAdapter.notifyItemRangeChanged(0, fileModelArrayList.size());
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PlacesMediaActivity.this, "Already in Favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlacesMediaActivity.this, "No Media files are selected.", Toast.LENGTH_SHORT).show();
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
                                Log.d("--check--", "albumFileList: " + fileModelArrayList.size());

                                FileModel fileModel = fileModelArrayList.get(position);

                                isFavorite(fileModel.getPath(), position);
                                removeFromPlaces(fileModel.getPath());
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

                                    placesMediaAdapter.swapSelectedList(selectedItems, isMultipleSelection);
                                    placesMediaAdapter.notifyItemRangeChanged(0, fileModelArrayList.size());

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

            placesMediaAdapter.notifySingleSelection(selectedItems, position);
        } else {
            Intent intent = new Intent(PlacesMediaActivity.this, FullScreenMediaActivity.class);

//            intent.putParcelableArrayListExtra("fileModelArrayList", fileModelArrayList);
            Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
            intent.putExtra("position", position);
            intent.putExtra("mediaType", mediaType);
            intent.putExtra("subMediaType", subMediaType);

//        startActivity(intent);
            showInterstitialAd(PlacesMediaActivity.this, intent, null);
        }
    }
}
