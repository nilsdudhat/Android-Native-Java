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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.SelectionMediaAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivitySelectionMediaBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.SelectionClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DeleteMediaManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SelectionMediaActivity extends BaseActivity implements SelectionClickListener {

    ActivitySelectionMediaBinding binding;

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    SelectionMediaAdapter selectionMediaAdapter;

    MutableLiveData<ArrayList<Integer>> selectedData;

    boolean isAlreadyFavorites = true;
    List<File> fileList = new ArrayList<>();
    ArrayList<Integer> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(SelectionMediaActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        loadInterstitialAd(SelectionMediaActivity.this);
        showBannerAd(SelectionMediaActivity.this, binding.adBanner);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!Constant.INTENT_FILE_MODEL_ARRAY_LIST.isEmpty()) {
            fileModelArrayList = new ArrayList<>(Constant.INTENT_FILE_MODEL_ARRAY_LIST);
        }

        if (fileModelArrayList == null) {
            finish();
        }

        if (fileModelArrayList.isEmpty()) {
            finish();
        } else {
            setUpRecyclerView();

            if (selectedData == null) {
                selectedData = new MutableLiveData<>();
            }

            selectedData.observe(SelectionMediaActivity.this, new Observer<ArrayList<Integer>>() {
                @Override
                public void onChanged(ArrayList<Integer> integers) {
                    binding.txtCount.setText(integers.size() + " Items Selected");
                }
            });

            binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteMediaFiles();
                }
            });

            binding.imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToFavorites();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == 2021) {
                        Log.d("--selection--", "onActivityResult: " + "Success");
                        for (int i = 0; i < selectedItems.size(); i++) {
                            int position = selectedItems.get(i);

                            removeFromDataBase(position);
                        }
                        selectedItems = new ArrayList<>();
                        fileList = new ArrayList<>();

                        Log.d("--selection--", "favorites checked and deleted: " + "Success");

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                selectedData.setValue(selectedItems);

                                selectionMediaAdapter.swapSelectedItems(selectedItems);
                                selectionMediaAdapter.swapList(fileModelArrayList);

                                Log.d("--selection--", "All Done: ");
                            }
                        });
                    }
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

                    for (int i = 0; i < selectedItems.size(); i++) {
                        int position = selectedItems.get(i);

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
                                Toast.makeText(SelectionMediaActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();

                                selectedItems = new ArrayList<>();
                                selectedData.setValue(selectedItems);
                                selectionMediaAdapter.swapSelectedItems(selectedItems);
                                selectionMediaAdapter.notifyItemRangeChanged(0, fileModelArrayList.size());
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SelectionMediaActivity.this, "Already in Favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SelectionMediaActivity.this, "No Media files are selected.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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

                    Log.d("--selection--", "run: " + Arrays.asList(selectedItems.toArray()));

                    for (int i = 0; i < selectedItems.size(); i++) {
                        int position = selectedItems.get(i);

                        FileModel fileModel = fileModelArrayList.get(position);
                        fileList.add(new File(fileModel.getPath()));
                    }

                    Log.d("--selection--", "run: " + Arrays.asList(fileList.toArray()));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        DeleteMediaManager.deleteFilesAbove10(fileList, 2021, SelectionMediaActivity.this, new Intent());
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialog dialog = new Dialog(SelectionMediaActivity.this, R.style.CustomDialog);
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

                                        ProgressDialog progressDialog = new ProgressDialog(SelectionMediaActivity.this);
                                        progressDialog.setMessage("Deleting Files");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        for (int i = 0; i < fileList.size(); i++) {
                                            File file = fileList.get(i);

                                            if (file.exists()) {
                                                if (file.delete()) {
                                                    MediaScannerConnection.scanFile(SelectionMediaActivity.this,
                                                            new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {

                                                            });
                                                    removeFromDataBase(selectedItems.get(i));
                                                } else {
                                                    Toast.makeText(SelectionMediaActivity.this, "Could not delete this file.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        selectedItems = new ArrayList<>();
                                        selectedData.setValue(selectedItems);

                                        selectionMediaAdapter.swapSelectedItems(selectedItems);
                                        selectionMediaAdapter.swapList(fileModelArrayList);

                                        fileList = new ArrayList<>();
                                        progressDialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void removeFromDataBase(int position) {

        new Thread(() -> {
            FileModel fileModel = fileModelArrayList.get(position);
            if (allMediaViewModel.isMediaExist(fileModel.getPath())) {
                MediaModel mediaModel = allMediaViewModel.getMediaModelByPath(fileModel.getPath());
                allMediaViewModel.deleteMedia(mediaModel);
            }
            if (albumsViewModel.isFavoriteExist(fileModel.getPath())) {
                AlbumDBModel.FavoriteDBModel favoriteDBModel = albumsViewModel.getFavoriteByPath(fileModel.getPath());
                albumsViewModel.deleteFavorite(favoriteDBModel);
            }
            if (albumsViewModel.isFileModelWithAddressExist(fileModel.getPath())) {
                AlbumDBModel.AddressDBModel addressDBModel = albumsViewModel.getAddressByPath(fileModel.getPath());
                albumsViewModel.deleteAddress(addressDBModel);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fileModelArrayList.remove(position);

                    if (fileModelArrayList.isEmpty()) {
                        finish();
                    } else {
                        selectionMediaAdapter.swapList(fileModelArrayList);
                    }
                }
            });
        }).start();
    }

    private void setUpRecyclerView() {
        selectionMediaAdapter = new SelectionMediaAdapter(SelectionMediaActivity.this, this);
        selectionMediaAdapter.swapList(fileModelArrayList);

        binding.rvSelectionMedia.setLayoutManager(new WrapperGridLayoutManager(SelectionMediaActivity.this, 3));
        binding.rvSelectionMedia.setAdapter(selectionMediaAdapter);
    }

    @Override
    public void onSelect(int position) {
        Log.d("--selection--", "onSelect: " + position);
        if (selectedItems.contains(position)) {
            selectedItems.remove((Integer) position);
        } else {
            selectedItems.add(position);
        }
        selectedData.setValue(selectedItems);
        selectionMediaAdapter.notifyItemChanged(position);
        selectionMediaAdapter.swapSelectedItems(selectedItems);
    }
}
