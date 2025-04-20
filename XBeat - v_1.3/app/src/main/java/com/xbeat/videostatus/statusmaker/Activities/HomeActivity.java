package com.xbeat.videostatus.statusmaker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.Customs.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.R;

public class HomeActivity extends BaseActivity {

    LinearLayout ll_favourites;
    LinearLayout ll_edit;
    LinearLayout ll_album;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showInterstitial(HomeActivity.this);
        showBannerAd(HomeActivity.this, findViewById(R.id.ad_banner));

        ll_edit = findViewById(R.id.ll_edit);
        ll_favourites = findViewById(R.id.ll_favourites);
        ll_album = findViewById(R.id.ll_album);

        YoYo.with(Techniques.FadeInLeft).duration(2500).playOn(ll_favourites);

        YoYo.with(Techniques.FadeInRight).duration(2500).playOn(ll_album);

        ll_album.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(HomeActivity.this, AlbumActivity.class));
            }
        });

        ll_favourites.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(HomeActivity.this, FavouritesActivity.class));
            }
        });

        ll_edit.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(HomeActivity.this, APIActivity.class));
            }
        });
    }
}
