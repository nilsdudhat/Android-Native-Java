package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivityFullScreenWallpaperBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.BitmapUtils;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FullScreenWallpaperActivity extends BaseActivity {

    ActivityFullScreenWallpaperBinding binding;

    Bitmap bitmap;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullScreenWallpaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadInterstitialAd(FullScreenWallpaperActivity.this);

        binding.backCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float aspectRatio = ((float) width / (float) height);
        Log.d("==aspect--", "height: " + height);
        Log.d("==aspect--", "width: " + width);
        Log.d("==aspect--", "aspectRatio in decimals: " + aspectRatio);

        int factor = greatestCommonFactor(width, height);

        int widthRatio = width / factor;
        int heightRatio = height / factor;

        Log.d("==aspect--", "aspectRatio: " + widthRatio + "/" + heightRatio);

        if (getIntent().hasExtra("wallpaper")) {

            Wallpaper.Detail wallpaperModel = (Wallpaper.Detail) getIntent().getSerializableExtra("wallpaper");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (!wallpaperModel.isFromAPI()) {
                            if (wallpaperModel.isFromAssets()) {
                                bitmap = BitmapUtils.getBitmapFromAsset(FullScreenWallpaperActivity.this, wallpaperModel.getName());
                            } else {
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                bitmap = BitmapFactory.decodeFile(wallpaperModel.getImage(), bmOptions);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.imgWallpaper.setImageBitmap(bitmap);
                                }
                            });
                        } else {
                            try {
                                URL url = new URL(wallpaperModel.getImage());
                                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.imgWallpaper.setImageBitmap(bitmap);
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }

        if (Constant.byteArray != null) {
            byte[] byteArray = Constant.byteArray;
            Constant.byteArray = null;

            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            binding.imgWallpaper.setImageBitmap(bitmap);
        }

        // share image on various apps
        binding.shareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, String.valueOf(System.currentTimeMillis()), null);
                Uri bitmapUri = Uri.parse(bitmapPath);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                intent.putExtra(Intent.EXTRA_TEXT, "Get awesome Wallpapers from.. " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });


        // apply wallpaper
        binding.wallpaperCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogScreenChooser = new Dialog(FullScreenWallpaperActivity.this, R.style.CustomDialog);
                dialogScreenChooser.setCancelable(true);
                dialogScreenChooser.setCanceledOnTouchOutside(true);
                dialogScreenChooser.setContentView(R.layout.dialog_wallpaper_for_full_screen);
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

                Dialog dialogWallpaper = new Dialog(FullScreenWallpaperActivity.this, R.style.CustomDialog);
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

                        Utils.saveImage(FullScreenWallpaperActivity.this, bitmap);
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

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Utils.setWallpaperOnBoth(FullScreenWallpaperActivity.this, bitmap);

                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_HOME);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
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

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Utils.setHomeScreenWallpaper(FullScreenWallpaperActivity.this, bitmap);

                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_HOME);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
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
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.setLockScreenWallpaper(FullScreenWallpaperActivity.this, bitmap);
                                        }
                                    });
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


        // preview activity
        binding.previewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullScreenWallpaperActivity.this, PreviewActivity.class);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                Constant.byteArray = stream.toByteArray();

                intent.putExtra("image", getIntent().getStringExtra("image"));
                showInterstitialAd(FullScreenWallpaperActivity.this, intent, null);
            }
        });
    }

    public int greatestCommonFactor(int width, int height) {
        return (height == 0) ? width : greatestCommonFactor(height, width % height);
    }
}
