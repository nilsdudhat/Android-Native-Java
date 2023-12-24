package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import static java.lang.Math.abs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters.WallpaperPagerAdapter;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivityWallpaperBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.WallpaperPagerClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.BitmapUtils;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class WallpaperActivity extends BaseActivity implements WallpaperPagerClickListener {

    ActivityWallpaperBinding binding;

    int current_position;
    ArrayList<Wallpaper.Detail> wallpaperList = new ArrayList<>();

    Bitmap currentBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWallpaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        showBannerAd(WallpaperActivity.this, binding.adBanner);

        binding.backCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // getting values from static model
        if (Constant.listModel != null) {
            current_position = Constant.listModel.getPosition();
            wallpaperList = new ArrayList<>(Constant.listModel.getWallpaperList());

            Constant.listModel = null; // signing out values from static model

            Wallpaper.Detail wallpaperModel = wallpaperList.get(current_position);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (!wallpaperModel.isFromAPI()) {
                        if (wallpaperModel.isFromAssets()) {
                            currentBitmap = BitmapUtils.getBitmapFromAsset(WallpaperActivity.this, wallpaperModel.getName());
                        } else {
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            currentBitmap = BitmapFactory.decodeFile(wallpaperModel.getImage(), bmOptions);
                        }
                    } else {
                        try {
                            URL url = new URL(wallpaperModel.getImage());
                            currentBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (MalformedURLException e) {
                            currentBitmap = null;
                            e.printStackTrace();
                        } catch (IOException e) {
                            currentBitmap = null;
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        WallpaperPagerAdapter wallpaperPagerAdapter = new WallpaperPagerAdapter(WallpaperActivity.this, wallpaperList, this);
        binding.viewPager.setAdapter(wallpaperPagerAdapter);
        binding.viewPager.setCurrentItem(current_position);
        binding.viewPager.setPageMargin(50); // margin between two items of view-pager
        binding.viewPager.setClipToPadding(false); // this will work same as recyclerview
        binding.viewPager.setPadding(100, 0, 100, 0);

        binding.txtTitle.setText(wallpaperList.get(current_position).getName()); // text title for current item on first view

        // viewpager transformation animation
        ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setScaleY(1 - (0.25f * abs(position)));
            }
        };
        binding.viewPager.setPageTransformer(true, pageTransformer);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current_position = position;
                Log.d("--scroll--", "position: " + current_position);

                binding.txtTitle.setText(wallpaperList.get(position).getName()); // change text title on scroll

                Wallpaper.Detail wallpaperModel = wallpaperList.get(current_position);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (!wallpaperModel.isFromAPI()) {
                            if (wallpaperModel.isFromAssets()) {
                                currentBitmap = BitmapUtils.getBitmapFromAsset(WallpaperActivity.this, wallpaperModel.getName());
                            } else {
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                currentBitmap = BitmapFactory.decodeFile(wallpaperModel.getImage(), bmOptions);
                            }
                        } else {
                            try {
                                URL url = new URL(wallpaperModel.getImage());
                                currentBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (IOException e) {
                                currentBitmap = null;
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // details of wallpaper activity
        binding.detailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(WallpaperActivity.this, R.style.CustomDialog);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_deatails);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                TextView txt_wallpaper_name = dialog.findViewById(R.id.txt_wallpaper_name);
                TextView txt_wallpaper_resolution = dialog.findViewById(R.id.txt_wallpaper_resolution);
                TextView txt_wallpaper_size = dialog.findViewById(R.id.txt_wallpaper_size);
                LinearLayout ll_name = dialog.findViewById(R.id.ll_name);

                if (getIntent().hasExtra("image")) {
                    if (getIntent().getStringExtra("image").equalsIgnoreCase("wallpaper")) {
                        Objects.requireNonNull(ll_name).setVisibility(View.VISIBLE);

                        Objects.requireNonNull(txt_wallpaper_name).setText(wallpaperList.get(current_position).getName());
                    } else if (getIntent().getStringExtra("image").equalsIgnoreCase("gallery")) {
                        Objects.requireNonNull(ll_name).setVisibility(View.VISIBLE);

                        File file = new File(wallpaperList.get(current_position).getImage());
                        Objects.requireNonNull(txt_wallpaper_name).setText(file.getName());
                    } else {
                        Objects.requireNonNull(ll_name).setVisibility(View.GONE);
                    }
                }
                String name = wallpaperList.get(current_position).getName();
                if (name == null) {
                    File file = new File(wallpaperList.get(current_position).getImage());
                    Objects.requireNonNull(txt_wallpaper_name).setText(file.getName());
                } else {
                    if (name.isEmpty()) {
                        File file = new File(wallpaperList.get(current_position).getImage());
                        Objects.requireNonNull(txt_wallpaper_name).setText(file.getName());
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (currentBitmap != null) {
                        int width = currentBitmap.getWidth();
                        int height = currentBitmap.getHeight();

                        Objects.requireNonNull(txt_wallpaper_resolution).setText(width + "x" + height);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imageInByte = stream.toByteArray();
                        long lengthBitmap = imageInByte.length;

                        Objects.requireNonNull(txt_wallpaper_size).setText(Utils.getBitmapSize(lengthBitmap));
                    }
                }
            }
        });


        // apply wallpaper
        binding.wallpaperCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogScreenChooser = new Dialog(WallpaperActivity.this, R.style.CustomDialog);
                dialogScreenChooser.setCancelable(true);
                dialogScreenChooser.setCanceledOnTouchOutside(true);
                dialogScreenChooser.setContentView(R.layout.dialog_wallpaper);
                dialogScreenChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogScreenChooser.show();

                LinearLayout ll_home = dialogScreenChooser.findViewById(R.id.ll_home);
                LinearLayout ll_lock = dialogScreenChooser.findViewById(R.id.ll_lock);
                LinearLayout ll_both = dialogScreenChooser.findViewById(R.id.ll_both);
                LinearLayout ll_download = dialogScreenChooser.findViewById(R.id.ll_download);
                TextView txt_cancel = dialogScreenChooser.findViewById(R.id.txt_cancel);

                if (getIntent().hasExtra("image")) {
                    if (getIntent().getStringExtra("image").equalsIgnoreCase("wallpaper")) {
                        ll_download.setVisibility(View.VISIBLE);
                    } else if (getIntent().getStringExtra("image").equalsIgnoreCase("gallery")) {
                        ll_download.setVisibility(View.GONE);
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ll_lock.setVisibility(View.VISIBLE);
                    ll_both.setVisibility(View.VISIBLE);
                } else {
                    ll_lock.setVisibility(View.GONE);
                    ll_both.setVisibility(View.GONE);
                }

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogScreenChooser.dismiss();
                    }
                });

                Dialog dialogWallpaper = new Dialog(WallpaperActivity.this, R.style.CustomDialog);
                dialogWallpaper.setCancelable(true);
                dialogWallpaper.setCanceledOnTouchOutside(true);
                dialogWallpaper.setContentView(R.layout.dialog_set_wallpaper);
                dialogWallpaper.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                TextView txt_title = dialogWallpaper.findViewById(R.id.txt_title);
                TextView txt_ok = dialogWallpaper.findViewById(R.id.txt_ok);
                TextView txt_no = dialogWallpaper.findViewById(R.id.txt_no);

                ll_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogScreenChooser.dismiss();

                        if (currentBitmap != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Utils.saveImage(WallpaperActivity.this, currentBitmap);
                            }
                        }
                    }
                });

                ll_both.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogScreenChooser.dismiss();
                        dialogWallpaper.show();

                        txt_title.setText("Do you want to set this image as your Home and Lock Screen Wallpaper?");

                        txt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogWallpaper.dismiss();

                                try {
                                    Utils.setWallpaperOnBoth(WallpaperActivity.this, currentBitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

                ll_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogScreenChooser.dismiss();
                        dialogWallpaper.show();

                        txt_title.setText("Do you want to set this image as your Home Screen Wallpaper?");

                        txt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogWallpaper.dismiss();

                                if (currentBitmap != null) {
                                    try {
                                        Utils.setHomeScreenWallpaper(WallpaperActivity.this, currentBitmap);

                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });

                ll_lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogScreenChooser.dismiss();
                        dialogWallpaper.show();

                        txt_title.setText("Do you want to set this image as your Lock Screen Wallpaper?");

                        txt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogWallpaper.dismiss();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if (currentBitmap != null) {
                                        Utils.setLockScreenWallpaper(WallpaperActivity.this, currentBitmap);

                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }
                });

                txt_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWallpaper.dismiss();
                    }
                });
            }
        });

        // share image on various apps
        binding.shareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBitmap != null) {
                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), currentBitmap, String.valueOf(System.currentTimeMillis()), null);
                    Uri bitmapUri = Uri.parse(bitmapPath);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/png");
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    intent.putExtra(Intent.EXTRA_TEXT, "Get awesome Wallpapers from.. " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            }
        });
    }

    @Override
    public void onClickListener(Wallpaper.Detail wallpaperModel) {
        try {
            Intent intent = new Intent(WallpaperActivity.this, FullScreenWallpaperActivity.class);
            intent.putExtra("wallpaper", wallpaperModel);
            intent.putExtra("image", getIntent().getStringExtra("image"));
            showInterstitialAd(WallpaperActivity.this, intent, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
