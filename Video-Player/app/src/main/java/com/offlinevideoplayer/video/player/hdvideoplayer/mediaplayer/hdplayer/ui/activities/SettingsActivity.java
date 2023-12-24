package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AdsBaseActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

public class SettingsActivity extends AdsBaseActivity {

    SwitchCompat switch_theme;
    ImageView img_theme;
    SwitchCompat switch_scroll_to_highlighted;
    SwitchCompat switch_recent_video;

    RelativeLayout rl_display_recent_options;
    LinearLayout ll_recent_options;
    ImageView img_drop_down;

    TextView txt_recent_video, txt_scroll_to_highlighted;

    ImageView img_back;

    SharedPreferences mPrefs;
    boolean isDark = true;
    boolean isFloating = true;
    boolean isScrollToHighlight = true;
    boolean isDisplayRecentVideo = true;
    boolean isQuickQuit = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        isDark = mPrefs.getBoolean("theme", true);
        Log.d("--theme--", "isDark: " + isDark);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Utils.bottomNavigationBlackColor(SettingsActivity.this);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Utils.bottomNavigationWhiteColor(SettingsActivity.this);
        }

        setContentView(R.layout.activity_settings);

        showBannerAd(SettingsActivity.this, findViewById(R.id.ad_banner));

        img_theme = findViewById(R.id.img_theme);
        img_back = findViewById(R.id.img_back);
        rl_display_recent_options = findViewById(R.id.rl_display_recent_options);
        ll_recent_options = findViewById(R.id.ll_recent_options);
        img_drop_down = findViewById(R.id.img_drop_down);
        switch_theme = findViewById(R.id.switch_theme);
        switch_scroll_to_highlighted = findViewById(R.id.switch_scroll_to_highlighted);
        switch_recent_video = findViewById(R.id.switch_recent_video);

        txt_recent_video = findViewById(R.id.txt_recent_video);
        txt_scroll_to_highlighted = findViewById(R.id.txt_scroll_to_highlighted);

        TextView txt_version_name = findViewById(R.id.txt_version_name);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            txt_version_name.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            txt_version_name.setVisibility(View.GONE);
            e.printStackTrace();
        }

        switch_theme.setChecked(isDark);

        if (switch_theme.isChecked()) {
            img_theme.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.ic_dark));
        } else {
            img_theme.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.ic_light));
        }

        ll_recent_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl_display_recent_options.getVisibility() == View.VISIBLE) {
                    rl_display_recent_options.setVisibility(View.GONE);
                    img_drop_down.setRotation(0);
                } else {
                    rl_display_recent_options.setVisibility(View.VISIBLE);
                    img_drop_down.setRotation(180);
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch_theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try {
                    mPrefs.edit().putBoolean("theme", isChecked).apply();
                    if (switch_theme.isChecked()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    Log.d("--theme--", "switch_theme: " + isChecked);
                    recreate();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        isFloating = mPrefs.getBoolean("floating_btn", true);

        isScrollToHighlight = mPrefs.getBoolean("scroll_down_to_highlight", true);

        switch_scroll_to_highlighted.setChecked(isScrollToHighlight);

        switch_scroll_to_highlighted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPrefs.edit().putBoolean("scroll_down_to_highlight", true).apply();
                    txt_scroll_to_highlighted.setText("When disable, the Highlighted video will not be show on top in the home screen");
                } else {
                    mPrefs.edit().putBoolean("scroll_down_to_highlight", false).apply();
                    txt_scroll_to_highlighted.setText("When enable, the Highlighted video will be show on top in the home screen");
                }
            }
        });

        isDisplayRecentVideo = mPrefs.getBoolean("display_recent_video", true);

        switch_recent_video.setChecked(isDisplayRecentVideo);

        switch_recent_video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPrefs.edit().putBoolean("display_recent_video", true).apply();
                    txt_recent_video.setText("When disable, the recently played video card will not be display on the home screen");
                } else {
                    mPrefs.edit().putBoolean("display_recent_video", false).apply();
                    txt_recent_video.setText("When enable, the recently played video card will be display on the home screen");
                }
                Utils.refreshFiles = true;
            }
        });

        isQuickQuit = mPrefs.getBoolean("display_quick_quit", true);

    }
}
