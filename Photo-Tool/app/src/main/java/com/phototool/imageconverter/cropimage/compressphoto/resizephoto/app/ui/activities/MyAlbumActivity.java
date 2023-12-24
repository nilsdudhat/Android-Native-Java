package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters.ImageAdapter;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AdsBaseActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.databinding.ActivityAlbumBinding;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.interfaces.DeleteClickListener;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.interfaces.ImageClickListener;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.media.MediaLoader;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.ImageModel;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.DeleteMediaManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyAlbumActivity extends AdsBaseActivity implements ImageClickListener, DeleteClickListener {

    ActivityAlbumBinding binding;

    ArrayList<ImageModel> pathList = new ArrayList<>();
    ImageAdapter imageAdapter;
    ArrayList<File> fileList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        showBannerAd(MyAlbumActivity.this, findViewById(R.id.ad_banner));
        loadInterstitialAd(MyAlbumActivity.this);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgSelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imgSelDelete.setVisibility(View.GONE);
                imageAdapter.deleteSelectedImages();
            }
        });


        binding.tvTitleAlbumImage.setText(getString(R.string.app_name));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyAlbumActivity.this, 2);
        binding.rcvAlbumImages.setLayoutManager(gridLayoutManager);

        imageAdapter = new ImageAdapter(MyAlbumActivity.this, this, this);
        binding.rcvAlbumImages.setAdapter(imageAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                pathList = MediaLoader.loadFilesFromFolder(MyAlbumActivity.this, getString(R.string.app_name));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pathList.isEmpty()) {
                            binding.rcvAlbumImages.setVisibility(View.GONE);
                            binding.noDataContainer.setVisibility(View.VISIBLE);
                        } else {
                            binding.rcvAlbumImages.setVisibility(View.VISIBLE);
                            binding.noDataContainer.setVisibility(View.GONE);

                            imageAdapter.swapList(pathList);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onImageClick(String path) {
        Intent intent = new Intent(MyAlbumActivity.this, PreviewImageActivity.class);
        intent.putExtra("path", path);
        showInterstitialAd(MyAlbumActivity.this, intent, null);
    }

    @Override
    public void onItemSelected(int selectedSize, int totalSize) {
//        binding.tvSelTitle.setText(selectedSize + " selected");
        if (selectedSize == 0) {
            binding.imgSelDelete.setVisibility(View.GONE);
        } else {
            binding.imgSelDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteClick(List<ImageModel> deleteList) {

        fileList = new ArrayList<>();
        for (int i = 0; i < deleteList.size(); i++) {
            Log.d("--delete--", "onDeleteClick: " + deleteList.get(i).getPathList());
            pathList.remove(deleteList.get(i));
            ImageModel imageModel = deleteList.get(i);
            fileList.add(new File(imageModel.getPathList()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                DeleteMediaManager.deleteFilesAbove10(fileList, 2022, MyAlbumActivity.this, new Intent());
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Dialog dialog = new Dialog(MyAlbumActivity.this, R.style.CustomDialog);
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
            txt_cancel.setOnClickListener(v -> dialog.dismiss());

            txt_ok.setOnClickListener(v -> {
                dialog.dismiss();
                for (int i = 0; i < fileList.size(); i++) {
                    File file = fileList.get(i);
                    if (file.exists()) {
                        if (file.delete()) {
                            MediaScannerConnection.scanFile(MyAlbumActivity.this,
                                    new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
                                    });
                        } else {
                            Toast.makeText(this, "Could not delete this file.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                imageAdapter.swapList(pathList);
                dialog.dismiss();

                if (pathList.isEmpty()) {
                    binding.rcvAlbumImages.setVisibility(View.GONE);
                    binding.noDataContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.rcvAlbumImages.setVisibility(View.VISIBLE);
                    binding.noDataContainer.setVisibility(View.GONE);
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2022) {
//                pathList.remove(deletePosition);
                imageAdapter.swapList(pathList);

                if (pathList.isEmpty()) {
                    binding.rcvAlbumImages.setVisibility(View.GONE);
                    binding.noDataContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.rcvAlbumImages.setVisibility(View.VISIBLE);
                    binding.noDataContainer.setVisibility(View.GONE);
                }
            }
        }
    }
}
