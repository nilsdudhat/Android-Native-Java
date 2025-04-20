package com.xbeat.videostatus.statusmaker.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;

import java.io.File;

public class VideoPreviewActivity extends BaseActivity {

    private PlayerView exoPlayerVideoDetail;
    public ProgressBar progressBarExoplayer;
    private ProgressDialog progressDialog;
    SimpleExoPlayer simpleExoPlayer;
    String videoName = "";
    ModelVideoList videoObject;
    String videoPath = "";
    VideoPreviewActivity videoPreviewActivity;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video_preview);

        showInterstitial(VideoPreviewActivity.this);
        showBannerAd(VideoPreviewActivity.this, findViewById(R.id.ad_banner));

        exoPlayerVideoDetail = (PlayerView) findViewById(R.id.exo_player_video_detail);
        progressBarExoplayer = (ProgressBar) findViewById(R.id.progressBar_exoplayer);

        videoPreviewActivity = this;

        if (getIntent().getExtras() != null) {
            this.videoObject = (ModelVideoList) new Gson().fromJson(getIntent().getStringExtra("video_object"), ModelVideoList.class);
            this.videoPath = getIntent().getStringExtra("filePath");
            this.videoName = getIntent().getStringExtra("fileName");
        }
    }

    public void handleClick(View view) {
        Uri uri = Uri.fromFile(new File(this.videoPath).getAbsoluteFile());
        switch (view.getId()) {
            case R.id.ib_back:
                onBackPressed();
                return;
            case R.id.ib_back_home:
                create_progress_dialog();
                dismiss_dialog();
                next_activity();
                return;
            case R.id.iv_wa:
                Intent intentWhatsApp = new Intent(Intent.ACTION_SEND);
                intentWhatsApp.setType("video/*");
                intentWhatsApp.setPackage("com.whatsapp");
                intentWhatsApp.putExtra(Intent.EXTRA_TEXT, "Hey!!... I have found an awesome app. You can download it from Google Play..\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                intentWhatsApp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentWhatsApp.putExtra(Intent.EXTRA_STREAM, uri);
                if (appInstalledOrNot("com.whatsapp")) {
                    try {
                        startActivity(Intent.createChooser(intentWhatsApp, "Share Status..."));
                        return;
                    } catch (ActivityNotFoundException unused4) {
                        Toast.makeText(this, "Please Install WhatsApp", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Please Install WhatsApp", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_fb:
                Intent intentFb = new Intent("android.intent.action.SEND");
                intentFb.setType("video/*");
                intentFb.setPackage("com.facebook.katana");
                intentFb.putExtra(Intent.EXTRA_TEXT, "Hey!!... I have found an awesome app. You can download it from Google Play..\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                intentFb.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentFb.putExtra(Intent.EXTRA_STREAM, uri);
                if (appInstalledOrNot("com.facebook.katana")) {
                    try {
                        startActivity(Intent.createChooser(intentFb, "Share Status..."));
                        return;
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(this, "Please Install Facebook", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Please Install Facebook", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_twitter:
                Intent intentTwit = new Intent(Intent.ACTION_SEND);
                intentTwit.setType("video/*");
                intentTwit.setPackage("com.twitter.android");
                intentTwit.putExtra(Intent.EXTRA_TEXT, "Hey!!... I have found an awesome app. You can download it from Google Play..\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                intentTwit.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentTwit.putExtra(Intent.EXTRA_STREAM, uri);
                if (appInstalledOrNot("com.twitter.android")) {
                    try {
                        startActivity(Intent.createChooser(intentTwit, "Share Status..."));
                        return;
                    } catch (ActivityNotFoundException unused2) {
                        Toast.makeText(this, "Please Install Twitter", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Please Install Twitter", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_insta:
                Intent intentInsta = new Intent("android.intent.action.SEND");
                intentInsta.setType("video/*");
                intentInsta.setPackage("com.instagram.android");
                intentInsta.putExtra(Intent.EXTRA_TEXT, "Hey!!... I have found an awesome app. You can download it from Google Play..\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                intentInsta.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentInsta.putExtra(Intent.EXTRA_STREAM, uri);
                if (appInstalledOrNot("com.instagram.android")) {
                    try {
                        startActivity(Intent.createChooser(intentInsta, "Share Status..."));
                        return;
                    } catch (ActivityNotFoundException unused2) {
                        Toast.makeText(this, "Please Install Instagram", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Please Install Instagram", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.iv_share:
                try {
                    Intent intentShare = new Intent("android.intent.action.SEND");
                    intentShare.setType("video/*");
                    intentShare.putExtra(Intent.EXTRA_TEXT, "Hey!!... I have found an awesome app. You can download it from Google Play..\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                    intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentShare.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intentShare, "Share Your Status!"));
                    return;
                } catch (Exception unused3) {
                    unused3.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    private boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void initializePlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance((Context) this, (RenderersFactory) new DefaultRenderersFactory(getApplicationContext()), (TrackSelector) new DefaultTrackSelector());
        exoPlayerVideoDetail.setPlayer(this.simpleExoPlayer);
        simpleExoPlayer.prepare(new ExtractorMediaSource(Uri.parse(this.videoPath), new DefaultDataSourceFactory((Context) this, Util.getUserAgent(this, "MyVideoMakerApplication")), new DefaultExtractorsFactory(), (Handler) null, (ExtractorMediaSource.EventListener) null));
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        exoPlayerVideoDetail.hideController();
        simpleExoPlayer.addListener(new Player.EventListener() {
            public void onIsPlayingChanged(boolean z) {
            }

            public void onLoadingChanged(boolean z) {
            }

            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            public void onPlaybackSuppressionReasonChanged(int i) {
            }

            public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            }

            public void onPositionDiscontinuity(int i) {
            }

            public void onRepeatModeChanged(int i) {
            }

            public void onSeekProcessed() {
            }

            public void onShuffleModeEnabledChanged(boolean z) {
            }

            public void onTimelineChanged(Timeline timeline, int i) {
            }

            public void onTimelineChanged(Timeline timeline, Object obj, int i) {
            }

            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            }

            public void onPlayerStateChanged(boolean z, int i) {
                int i2;
                ProgressBar progressBar;
                if (i == 2) {
                    progressBar = VideoPreviewActivity.this.progressBarExoplayer;
                    i2 = 0;
                } else {
                    progressBar = VideoPreviewActivity.this.progressBarExoplayer;
                    i2 = 4;
                }
                progressBar.setVisibility(i2);
            }
        });
    }

    public void pausePlayer() {
        this.simpleExoPlayer.setPlayWhenReady(false);
        this.simpleExoPlayer.getPlaybackState();
    }

    private void startPlayer() {
        this.simpleExoPlayer.setPlayWhenReady(true);
        this.simpleExoPlayer.getPlaybackState();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onPause() {
        super.onPause();
        pausePlayer();
    }

    public void onResume() {
        super.onResume();
        startPlayer();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.simpleExoPlayer.release();
    }

    public void onStop() {
        super.onStop();
        this.simpleExoPlayer.release();
    }

    public void onStart() {
        super.onStart();
        if (videoObject == null) {
            videoObject = (ModelVideoList) new Gson().fromJson(getIntent().getStringExtra("video_object"), ModelVideoList.class);
            videoPath = getIntent().getStringExtra("filePath");
            videoName = getIntent().getStringExtra("fileName");
        }
        initializePlayer();
    }

    public void next_activity() {
        dismiss_dialog();

        Intent intent = new Intent(VideoPreviewActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    private void create_progress_dialog() {
        dismiss_dialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Ad is loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismiss_dialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
