package com.cartoon2021.photo.editor.GlobalActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.CartoonEditor.Activity.EditorHomeActivity;
import com.cartoon2021.photo.editor.CartoonMaker.activities.MakerHomeActivity;
import com.cartoon2021.photo.editor.R;

public class DashBoardActivity extends BaseActivity {

    public static Uri uri;

    CardView card_cartoon_editor;
    CardView card_cartoon_maker;

    ImageView img_back;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_dashboard);

        showInterstitial(DashBoardActivity.this);
        showMiniNativeAd(DashBoardActivity.this, findViewById(R.id.ad_mini_native));

        card_cartoon_maker = findViewById(R.id.card_cartoon_maker);
        card_cartoon_editor = findViewById(R.id.card_cartoon_editor);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> onBackPressed());

        card_cartoon_maker.setOnClickListener(v -> {
            startActivity(new Intent(DashBoardActivity.this, MakerHomeActivity.class));
        });

        card_cartoon_editor.setOnClickListener(v -> {
            startActivity(new Intent(DashBoardActivity.this, EditorHomeActivity.class));
        });
    }

//    @Override
//    public void onBackPressed() {
//        Glob.onBackPressedIntent(DashBoardActivity.this, new Intent(DashBoardActivity.this, StartActivity.class));
//        finish();
//    }
}