package com.xbeat.videostatus.statusmaker.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.APICall.APIClient;
import com.xbeat.videostatus.statusmaker.APICall.APIInterface;
import com.xbeat.videostatus.statusmaker.Activities.APIActivity;
import com.xbeat.videostatus.statusmaker.Activities.CategoryDataActivity;
import com.xbeat.videostatus.statusmaker.Activities.PlayVideoActivity;
import com.xbeat.videostatus.statusmaker.Adapters.CategoryListAdapter;
import com.xbeat.videostatus.statusmaker.Adapters.VideoListAdapter;
import com.xbeat.videostatus.statusmaker.Customs.Globals;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoListData;
import com.xbeat.videostatus.statusmaker.Models.VideoCategoryData;
import com.xbeat.videostatus.statusmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIFragment extends Fragment
        implements VideoListAdapter.VideoSelectListener, CategoryListAdapter.CategorySelectListener {

    APIActivity activity;
    APIInterface apiInterface;
    String category = "newest";
    public ArrayList<VideoCategoryData> categoryList = new ArrayList<>();
    public int mPosition = 0;
    public ModelVideoListData modelVideoList;
    public StaggeredGridLayoutManager staggeredGridLayoutManager;
    public VideoListAdapter videoListAdapter;
    public ArrayList<ModelVideoList> videoLists = new ArrayList<>();

    TextView btnTryAgain;
    ProgressBar progressVideoList;
    RecyclerView rvVideoList;
    SwipeRefreshLayout swipeVideoList;
    RecyclerView rv_category_list;

    public void callViewVideoApi(String str) {
    }

    public APIFragment() {
    }

    public APIFragment(APIActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_api, container, false);

        rvVideoList = (RecyclerView) view.findViewById(R.id.rv_video_list);
        swipeVideoList = (SwipeRefreshLayout) view.findViewById(R.id.swipe_video_list);
        progressVideoList = (ProgressBar) view.findViewById(R.id.progress_video_list);
        rv_category_list = view.findViewById(R.id.rv_category_list);

        apiInterface = (APIInterface) APIClient.getClient().create(APIInterface.class);
        if (Globals.isNetworkAvailable(activity)) {
            callApi(false, category);
        } else {
            if (videoLists != null) {
                videoLists.size();
            }
        }

        swipeVideoList.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeVideoList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                callApi(true, category);
            }
        });
        btnTryAgain = (TextView) view.findViewById(R.id.btn_try_again);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Globals.isNetworkAvailable(activity)) {
                    callApi(false, category);
                }
            }
        });

        return view;
    }

    public void callApi(final boolean z, String str) {
        if (z) {
            swipeVideoList.setRefreshing(true);
        } else {
            progressVideoList.setVisibility(View.VISIBLE);
        }
        disableScreen(videoLists == null || videoLists.size() <= 0);
        apiInterface.getVideoList(str).enqueue(new Callback<ModelVideoListData>() {
            public void onResponse(@NonNull Call<ModelVideoListData> call, @NonNull Response<ModelVideoListData> response) {
                Log.e("test", response.code() + "");
                modelVideoList = response.body();
                videoLists = modelVideoList.getData().getTemplatesList();
                categoryList = modelVideoList.getData().getCategoriesList();
                setData();
                if (z) {
                    swipeVideoList.setRefreshing(false);
                } else {
                    progressVideoList.setVisibility(View.GONE);
                }
                disableScreen(false);
            }

            public void onFailure(@NonNull Call<ModelVideoListData> call, @NonNull Throwable th) {
                call.cancel();
                if (z) {
                    swipeVideoList.setRefreshing(false);
                } else {
                    progressVideoList.setVisibility(View.GONE);
                }
                Log.e("MainActivity", "onFailure Message : " + th.getMessage());
                disableScreen(false);
            }
        });
    }

    public void setData() {
        if (videoLists != null) {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            rvVideoList.setLayoutManager(staggeredGridLayoutManager);
            videoListAdapter = new VideoListAdapter(videoLists, activity, this);
            rvVideoList.setAdapter(videoListAdapter);
        }

        if (categoryList != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            rv_category_list.setLayoutManager(linearLayoutManager);
            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(activity, categoryList, this);
            rv_category_list.setAdapter(categoryListAdapter);
        }
    }

    public void disableScreen(boolean z) {
        if (z) {
            activity.getWindow().setFlags(16, 16);
        } else {
            activity.getWindow().clearFlags(16);
        }
    }

    public void onVideoSelectListener(int i, ModelVideoList modelVideoList2) {
        mPosition = i;
        callViewVideoApi(String.valueOf(modelVideoList2.getId()));

        Intent intentVideoObject = new Intent(activity, PlayVideoActivity.class);
        intentVideoObject.putExtra("video_object", new Gson().toJson((Object) modelVideoList2));
        activity.startActivity(intentVideoObject);
    }

    public void onCategorySelectListener(VideoCategoryData videoCategoryData) {
        Intent intentCategoryObject = new Intent(activity, CategoryDataActivity.class);
        intentCategoryObject.putExtra("categoryObject", videoCategoryData);
        activity.startActivity(intentCategoryObject);
    }
}
