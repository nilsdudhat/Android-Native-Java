package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivityPreviewBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PreviewActivity extends BaseActivity {

    Bitmap bitmap;

    ActivityPreviewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        showBannerAd(PreviewActivity.this, binding.adBanner);

        binding.backCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Constant.byteArray != null) {
            byte[] byteArray = Constant.byteArray;
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Constant.byteArray = null;

            binding.includePreview.imgWallpaper.setImageBitmap(bitmap);

            int brightness = Utils.calculateBrightness(bitmap, 1);

            if (brightness > 150 || brightness < 0) { // considering light bitmap
                Log.d("--brightness--", "onCreate: " + brightness + " Light");
                // for lock screen
                binding.includePreview.imgLock.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.includePreview.txtLockTime.setTextColor(Color.BLACK);
                binding.includePreview.txtLockDate.setTextColor(Color.BLACK);
                binding.includePreview.imgLockNotificationPreview.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.includePreview.imgLockNotification.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);

                // for home screen
                binding.includePreview.txtHomeTime.setTextColor(Color.BLACK);
                binding.includePreview.txtHomeDay.setTextColor(Color.BLACK);
                binding.includePreview.txtHomeTime.setTextColor(Color.BLACK);
                binding.includePreview.txtHomeDate.setTextColor(Color.BLACK);
                binding.includePreview.imgHomeNotification.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
            } else { // considering dark bitmap
                Log.d("--brightness--", "onCreate: " + brightness + " Dark");
                // for lock screen
                binding.includePreview.imgLock.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.includePreview.txtLockTime.setTextColor(Color.WHITE);
                binding.includePreview.txtLockDate.setTextColor(Color.WHITE);
                binding.includePreview.imgLockNotificationPreview.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.includePreview.imgLockNotification.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);

                // for home screen
                binding.includePreview.txtHomeTime.setTextColor(Color.WHITE);
                binding.includePreview.txtHomeDay.setTextColor(Color.WHITE);
                binding.includePreview.txtHomeTime.setTextColor(Color.WHITE);
                binding.includePreview.txtHomeDate.setTextColor(Color.WHITE);
                binding.includePreview.imgHomeNotification.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float aspectRatio = ((float) width / (float) height);
        Log.d("--aspect--", "height: " + height);
        Log.d("--aspect--", "width: " + width);
        Log.d("--aspect--", "aspectRatio in decimals: " + aspectRatio);

        int factor = greatestCommonFactor(width, height);

        int widthRatio = width / factor;
        int heightRatio = height / factor;

        Log.d("--aspect--", "aspectRatio: " + widthRatio + "/" + heightRatio);

        ViewTreeObserver viewTreeObserver = binding.previewContainer.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.previewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = binding.previewContainer.getMeasuredWidth();
                int height = binding.previewContainer.getMeasuredHeight();

                float widthFactor = (float) width / (float) widthRatio;
                float heightFactor = (float) height / (float) heightRatio;

                float intrinsicHeight;
                float intrinsicWidth;

                if (widthFactor >= heightFactor) {
                    intrinsicHeight = heightRatio * heightFactor;
                    intrinsicWidth = widthRatio * heightFactor;
                } else {
                    intrinsicHeight = heightRatio * widthFactor;
                    intrinsicWidth = widthRatio * widthFactor;
                }
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) intrinsicWidth, (int) intrinsicHeight);

                layoutParams.leftToLeft = binding.parent.getId();
                layoutParams.rightToRight = binding.parent.getId();
                layoutParams.topToBottom = binding.header.getId();
                layoutParams.bottomToTop = binding.wallpaperCard.getId();
                layoutParams.bottomMargin = (int) getResources().getDimension(com.intuit.sdp.R.dimen._25sdp);

                binding.previewContainer.setLayoutParams(layoutParams);

                Log.d("--aspect--", "onGlobalLayout: " + width + "/" + height);
            }
        });

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.framePreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = binding.framePreview.getWidth();
                int height = binding.framePreview.getHeight();

                Log.d("--aspect--", "changed: " + width + "/" + height);
            }
        });

        /*new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);

                    try {
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                binding.framePreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                int width = binding.framePreview.getWidth();
                                int height = binding.framePreview.getHeight();

                                Log.d("--aspect--", "changed: " + width + "/" + height);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/

        setTime();

        binding.includePreview.cardPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.includePreview.homeScreenContainer.getVisibility() == View.VISIBLE) {
                    binding.includePreview.homeScreenContainer.setVisibility(View.GONE);
                    binding.includePreview.lockScreenContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.includePreview.homeScreenContainer.setVisibility(View.VISIBLE);
                    binding.includePreview.lockScreenContainer.setVisibility(View.GONE);
                }
            }
        });

        // apply wallpaper
        binding.wallpaperCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogScreenChooser = new Dialog(PreviewActivity.this, R.style.CustomDialog);
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ll_lock.setVisibility(View.VISIBLE);
                    ll_both.setVisibility(View.VISIBLE);
                } else {
                    ll_lock.setVisibility(View.GONE);
                    ll_both.setVisibility(View.GONE);
                }

                if (getIntent().hasExtra("image")) {
                    if (getIntent().getStringExtra("image").equalsIgnoreCase("wallpaper")) {
                        ll_download.setVisibility(View.VISIBLE);
                    } else if (getIntent().getStringExtra("image").equalsIgnoreCase("gallery")) {
                        ll_download.setVisibility(View.GONE);
                    }
                }

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogScreenChooser.dismiss();
                    }
                });

                Dialog dialogWallpaper = new Dialog(PreviewActivity.this, R.style.CustomDialog);
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

                        Utils.saveImage(PreviewActivity.this, bitmap);
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
                                            Utils.setWallpaperOnBoth(PreviewActivity.this, bitmap);

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
                                            Utils.setHomeScreenWallpaper(PreviewActivity.this, bitmap);

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
                                            Utils.setLockScreenWallpaper(PreviewActivity.this, bitmap);
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
    }

    private void setTime() {
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = timeSdf.format(new Date());

        binding.includePreview.txtHomeTime.setText(time);
        binding.includePreview.txtLockTime.setText(time);

        SimpleDateFormat dateHomeSdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String homeDate = dateHomeSdf.format(new Date());

        binding.includePreview.txtHomeDate.setText(homeDate);

        SimpleDateFormat dateLockSdf = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());
        String lockDate = dateLockSdf.format(new Date());

        binding.includePreview.txtLockDate.setText(lockDate);

        Calendar calendar = Calendar.getInstance();
        Date dateForDay = calendar.getTime();

        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateForDay.getTime());
        binding.includePreview.txtHomeDay.setText(day);
    }

    public int greatestCommonFactor(int width, int height) {
        return (height == 0) ? width : greatestCommonFactor(height, width % height);
    }
}