package com.allvideo.hdplayer.Activities;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.Models.VideoModel;
import com.allvideo.hdplayer.R;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class MyPlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    float current_volume;

    ImageView img_lock;
    ImageView img_unlock;
    ImageView img_volume;
    ImageView img_volume_mute;
    ImageView portrait_landscape;
    ImageView landscape_portrait;
    ImageView img_fill_screen;
    ImageView img_fit_screen;
    ImageView img_zoom_screen;

    ImageView exo_play;
    ImageView exo_pause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_player);

        AdUtils.showInterstitial(MyPlayerActivity.this);

        exo_play = findViewById(R.id.exo_play);
        exo_pause = findViewById(R.id.exo_pause);
        playerView = findViewById(R.id.exo_player);
        img_lock = findViewById(R.id.img_lock);
        img_unlock = findViewById(R.id.img_unlock);
        img_volume = findViewById(R.id.img_volume);
        img_volume_mute = findViewById(R.id.img_volume_mute);
        portrait_landscape = findViewById(R.id.img_portrait_landscape);
        landscape_portrait = findViewById(R.id.img_landscape_portrait);
        img_fill_screen = findViewById(R.id.img_fill_screen);
        img_fit_screen = findViewById(R.id.img_fit_screen);
        img_zoom_screen = findViewById(R.id.img_zoom_screen);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String stringPath = getIntent().getStringExtra("path");
        File filePath = new File(stringPath);

        TextView text_title = findViewById(R.id.txt_video_name);
        text_title.setText(filePath.getName());

        Uri uri = Uri.parse(stringPath);

        DataSource.Factory factory = new DefaultDataSourceFactory(MyPlayerActivity.this, Util.getUserAgent(MyPlayerActivity.this, String.valueOf(R.string.app_name)));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);
        simpleExoPlayer = new SimpleExoPlayer.Builder(MyPlayerActivity.this).build();

        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

        current_volume = simpleExoPlayer.getVolume();

        lock_screen();
        mute_volume();
        portrait_orientation();
        fill_screen();

        hideSystemUi();
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void fill_screen() {
        if (img_fill_screen.getVisibility() == View.VISIBLE) {
            img_fill_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_fill_screen.setVisibility(View.GONE);
                    img_zoom_screen.setVisibility(View.VISIBLE);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    zoom_screen();
                }
            });
        }
    }

    private void zoom_screen() {
        if (img_zoom_screen.getVisibility() == View.VISIBLE) {
            img_zoom_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_zoom_screen.setVisibility(View.GONE);
                    img_fit_screen.setVisibility(View.VISIBLE);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    fit_screen();
                }
            });
        }
    }

    private void fit_screen() {
        if (img_fit_screen.getVisibility() == View.VISIBLE) {
            img_fit_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_fit_screen.setVisibility(View.GONE);
                    img_fill_screen.setVisibility(View.VISIBLE);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    fill_screen();
                }
            });
        }
    }

    private void portrait_orientation() {
        landscape_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landscape_portrait.setVisibility(View.GONE);
                portrait_landscape.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                landscape_orientation();
            }
        });
    }

    private void landscape_orientation() {
        portrait_landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portrait_landscape.setVisibility(View.GONE);
                landscape_portrait.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                portrait_orientation();
            }
        });
    }

    private void mute_volume() {
        if (img_volume_mute.getVisibility() == View.VISIBLE) {
            img_volume_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_volume_mute.setVisibility(View.GONE);
                    img_volume.setVisibility(View.VISIBLE);
                    simpleExoPlayer.setVolume(0f);
                    unmute_volume();
                }
            });
        }
    }

    private void unmute_volume() {
        if (img_volume.getVisibility() == View.VISIBLE) {
            img_volume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_volume.setVisibility(View.GONE);
                    img_volume_mute.setVisibility(View.VISIBLE);
                    simpleExoPlayer.setVolume(current_volume);
                    mute_volume();
                }
            });
        }
    }

    private void unlock_screen() {
        if (img_unlock.getVisibility() == View.VISIBLE) {
            img_unlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_unlock.setVisibility(View.GONE);
                    img_lock.setVisibility(View.VISIBLE);
                    findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.volume_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.exo_functions).setVisibility(View.VISIBLE);
                    findViewById(R.id.duration_layout).setVisibility(View.VISIBLE);
                    lock_screen();
                }
            });
        }
    }

    private void lock_screen() {
        if (img_lock.getVisibility() == View.VISIBLE) {
            img_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_lock.setVisibility(View.GONE);
                    img_unlock.setVisibility(View.VISIBLE);
                    findViewById(R.id.left_layout).setVisibility(View.GONE);
                    findViewById(R.id.volume_layout).setVisibility(View.GONE);
                    findViewById(R.id.title_layout).setVisibility(View.GONE);
                    findViewById(R.id.exo_functions).setVisibility(View.GONE);
                    findViewById(R.id.duration_layout).setVisibility(View.GONE);
                    unlock_screen();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        if (img_unlock.getVisibility() == View.VISIBLE) {
//            Do nothing
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
    }
}