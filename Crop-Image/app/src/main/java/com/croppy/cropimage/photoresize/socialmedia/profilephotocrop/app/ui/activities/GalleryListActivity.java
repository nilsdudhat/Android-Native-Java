package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adsintegration.AdsBaseActivity;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.fragments.PhotoFragment;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Share;

import java.util.Objects;

public class GalleryListActivity extends AdsBaseActivity {

    public Activity mContext;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery_album);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        mContext = this;

        loadInterstitialAd(GalleryListActivity.this);
        showBannerAd(GalleryListActivity.this, findViewById(R.id.ad_banner));

        findViewById(R.id.img_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initViewAction();
    }

    private void initViewAction() {
        updateFragment(PhotoFragment.newInstance(GalleryListActivity.this));
    }

    public void updateFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.simpleFrameLayout, fragment);
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        beginTransaction.commit();
    }

    public void onBackPressed() {
        Share.lst_album_image.clear();
        finish();
    }
}
