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

public class DashBoardActivity extends BaseActivity {

    LinearLayout rl_start;
    LinearLayout rl_rate;
    LinearLayout rl_share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        showInterstitial(DashBoardActivity.this);
        showBannerAd(DashBoardActivity.this, findViewById(R.id.ad_banner));

        rl_start = findViewById(R.id.rl_start);
        rl_rate = findViewById(R.id.rl_rate);
        rl_share = findViewById(R.id.rl_share);

        YoYo.with(Techniques.FadeInLeft).duration(2500).playOn(rl_rate);

        YoYo.with(Techniques.FadeInRight).duration(2500).playOn(rl_share);

        rl_start.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, HomeActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DashBoardActivity.this, ThankYouActivity.class));
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
