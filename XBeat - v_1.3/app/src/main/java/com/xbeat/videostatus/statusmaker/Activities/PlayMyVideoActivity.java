package com.xbeat.videostatus.statusmaker.Activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.R;

import java.io.File;

public class PlayMyVideoActivity extends BaseActivity {

    public PlayerView exoPlayerVideoDetail;
    public LinearLayout layoutTryAgain;
    public ProgressBar progressBarExoplayer;
    SimpleExoPlayer simpleExoPlayer;
    String videoFilePath;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_play_my_video);

        showInterstitial(PlayMyVideoActivity.this);
        showMiniNativeAd(PlayMyVideoActivity.this, findViewById(R.id.ad_mini_native));

        ImageView ibBack = (ImageView) findViewById(R.id.ib_back);
        TextView btnTryAgain = (TextView) findViewById(R.id.btn_try_again);
        exoPlayerVideoDetail = (PlayerView) findViewById(R.id.exo_player_video_detail);
        ImageView ibDelete = (ImageView) findViewById(R.id.ib_delete);
        layoutTryAgain = (LinearLayout) findViewById(R.id.layout_try_again);
        progressBarExoplayer = (ProgressBar) findViewById(R.id.progressBar_exoplayer);

        if (getIntent().getExtras() != null) {
            this.videoFilePath = getIntent().getStringExtra("videoFilePath");
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                layoutTryAgain.setVisibility(View.GONE);
                progressBarExoplayer.setVisibility(View.VISIBLE);
                initializePlayer();
            }
        });
        ibDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pausePlayer();
                final Dialog dialog = new Dialog(PlayMyVideoActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete_video);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
//                        deleteFile(new File(videoFilePath));
                        deleteNew(PlayMyVideoActivity.this, new File(videoFilePath));
                        finish();
                    }
                });
                dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void deleteNew(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        Log.d("--click--", "where: " + where);

        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };

        Log.d("--click--", "length: " + selectionArgs.length);

        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        Log.d("--click--", "toString: " + filesUri.toString());

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {
            Log.d("--click--", "file.exists()");

            contentResolver.delete(filesUri, where, selectionArgs);
        }

        if (file.exists()) {
            Toast.makeText(context, "Could not delete file, Sorry.", Toast.LENGTH_SHORT).show();
        }
    }

    public void share1(View view) {
        Uri uri = Uri.fromFile(new File(videoFilePath).getAbsoluteFile());
        try {
            Intent intentShare = new Intent("android.intent.action.SEND");
            intentShare.setType("video/*");
            intentShare.putExtra("android.intent.extra.TITLE", "I’ve use this Application. Download on Google Play..\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
            intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShare.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intentShare, "Share Your Status!"));
        } catch (Exception unused3) {
            unused3.printStackTrace();
        }
    }

    public void initializePlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance((Context) this, (RenderersFactory) new DefaultRenderersFactory(getApplicationContext()), (TrackSelector) new DefaultTrackSelector());
        exoPlayerVideoDetail.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(new ExtractorMediaSource(Uri.parse(videoFilePath), new DefaultDataSourceFactory((Context) this, Util.getUserAgent(this, "MyVideoMakerApplication")), new DefaultExtractorsFactory(), (Handler) null, (ExtractorMediaSource.EventListener) null));
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

            public void onPlayerError(ExoPlaybackException exoPlaybackException) {
                if (exoPlaybackException != null && exoPlaybackException.getMessage() != null && exoPlaybackException.getMessage().contains("Unable to connect")) {
                    exoPlayerVideoDetail.hideController();
                    layoutTryAgain.setVisibility(View.VISIBLE);
                }
            }

            public void onPlayerStateChanged(boolean z, int i) {
                int i2;
                ProgressBar progressBar;
                if (i == 2) {
                    progressBar = progressBarExoplayer;
                    i2 = 0;
                } else {
                    progressBar = progressBarExoplayer;
                    i2 = 4;
                }
                progressBar.setVisibility(i2);
            }
        });
    }

    public void pausePlayer() {
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    private void startPlayer() {
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
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

    public void onStart() {
        super.onStart();
        if (videoFilePath == null && getIntent().getExtras() != null) {
            videoFilePath = getIntent().getStringExtra("videoFilePath");
        }
        initializePlayer();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        simpleExoPlayer.release();
    }

    public void onStop() {
        super.onStop();
        simpleExoPlayer.release();
    }

    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.delete()) {
                Log.e("-->", "file Deleted :" + file);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + file);
            }
        }
        if (!file.exists()) {
            return;
        }
        if (file.delete()) {
            Log.e("-->", "file Deleted :" + file);
            callBroadCast();
            return;
        }
        Log.e("-->", "file not Deleted :" + file);
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{getFilesDir().getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + str + ":");
                    StringBuilder sb = new StringBuilder();
                    sb.append("-> uri=");
                    sb.append(uri);
                    Log.e("ExternalStorage", sb.toString());
                }
            });
            return;
        }
        Log.e("-->", " < 14");
        sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }
}
