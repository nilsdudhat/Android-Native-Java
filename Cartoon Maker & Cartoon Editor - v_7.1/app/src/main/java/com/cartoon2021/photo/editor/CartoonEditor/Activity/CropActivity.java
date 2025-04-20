package com.cartoon2021.photo.editor.CartoonEditor.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.isseiaoki.simplecropview.CropImageView;

import com.cartoon2021.photo.editor.R;
import com.cartoon2021.photo.editor.Glob;

public class CropActivity extends BaseActivity {
    private final View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View view) {
            int id = view.getId();
            if (id != R.id.img_back) {
                switch (id) {
                    case R.id.button16_9:
                        mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                        return;
                    case R.id.button1_1:
                        mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                        return;
                    case R.id.button3_4:
                        mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                        return;
                    case R.id.button4_3:
                        mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                        return;
                    case R.id.button9_16:
                        mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                        return;
                    case R.id.buttonCircle:
                        mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                        return;
                    case R.id.buttonCustom:
                        mCropView.setCustomRatio(7, 5);
                        return;
                    case R.id.btn_crop:
                        saveImage();
                        return;
                    case R.id.buttonFree:
                        mCropView.setCropMode(CropImageView.CropMode.FREE);
                        return;
                    default:
                        switch (id) {
                            case R.id.buttonRotateLeft:
                                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                                return;
                            case R.id.buttonRotateRight:
                                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                                return;
                            default:
                        }
                }
            } else {
                onBackPressed();
            }
        }
    };

    public CropImageView mCropView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.crop_activity);

        showInterstitial(CropActivity.this);
        showBannerAd(CropActivity.this, findViewById(R.id.ad_banner));

        bindViews();
        this.mCropView.setCropMode(CropImageView.CropMode.SQUARE);
        this.mCropView.setImageBitmap(Glob.gallery_piv);
    }

    private void bindViews() {
        this.mCropView = findViewById(R.id.cropImageView);
        findViewById(R.id.btn_crop).setOnClickListener(this.btnListener);
        findViewById(R.id.img_back).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonRotateLeft).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonRotateRight).setOnClickListener(this.btnListener);
        findViewById(R.id.btn_crop).setOnClickListener(this.btnListener);
        findViewById(R.id.button1_1).setOnClickListener(this.btnListener);
        findViewById(R.id.button3_4).setOnClickListener(this.btnListener);
        findViewById(R.id.button4_3).setOnClickListener(this.btnListener);
        findViewById(R.id.button9_16).setOnClickListener(this.btnListener);
        findViewById(R.id.button16_9).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonFree).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonRotateLeft).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonRotateRight).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonCustom).setOnClickListener(this.btnListener);
        findViewById(R.id.buttonCircle).setOnClickListener(this.btnListener);
    }

    public void saveImage() {
        Glob.gallery_piv = this.mCropView.getCroppedBitmap();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        if (Glob.gallery_piv.getHeight() > Glob.gallery_piv.getWidth()) {
            if (Glob.gallery_piv.getHeight() > height) {
                Glob.gallery_piv = Bitmap.createScaledBitmap(Glob.gallery_piv, (Glob.gallery_piv.getWidth() * height) / Glob.gallery_piv.getHeight(), height, false);
            }
            if (Glob.gallery_piv.getWidth() > width) {
                Glob.gallery_piv = Bitmap.createScaledBitmap(Glob.gallery_piv, width, (Glob.gallery_piv.getHeight() * width) / Glob.gallery_piv.getWidth(), false);
            }
        } else {
            if (Glob.gallery_piv.getWidth() > width) {
                Glob.gallery_piv = Bitmap.createScaledBitmap(Glob.gallery_piv, width, (Glob.gallery_piv.getHeight() * width) / Glob.gallery_piv.getWidth(), false);
            }
            if (Glob.gallery_piv.getHeight() > height) {
                Glob.gallery_piv = Bitmap.createScaledBitmap(Glob.gallery_piv, (Glob.gallery_piv.getWidth() * height) / Glob.gallery_piv.getHeight(), height, false);
            }
        }
        final String path = getPath(getApplicationContext(), Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), Glob.gallery_piv, "some Title", "some_Description")));
        Intent intent = new Intent(CropActivity.this, PhotoLabActivity.class);
        intent.putExtra("imageUri", path);
        startActivity(intent);
    }

    public static String getPath(Context context, Uri uri) {
        String[] strArr = {"_data"};
        Cursor query = context.getContentResolver().query(uri, strArr, (String) null, (String[]) null, (String) null);
        String str = null;
        if (query != null) {
            if (query.moveToFirst()) {
                str = query.getString(query.getColumnIndexOrThrow(strArr[0]));
            }
            query.close();
        }
        return str == null ? "Not found" : str;
    }

//    @Override
//    public void onBackPressed() {
//        Glob.onBackPressedIntent(CropActivity.this, new Intent(CropActivity.this, EditorHomeActivity.class));
//        finish();
//    }
}
