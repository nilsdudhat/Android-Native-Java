package com.xbeat.videostatus.statusmaker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.Adapters.MyVideoListAdapter;
import com.xbeat.videostatus.statusmaker.Customs.CompareVideo;
import com.xbeat.videostatus.statusmaker.Customs.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Models.MyCreationVideoData;
import com.xbeat.videostatus.statusmaker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AlbumActivity extends BaseActivity implements MyVideoListAdapter.VideoSelectListener {

    AlbumActivity mActivity;
    TextView btnStartCreating;
    ImageView ibBack;
    private RelativeLayout layoutStartCreating;
    MediaMetadataRetriever metadataRetriever;
    ArrayList<MyCreationVideoData> myVideoList = new ArrayList<>();
    private ProgressBar progressVideoList;
    private RecyclerView rvVideoList;
    public StaggeredGridLayoutManager staggeredGridLayoutManager;
    SwipeRefreshLayout swipeVideoList;
    public MyVideoListAdapter videoListAdapter;

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_videos);

        showInterstitial(AlbumActivity.this);
        showMiniNativeAd(AlbumActivity.this, findViewById(R.id.ad_mini_native));

        this.swipeVideoList = (SwipeRefreshLayout) findViewById(R.id.swipe_video_list);
        this.btnStartCreating = (TextView) findViewById(R.id.btn_start_creating);
        this.layoutStartCreating = (RelativeLayout) findViewById(R.id.layout_start_creating);
        this.progressVideoList = (ProgressBar) findViewById(R.id.progress_video_list);
        this.rvVideoList = (RecyclerView) findViewById(R.id.rv_video_list);
        this.ibBack = (ImageView) findViewById(R.id.ib_back);

        mActivity = this;

        ibBack.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });

        this.swipeVideoList.setEnabled(false);

        btnStartCreating.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(AlbumActivity.this, APIActivity.class));
                finish();
            }
        });
    }

    public ArrayList<MyCreationVideoData> getVideoList() {
        this.myVideoList.clear();
        File file = new File(getFilePath(mActivity));
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            Arrays.sort(listFiles, CompareVideo.compareVideo);
        }
        if (listFiles != null && listFiles.length > 0) {
            for (File file2 : listFiles) {
                if (!(file2 == null || file2.getAbsolutePath() == null || file2.getAbsolutePath().isEmpty())) {
                    try {
                        if (file2.getName().endsWith(".mp4") || file2.getName().endsWith(".3gp")) {
                            metadataRetriever = new MediaMetadataRetriever();
                            metadataRetriever.setDataSource(file2.getAbsolutePath());
                            myVideoList.add(new MyCreationVideoData(file2.getName(), file2.getAbsolutePath(), Integer.parseInt(this.metadataRetriever.extractMetadata(19)), Integer.parseInt(this.metadataRetriever.extractMetadata(18))));
                        }
                    } catch (Exception unused) {
                        unused.printStackTrace();
                    }
                }
            }
            if (metadataRetriever != null) {
                metadataRetriever.release();
            }
        }
        return this.myVideoList;
    }

    public void setData() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        rvVideoList.setLayoutManager(this.staggeredGridLayoutManager);
        videoListAdapter = new MyVideoListAdapter(this.myVideoList, mActivity, this);
        rvVideoList.setAdapter(this.videoListAdapter);
    }

    public void onResume() {
        super.onResume();
        this.myVideoList = getVideoList();
        if (this.myVideoList.size() > 0) {
            setData();
            rvVideoList.setVisibility(View.VISIBLE);
            layoutStartCreating.setVisibility(View.GONE);
        } else {
            rvVideoList.setVisibility(View.GONE);
            this.layoutStartCreating.setVisibility(View.VISIBLE);
        }
        this.progressVideoList.setVisibility(View.GONE);
    }

    public final String getFilePath(Activity activity) {
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

    public void onVideoSelectListener(int i, MyCreationVideoData myCreationVideoData) {
        Intent intentPlay = new Intent(AlbumActivity.this, PlayMyVideoActivity.class);
        intentPlay.putExtra("videoFilePath", myCreationVideoData.getFilePath());
        startActivity(intentPlay);
    }
}
