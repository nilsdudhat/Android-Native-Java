package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.VideoPlayerActivity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings({"ALL", "unchecked"})
public class floating extends Service {

    List<VideoModel> list;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private int LAYOUT_FLAG;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private int position;
    private SharedPreferences appPreferences;
    private boolean isPIPmode;
    private int width, height;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        player.setPlayWhenReady(false);
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dothis(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("InflateParams")
    private void dothis(Intent intent) {
        appPreferences = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        width = appPreferences.getInt("width", 0);
        height = appPreferences.getInt("height", 0);
        Log.d("--format--", "popup:----- W x H " + width + "x" + height);


        mFloatingView = LayoutInflater.from(this).inflate(R.layout.popup_window, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        mFloatingView.findViewById(R.id.popup_player_view).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if (playerView.isControllerVisible()) {
                            playerView.hideController();
                        } else playerView.showController();
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        position = appPreferences.getInt("position", 0);
        list = (List<VideoModel>) intent.getSerializableExtra("list");
        long current = appPreferences.getLong("seek_position", 0);
        playerView = mFloatingView.findViewById(R.id.popup_player_view);

        ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        playerView.setLayoutParams(layoutParams);

        player = ExoPlayerFactory.newSimpleInstance(this);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "com.abc.maxvideoplayer"));

        MediaSource[] videoSources = new MediaSource[list.size()];
        for (int i = 0; i < list.size(); i++) {
            videoSources[i] = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(list.get(i).getPath()));
        }

        MediaSource videoSource = videoSources.length == 1 ? videoSources[0] :
                new ConcatenatingMediaSource(videoSources);

        playerView.setPlayer(player);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.seekTo(position, current);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        ImageButton close = mFloatingView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

        ImageButton full = mFloatingView.findViewById(R.id.full);
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                appPreferences.edit().putLong("seek_position", player.getCurrentPosition()).apply();
                appPreferences.edit().putInt("position", player.getCurrentWindowIndex()).apply();
                intent.putExtra("list", (Serializable) list);
                startActivity(intent);
                stopService();

            }
        });
    }


    public void stopService() {
        try {
            stopForeground(true);
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
