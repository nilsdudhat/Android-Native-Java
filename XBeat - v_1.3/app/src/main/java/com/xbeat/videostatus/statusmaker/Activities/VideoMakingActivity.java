package com.xbeat.videostatus.statusmaker.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
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
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.AdUtils.AdUtils;
import com.xbeat.videostatus.statusmaker.AdUtils.AppUtility;
import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.AdUtils.Constant;
import com.xbeat.videostatus.statusmaker.AdUtils.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Adapters.ImageListAdapter;
import com.xbeat.videostatus.statusmaker.Models.ImageJsonData;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.Models.VideoJsonData;
import com.xbeat.videostatus.statusmaker.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoMakingActivity extends BaseActivity implements OnUserEarnedRewardListener, AdUtils.RewardedDismissed {

    public static final int REQUEST_PICK = 9162;
    public static VideoMakingActivity videoMakingActivity;
    public String backgroundVideoPath = "";
    String compareStr = "";
    String compareString = "";
    String destinationVideoFileName = "";
    public PlayerView exoPlayerVideoDetail;

    public int i = 0;
    public ArrayList<ImageJsonData> imageList = new ArrayList<>();
    ImageListAdapter imageListAdapter;
    GetImageListFromJSON imageListFromJSONTask;

    boolean isCraftingDone = false;
    int isProgressMatch = 0;
    boolean isRunning = true;
    Animation mAnimation;
    Runnable mRunnable = new MyRunnableTask();
    Handler myHandler = new Handler();
    String outputVideoFilePath = "";

    public CircularProgressView pgsBar;
    public ProgressBar progressBarExoplayer;
    private ProgressDialog progressDialog;
    String pythonFilePath = "";
    public JSONObject pythonJsonObject;
    ConstraintLayout rlExportVideo;
    int roundedImageHeight;
    int roundedImageWidth;
    int selectedImagePosition = 0;
    SimpleExoPlayer simpleExoPlayer;
    String unZipFileName = "";
    String videoDestinationPath = "";
    public long videoDuration;
    public ArrayList<VideoJsonData> videoList = new ArrayList<>();
    ModelVideoList videoObject;

    TextView bt_cancel;

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }

    @Override
    public void onRewardedDismissed() {
        if (rlExportVideo.getVisibility() == View.GONE) {
            saveVideo();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetImageListFromJSON extends AsyncTask<String, String, String> {
        private GetImageListFromJSON() {
        }

        GetImageListFromJSON(VideoMakingActivity videoMakingActivity, VideoMakingActivity videoMakingActivity2, VideoMakingActivity videoMakingActivity3, VideoMakingActivity videoMakingActivity4) {
            this();
        }

        public String doInBackground(String... strArr) {
            try {
                pythonJsonObject = new JSONObject(getStringFromFile(strArr[0]));
                JSONArray jSONArray = pythonJsonObject.getJSONArray("images");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    imageList.add(new ImageJsonData(jSONObject.getString("name"), jSONObject.getInt("w"), jSONObject.getInt("h"), getFilePath(VideoMakingActivity.this) + unZipFileName + File.separator + jSONObject.getString("name"), jSONObject.getJSONArray("prefix"), jSONObject.getJSONArray("postfix")));
                }
                videoDuration = Long.parseLong(pythonJsonObject.getJSONObject(MimeTypes.BASE_TYPE_VIDEO).getString("duration"));
                return null;
            } catch (Exception unused) {
                return null;
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        public void onPostExecute(String str) {
            imageListAdapter.notifyDataSetChanged();
        }

        public void onPreExecute() {
            super.onPreExecute();
        }
    }

    class MyRunnableTask implements Runnable {
        MyRunnableTask() {
        }

        public void run() {
            rlExportVideo.setVisibility(View.GONE);
            isProgressMatch = 0;
            pgsBar.setProgress(0, false);
            Toast.makeText(VideoMakingActivity.this, "Video Saved in Gallery.", Toast.LENGTH_SHORT).show();
            myHandler.removeCallbacks(mRunnable);
            isCraftingDone = true;
            if (isRunning) {
                isCraftingDone = false;
                moveToNext();
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video_making);

//        showInterstitial(VideoMakingActivity.this);
        showBannerAd(VideoMakingActivity.this, findViewById(R.id.ad_banner));
        showMiniNativeAd(VideoMakingActivity.this, findViewById(R.id.ad_mini_native));

        bt_cancel = (TextView) findViewById(R.id.bt_cancel);
        exoPlayerVideoDetail = findViewById(R.id.exo_player_video_detail);
        progressBarExoplayer = (ProgressBar) findViewById(R.id.progressBar_exoplayer);
        pgsBar = (CircularProgressView) findViewById(R.id.pBar);
        RecyclerView rvImageList = (RecyclerView) findViewById(R.id.rv_image_list);
        ImageView ibBack = (ImageView) findViewById(R.id.ib_back);
        TextView btnExportVideo = (TextView) findViewById(R.id.btn_export_video);
        rlExportVideo = (ConstraintLayout) findViewById(R.id.rl_export_video);

        videoMakingActivity = this;
        this.mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        if (getIntent().getExtras() != null) {
            this.videoObject = (ModelVideoList) new Gson().fromJson(getIntent().getStringExtra("video_object"), ModelVideoList.class);
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        exoPlayerVideoDetail.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (rlExportVideo.getVisibility() == View.VISIBLE) {
                    getSystemService(INPUT_METHOD_SERVICE);
                    exoPlayerVideoDetail.hideController();
                    rlExportVideo.startAnimation(mAnimation);
                    return false;
                } else if (isPlaying()) {
                    pausePlayer();
                    return false;
                } else {
                    startPlayer();
                    return false;
                }
            }
        });
        this.rlExportVideo.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exoPlayerVideoDetail.hideController();
                rlExportVideo.startAnimation(mAnimation);
                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("RewardPreferences", MODE_PRIVATE);

        Dialog dialogRewarded = new Dialog(VideoMakingActivity.this);
        dialogRewarded.setCancelable(true);
        dialogRewarded.setContentView(R.layout.dialod_rewarded);
        dialogRewarded.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogRewarded.findViewById(R.id.tv_no).setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                dialogRewarded.dismiss();
                sharedPreferences.edit().putBoolean("reward", true).apply();
            }
        });

        dialogRewarded.findViewById(R.id.tv_yes).setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                dialogRewarded.dismiss();
                sharedPreferences.edit().putBoolean("reward", true).apply();

                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AdUtils.fullscreenCount = 0;
                        showRewardedInterstitialAd(VideoMakingActivity.this, VideoMakingActivity.this);
                    }
                }, 0);
            }
        });

        btnExportVideo.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {

                if (rlExportVideo.getVisibility() == View.GONE) {
                    AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(VideoMakingActivity.this, Constant.COUNTER, ""));
                    saveVideo();
                }

