package com.xbeat.videostatus.statusmaker.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.APICall.APIClient;
import com.xbeat.videostatus.statusmaker.APICall.APIInterface;
import com.xbeat.videostatus.statusmaker.Adapters.VideoListAdapter;
import com.xbeat.videostatus.statusmaker.Customs.Globals;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoListDataByCategory;
import com.xbeat.videostatus.statusmaker.Models.VideoCategoryData;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDataActivity extends AppCompatActivity implements VideoListAdapter.VideoSelectListener {

    public static CategoryDataActivity mActivity;
    APIInterface apiInterface;
    String category = "random";
    public ImageView ibFilter;
    public LinearLayout layoutTryAgain;
    public int mPosition = 0;
    public ModelVideoListDataByCategory modelVideoList;
    PopupWindow popupWindow;
    public ProgressBar progressVideoList;
    private RecyclerView rvVideoList;
    public StaggeredGridLayoutManager staggeredGridLayoutManager;
    public SwipeRefreshLayout swipeVideoList;
    public TextView textNoData;
    TextView textTitle;
    private Toolbar toolbar;
    VideoCategoryData videoCategoryData;
    public VideoListAdapter videoListAdapter;
    public ArrayList<ModelVideoList> videoLists = new ArrayList<>();

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_category_data);

        textTitle = (TextView) findViewById(R.id.text_title);
        ibFilter = (ImageView) findViewById(R.id.img_filter_menu);
        textNoData = (TextView) findViewById(R.id.text_no_data);
        textNoData = (TextView) findViewById(R.id.text_no_data);
        rvVideoList = (RecyclerView) findViewById(R.id.rv_video_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView ibBack = (ImageView) findViewById(R.id.ib_back);
        TextView btnTryAgain = (TextView) findViewById(R.id.btn_try_again);
        layoutTryAgain = (LinearLayout) findViewById(R.id.layout_try_again);
        swipeVideoList = (SwipeRefreshLayout) findViewById(R.id.swipe_video_list);
        progressVideoList = (ProgressBar) findViewById(R.id.progress_video_list);

        mActivity = this;
        if (getIntent() != null) {
            this.videoCategoryData = (VideoCategoryData) getIntent().getSerializableExtra("categoryObject");
        }
        this.apiInterface = (APIInterface) APIClient.getClient().create(APIInterface.class);
        this.textTitle.setText(this.videoCategoryData.getName());
        if (Globals.isNetworkAvailable(mActivity)) {
            callApi(false, this.category);
        } else {
            ArrayList<ModelVideoList> arrayList = this.videoLists;
            if (arrayList != null && arrayList.size() > 0) {
                this.layoutTryAgain.setVisibility(View.VISIBLE);
            }
        }
        this.swipeVideoList.setColorSchemeResources(R.color.colorPrimaryLite);
        this.swipeVideoList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                callApi(true, category);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CategoryDataActivity.this.onBackPressed();
            }
        });
        this.ibFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                initiatePopupWindow(ibFilter);
            }
        });
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Globals.isNetworkAvailable(CategoryDataActivity.mActivity)) {
                    layoutTryAgain.setVisibility(View.GONE);
                    callApi(false, category);
                }
            }
        });
    }

    public void callApi(final boolean z, String str) {
        if (z) {
            swipeVideoList.setRefreshing(true);
        } else {
            progressVideoList.setVisibility(View.VISIBLE);
        }
        if (videoLists == null || videoLists.size() <= 0) {
            disableScreen(true);
        } else {
            disableScreen(false);
        }
        this.textNoData.setVisibility(View.GONE);
        this.apiInterface.getVideoListByCategory(str, String.valueOf(this.videoCategoryData.getId())).enqueue(new Callback<ModelVideoListDataByCategory>() {
            public void onResponse(Call<ModelVideoListDataByCategory> call, Response<ModelVideoListDataByCategory> response) {
                layoutTryAgain.setVisibility(View.GONE);
                Log.e("TAG", response.code() + "");
                modelVideoList = response.body();
                if (modelVideoList.getData() != null) {
                    videoLists = modelVideoList.getData().getTemplatesList();
                    if (videoLists != null) {
                        setData();
                    } else {
                        textNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    textNoData.setVisibility(View.VISIBLE);
                }
                if (z) {
                    swipeVideoList.setRefreshing(false);
                } else {
                    progressVideoList.setVisibility(View.GONE);
                }
                disableScreen(false);
            }

            public void onFailure(Call<ModelVideoListDataByCategory> call, Throwable th) {
                call.cancel();
                if (z) {
                    swipeVideoList.setRefreshing(false);
                } else {
                    progressVideoList.setVisibility(View.GONE);
                }
                Log.e("MainActivity", "onFailure Message : " + th.getMessage());
                if (!z) {
                    layoutTryAgain.setVisibility(View.VISIBLE);
                }
                disableScreen(false);
            }
        });
    }

    public void setData() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        rvVideoList.setLayoutManager(staggeredGridLayoutManager);
        videoListAdapter = new VideoListAdapter(videoLists, mActivity, this);
        rvVideoList.setAdapter(videoListAdapter);
    }

    public void initiatePopupWindow(View view) {
        try {
            View inflate = ((LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_filter_popup, (ViewGroup) null);
            popupWindow = new PopupWindow(inflate, -2, -2);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(false);
            popupWindow.showAtLocation(ibFilter, 53, Globals.getDensity(1d), toolbar.getHeight());
            ((LinearLayout) inflate.findViewById(R.id.layout_random)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    category = "random";
                    callApi(false, category);
                    popupWindow.dismiss();
                }
            });
            ((LinearLayout) inflate.findViewById(R.id.layout_popular)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    category = "popular";
                    callApi(false, category);
                    popupWindow.dismiss();
                }
            });
            ((LinearLayout) inflate.findViewById(R.id.layout_latest)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    category = "newest";
                    callApi(false, category);
                    popupWindow.dismiss();
                }
            });
            ((LinearLayout) inflate.findViewById(R.id.layout_oldest)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    category = "oldest";
                    callApi(false, category);
                    popupWindow.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableScreen(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
        } else {
            getWindow().clearFlags(16);
        }
    }

    public void onVideoSelectListener(int i, ModelVideoList modelVideoList2) {
        this.mPosition = i;
        Intent intent = new Intent(CategoryDataActivity.this, PlayVideoActivity.class);
        intent.putExtra("video_object", new Gson().toJson((Object) modelVideoList2));
        startActivity(intent);
    }
}
