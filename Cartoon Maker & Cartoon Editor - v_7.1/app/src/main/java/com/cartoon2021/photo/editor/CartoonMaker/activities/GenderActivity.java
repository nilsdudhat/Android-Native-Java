package com.cartoon2021.photo.editor.CartoonMaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.AdUtils.DebounceClickListener;
import com.cartoon2021.photo.editor.R;

public class GenderActivity extends BaseActivity {

    public static boolean isFemale = false;
    public static boolean isMale = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gender);

        findViewById(R.id.img_back).setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });

        showMiniNativeAd(GenderActivity.this, findViewById(R.id.ad_mini_native));
        showInterstitial(GenderActivity.this);
    }

    public void maleSelect(View view) {
        isMale = true;
        startActivity(new Intent(this, MaleActivity.class));
    }

    public void femaleSelect(View view) {
        isFemale = true;
        startActivity(new Intent(this, FemaleActivity.class));
    }
}
