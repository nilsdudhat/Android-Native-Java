package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AdsBaseActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.databinding.ActivityHomeBinding;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.AppUtils;

import java.util.Objects;

public class HomeActivity extends AdsBaseActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        loadInterstitialAd(HomeActivity.this);
        showBannerAd(HomeActivity.this, binding.adBanner);

        binding.constraintPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.openBrowser(HomeActivity.this, "http://bluemoonmobileapps.com/privacy.html");
            }
        });

        binding.constraintShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.shareApp(HomeActivity.this);
            }
        });

        binding.constraintMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.openBrowser(HomeActivity.this, "https://play.google.com/store/apps/dev?id=4710251405227521498");
            }
        });

        binding.constraintRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.rateApp(HomeActivity.this);
            }
        });

        binding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitialAd(HomeActivity.this, new Intent(HomeActivity.this, GalleryListActivity.class), null);
            }
        });

        binding.btnMyCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitialAd(HomeActivity.this, new Intent(HomeActivity.this, MyAlbumActivity.class), null);
            }
        });
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

                QuickQuitActivity.exitApplication(HomeActivity.this);
            }
        });
    }
}