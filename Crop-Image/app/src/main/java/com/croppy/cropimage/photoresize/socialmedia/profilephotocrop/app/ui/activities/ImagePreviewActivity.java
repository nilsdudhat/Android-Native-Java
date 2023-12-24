package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adsintegration.AdsBaseActivity;

import java.io.File;
import java.util.Objects;

public class ImagePreviewActivity extends AdsBaseActivity {

    File file;

    public static void shareImageDialog(Activity context, String mediaPath, String photo_name, String package_name) {
        try {
            File file = new File(mediaPath);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.putExtra("android.intent.extra.TEXT", "" + getShareText(context, photo_name));
            intent.putExtra("android.intent.extra.SUBJECT", "" + context.getString(R.string.full_app_name));
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            if (!package_name.equalsIgnoreCase("all")) {
                filterByPackageName(context, intent, package_name);
            }
            context.startActivityForResult(Intent.createChooser(intent, "Share image by..."), 108);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getShareText(Context context, String photo_name) {
        String appLink = "http://play.google.com/store/apps/details?id=" + context.getPackageName();
        return getRandomStartText() + " application of \"" + context.getString(R.string.full_app_name) + "\" download from the following link: \n" + appLink;
    }

    public static String getRandomStartText() {
        try {
            String[] arr = new String[]{"A beautiful", "Cool", "An Awesome", "The best"};
            return arr[random(0, arr.length - 1)];
        } catch (Exception e) {
            e.printStackTrace();
            return "A beautiful";
        }
    }

    public static int random(int min, int max) {
        int random = Math.round((float) (((double) min) + (Math.random() * ((double) ((max - min) + 1)))));
        if (random >= max) {
            return max;
        }
        return random;
    }

    public static void filterByPackageName(Context context, Intent intent, String prefix) {
        for (ResolveInfo info : context.getPackageManager().queryIntentActivities(intent, 0)) {
            Log.e("packageName:", "" + info.activityInfo.packageName.toLowerCase());
            if (info.activityInfo.packageName.toLowerCase().startsWith(prefix)) {
                intent.setPackage(info.activityInfo.packageName);
                return;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ImageView img_cropped = findViewById(R.id.img_cropped);
        TextView txt_path = findViewById(R.id.txt_path);

        showSmallNativeAd(ImagePreviewActivity.this, findViewById(R.id.ad_small_native));

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String path = "";

        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }

        if (!path.isEmpty()) {
            Log.d("--path--", "name: " + path);

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Log.d("--bitmap--", "onCreate: " + bitmap);

            file = new File(path);
            Glide.with(ImagePreviewActivity.this).load(file.getAbsoluteFile()).into(img_cropped);

            img_cropped.setBackgroundResource(R.drawable.img_transparent);

            txt_path.setText(path);

            findViewById(R.id.img_whatsapp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareImage("com.whatsapp");
                }
            });
            findViewById(R.id.img_insta).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareImage("com.instagram.android");
                }
            });
            findViewById(R.id.img_facebook).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareImage("com.facebook.katana");
                }
            });
            findViewById(R.id.img_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareImage("all");
                }
            });
        }
    }

    void shareImage(String appPackage) {
        String str_path = String.valueOf(file);
        String str_photo_name = file.getName();
        shareImageDialog(ImagePreviewActivity.this, str_path, str_photo_name, appPackage);
    }
}
