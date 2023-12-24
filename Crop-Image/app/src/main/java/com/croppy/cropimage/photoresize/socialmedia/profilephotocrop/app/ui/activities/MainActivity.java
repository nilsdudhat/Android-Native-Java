package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adapters.ShowCasePagerAdapter;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adsintegration.AdsBaseActivity;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.databinding.ActivityMainBinding;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Share;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AdsBaseActivity {

    public String mSelectedOutputPath;

    ActivityMainBinding binding;

    ArrayList<Integer> showCaseImageList = new ArrayList<>(Arrays.asList(R.drawable.img_showcase_1, R.drawable.img_showcase_2, R.drawable.img_showcase_3));
    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // There are no request codes
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Log.d("--url--", "onClick: " + mSelectedOutputPath);
                        String sb2 = "file:///" + mSelectedOutputPath;
                        Share.SAVED_BITMAP = Uri.parse(sb2);
                        Share.imageUrl = mSelectedOutputPath;
                        Intent intent = new Intent(MainActivity.this, CropActivity.class);
                        intent.putExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE, mSelectedOutputPath);
                        intent.setData(Share.SAVED_BITMAP);
                        showInterstitialAd(MainActivity.this, intent, null);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.setBottomNavigationColor(MainActivity.this, getColor(R.color.black));
        }

        showBannerAd(MainActivity.this, binding.adBanner);

        binding.constraintPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openBrowser(MainActivity.this, "http://bluemoonmobileapps.com/privacy.html");
            }
        });

        binding.constraintShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.shareApp(MainActivity.this);
            }
        });

        binding.constraintMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openBrowser(MainActivity.this, "https://play.google.com/store/apps/dev?id=4710251405227521498");
            }
        });

        binding.constraintRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.rateApp(MainActivity.this);
            }
        });

        ShowCasePagerAdapter showCasePagerAdapter = new ShowCasePagerAdapter(MainActivity.this, showCaseImageList);
        binding.autoViewPager.setAdapter(showCasePagerAdapter);
        binding.autoViewPager.startAutoScroll();
        binding.autoViewPager.setInterval(4000);
        binding.autoViewPager.setStopScrollWhenTouch(false);
        binding.dotsIndicator.setViewPager(binding.autoViewPager);

        binding.btnCroppy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogScreenChooser = new Dialog(MainActivity.this, R.style.CustomDialog);
                dialogScreenChooser.setCancelable(true);
                dialogScreenChooser.setCanceledOnTouchOutside(true);
                dialogScreenChooser.setContentView(R.layout.dialog_edit);
                dialogScreenChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogScreenChooser.show();

                ImageView btn_open_camera = dialogScreenChooser.findViewById(R.id.btn_open_camera);
                ImageView btn_open_gallery = dialogScreenChooser.findViewById(R.id.btn_open_gallery);
                ImageView img_close = dialogScreenChooser.findViewById(R.id.img_close);

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogScreenChooser.dismiss();
                    }
                });

                btn_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogScreenChooser.dismiss();

                        showInterstitialAd(MainActivity.this, new Intent(MainActivity.this, GalleryListActivity.class), null);
                    }
                });

                btn_open_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogScreenChooser.dismiss();

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", createImageFile());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraResultLauncher.launch(intent);
                    }
                });
            }
        });

        binding.btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitialAd(MainActivity.this, new Intent(MainActivity.this, MyAlbumActivity.class), null);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("--touch--", "dispatchTouchEvent X: " + ev.getX() + "--- Y: " + ev.getY());
        if (ev.getY() < 1200) {
            return false; // disabling touch event for viewpager
        }
        return super.dispatchTouchEvent(ev);
    }

    private File createImageFile() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + getPackageName() + "/CamPic/");
        storageDir.mkdirs();
        File image = null;
        try {
            image = new File(storageDir, getString(R.string.temp));
            if (image.exists())
                image.delete();
            image.createNewFile();

            mSelectedOutputPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onBackPressed() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.SheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        bottomSheetDialog.setCancelable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomSheetDialog.create();
        }
        bottomSheetDialog.show();

        TextView txt_no = bottomSheetDialog.findViewById(R.id.txt_no);
        TextView txt_yes = bottomSheetDialog.findViewById(R.id.txt_yes);

        Objects.requireNonNull(txt_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        Objects.requireNonNull(txt_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

                System.exit(0);
            }
        });
    }
}