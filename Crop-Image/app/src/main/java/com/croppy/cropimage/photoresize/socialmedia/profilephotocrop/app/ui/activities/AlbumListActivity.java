package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.ui.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adapters.PhoneAlbumImagesAdapter;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adsintegration.AdsBaseActivity;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils.Share;

import java.util.Objects;

public class AlbumListActivity extends AdsBaseActivity {

    public Activity mContext;

    RecyclerView rcv_album_images;
    TextView tv_title_album_image;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_album);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        this.mContext = this;

        loadInterstitialAd(AlbumListActivity.this);
        showBannerAd(AlbumListActivity.this, findViewById(R.id.ad_banner));

        initView();

        setRecyclerView();
    }

    private void setRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        PhoneAlbumImagesAdapter albumAdapter = new PhoneAlbumImagesAdapter(AlbumListActivity.this, Share.lst_album_image);

        rcv_album_images.setLayoutManager(gridLayoutManager);
        try {
            rcv_album_images.setAdapter(albumAdapter);
            tv_title_album_image.setText(getIntent().getExtras().getString(Share.KEYNAME.ALBUM_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rcv_album_images = findViewById(R.id.rcv_album_images);
        tv_title_album_image = findViewById(R.id.tv_title_album_image);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
