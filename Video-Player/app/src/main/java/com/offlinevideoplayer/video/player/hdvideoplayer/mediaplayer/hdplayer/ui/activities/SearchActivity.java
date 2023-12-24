package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AdsBaseActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters.SearchAdapter;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchActivity extends AdsBaseActivity implements SearchAdapter.SearchedItemClicked, SearchAdapter.SearchListener {

    EditText edt_search;
    ImageView img_back;

    TextView txt_search_video;
    RecyclerView rv_search;

    SearchAdapter searchAdapter;
    ArrayList<VideoModel> videoModelList = new ArrayList<>();

    SharedPreferences mPrefs;
    boolean isDark = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        isDark = mPrefs.getBoolean("theme", true);
        Log.d("--theme--", "isDark: " + isDark);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Utils.bottomNavigationBlackColor(SearchActivity.this);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Utils.bottomNavigationWhiteColor(SearchActivity.this);
        }

        setContentView(R.layout.activity_search);

        showBannerAd(SearchActivity.this, findViewById(R.id.ad_banner));

        img_back = findViewById(R.id.img_back);
        rv_search = findViewById(R.id.rv_search);
        txt_search_video = findViewById(R.id.txt_search_video);
        edt_search = findViewById(R.id.edt_search);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        videoModelList = new ArrayList<>(Utils.videoModelArrayList);

        searchAdapter = new SearchAdapter(SearchActivity.this, videoModelList, this, this);
        rv_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false));
        rv_search.setAdapter(searchAdapter);

        if (edt_search.getText().toString().length() != 0) {
            if (searchAdapter != null) {
                searchAdapter.getFilter().filter(edt_search.getText().toString());
            }
        }

        edt_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Utils.hideSoftKeyboard(SearchActivity.this);
                }
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (edt_search.getText().toString().length() == 0) {
                    if (searchAdapter != null) {
                        searchAdapter.getFilter().filter(edt_search.getText().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchAdapter != null) {
                    searchAdapter.getFilter().filter(edt_search.getText().toString());
                }
            }
        });
    }

    @Override
    public void onSearchedItemClick(VideoModel videoModel) {

        Intent intent = new Intent(SearchActivity.this, VideoPlayerActivity.class);
//        Intent intent = new Intent(SearchActivity.this, CustomPlayerActivity.class);

        int selected_position = 0;

        for (int i = 0; i < videoModelList.size(); i++) {
            if (videoModelList.get(i).getPath().equals(videoModel.getPath())) {
                selected_position = i;
                break;
            }
        }

        mPrefs.edit().putInt("position", selected_position).apply();
        intent.putExtra("list", (Serializable) videoModelList);
        showInterstitialAd(SearchActivity.this, intent, null);
    }

    @Override
    public void searchedItem(boolean isResultFound) {
        if (isResultFound) {
            txt_search_video.setVisibility(View.GONE);
            rv_search.setVisibility(View.VISIBLE);
        } else {
            txt_search_video.setVisibility(View.VISIBLE);
            rv_search.setVisibility(View.GONE);
        }
    }
}
