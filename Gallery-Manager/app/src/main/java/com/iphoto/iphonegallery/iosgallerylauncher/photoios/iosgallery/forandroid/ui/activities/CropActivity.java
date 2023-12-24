package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AspectRatioAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityCropBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;
import com.isseiaoki.simplecropview.CropImageView;
import com.steelkiwi.cropiwa.AspectRatio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

public class CropActivity extends BaseActivity implements AspectRatioAdapter.OnNewSelectedListener {

    ActivityCropBinding binding;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(CropActivity.this); // providing theme for activity before @onCreate
        super.onCreate(savedInstanceState);
        binding = ActivityCropBinding.inflate(getLayoutInflater()); // inflating binding
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide(); // removing Action Bar

        showBannerAd(CropActivity.this, binding.adBanner);

        binding.backContainer.setOnClickListener(view -> onBackPressed());

        if (getIntent().hasExtra("fileModel")) {
            FileModel fileModel = getIntent().getParcelableExtra("fileModel");

            binding.txtMediaDate.setText(DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModel.getDateModified()));

            File file = new File(fileModel.getPath());

            binding.txtMediaTitle.setText(file.getName());

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);

            if (bitmap == null) {
                finish();
            } else {
                binding.cropImageView.setImageBitmap(bitmap);
                binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);

                setUpRecyclerView();

                binding.wheelView.setListener(new HorizontalWheelView.Listener() {
                    @Override
                    public void onRotationChanged(double radians) {
                        super.onRotationChanged(radians);

                        double angle = binding.wheelView.getDegreesAngle();

                        updateText(angle);
                        updateImage(angle);
                    }
                });

                binding.imgRefresh.setOnClickListener(view -> {

                    // updating crop view
                    binding.cropImageView.setImageBitmap(bitmap);
                    binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);
                    
                    // resetting wheel
                    binding.wheelView.setDegreesAngle(0);

                    updateText(0);
                    updateImage(0);

                    // refreshing recyclerview
                    setUpRecyclerView();
                });

                binding.imgCrop.setOnClickListener(view -> {
                    if (binding.ratioContainer.getVisibility() == View.VISIBLE) {
                        binding.ratioContainer.setVisibility(View.GONE);

                        binding.imgCrop.setBackgroundResource(R.drawable.bg_round_not_selected);

                        binding.imgCrop.setImageResource(R.drawable.ic_crop_light);

                        binding.llWheel.setVisibility(View.VISIBLE);
                    } else {
                        binding.ratioContainer.setVisibility(View.VISIBLE);

                        binding.imgCrop.setBackgroundResource(R.drawable.bg_round_selected);

                        if (ThemeUtils.isDark(CropActivity.this)) {
                            binding.imgCrop.setImageResource(R.drawable.ic_crop_dark);
                        } else {
                            binding.imgCrop.setImageResource(R.drawable.ic_crop_light);
                        }

                        binding.llWheel.setVisibility(View.GONE);
                    }
                });

                binding.imgFlip.setOnClickListener(view -> {
                    binding.cropImageView.setImageBitmap(binding.cropImageView.getCroppedBitmap());
                    ImageFlipper.flip((ImageView) binding.cropImageView, FlipDirection.HORIZONTAL);
                });

                binding.imgRotate.setOnClickListener(view -> binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D));

                binding.doneContainer.setOnClickListener(view -> {

                    dismissProgressDialog();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Bitmap saveBitmap = binding.cropImageView.getCroppedBitmap();

                            if (saveBitmap != null) {
                                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + getResources().getString(R.string.app_name) + "/");
                                Log.d("--folder--", "save_image: directory " + dir);
                                if (!dir.exists()) {
                                    dir.mkdir();
                                }

                                File saveFile = new File(dir,
                                        file.getName().substring(0, file.getName().lastIndexOf(".")) +
                                                "_" + System.currentTimeMillis() + ".jpeg");
                                Log.d("--folder--", "save_image: saveFile " + saveFile);

                                try {
                                    OutputStream output = new FileOutputStream(saveFile);
                                    saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                                    output.flush();
                                    output.close();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Saved to " + saveFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                                            dismissProgressDialog();

                                            MakeSureFileWasCreatedThenMakeAvailable(saveFile);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CropActivity.this, "Something went wrong...!", Toast.LENGTH_LONG).show();

                                            dismissProgressDialog();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CropActivity.this, "Something went wrong...!", Toast.LENGTH_LONG).show();

                                        dismissProgressDialog();
                                    }
                                });
                            }
                        }

                        private void MakeSureFileWasCreatedThenMakeAvailable(File file1) {
                            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file1.toString()}, new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    finish();
                                }
                            });
                        }
                    }).start();
                });
            }
        } else {
            finish();
        }
    }

    private void setUpRecyclerView() {
        AspectRatioAdapter aspectRatioAdapter = new AspectRatioAdapter(CropActivity.this);
        aspectRatioAdapter.setListener(this);

        binding.recyclerViewRatio.setLayoutManager(new LinearLayoutManager(CropActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewRatio.setAdapter(aspectRatioAdapter);
    }

    private void updateImage(double angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        binding.cropImageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //required
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        binding.cropImageView.setImageBitmap(rotated);
    }

    private void updateText(double angle) {
        String text = String.format(Locale.US, "%.0f°", angle);
        binding.txtAngle.setText(text);
    }

    @Override
    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        if (aspectRatio.getWidth() == 10 && aspectRatio.getHeight() == 10) {
            binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);
        } else if (aspectRatio.getWidth() == 0 && aspectRatio.getHeight() == 0) {
            binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        } else if (aspectRatio.getWidth() == -1 && aspectRatio.getHeight() == -1) {
            binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
        } else {
            binding.cropImageView.setCustomRatio(aspectRatio.getWidth(), aspectRatio.getHeight());
        }
    }
}
