package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters.ExtensionAdapter;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AdsBaseActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.databinding.ActivityConverterBinding;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.AppUtils;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.BitmapUtils;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.Share;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ConverterActivity extends AdsBaseActivity implements ExtensionAdapter.SelectExtension {

    ActivityConverterBinding binding;

    Bitmap initialBitmap = null;

    float aspectRatio = 0f;

    boolean isClipped = true;

    String extension;

    ExtensionAdapter extensionAdapter;

    ArrayList<String> extensionList = new ArrayList<>(Arrays.asList(".avif", ".bmp", ".gif", ".ico", ".jpeg", ".jpg", ".png", ".svg", ".webp", ".tif", ".tiff", ".raw", ".heif", ".eps"));

    Dialog progressDialog;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding = ActivityConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        loadInterstitialAd(ConverterActivity.this);

        progressDialog = new Dialog(ConverterActivity.this, R.style.CustomDialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.edtHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AppUtils.hideSoftKeyboard(ConverterActivity.this);
                } else {
                    AppUtils.showSoftKeyboard(ConverterActivity.this);
                }
            }
        });

        initialBitmap = Share.croppedBitmap;

        if (Share.originalPath != null) {
            Log.d("--img_selected--", "onPostCreate: " + Share.originalPath);
            binding.txtPath.setText(Share.originalPath);
            binding.txtOriginalSize.setText(BitmapUtils.getBitmapSize(Share.originalPath));

            extension = Share.originalPath.substring(Share.originalPath.lastIndexOf("."));

            if (extensionList.contains(extension)) {
                int extensionIndex = extensionList.indexOf(extension);

                binding.rvExtension.setLayoutManager(new LinearLayoutManager(ConverterActivity.this, LinearLayoutManager.HORIZONTAL, false));

                extensionAdapter = new ExtensionAdapter(ConverterActivity.this, ConverterActivity.this);
                extensionAdapter.swapList(extensionList);
                extensionAdapter.swapSelectedExtension(extensionIndex);

                binding.rvExtension.setAdapter(extensionAdapter);

                Objects.requireNonNull(binding.rvExtension.getLayoutManager()).smoothScrollToPosition(binding.rvExtension, new RecyclerView.State(), extensionIndex);
            }
        }

        if (initialBitmap != null) {
            Share.croppedBitmap = null;
            aspectRatio = BitmapUtils.getAspectRatio(initialBitmap);

            binding.txtResolution.setText(initialBitmap.getWidth() + "x" + initialBitmap.getHeight());

            binding.edtHeight.setText(String.valueOf(initialBitmap.getHeight()));
            binding.edtWidth.setText(String.valueOf(initialBitmap.getWidth()));

            String originalSize = BitmapUtils.getBitmapSize(initialBitmap);
            binding.txtEditedSize.setText(originalSize);

            binding.imgEdited.setImageBitmap(initialBitmap);
        }

        binding.seekCompress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.txtCompress.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.txtExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppUtils.hideSoftKeyboard(ConverterActivity.this);

                        if (Share.editedBitmap == null) {
                            Share.editedBitmap = initialBitmap;
                        }

                        Bitmap cropped_bitmap = Share.editedBitmap;
                        cropped_bitmap.setHasAlpha(true); // for transparency of background
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.app_name);
                        File filePath = new File(new File(path), new File(Share.originalPath).getName().substring(0, new File(Share.originalPath).getName().lastIndexOf(".")) + "_" + System.currentTimeMillis() + extension);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            final ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, filePath.getName());
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/" + extension.substring(1)); // jpeg format is good for processing but it cannot create transparent image
                            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/" + getString(R.string.app_name));

                            final ContentResolver resolver = getContentResolver();
                            Uri uri = null;

                            try {
                                final Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                                uri = resolver.insert(contentUri, values);

                                if (uri == null)
                                    throw new IOException("Failed to create new MediaStore record.");

                                try (final OutputStream stream = resolver.openOutputStream(uri)) {
                                    if (stream == null) {
                                        Toast.makeText(ConverterActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
                                        throw new IOException("Failed to open output stream.");
                                    }

                                    if (!cropped_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                                        Toast.makeText(ConverterActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ConverterActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ConverterActivity.this, "Failed to Save.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });

        binding.edtHeight.addTextChangedListener(getTextWatcher());
        binding.edtWidth.addTextChangedListener(getTextWatcher());

//        binding.imgClip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (isClipped) {
//                        isClipped = false;
//                        binding.imgClip.setBackgroundResource(R.drawable.bg_clip_white);
//                        binding.imgClip.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
//                    } else {
//                        isClipped = true;
//                        binding.imgClip.setBackgroundResource(R.drawable.bg_clip_black);
//                        binding.imgClip.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
//
//                        String width = binding.edtWidth.getText().toString();
//                        String height = binding.edtHeight.getText().toString();
//                        Log.d("--ratio--", "onClick: width before remove 0 " + width);
//                        if (width.startsWith("0") || height.startsWith("0")) {
//                            width = binding.edtWidth.getText().toString().substring(0);
//                            height = binding.edtHeight.getText().toString().substring(0);
//                        }
//                        Log.d("--ratio--", "onClick: width before remove 0 " + width);
//
//                        int aspectHeight = (int) (Integer.parseInt(width) / aspectRatio);
//
//                        if (aspectHeight != Integer.parseInt(height)) {
//                            binding.edtHeight.setText(String.valueOf(aspectHeight));
//                        }
//                    }
//                }
//            }
//        });

        binding.txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideSoftKeyboard(ConverterActivity.this);

                if (binding.edtWidth.toString().isEmpty() || binding.edtHeight.toString().isEmpty()) {

                } else {
                    int width = Integer.parseInt(binding.edtWidth.getText().toString());
                    int height = Integer.parseInt(binding.edtHeight.getText().toString());

                    if (width == 0) {
                        binding.edtWidth.setError("not valid input!");
                    } else if (height == 0) {
                        binding.edtHeight.setError("not valid input!");
                    } else if (binding.seekCompress.getProgress() == 0) {
                        Toast.makeText(ConverterActivity.this, "Invalid compress level", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO: get compressed and resized bitmap

                        Log.d("--bitmap--", "size 0: " + BitmapUtils.getBitmapSize(initialBitmap));
                        Log.d("--bitmap--", "resolution 0: " + BitmapUtils.getResolution(initialBitmap));

                        progressDialog.show();

                        new Thread(new Runnable() { // compress image
                            @Override
                            public void run() {
                                Share.editedBitmap = BitmapUtils.compressBitmap(initialBitmap, binding.seekCompress.getProgress());

                                Log.d("--bitmap--", "size 1: " + BitmapUtils.getBitmapSize(Share.editedBitmap));
                                Log.d("--bitmap--", "resolution 1: " + BitmapUtils.getResolution(Share.editedBitmap));

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Share.editedBitmap = BitmapUtils.getResizedBitmap(Share.editedBitmap, width, height);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Glide.with(ConverterActivity.this).load(Share.editedBitmap).into(binding.imgEdited);

                                                binding.txtEditedSize.setText(BitmapUtils.getBitmapSize(Share.editedBitmap));
                                                binding.txtExport.setVisibility(View.VISIBLE);

                                                Log.d("--bitmap--", "size 2: " + BitmapUtils.getBitmapSize(Share.editedBitmap));
                                                Log.d("--bitmap--", "resolution 2: " + BitmapUtils.getResolution(Share.editedBitmap));

                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).start();
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private void nextActivity(File filePath) {
        BitmapUtils.MakeSureFileWasCreatedThenMakeAvailable(ConverterActivity.this, filePath);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();

                Toast.makeText(ConverterActivity.this, "Image Save Successfully.", Toast.LENGTH_SHORT).show();

                Log.d("--path--", "path: " + filePath.getAbsolutePath());

                Intent intent = new Intent(ConverterActivity.this, PreviewImageActivity.class);
                intent.putExtra("path", filePath.getAbsolutePath());
                intent.putExtra("extension", extension);
                showInterstitialAd(ConverterActivity.this, intent, null);
            }
        });
    }

    int height_multi, width_multi;
    int value;

    private TextWatcher getTextWatcher() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do what you want with your EditText
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    binding.edtWidth.setText("1");
                    binding.edtHeight.setText("1");
                    Log.d("--click--", "length " + editable.length());
                }

                String strWidth = binding.edtWidth.getText().toString();
                String strHeight = binding.edtHeight.getText().toString();

                if (isClipped) {

                    if (editable.hashCode() == binding.edtWidth.getText().hashCode()) {
                        int width = Integer.parseInt(strWidth);
                        height_multi = (int) (width / aspectRatio);
                        Log.d("--click--", "step 1: height_multi " + height_multi);
                        if (strHeight.isEmpty()) {
                            if (String.valueOf(height_multi).startsWith("0") && String.valueOf(height_multi).length() > 1) {
                                height_multi = Integer.parseInt(String.valueOf(height_multi).substring(0));
                            }
                            binding.edtHeight.setText(String.valueOf(height_multi));
                            Log.d("--click--", "step 2: height set is empty " + height_multi);
                        } else {
                            value = Integer.parseInt(strHeight);
                            if (height_multi != value) {
                                if (String.valueOf(height_multi).startsWith("0") && String.valueOf(height_multi).length() > 1) {
                                    height_multi = Integer.parseInt(String.valueOf(height_multi).substring(0));
                                }
                                binding.edtHeight.setText(String.valueOf(height_multi));
                                Log.d("--click--", "step 3: height set " + height_multi);
                            }
                        }
                    } else if (editable.hashCode() == binding.edtHeight.getText().hashCode()) {
                        int height = Integer.parseInt(strHeight);
                        width_multi = (int) (height * aspectRatio);
                        Log.d("--click--", "step 4: width_multi " + width_multi);

                        if (strWidth.isEmpty()) {
                            if (String.valueOf(width_multi).startsWith("0") && String.valueOf(width_multi).length() > 1) {
                                width_multi = Integer.parseInt(String.valueOf(width_multi).substring(0));
                            }
                            binding.edtWidth.setText(String.valueOf(width_multi));
                            Log.d("--click--", "step 5: width set is empty " + width_multi);
                        } else {
                            value = Integer.parseInt(strWidth);
                            if (width_multi != value) {
                                if (String.valueOf(width_multi).startsWith("0") && String.valueOf(width_multi).length() > 1) {
                                    width_multi = Integer.parseInt(String.valueOf(width_multi).substring(0));
                                }
                                binding.edtWidth.setText(String.valueOf(width_multi));
                                Log.d("--click--", "step 6: width set " + width_multi);
                            }
                        }
                    }
                } else {
                    if (editable.hashCode() == binding.edtWidth.getText().hashCode()) {
                        if (strWidth.startsWith("0") && strWidth.length() > 1) {
                            binding.edtWidth.setText(strWidth.substring(1));
                        }
                    } else if (editable.hashCode() == binding.edtHeight.getText().hashCode()) {
                        if (strHeight.startsWith("0") && strHeight.length() > 1) {
                            binding.edtHeight.setText(strHeight.substring(1));
                        }
                    }
                }

                binding.edtWidth.setSelection(binding.edtWidth.getText().toString().length());
                binding.edtHeight.setSelection(binding.edtHeight.getText().toString().length());
            }
        };
    }

    @Override
    public void onSelection(int position) {
        extension = extensionList.get(position);

        extensionAdapter.swapSelectedExtension(position);
        extensionAdapter.notifyItemRangeChanged(0, extensionList.size());
    }
}