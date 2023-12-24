package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adsintegration.AdsBaseActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomPlayerActivity extends AdsBaseActivity {

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    float current_volume;
    int position = -1;

    List<FileModel> arrayList = new ArrayList<>();

    ImageView img_lock;
    ImageView img_unlock;
    ImageView img_volume;
    ImageView img_volume_mute;
    ImageView img_orientation;
    ImageView img_fill_screen;
    ImageView img_fit_screen;
    ImageView img_zoom_screen;
    TextView text_title;
    TextView txt_date;

    SharedPreferences appPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.setTheme(CustomPlayerActivity.this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(CustomPlayerActivity.this, R.color.black));
        }

        appPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        txt_date = findViewById(R.id.txt_media_date);
        text_title = findViewById(R.id.txt_video_name);
        playerView = findViewById(R.id.exo_player);
        img_lock = findViewById(R.id.img_lock);
        img_unlock = findViewById(R.id.img_unlock);
        img_volume = findViewById(R.id.img_volume);
        img_volume_mute = findViewById(R.id.img_volume_mute);
        img_orientation = findViewById(R.id.img_orientation);
        img_fill_screen = findViewById(R.id.img_fill_screen);
        img_fit_screen = findViewById(R.id.img_fit_screen);
        img_zoom_screen = findViewById(R.id.img_zoom_screen);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        position = getIntent().getIntExtra("position", -1);

        arrayList = new ArrayList<>(Constant.INTENT_FILE_MODEL_ARRAY_LIST);
        Log.d("--arrayList--", "onCreate: " + arrayList.size());

        initializePlayer();

        boolean is_orientation_changed = appPreferences.getBoolean("is_orientation_changed", false);
        if (is_orientation_changed) {
            appPreferences.edit().putBoolean("is_orientation_changed", false).apply();

            long seekTo = appPreferences.getLong("seek_position", 0);
            simpleExoPlayer.seekTo(seekTo);
        }

        lock_screen();
        set_volume();
        screen_orientation();
        fill_screen();
    }

    private void set_volume() {
        boolean isMute = appPreferences.getBoolean("isMute", false);
        if (isMute) {
            img_volume_mute.setVisibility(View.VISIBLE);
            img_volume.setVisibility(View.GONE);

            simpleExoPlayer.setVolume(0f);
        } else {
            img_volume_mute.setVisibility(View.GONE);
            img_volume.setVisibility(View.VISIBLE);

            simpleExoPlayer.setVolume(current_volume);
        }

        img_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPreferences.edit().putBoolean("isMute", true).apply();

                img_volume_mute.setVisibility(View.VISIBLE);
                img_volume.setVisibility(View.GONE);

                simpleExoPlayer.setVolume(0f);
            }
        });

        img_volume_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPreferences.edit().putBoolean("isMute", false).apply();

                img_volume.setVisibility(View.VISIBLE);
                img_volume_mute.setVisibility(View.GONE);

                simpleExoPlayer.setVolume(current_volume);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            current_volume = simpleExoPlayer.getVolume();
        }
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (img_unlock.getVisibility() == View.VISIBLE) {
                Toast.makeText(this, "Please unlock screen to go back.", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void initializePlayer() {
        FileModel fileModel = arrayList.get(position);

        txt_date.setText(DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModel.getDateModified()));

        String path = fileModel.getPath();

        String filename = new File(path).getName();
        String result = filename.substring(0, filename.lastIndexOf("."));
        text_title.setText(result);

        Uri uri = Uri.parse(path);
        simpleExoPlayer = new SimpleExoPlayer.Builder(CustomPlayerActivity.this).build();
        DataSource.Factory factory = new DefaultDataSourceFactory(CustomPlayerActivity.this, Util.getUserAgent(CustomPlayerActivity.this, String.valueOf(R.string.app_name)));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

        current_volume = simpleExoPlayer.getVolume();

        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);

                long seekPosition = simpleExoPlayer.getCurrentPosition();
                appPreferences.edit().putLong("seek_position", seekPosition).apply();

//                current_volume = simpleExoPlayer.getVolume();
            }
        });
    }

    private void screen_orientation() {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            img_orientation.setImageResource(R.drawable.portrait_to_landscape);
        } else {
            img_orientation.setImageResource(R.drawable.landscape_to_portrait);
        }

        img_orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long seekPosition = simpleExoPlayer.getCurrentPosition();
                appPreferences.edit().putLong("seek_position", seekPosition).apply();

                appPreferences.edit().putBoolean("is_orientation_changed", true).apply();

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // code for portrait mode
                    seekTo = simpleExoPlayer.getCurrentPosition();

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    img_orientation.setImageResource(R.drawable.landscape_to_portrait);
                    simpleExoPlayer.seekTo(seekTo);
                } else {
                    // code for landscape mode
                    seekTo = simpleExoPlayer.getCurrentPosition();

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    img_orientation.setImageResource(R.drawable.portrait_to_landscape);
                    simpleExoPlayer.seekTo(seekTo);
                }
            }
        });
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

    long seekTo = 0;

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
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheUtils.deleteCache(CustomPlayerActivity.this);
            }
        }).start();
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