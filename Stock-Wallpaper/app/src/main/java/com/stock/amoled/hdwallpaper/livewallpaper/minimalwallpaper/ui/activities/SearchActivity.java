package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters.SearchAdapter;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.ActivitySearchBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.SearchClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.SearchListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.ListModel;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends BaseActivity implements SearchListener, SearchClickListener {

    ActivitySearchBinding binding;

    ArrayList<Wallpaper.Detail> wallpaperModelArrayList = new ArrayList<>();

    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.backCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.rvSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("--touch--", "onTouch: " + view.toString());
                Utils.hideSoftKeyboard(SearchActivity.this);
                return false;
            }
        });

        if (Constant.listModel != null) {
            wallpaperModelArrayList = new ArrayList<>(Constant.listModel.getWallpaperList());
        }

        searchAdapter = new SearchAdapter(SearchActivity.this, wallpaperModelArrayList, this, this);
        binding.rvSearch.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        binding.rvSearch.setAdapter(searchAdapter);

        if (binding.edtSearch.getText().toString().length() != 0) {
            if (searchAdapter != null) {
                searchAdapter.getFilter().filter(binding.edtSearch.getText().toString());
            }
        }

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (binding.edtSearch.getText().toString().length() == 0) {
                    if (searchAdapter != null) {
                        searchAdapter.getFilter().filter(binding.edtSearch.getText().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchAdapter != null) {
                    searchAdapter.getFilter().filter(binding.edtSearch.getText().toString());
                }
            }
        });
        binding.edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Utils.hideSoftKeyboard(SearchActivity.this);
                }
            }
        });
    }

    @Override
    public void searchedItem(boolean isResultFound) {
        if (isResultFound) {
            binding.txtSearchWallpaper.setVisibility(View.GONE);
            binding.rvSearch.setVisibility(View.VISIBLE);
        } else {
            binding.txtSearchWallpaper.setVisibility(View.VISIBLE);
            binding.rvSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSearchClickListener(int position, ArrayList<Wallpaper.Detail> wallpaperModelArrayList) {
        Intent intent = new Intent(SearchActivity.this, WallpaperActivity.class);
        Constant.listModel = new ListModel(position, wallpaperModelArrayList);
        intent.putExtra("image", "wallpaper");
        startActivity(intent);
    }
}
