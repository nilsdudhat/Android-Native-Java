package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters.PhoneAlbumImagesAdapter;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adsintegration.AdsBaseActivity;
import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils.Share;

import java.util.Objects;

public class AlbumListActivity extends AdsBaseActivity {

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
}
