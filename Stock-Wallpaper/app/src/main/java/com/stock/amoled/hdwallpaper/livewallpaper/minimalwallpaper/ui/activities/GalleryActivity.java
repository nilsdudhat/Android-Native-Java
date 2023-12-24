package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters.WallpaperAdapter;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivityGalleryBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.WallpaperClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.mediastore.MediaLoader;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.ListModel;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryActivity extends BaseActivity implements WallpaperClickListener {

    ArrayList<Wallpaper.Detail> wallpaperList = new ArrayList<>();
    Dialog progressDialog;

    WallpaperAdapter wallpaperAdapter;

    ActivityGalleryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        loadInterstitialAd(GalleryActivity.this);
        showBannerAd(GalleryActivity.this, binding.adBanner);

        binding.backCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new Dialog(GalleryActivity.this, R.style.CustomDialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // initialising wallpaper adapter
        wallpaperAdapter = new WallpaperAdapter(GalleryActivity.this, this);

        binding.rvWallpapers.setLayoutManager(new GridLayoutManager(GalleryActivity.this, 2));
        binding.rvWallpapers.setAdapter(wallpaperAdapter);

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra("gallery")) {
                if (getIntent().getStringExtra("gallery").equalsIgnoreCase("All")) {
                    binding.txtTitle.setText("Gallery");
                    loadAllMediaFiles();
                }
                if (getIntent().getStringExtra("gallery").equalsIgnoreCase("Downloads")) {
                    binding.txtTitle.setText("Downloaded Wallpapers");
                    loadDownloadsMediaFiles();
                }
            }
        }
    }

    private void loadDownloadsMediaFiles() {
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                wallpaperList = new ArrayList<>(MediaLoader.getMediaFilesFromFolder(GalleryActivity.this, getString(R.string.app_name)));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        if (!wallpaperList.isEmpty()) {

                            // swap wallpaper bitmap array in adapter
                            wallpaperAdapter.swapWallpaperList(wallpaperList);
                            binding.rvWallpapers.setVisibility(View.VISIBLE);
                            binding.noData.setVisibility(View.GONE);
                        } else {
                            binding.noData.setVisibility(View.VISIBLE);
                            binding.rvWallpapers.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    private void loadAllMediaFiles() {
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                wallpaperList = new ArrayList<>(MediaLoader.getAllMedia(GalleryActivity.this));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        if (!wallpaperList.isEmpty()) {

                            // swap wallpaper bitmap array in adapter
                            wallpaperAdapter.swapWallpaperList(wallpaperList);
                            binding.rvWallpapers.setVisibility(View.VISIBLE);
                            binding.noData.setVisibility(View.GONE);
                        } else {
                            binding.noData.setVisibility(View.VISIBLE);
                            binding.rvWallpapers.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onWallpaperClick(int position) {
        Intent intent = new Intent(GalleryActivity.this, WallpaperActivity.class);
        Constant.listModel = new ListModel(position, wallpaperList); // signing values to static model to use in WallpaperActivity
        intent.putExtra("image", "gallery");
        showInterstitialAd(GalleryActivity.this, intent, null);
    }
}