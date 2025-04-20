package com.cartoon2021.photo.editor.CartoonMaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.GlobalActivities.AlbumActivity;
import com.cartoon2021.photo.editor.R;

public class MakerHomeActivity extends BaseActivity {

    ImageView img_back;
    CardView card_edit;
    CardView card_album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_maker_home);

        showInterstitial(MakerHomeActivity.this);
        showMiniNativeAd(MakerHomeActivity.this, findViewById(R.id.ad_mini_native));

        card_album = findViewById(R.id.card_album);
        card_edit = findViewById(R.id.card_edit);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(v -> onBackPressed());

        card_edit.setOnClickListener(v -> {
            startActivity(new Intent(MakerHomeActivity.this, GenderActivity.class));
        });

        card_album.setOnClickListener(v -> {
            Intent intentAlbum = new Intent(MakerHomeActivity.this, AlbumActivity.class);
            intentAlbum.putExtra("type", "cartoon maker");
            startActivity(intentAlbum);
        });
    }

//    @Override
//    public void onBackPressed() {
//        Glob.onBackPressedIntent(MakerHomeActivity.this, new Intent(MakerHomeActivity.this, DashBoardActivity.class));
//        finish();
//    }
}
