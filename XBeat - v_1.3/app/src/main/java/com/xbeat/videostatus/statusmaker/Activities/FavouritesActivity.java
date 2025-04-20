package com.xbeat.videostatus.statusmaker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.AdUtils.BaseActivity;
import com.xbeat.videostatus.statusmaker.Adapters.VideoListAdapterNew;
import com.xbeat.videostatus.statusmaker.Customs.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Database.DatabaseHandler;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouritesActivity extends BaseActivity implements VideoListAdapterNew.VideoSelectListenerNew {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv_video_list;
    ProgressBar progress_video_list;
    List<ModelVideoList> lst = new ArrayList<>();
    RelativeLayout rl_no_data;
    TextView btn_start_saving;

    DatabaseHandler databaseHandler;

    ImageView ib_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        showInterstitial(FavouritesActivity.this);
        showMiniNativeAd(FavouritesActivity.this, findViewById(R.id.ad_mini_native));

        ib_back = findViewById(R.id.ib_back);
        swipeRefreshLayout = findViewById(R.id.swipe_video_list);
        rv_video_list = findViewById(R.id.rv_video_list);
        progress_video_list = findViewById(R.id.progress_video_list);
        rl_no_data = findViewById(R.id.rl_no_data);
        btn_start_saving = findViewById(R.id.btn_start_saving);

        databaseHandler = new DatabaseHandler(FavouritesActivity.this);

        ib_back.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpRecyclerView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        progress_video_list.setVisibility(View.VISIBLE);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        lst = databaseHandler.getModelArrayList(FavouritesActivity.this);

        if (lst.size() == 0) {
            rl_no_data.setVisibility(View.VISIBLE);

            btn_start_saving.setOnClickListener(v -> {
                startActivity(new Intent(FavouritesActivity.this, APIActivity.class));
            });
        } else {
            rl_no_data.setVisibility(View.GONE);
        }

        Gson gson = new Gson();
        String jsonInString = gson.toJson(lst);
        Log.d("--Gson--", "PurchaseDetails: " + jsonInString);

        if (lst != null) {
            swipeRefreshLayout.setRefreshing(false);
            progress_video_list.setVisibility(View.GONE);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            rv_video_list.setLayoutManager(staggeredGridLayoutManager);
            Collections.reverse(lst);
            VideoListAdapterNew videoListAdapter = new VideoListAdapterNew(lst, FavouritesActivity.this, this);
            rv_video_list.setAdapter(videoListAdapter);
        }
    }

    public void onVideoSelectListenerNew(int i, ModelVideoList videoListAdapter) {
        Intent intentVideoObject = new Intent(FavouritesActivity.this, PlayVideoActivity.class);
        intentVideoObject.putExtra("video_object", new Gson().toJson((Object) videoListAdapter));
        startActivity(intentVideoObject);
    }
}
