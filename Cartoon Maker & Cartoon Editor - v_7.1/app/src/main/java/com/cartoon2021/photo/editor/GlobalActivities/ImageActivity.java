package com.cartoon2021.photo.editor.GlobalActivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.IOException;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.R;
import com.cartoon2021.photo.editor.Glob;

public class ImageActivity extends BaseActivity {

    String path;
    ImageView img_selected;
    ImageView img_back;

    RelativeLayout rl_set_wallpaper;
    RelativeLayout rl_share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        showInterstitial(ImageActivity.this);
        showMiniNativeAd(ImageActivity.this, findViewById(R.id.ad_mini_native));

        img_selected = findViewById(R.id.img_selected);
        rl_set_wallpaper = findViewById(R.id.rl_set_wallpaper);
        rl_share = findViewById(R.id.rl_share);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(v -> onBackPressed());

        path = (String) getIntent().getExtras().get("path");
        Glide.with(ImageActivity.this).load(path).into(img_selected);

        rl_set_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
                builder.setTitle("Set As");
                builder.setMessage("Do You Want to Set As Wallpaper ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        setWallpaper(path);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
                builder.setTitle("Share");
                builder.setMessage("Do You Want to Share ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        shareImage(Glob.app_name + " Created By : " + Glob.app_link, path);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    public void shareImage(String s, String str2) {
        MediaScannerConnection.scanFile(this, new String[]{str2}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            @SuppressLint("WrongConstant")
            public void onScanCompleted(String str, Uri uri) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.TEXT", str);
                intent.putExtra("android.intent.extra.STREAM", uri);
                intent.addFlags(524288);
                startActivity(Intent.createChooser(intent, "Share Image"));
            }
        });
    }

    public void setWallpaper(String str2) {
        WallpaperManager instance = WallpaperManager.getInstance(ImageActivity.this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        int i2 = displayMetrics.widthPixels;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            instance.setBitmap(BitmapFactory.decodeFile(str2, options));
            instance.suggestDesiredDimensions(i2 / 2, i / 2);
            Toast.makeText(ImageActivity.this, "Wallpaper Set", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
