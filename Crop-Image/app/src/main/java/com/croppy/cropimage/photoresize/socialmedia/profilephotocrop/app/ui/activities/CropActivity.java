package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adapters.AspectAdapter;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adsintegration.AdsBaseActivity;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Share;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Utils;
import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;
import com.isseiaoki.simplecropview.CropImageView;
import com.steelkiwi.cropiwa.AspectRatio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

public class CropActivity extends AdsBaseActivity implements AspectAdapter.OnNewSelectedListener {

    public CropImageView crop_image_view;
    Dialog progressDialog;
    private Bitmap bitmap;
    //    private ConstraintLayout constraint_loader;
    private HorizontalWheelView wheel_view;
    private TextView txt_angle;

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
        wheel_view = (HorizontalWheelView) findViewById(R.id.wheel_view);
        txt_angle = (TextView) findViewById(R.id.txt_angle);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ratio);
        ImageView imageViewSaveCrop = findViewById(R.id.imageViewSaveCrop);

//        constraint_loader = (ConstraintLayout) findViewById(R.id.constraint_loader);
//        constraint_loader.setVisibility(View.GONE);

        File image = new File(Share.imageUrl);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        crop_image_view.setImageBitmap(this.bitmap);

        wheel_view.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                super.onRotationChanged(radians);

                double angle = wheel_view.getDegreesAngle();

                updateText(angle);
                updateImage(angle);
            }
        });

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

//                mLoading(true);
                progressDialog.show();


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Bitmap cropped_bitmap = crop_image_view.getCroppedBitmap();
                        cropped_bitmap.setHasAlpha(true); // for transparency of background
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.full_app_name);
                        File filePath = new File(new File(path), String.valueOf(System.currentTimeMillis()));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            final ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, filePath.getName());
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png"); // jpeg format is good for processing but it cannot create transparent image
                            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/" + getString(R.string.full_app_name));

                            final ContentResolver resolver = getContentResolver();
                            Uri uri = null;

                            try {
                                final Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                                uri = resolver.insert(contentUri, values);

                                if (uri == null)
                                    throw new IOException("Failed to create new MediaStore record.");

                                try (final OutputStream stream = resolver.openOutputStream(uri)) {
                                    if (stream == null) {
                                        Toast.makeText(CropActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
                                        throw new IOException("Failed to open output stream.");
                                    }

                                    if (!cropped_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                                        Toast.makeText(CropActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
                                        throw new IOException("Failed to save bitmap.");
                                    }
                                }
                                nextActivity(filePath);
                            } catch (IOException e) {
                                if (uri != null) {
                                    // Don't leave an orphan entry in the MediaStore
                                    resolver.delete(uri, null, null);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CropActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            if (!filePath.exists()) {
                                filePath.mkdirs();
                            }
                            Log.d("--filePath--", "saveImage: " + filePath.getAbsolutePath());

                            if (filePath.exists()) {
                                filePath.delete();
                            }
                            try {
                                FileOutputStream out = new FileOutputStream(filePath);
                                cropped_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.flush();
                                out.close();

                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                                nextActivity(filePath);
                            } catch (Exception e) {
                                e.printStackTrace();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CropActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    /*public void mLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            constraint_loader.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        constraint_loader.setVisibility(View.GONE);
    }*/

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

    private void nextActivity(File filePath) {
        Utils.MakeSureFileWasCreatedThenMakeAvailable(CropActivity.this, filePath);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mLoading(false);
                progressDialog.dismiss();

                Toast.makeText(CropActivity.this, "Image Save Successfully.", Toast.LENGTH_LONG).show();

                Log.d("--path--", "path: " + filePath.getAbsolutePath());

                Intent intent = new Intent(CropActivity.this, ImagePreviewActivity.class);
                intent.putExtra("path", filePath.getAbsolutePath() + ".png");
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