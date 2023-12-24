package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.isseiaoki.simplecropview.CropImageView;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters.AspectAdapter;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AdsBaseActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.Share;
import com.steelkiwi.cropiwa.AspectRatio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class CropActivity extends AdsBaseActivity implements AspectAdapter.OnNewSelectedListener {

    public CropImageView crop_image_view;
    private Bitmap bitmap;

    private TextView txt_angle;

    Dialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        loadInterstitialAd(CropActivity.this);

        progressDialog = new Dialog(CropActivity.this, R.style.CustomDialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        crop_image_view = (CropImageView) findViewById(R.id.crop_image_view);
//        wheel_view = (HorizontalWheelView) findViewById(R.id.wheel_view);
        txt_angle = (TextView) findViewById(R.id.txt_angle);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ratio);
        ImageView imageViewSaveCrop = findViewById(R.id.imageViewSaveCrop);

        File image = new File(Share.originalPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        crop_image_view.setImageBitmap(this.bitmap);

        /*wheel_view.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                super.onRotationChanged(radians);

                double angle = wheel_view.getDegreesAngle();

                updateText(angle);
                updateImage(angle);
            }
        });*/

        AspectAdapter aspectAdapter = new AspectAdapter();
        aspectAdapter.setListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(CropActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(aspectAdapter);

        crop_image_view.setCropMode(CropImageView.CropMode.FREE);

        findViewById(R.id.relativeLayoutRotate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
            }
        });
        findViewById(R.id.relativeLayouRotate90).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });
        findViewById(R.id.relativeLayoutVFlip).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ImageFlipper.flip((ImageView) crop_image_view, FlipDirection.VERTICAL);
            }
        });
        findViewById(R.id.relativeLayoutHFlip).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ImageFlipper.flip((ImageView) crop_image_view, FlipDirection.HORIZONTAL);
            }
        });
        imageViewSaveCrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Bitmap cropped_bitmap = crop_image_view.getCroppedBitmap();
                        cropped_bitmap.setHasAlpha(true); // for transparency of background

                        Share.croppedBitmap = cropped_bitmap;

                        ContextWrapper cw = new ContextWrapper(CropActivity.this);
                        // path to /data/data/yourapp/app_data/imageDir
                        File directory = cw.getDir("ImageConverter", Context.MODE_PRIVATE);
                        // Create ImageConverter
                        File myPath = new File(directory, "cropped.jpg");

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myPath);
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            cropped_bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                            Share.croppedPath = myPath.getAbsolutePath();

                            nextActivity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.imageViewCloseCrop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void updateImage(double angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        crop_image_view.setScaleType(ImageView.ScaleType.CENTER_CROP); //required
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        crop_image_view.setImageBitmap(rotated);
    }

    private void updateText(double angle) {
        String text = String.format(Locale.US, "%.0f°", angle);
        txt_angle.setText(text);
    }

    private void nextActivity() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();

                Intent intent = new Intent(CropActivity.this, ConverterActivity.class);
                showInterstitialAd(CropActivity.this, intent, null);
            }
        });
    }

    @Override
    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        if (aspectRatio.getWidth() == 10 && aspectRatio.getHeight() == 10) {
            crop_image_view.setCropMode(CropImageView.CropMode.FREE);
        } else if (aspectRatio.getWidth() == 0 && aspectRatio.getHeight() == 0) {
            crop_image_view.setCropMode(CropImageView.CropMode.CIRCLE);
        } else if (aspectRatio.getWidth() == -1 && aspectRatio.getHeight() == -1) {
            crop_image_view.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
        } else {
            crop_image_view.setCustomRatio(aspectRatio.getWidth(), aspectRatio.getHeight());
        }
    }
}