//                if (AppUtility.getBoolean(VideoMakingActivity.this, Constant.IS_GOOGLE_AD, false)) {
//                    if (!AppUtility.getString(VideoMakingActivity.this, Constant.GOOGLE_REWARD, "").trim().isEmpty()) {
//
//                        if (sharedPreferences.getBoolean("reward", false)) {
//                            dialogRewarded.show();
//
//                            Log.d("zigzag---", "onClick: " + "reward");
//                            sharedPreferences.edit().putBoolean("reward", false).apply();
//                        } else {
//                            sharedPreferences.edit().putBoolean("reward", true).apply();
//                            AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(VideoMakingActivity.this, Constant.COUNTER, ""));
//
//                            if (rlExportVideo.getVisibility() == View.GONE) {
//                                saveVideo();
//                            }
//                        }
//                    } else {
//                        if (rlExportVideo.getVisibility() == View.GONE) {
//                            AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(VideoMakingActivity.this, Constant.COUNTER, ""));
//                            saveVideo();
//                        }
//                    }
//                } else {
//                    if (rlExportVideo.getVisibility() == View.GONE) {
//                        AdUtils.fullscreenCount = Integer.parseInt(AppUtility.getString(VideoMakingActivity.this, Constant.COUNTER, ""));
//                        saveVideo();
//                    }
//                }
            }
        });

        String substring = this.videoObject.getZipUrl().substring(this.videoObject.getZipUrl().lastIndexOf(47) + 1);
        if (substring.indexOf(".") > 0) {
            this.unZipFileName = substring.substring(0, substring.lastIndexOf("."));
        }
        imageListAdapter = new ImageListAdapter(this, this.imageList);
        rvImageList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvImageList.setAdapter(this.imageListAdapter);
        pythonFilePath = getFilePath(this) + this.unZipFileName + File.separator + "python.json";
        imageListFromJSONTask = (GetImageListFromJSON) new GetImageListFromJSON(this, this, this, this).execute(new String[]{this.pythonFilePath});
        outputVideoFilePath = getFilePath(this) + this.unZipFileName + File.separator + "output.mp4";
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_example);
        if (decodeResource != null) {
            File file = new File(getFilePath(videoMakingActivity));
            File file2 = new File(file, getString(R.string.watermark_image));
            if (file2.exists()) {
                file2.delete();
            }
            saveImageToGallery(file2, decodeResource);
            file2.getPath();
        }

        bt_cancel.setOnClickListener(v -> {
            showCancelDownloadDialog();
        });
    }

    public boolean isPlaying() {
        return this.simpleExoPlayer.getPlaybackState() == Player.STATE_READY && this.simpleExoPlayer.getPlayWhenReady();
    }

    private void initializePlayer() {
        this.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance((Context) this, (RenderersFactory) new DefaultRenderersFactory(getApplicationContext()), (TrackSelector) new DefaultTrackSelector());
        this.exoPlayerVideoDetail.setPlayer(this.simpleExoPlayer);
        this.simpleExoPlayer.prepare(new ExtractorMediaSource(Uri.parse(this.outputVideoFilePath), new DefaultDataSourceFactory((Context) this, Util.getUserAgent(this, "MyVideoMakerApplication")), new DefaultExtractorsFactory(), (Handler) null, (ExtractorMediaSource.EventListener) null));
        this.simpleExoPlayer.setPlayWhenReady(true);
        this.simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        this.exoPlayerVideoDetail.hideController();
        this.simpleExoPlayer.addListener(new Player.EventListener() {
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

            public void onTimelineChanged(@NonNull Timeline timeline, int i) {
            }

            public void onTimelineChanged(@NonNull Timeline timeline, @NonNull Object obj, int i) {
            }

            public void onTracksChanged(@NonNull TrackGroupArray trackGroupArray, @NonNull TrackSelectionArray trackSelectionArray) {
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
        this.simpleExoPlayer.setPlayWhenReady(false);
        this.simpleExoPlayer.getPlaybackState();
    }

    public void startPlayer() {
        this.simpleExoPlayer.setPlayWhenReady(true);
        this.simpleExoPlayer.getPlaybackState();
    }

    public void onPause() {
        super.onPause();
        pausePlayer();
        this.isRunning = false;
    }

    public void onResume() {
        super.onResume();
        startPlayer();
        this.isRunning = true;
        if (this.isCraftingDone) {
            this.isCraftingDone = false;
            moveToNext();
        }
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
        if (this.videoObject == null && getIntent().getExtras() != null) {
            this.videoObject = (ModelVideoList) new Gson().fromJson(getIntent().getStringExtra("video_object"), ModelVideoList.class);
            if (this.videoObject.getZip().indexOf(".") > 0) {
                this.unZipFileName = this.videoObject.getZip().substring(0, this.videoObject.getZip().lastIndexOf("."));
            }
            this.outputVideoFilePath = getFilePath(this) + this.unZipFileName + File.separator + "output.mp4";
        }
        initializePlayer();
    }

    public String getFilePath(Context context) {
        String absolutePath = context.getFilesDir().getAbsolutePath();
        File file = new File(absolutePath + File.separator + context.getResources().getString(R.string.oreo_zip_directory) + File.separator + ".Temp_Video");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath() + File.separator;
    }

    public final String getDestinationPath(Activity activity) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "XBeat - Video Status Maker");
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file, "Created");
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file2.getAbsolutePath() + File.separator;
    }

    public String getStringFromFile(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    fileInputStream.close();
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            Log.e("FileException>>>>", Log.getStackTraceString(e));
            return "";
        }
    }

    public void saveVideo() {
        pausePlayer();
        this.exoPlayerVideoDetail.hideController();
        this.isProgressMatch = 0;
        pgsBar.setProgress(0, false);
        this.pgsBar.invalidate();
        this.rlExportVideo.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                makeNewQuery("");
            }
        }, 500);
    }

    private void makeNewQuery(String str) {
        this.destinationVideoFileName = this.unZipFileName + "_" + System.currentTimeMillis() + ".mp4";
        this.videoDestinationPath = getDestinationPath(this) + this.destinationVideoFileName;
        new File(this.videoDestinationPath);
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jSONArray = this.pythonJsonObject.getJSONArray("e");
            if (jSONArray.length() != 0) {
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    arrayList.add(replaceString(jSONArray.getString(i2)));
                }
            }
            for (int i3 = 0; i3 < this.imageList.size(); i3++) {
                if (this.imageList.get(i3).getPrefix().length() != 0) {
                    for (int i4 = 0; i4 < this.imageList.get(i3).getPrefix().length(); i4++) {
                        arrayList.add(replaceString(this.imageList.get(i3).getPrefix().getString(i4)));
                    }
                }
                arrayList.add(this.imageList.get(i3).getSelectedPath() == null ? this.imageList.get(i3).getImagePath() : this.imageList.get(i3).getSelectedPath());
                if (this.imageList.get(i3).getPostfix().length() != 0) {
                    for (int i5 = 0; i5 < this.imageList.get(i3).getPostfix().length(); i5++) {
                        arrayList.add(replaceString(this.imageList.get(i3).getPostfix().getString(i5)));
                    }
                }
            }
            JSONArray jSONArray2 = this.pythonJsonObject.getJSONArray("static_inputs");
            for (int i6 = 0; i6 < jSONArray2.length(); i6++) {
                JSONObject jSONObject = jSONArray2.getJSONObject(i6);
                this.videoList.add(new VideoJsonData(jSONObject.getString("name"), getFilePath(videoMakingActivity) + this.unZipFileName + File.separator + jSONObject.getString("name"), jSONObject.getJSONArray("prefix"), jSONObject.getJSONArray("postfix")));
                this.backgroundVideoPath = this.videoList.get(0).getVideoPath();
                if (this.videoList.get(i6).getPreFix().length() != 0) {
                    for (int i7 = 0; i7 < this.videoList.get(i6).getPreFix().length(); i7++) {
                        arrayList.add(replaceString(this.videoList.get(i6).getPreFix().getString(i7)));
                    }
                }
                arrayList.add(this.videoList.get(i6).getVideoPath());
                if (this.videoList.get(i6).getPostFix().length() != 0) {
                    for (int i8 = 0; i8 < this.videoList.get(i6).getPostFix().length(); i8++) {
                        arrayList.add(replaceString(this.videoList.get(i6).getPostFix().getString(i8)));
                    }
                }
            }
            arrayList.add("-i");
            arrayList.add(getFilePath(videoMakingActivity) + getString(R.string.watermark_image));
            if (!this.compareString.equals("")) {
                if (!this.compareStr.equals("")) {
                    arrayList.add("-ss");
                    arrayList.add(this.compareStr);
                }
                arrayList.add("-i");
                arrayList.add(this.compareString);
            }
            JSONArray jSONArray3 = this.pythonJsonObject.getJSONArray("m");
            if (jSONArray3.length() != 0) {
                for (int i9 = 0; i9 < jSONArray3.length(); i9++) {
                    arrayList.add(replaceString(jSONArray3.getString(i9)));
                }
            }
            if (this.compareString.equals("")) {
                JSONArray jSONArray4 = this.pythonJsonObject.getJSONArray("r");
                if (jSONArray4.length() != 0) {
                    for (int i10 = 0; i10 < jSONArray4.length(); i10++) {
                        arrayList.add(replaceString(jSONArray4.getString(i10)));
                    }
                }
            } else {
                JSONArray jSONArray5 = this.pythonJsonObject.getJSONArray("i");
                if (jSONArray5.length() != 0) {
                    for (int i11 = 0; i11 < jSONArray5.length(); i11++) {
                        arrayList.add(replaceString(jSONArray5.getString(i11)));
                    }
                }
            }
            JSONArray jSONArray6 = this.pythonJsonObject.getJSONArray("n");
            if (jSONArray6.length() != 0) {
                for (int i12 = 0; i12 < jSONArray6.length(); i12++) {
                    arrayList.add(replaceString(jSONArray6.getString(i12)));
                }
            }
            JSONArray jSONArray7 = this.pythonJsonObject.getJSONArray("g");
            if (jSONArray7.length() != 0) {
                for (int i13 = 0; i13 < jSONArray7.length(); i13++) {
                    arrayList.add(replaceString(jSONArray7.getString(i13)));
                }
            }
            JSONArray jSONArray8 = this.pythonJsonObject.getJSONArray("c");
            if (jSONArray8.length() != 0) {
                for (int i14 = 0; i14 < jSONArray8.length(); i14++) {
                    arrayList.add(replaceString(jSONArray8.getString(i14)));
                }
            }
            if (!this.compareString.equals("")) {
                JSONArray jSONArray9 = this.pythonJsonObject.getJSONArray("o");
                if (jSONArray9.length() != 0) {
                    for (int i15 = 0; i15 < jSONArray9.length(); i15++) {
                        arrayList.add(replaceString(jSONArray9.getString(i15)));
                    }
                }
            }
            JSONArray jSONArray10 = this.pythonJsonObject.getJSONArray("d");
            if (jSONArray10.length() != 0) {
                for (int i16 = 0; i16 < jSONArray10.length(); i16++) {
                    arrayList.add(replaceString(jSONArray10.getString(i16)));
                }
            }
            JSONArray jSONArray11 = this.pythonJsonObject.getJSONArray("s");
            if (jSONArray11.length() != 0) {
                for (int i17 = 0; i17 < jSONArray11.length(); i17++) {
                    arrayList.add(replaceString(jSONArray11.getString(i17)));
                }
            }
            if (this.compareString.equals("")) {
                arrayList.add("-c:a");
                arrayList.add("copy");
            }
            arrayList.add("-flags");
            arrayList.add("+global_header");
            arrayList.add(videoDestinationPath);
        } catch (Exception unused) {
            unused.printStackTrace();
        }
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        Log.i("JsonParser : command", Arrays.toString(strArr));
        try {
            if (pgsBar.getProgress() == 0) {
                pausePlayer();
            }
            fire_Command(strArr);
        } catch (Exception unused2) {
            unused2.printStackTrace();
        }
    }

    public final String replaceString(String str) {
        return str.replace("{pythoncomplex}", "filter_complex").replace("{pythonmerge}", "alphamerge").replace("{pythono}", "overlay").replace("{pythonz}", "zoom").replace("{pythonf}", "fade");
    }

    public static <T extends Throwable> T createThrowable(T t, String str) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        int length = stackTrace.length;
        int i2 = -1;
        for (int i3 = 0; i3 < length; i3++) {
            if (str.equals(stackTrace[i3].getClassName())) {
                i2 = i3;
            }
        }
        t.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(stackTrace, i2 + 1, length));
        return t;
    }

    public void fire_Command(String[] strArr) {
        try {
            FFmpeg.executeAsync(strArr, (ExecuteCallback) new ExecuteCallback() {
                public void apply(long j, int i) {
                    if (i == 0) {
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{new File(videoDestinationPath).getAbsolutePath()}, new String[]{"mp4"}, (MediaScannerConnection.OnScanCompletedListener) null);
                        myHandler.postDelayed(mRunnable, 500);
                        return;
                    }
                    Toast.makeText(VideoMakingActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure", "fail");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Config.enableStatisticsCallback(new StatisticsCallback() {
            final int videoLength = MediaPlayer.create(VideoMakingActivity.this, Uri.parse(videoObject.getVideoUrl())).getDuration();

            public void apply(Statistics newStatistics) {
                float progress = Float.parseFloat(String.valueOf(newStatistics.getTime())) / videoLength;
                float progressFinal = progress * 100;
                Log.d("Video Length", "Video Length: " + progressFinal);
                Log.d(Config.TAG, String.format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
                Log.d(Config.TAG, String.format("Quality: %f, time: %f", newStatistics.getVideoQuality(), newStatistics.getVideoFps()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pgsBar.setProgress((int) progressFinal, false);
                    }
                });
            }
        });
    }

    public void getNewImage(int i2) {
        if (this.rlExportVideo.getVisibility() != View.VISIBLE) {
            this.selectedImagePosition = i2;
            try {
                startActivityForResult(new Intent("android.intent.action.GET_CONTENT").setType("image/*"), REQUEST_PICK);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this, R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            this.rlExportVideo.startAnimation(this.mAnimation);
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_exit_screen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            ((TextView) dialog.findViewById(R.id.tv_title)).setText("Changing an image will stop current progress.");
            ((ImageView) dialog.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            ((CardView) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    isProgressMatch = 0;
                    pgsBar.setProgress(0, true);
                    rlExportVideo.setVisibility(View.GONE);
                    dialog.dismiss();
                    try {
                        startActivityForResult(new Intent("android.intent.action.GET_CONTENT").setType("image/*"), VideoMakingActivity.REQUEST_PICK);
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(VideoMakingActivity.this, R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((CardView) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void onActivityResult(int i2, int i3, Intent intent) {
        super.onActivityResult(i2, i3, intent);
        if (i2 == 9162 && i3 == -1) {
            beginCrop(intent.getData());
        } else if (i2 == 203 && i3 == -1) {
            this.imageList.get(this.selectedImagePosition).setSelectedPath(CropImage.getActivityResult(intent).getUri().toString());
            this.imageListAdapter.notifyDataSetChanged();
        } else if (i3 == 204) {
            CropImage.getActivityResult(intent).getError();
        }
    }

    private void beginCrop(Uri uri) {
        this.roundedImageWidth = Math.round((float) this.imageList.get(this.selectedImagePosition).getWidth());
        this.roundedImageHeight = Math.round((float) this.imageList.get(this.selectedImagePosition).getHeight());
        int gcdThing = gcdThing(this.roundedImageWidth, this.roundedImageHeight);
        Uri.fromFile(new File(getCacheDir(), "cropped"));
        CropImage.activity(uri).setAspectRatio(this.roundedImageWidth / gcdThing, this.roundedImageHeight / gcdThing).start(this);
    }

    private static int gcdThing(int i2, int i3) {
        return BigInteger.valueOf((long) i2).gcd(BigInteger.valueOf((long) i3)).intValue();
    }

    public void moveToNext() {
        create_progress_dialog();
        dismiss_dialog();
        next_activity();
    }

    public static void saveImageToGallery(File file, Bitmap bitmap) {
        if (bitmap != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("TAG", "saveImageToGallery: the path of bmp is " + file.getAbsolutePath());
            return;
        }
        throw new IllegalArgumentException("bmp should not be null");
    }

    public void onBackPressed() {
        if (this.rlExportVideo.getVisibility() == View.VISIBLE) {
            getSystemService(INPUT_METHOD_SERVICE);
        }
        showCancelDownloadDialog();
    }

    private void showCancelDownloadDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_screen);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((ImageView) dialog.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((CardView) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                FFmpeg.cancel();
                finish();
            }
        });
        ((CardView) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void next_activity() {
        dismiss_dialog();

        Intent intent = new Intent(VideoMakingActivity.this, VideoPreviewActivity.class);
        intent.putExtra("filePath", videoDestinationPath);
        intent.putExtra("fileName", destinationVideoFileName);
        intent.putExtra("video_object", new Gson().toJson((Object) this.videoObject));
        startActivity(intent);
    }

    private void create_progress_dialog() {
        dismiss_dialog();
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("Please wait...");
        this.progressDialog.setMessage("Ad is loading...");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void dismiss_dialog() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            this.progressDialog.dismiss();
        }
    }
}