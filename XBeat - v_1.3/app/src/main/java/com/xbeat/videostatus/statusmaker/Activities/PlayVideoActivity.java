package com.xbeat.videostatus.statusmaker.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
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
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.APICall.APIClient;
import com.xbeat.videostatus.statusmaker.APICall.APIInterface;
import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.Customs.Globals;
import com.xbeat.videostatus.statusmaker.Database.DatabaseHandler;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PlayVideoActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST = 5555;
    public static String picturePath;
    public static PlayVideoActivity playVideoActivity;
    public static String unZipFilePath;
    File TempDownloadFile = null;
    APIInterface apiInterface;
    TextView btnCreateVideo;
    TextView btnTryAgain;
    Cache cache;
    int downloadCount;
    File downloadFile = null;
    public String downloadFileName = "";
    public String downloadUrl = "";
    public PlayerView exoPlayerVideoDetail;
    public boolean isClosed;
    boolean isCreateButtonClick = false;
    boolean isDownloading = false;
    boolean isRunning = true;
    boolean isUnZipDone = false;
    LinearLayout layoutTryAgain;
    Dialog mDialog;

    TextView percentageTextView;
    public ProgressBar progressBarExoplayer;
    int progressCount = 0;
    RoundedHorizontalProgressBar roundedHorizontalProgressBar;
    SimpleExoPlayer simpleExoPlayer;
    ModelVideoList videoObject;

    RelativeLayout rl_bookmark_icons;
    ImageView img_bookmarked;
    ImageView img_un_bookmarked;

    DatabaseHandler databaseHandler;

    public void callDownloadVideoApi(String str) {
    }

    public static class VideoCache {
        private static SimpleCache sDownloadCache;

        public static SimpleCache getInstance(Context context) {
            if (sDownloadCache == null) {
                sDownloadCache = new SimpleCache(new File(context.getCacheDir(), "exoCache"), new LeastRecentlyUsedCacheEvictor(1073741824));
            }
            return sDownloadCache;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_play_video);

        showInterstitial(PlayVideoActivity.this);
        showBannerAd(PlayVideoActivity.this, findViewById(R.id.ad_banner));

        rl_bookmark_icons = findViewById(R.id.rl_bookmark_icons);
        img_bookmarked = findViewById(R.id.img_bookmarked);
        img_un_bookmarked = findViewById(R.id.img_un_bookmarked);
        btnCreateVideo = (TextView) findViewById(R.id.btn_create_video);
        layoutTryAgain = (LinearLayout) findViewById(R.id.layout_try_again);
        btnTryAgain = (TextView) findViewById(R.id.btn_try_again);
        progressBarExoplayer = (ProgressBar) findViewById(R.id.progressBar_exoplayer);
        exoPlayerVideoDetail = (PlayerView) findViewById(R.id.exo_player_video_detail);

        databaseHandler = new DatabaseHandler(PlayVideoActivity.this);

        playVideoActivity = this;
        apiInterface = (APIInterface) APIClient.getClient().create(APIInterface.class);
        if (getIntent().getExtras() != null) {
            videoObject = (ModelVideoList) new Gson().fromJson(getIntent().getStringExtra("video_object"), ModelVideoList.class);
        }
        if (videoObject != null) {
            Log.e("video selected", "Selected Video Id = " + videoObject.getId());
        }
        isPermissionGiven();
        setDownloadDialog();
        btnCreateVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                isCreateButtonClick = true;
                if (isPermissionGiven()) {
                    btnCreateVideo.setEnabled(false);
                    createVideo();
                }
            }
        });
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                layoutTryAgain.setVisibility(View.GONE);
                progressBarExoplayer.setVisibility(View.VISIBLE);
                initializePlayer();
            }
        });

        String created = String.valueOf(videoObject.getCreated());
        String height = String.valueOf(videoObject.getHeight());
        String is_hot = String.valueOf(videoObject.getIsHot());
        String is_new = String.valueOf(videoObject.getIsNew());
        String thumb_url = String.valueOf(videoObject.getThumbUrl());
        String title = String.valueOf(videoObject.getTitle());
        String video_url = String.valueOf(videoObject.getVideoUrl());
        String width = String.valueOf(videoObject.getWidth());
        String zip = String.valueOf(videoObject.getZip());
        String zip_url = String.valueOf(videoObject.getZipUrl());
        String video_id = String.valueOf(videoObject.getId());

        boolean b = databaseHandler.checkIsDataAlreadyInDBorNot("favourites", "video_id", video_id);
        if (!b) {
            img_un_bookmarked.setVisibility(View.VISIBLE);
            img_bookmarked.setVisibility(View.GONE);
        } else {
            img_un_bookmarked.setVisibility(View.GONE);
            img_bookmarked.setVisibility(View.VISIBLE);
        }

        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        if (img_un_bookmarked.getVisibility() == View.VISIBLE) {
            rl_bookmark_icons.startAnimation(zoomOutAnimation);

        } else {
            rl_bookmark_icons.clearAnimation();
        }

        rl_bookmark_icons.setOnClickListener(v -> {
            if (img_bookmarked.getVisibility() == View.VISIBLE) {
                img_un_bookmarked.setVisibility(View.VISIBLE);
                img_bookmarked.setVisibility(View.GONE);
                databaseHandler.deleteItem(video_id);

                rl_bookmark_icons.startAnimation(zoomOutAnimation);

                Log.e("----click----", "unBookmarked");
            } else if (img_un_bookmarked.getVisibility() == View.VISIBLE) {
                img_un_bookmarked.setVisibility(View.GONE);
                img_bookmarked.setVisibility(View.VISIBLE);
                Log.e("----click----", "Bookmarked");

                databaseHandler.insertCalender(
                        created, height, is_hot, is_new, thumb_url,
                        title, video_url, width ,zip, zip_url, video_id);

                zoomOutAnimation.reset();
                rl_bookmark_icons.clearAnimation();
            }
        });
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == MY_PERMISSIONS_REQUEST && iArr.length > 0) {
            if (iArr[0] == -1) {
                Toast.makeText(this, "Permission denied, To be continue you have to allow the permission.", Toast.LENGTH_SHORT).show();
            } else if (iArr[0] == 0 && isCreateButtonClick) {
                isCreateButtonClick = false;
                createVideo();
            }
        }
    }

    public boolean isPermissionGiven() {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST);
        return false;
    }

    public boolean checkForDownloaded() {
        downloadUrl = videoObject.getZipUrl();
        String[] split = downloadUrl.split("/");
        downloadFileName = split[split.length - 1];
        File filesDir = getFilesDir();
        TempDownloadFile = new File(filesDir.getAbsolutePath() + File.separator + getResources().getString(R.string.oreo_zip_directory));
        if (!TempDownloadFile.exists()) {
            return false;
        }
        TempDownloadFile = new File(TempDownloadFile, "Temp_Video");
        if (!TempDownloadFile.exists()) {
            return false;
        }
        downloadFile = new File(TempDownloadFile, downloadFileName);
        File file = new File(TempDownloadFile, downloadFileName.split("zip")[0]);
        if (!file.exists() || file.length() <= 0) {
            return false;
        }
        picturePath = file.getPath();
        return true;
    }

    public void initializePlayer() {
        ExtractorMediaSource extractorMediaSource;
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance((Context) this, (RenderersFactory) new DefaultRenderersFactory(getApplicationContext()), (TrackSelector) new DefaultTrackSelector());
        exoPlayerVideoDetail.setPlayer(simpleExoPlayer);
        if (!checkForDownloaded()) {
            extractorMediaSource = new ExtractorMediaSource.Factory(new CacheDataSourceFactory(VideoCache.getInstance(this), new DefaultDataSourceFactory((Context) this, "MyVideoMakerApplication"))).createMediaSource(Uri.parse(videoObject.getVideoUrl()));
            cache = null;
        } else {
            extractorMediaSource = new ExtractorMediaSource(Uri.parse(picturePath + File.separator + "output.mp4"), new DefaultDataSourceFactory((Context) this, Util.getUserAgent(this, "MyVideoMakerApplication")), new DefaultExtractorsFactory(), (Handler) null, (ExtractorMediaSource.EventListener) null);
        }
        simpleExoPlayer.prepare(extractorMediaSource);
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
                if (i == 2) {
                    i2 = 0;
                } else {
                    i2 = 4;
                }
                progressBarExoplayer.setVisibility(i2);
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

    public void InitDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            deleteFile();
            mDialog.dismiss();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    btnCreateVideo.setEnabled(true);
                }
            }, 500);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onPause() {
        super.onPause();
        isClosed = true;
        pausePlayer();
        isRunning = false;
    }

    public void onResume() {
        super.onResume();
        isClosed = false;
        isRunning = true;
        if (mDialog != null && !mDialog.isShowing()) {
            startPlayer();
        } else if (isUnZipDone) {
            Log.e("Unzip", "Unzip success");
            btnCreateVideo.setEnabled(true);
            picturePath = downloadFile.getPath();
            if (isDownloading) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                isDownloading = false;
                isUnZipDone = false;
            }
            moveToNext();
        }
    }

    public void onStart() {
        super.onStart();
        if (videoObject == null && getIntent().getExtras() != null) {
            videoObject = (ModelVideoList) new Gson().fromJson(getIntent().getStringExtra("video_object"), ModelVideoList.class);
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

    public void deleteFile() {
        if (downloadFile.exists()) {
            if (downloadFile.delete()) {
                Log.e("-->", "file Deleted :" + downloadFile);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + downloadFile);
            }
        }
        if (!downloadFile.exists()) {
            return;
        }
        if (downloadFile.delete()) {
            Log.e("-->", "file Deleted :" + downloadFile);
            callBroadCast();
            return;
        }
        Log.e("-->", "file not Deleted :" + downloadFile);
    }

    public void callBroadCast() {
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
    }

    public void setDownloadDialog() {
        if (videoObject != null) {
            mDialog = new Dialog(this);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.download_dialog);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            ((TextView) mDialog.findViewById(R.id.tv_effect_name)).setText(videoObject.getTitle());
            roundedHorizontalProgressBar = (RoundedHorizontalProgressBar) mDialog.findViewById(R.id.rh_progress_bar);
            percentageTextView = (TextView) mDialog.findViewById(R.id.per_txt);

            showMiniNativeAd(playVideoActivity, mDialog.findViewById(R.id.ad_mini_native));

            ((TextView) mDialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    isDownloading = false;
                    PRDownloader.cancel(downloadCount);
                    percentageTextView.setText("Downloading... ");
                    InitDialog();
                }
            });
        }
    }

    public void downloadFile() {
        try {
            mDialog.show();
            isDownloading = true;
            downloadCount = PRDownloader.download(downloadUrl, TempDownloadFile.getPath(), downloadFileName).build().setOnStartOrResumeListener(new OnStartOrResumeListener() {
                public void onStartOrResume() {
                    Log.e("PRDownloader : ", "onStartOrResume");
                }
            }).setOnPauseListener(new OnPauseListener() {
                public void onPause() {
                    Log.e("PRDownloader : ", "onPause");
                }
            }).setOnCancelListener(new OnCancelListener() {
                public void onCancel() {
                    Log.e("PRDownloader : ", "onCancel");
                }
            }).setOnProgressListener(new OnProgressListener() {
                public void onProgress(Progress progress) {
                    Log.e("PRDownloader : ", "onProgress" + progress);
                    long j = (progress.currentBytes * 100) / progress.totalBytes;
                    Log.e("PRDownloader : ", "onProgress" + j);
                    int i = (int) j;
                    percentageTextView.setText("Downloading... " + i + "%");
                    roundedHorizontalProgressBar.animateProgress(0, progressCount, i);
                    progressCount = i;
                }
            }).start(new OnDownloadListener() {
                public void onDownloadComplete() {
                    Log.e("PRDownloader : ", "onDownloadComplete");
                    try {
                        if (downloadFile != null) {
                            unzip(downloadFile.getPath(), TempDownloadFile.getAbsolutePath());
                            return;
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                btnCreateVideo.setEnabled(true);
                            }
                        }, 3000);
                        Log.e("fdf", "Download Failed");
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                btnCreateVideo.setEnabled(true);
                            }
                        }, 3000);
                        Log.e("fdgd", "Download Failed with Exception - " + e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(Error error) {
                    Log.e("PRDownloader : ", "onError" + error.getServerErrorMessage());
                    Log.e("PRDownloader : ", "onError" + error.getConnectionException());
                    Log.e("PRDownloader : ", "onError" + error.isConnectionError());
                    Log.e("PRDownloader : ", "onError" + error.isServerError());
                    Toast.makeText(PlayVideoActivity.this, error.getServerErrorMessage(), Toast.LENGTH_SHORT).show();
                    percentageTextView.setText("Downloading... ");
                    InitDialog();
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ERROR: ", e.toString());
            percentageTextView.setText("Downloading... ");
            InitDialog();
            Dialog dialog = mDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public void createVideo() {
        callDownloadVideoApi(String.valueOf(videoObject.getId()));
        if (Globals.isNetworkAvailable(this)) {
            String zipUrl = videoObject.getZipUrl();
            downloadUrl = zipUrl;
            String[] split = zipUrl.split("/");
            downloadFileName = split[split.length - 1];
            File filesDir = getFilesDir();
            File file = new File(filesDir.getAbsolutePath() + File.separator + getResources().getString(R.string.oreo_zip_directory));
            TempDownloadFile = file;
            if (!file.exists()) {
                TempDownloadFile.mkdir();
            }
            File file2 = new File(TempDownloadFile, ".Temp_Video");
            TempDownloadFile = file2;
            if (!file2.exists()) {
                TempDownloadFile.mkdir();
            }
            downloadFile = new File(TempDownloadFile, downloadFileName);
            File file3 = new File(TempDownloadFile, downloadFileName.split(".zip")[0]);
            if (!file3.exists()) {
                pausePlayer();
                downloadFile();
                return;
            }
            unZipFilePath = file3.getPath();
            moveToNext();
            return;
        }
        Toast.makeText(this, "Please Connect to Internet.", Toast.LENGTH_SHORT).show();
    }

    public final void moveToNext() {
        Log.e("MoveToNext", "After Unzip file MoveToNext");
        btnCreateVideo.setEnabled(true);

        Intent intent = new Intent(PlayVideoActivity.this, VideoMakingActivity.class);
        intent.putExtra("fromBackground", false);
        intent.putExtra("video_object", new Gson().toJson((Object) videoObject));
        startActivity(intent);
    }

    public void unzip(String str, String str2) {
        try {
            ZipFile zipFile = new ZipFile(new File(str));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                unzipEntry(zipFile, (ZipEntry) entries.nextElement(), str2);
            }
            isUnZipDone = true;
            if (isRunning) {
                Log.e("Unzip", "Unzip success");
                btnCreateVideo.setEnabled(true);
                picturePath = downloadFile.getPath();
                if (isDownloading) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    isDownloading = false;
                    isUnZipDone = false;
                }
                moveToNext();
            }
        } catch (Exception e) {
            Log.e("Unzip zip", "Unzip exception", e);
        }
    }

    private void unzipEntry(ZipFile zipFile, ZipEntry zipEntry, String str) throws IOException {
        if (zipEntry.isDirectory()) {
            createDir(new File(str, zipEntry.getName()));
            return;
        }
        File file = new File(str, zipEntry.getName());
        if (!file.getParentFile().exists()) {
            createDir(file.getParentFile());
        }
        Log.v("ZIP E", "Extracting: " + zipEntry);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        while (true) {
            try {
                int read = bufferedInputStream.read();
                if (read != -1) {
                    bufferedOutputStream.write(read);
                } else {
                    bufferedOutputStream.close();
                    return;
                }
            } finally {
                // Do Nothing
            }
        }
    }

    private void createDir(File file) {
        if (!file.exists()) {
            Log.v("ZIP E", "Creating dir " + file.getName());
            if (!file.mkdirs()) {
                throw new RuntimeException("Can not create dir " + file);
            }
        }
    }
}