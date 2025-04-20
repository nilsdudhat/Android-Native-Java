package com.xbeat.videostatus.statusmaker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.xbeat.videostatus.statusmaker.APICall.APIClient;
import com.xbeat.videostatus.statusmaker.APICall.APIInterface;
import com.xbeat.videostatus.statusmaker.Adapters.SearchVideoListAdapter;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements SearchVideoListAdapter.VideoSelectListener {

    APIInterface apiInterface;
    public EditText etSearch;
    private RecyclerView recyclerTrending;
    ArrayList<ModelVideoList> searchVideoList = new ArrayList<>();
    SearchVideoListAdapter searchVideoListAdapter;
    public TextView textTrending;
    public ArrayList<ModelVideoList> videosList = new ArrayList<>();

    public void callViewVideoApi(String str) {
    }

    public void getSearchData(String str) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_search);

        recyclerTrending = (RecyclerView) findViewById(R.id.recycler_trending);
        etSearch = (EditText) findViewById(R.id.et_search);
        ImageView ibBack = (ImageView) findViewById(R.id.ib_back);
        textTrending = (TextView) findViewById(R.id.text_trending);

        apiInterface = (APIInterface) APIClient.getClient().create(APIInterface.class);
        if (getIntent().getExtras() != null) {
            videosList = (ArrayList) getIntent().getSerializableExtra("videoList");

            for (int i = 0; i < videosList.size(); i++) {
                if (videosList.get(i) == null) {
                    videosList.remove(i);
                }
            }
        }
        this.recyclerTrending.setLayoutManager(new LinearLayoutManager(this));
        this.searchVideoListAdapter = new SearchVideoListAdapter(this.videosList, this, this);
        this.recyclerTrending.setAdapter(searchVideoListAdapter);
        setSearchView();
        ibBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setSearchView() {
        this.recyclerTrending.setLayoutManager(new LinearLayoutManager(this));
        this.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (!z) {
                    hideSoftKeyboard();
                }
            }
        });
        this.etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (etSearch.getText().toString().length() == 0) {
                    textTrending.setVisibility(View.VISIBLE);
                } else {
                    textTrending.setVisibility(View.GONE);
                }
                if (searchVideoListAdapter != null) {
                    searchVideoListAdapter.getFilter().filter(etSearch.getText().toString());
                }
            }

            public void afterTextChanged(Editable editable) {
                if (etSearch.getText().toString().length() == 0) {
                    textTrending.setVisibility(View.VISIBLE);
                } else {
                    textTrending.setVisibility(View.GONE);
                }
                if (searchVideoListAdapter != null) {
                    searchVideoListAdapter.getFilter().filter(etSearch.getText().toString());
                }
            }
        });
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (getWindow().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList() {
        this.searchVideoListAdapter = new SearchVideoListAdapter(this.searchVideoList, this, this);
        this.recyclerTrending.setAdapter(this.searchVideoListAdapter);
    }

    public void onVideoSelectListener(int i, ModelVideoList modelVideoList) {
        callViewVideoApi(String.valueOf(modelVideoList.getId()));
        Intent intent = new Intent(SearchActivity.this, PlayVideoActivity.class);
        intent.putExtra("video_object", new Gson().toJson((Object) modelVideoList));
        startActivity(intent);
    }
}
