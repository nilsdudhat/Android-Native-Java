package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;


import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_PAUSE;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_PLAY;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_STOP;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;

import java.util.List;

public class PlayerService extends Service implements AudioManager.OnAudioFocusChangeListener, Player.EventListener {

    private final IBinder playerBind = new PlayerBinder();
    public MediaSessionCompat mediaSession;
    private SimpleExoPlayer exoPlayer;
    private PlayerNotificationManager notificationManager;
    private MediaControllerCompat.TransportControls transportControls;

    private AudioManager audioManager;

    private String status = "Playing";
    private String Channel_ID = "playback_channel";

    private String streamUrl;
    private final MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();

            pause();
        }

        @Override
        public void onStop() {
            super.onStop();

            stop();

            notificationManager.setPlayer(null);
        }

        @Override
        public void onPlay() {
            super.onPlay();

            resume();
        }
    };
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        this.mIntent = intent;
        return playerBind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        notificationManager = new PlayerNotificationManager(PlayerService.this, Channel_ID, 2, new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public String getCurrentContentTitle(Player player) {
                return null;
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                return null;
            }

            @Nullable
            @Override
            public String getCurrentContentText(Player player) {
                return null;
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return null;
            }
        });

        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(mediasSessionCallback);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        mIntent = intent;
        Log.e("Player ", " : 1");
        if (TextUtils.isEmpty(action))
            return START_NOT_STICKY;

        Log.e("Player ", " : 2");
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            stop();

            return START_NOT_STICKY;
        }
        Log.e("Player ", " : 3");

        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            if ("PlaybackStatus_STOPPED" == status) {
                transportControls.stop();
            } else {
                transportControls.pause();
            }
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            pause();
            notificationManager.setPlayer(null);
        }
        Log.e("Player ", " : 4");

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                com.google.android.exoplayer2.util.Util.getUserAgent(this, "com.abc.maxvideoplayer"));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this);


        List<VideoModel> list = (List<VideoModel>) mIntent.getSerializableExtra("list");
        Log.e("list ", " Player service" + list.size());
        MediaSource[] videoSources = new MediaSource[list.size()];
        for (int i = 0; i < list.size(); i++) {
            videoSources[i] = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(list.get(i).getPath()));
        }

        MediaSource videoSource = videoSources.length == 1 ? videoSources[0] :
                new ConcatenatingMediaSource(videoSources);

        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

        status = "PlaybackStatus_IDLE";

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (status.equals("PlaybackStatus_IDLE"))
            stopSelf();


        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        pause();

        exoPlayer.release();
        exoPlayer.removeListener(this);

        notificationManager.setPlayer(null);

        mediaSession.release();

        super.onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                exoPlayer.setVolume(0.8f);
                resume();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                stop();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (isPlaying()) pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (isPlaying())
                    exoPlayer.setVolume(0.1f);
                break;
        }

    }

    public void playOrPause(String url) {
        if (streamUrl != null && streamUrl.equals(url)) {
            play();
        } else {
            //init(url);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                status = "PlaybackStatus.LOADING";
                break;
            case Player.STATE_ENDED:
                status = "PlaybackStatus.STOPPED";
                break;
            case Player.STATE_IDLE:
                status = "PlaybackStatus.IDLE";
                break;
            case Player.STATE_READY:
                status = playWhenReady ? "PlaybackStatus.PLAYING" : "PlaybackStatus.PAUSED";
                break;
            default:
                status = "PlaybackStatus.IDLE";
                break;
        }

//        if (!status.equals("PlaybackStatus.IDLE"))
//            notificationManager.startNotify(status);

    }

    public String getStatus() {
        return status;
    }

    private void play() {
        exoPlayer.setPlayWhenReady(true);
    }

    private void pause() {
        exoPlayer.setPlayWhenReady(false);

        audioManager.abandonAudioFocus(this);
    }

    private void resume() {
        if (streamUrl != null)
            play();
    }

    private void stop() {
        exoPlayer.stop();

        audioManager.abandonAudioFocus(this);
    }

    public boolean isPlaying() {
        return this.status.equals("PlaybackStatus.PLAYING");
    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

}
