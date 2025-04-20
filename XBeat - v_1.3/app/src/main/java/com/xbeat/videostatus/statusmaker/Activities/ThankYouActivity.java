package com.xbeat.videostatus.statusmaker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.Customs.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.R;

public class ThankYouActivity extends BaseActivity {

    TextView txt_yes;
    TextView txt_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        showInterstitial(ThankYouActivity.this);
        showBannerAd(ThankYouActivity.this, findViewById(R.id.ad_banner));

        txt_yes = findViewById(R.id.txt_yes);
        txt_no = findViewById(R.id.txt_no);

        txt_yes.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        txt_no.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(ThankYouActivity.this, DashBoardActivity.class));
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}
