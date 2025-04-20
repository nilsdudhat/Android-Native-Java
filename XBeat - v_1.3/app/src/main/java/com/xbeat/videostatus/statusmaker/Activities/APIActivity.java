package com.xbeat.videostatus.statusmaker.Activities;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.xbeat.videostatus.statusmaker.Customs.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Fragments.APIFragment;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;

public class APIActivity extends AppCompatActivity {

    FrameLayout frame_layout;
    APIFragment apiFragment;

    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        img_back = findViewById(R.id.img_back);
        frame_layout = findViewById(R.id.frame_layout);

        img_back.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });

        apiFragment = new APIFragment(APIActivity.this);

        loadFragment(apiFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void handleClick(View view) {
        ArrayList<ModelVideoList> arrayList;
        int id = view.getId();
        if (id == R.id.img_search && (arrayList = apiFragment.videoLists) != null && arrayList.size() > 0) {
            Intent intentSearch = new Intent(APIActivity.this, SearchActivity.class);
            intentSearch.putExtra("videoList", apiFragment.videoLists);
            startActivity(intentSearch);
        }
    }
}
