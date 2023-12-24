package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.Constant;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters.WallpaperAdapter;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.api.APIClient;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.api.APIRest;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.databinding.FragmentHomeBinding;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.WallpaperClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.ListModel;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities.MainActivity;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities.WallpaperActivity;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements WallpaperClickListener {

    Activity activity;

    WallpaperAdapter wallpaperAdapter;

    ArrayList<Wallpaper.Detail> wallpaperList = new ArrayList<>();

    Dialog progressDialog;

    boolean isLoading = false;

    HomeFragment fragment;
    FragmentHomeBinding binding;

    public HomeFragment getInstance() {
        if (fragment == null) {
            fragment = new HomeFragment();
        }
        return fragment;
    }

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        activity = requireActivity();

        progressDialog = new Dialog(activity, R.style.CustomDialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // initialising wallpaper adapter
        wallpaperAdapter = new WallpaperAdapter(activity, this);

        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        binding.rvWallpapers.setLayoutManager(layoutManager);

        binding.rvWallpapers.setAdapter(wallpaperAdapter);
        wallpaperAdapter.swapWallpaperList(wallpaperList);
        wallpaperAdapter.notifyItemRangeChanged(0, wallpaperList.size());

        loadAllWallpapers();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("--check--", "onViewCreated: ");

        if (activity != null) {
            if (!isLoading) {
                if (NetworkUtils.getConnectivityStatus(activity)) {
                    binding.dataContainer.setVisibility(View.VISIBLE);
                    binding.noInternetLayout.getRoot().setVisibility(View.GONE);
                } else {
                    binding.dataContainer.setVisibility(View.GONE);
                    binding.noInternetLayout.getRoot().setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public ArrayList<Wallpaper.Detail> getWallpaperList() {
        return wallpaperList;
    }

    int PAGE_SIZE = 10;
    int PAGE_NUMBER = 1;

    boolean isFirstTime = true;
    boolean isNextPage = false;
    boolean isLoadingWallpapers = false;

    private void loadAllWallpapers() {
        isLoadingWallpapers = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFirstTime) {
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.show();
                            }
                        });
                    }
                    getPageFromAPI();
                } else {
                    if (isNextPage) {
                        getPageFromAPI();
                    } else {
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    isLoadingWallpapers = false;
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    private void getPageFromAPI() {
        Call<Wallpaper> call = APIClient.getClient().create(APIRest.class).getAllWallpapers(PAGE_NUMBER, PAGE_SIZE);
        call.enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(@NonNull Call<Wallpaper> call, @NonNull Response<Wallpaper> response) {
                Log.d("--response--", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    Log.d("--response--", "onResponse: isSuccessful");

                    Wallpaper.Meta meta = Objects.requireNonNull(response.body()).getMeta();
                    isNextPage = meta.getNextPage();

                    if (isFirstTime) {
                        isFirstTime = false;
                    }

                    getWallpapersFromResponse(response);
                } else {
                    getPageFromAPI();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Wallpaper> call, @NonNull Throwable t) {
                Log.d("--response--", "onFailure: " + t.getMessage());

                getPageFromAPI();
            }
        });
    }

    private void getWallpapersFromResponse(Response<Wallpaper> response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // page from api
                ArrayList<Wallpaper.Detail> wallpaperArrayList = (ArrayList<Wallpaper.Detail>) Objects.requireNonNull(response.body()).getDetail();
                Log.d("--pages--", "getWallpapersFromResponse: " + PAGE_NUMBER + " size:" + wallpaperArrayList.size());

                ArrayList<Wallpaper.Detail> tempWallpaperList = new ArrayList<>(wallpaperList);

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!wallpaperArrayList.isEmpty()) {
                                wallpaperList.addAll(wallpaperArrayList);

                                wallpaperAdapter.swapWallpaperList(wallpaperList);
                                if (isNextPage) {
                                    wallpaperAdapter.notifyItemRangeChanged(tempWallpaperList.size(), tempWallpaperList.size() + PAGE_SIZE);
                                } else {
                                    wallpaperAdapter.notifyItemRangeChanged(tempWallpaperList.size(), tempWallpaperList.size() + wallpaperArrayList.size());
                                }
                            }
                            progressDialog.dismiss();
                            isLoadingWallpapers = false;

                            if (isNextPage) {
                                PAGE_NUMBER++;
                                loadAllWallpapers();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void isConnected(boolean isConnected) {
        Log.d("--check--", "isConnected: ");
        if (!isLoading) {
            if (isConnected) {
                binding.dataContainer.setVisibility(View.VISIBLE);
                binding.noInternetLayout.getRoot().setVisibility(View.GONE);
            } else {
                binding.dataContainer.setVisibility(View.GONE);
                binding.noInternetLayout.getRoot().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onWallpaperClick(int position) {
        if (activity != null) {
            Intent intent = new Intent(activity, WallpaperActivity.class);
            Constant.listModel = new ListModel(position, wallpaperList); // signing values to static model to use in WallpaperActivity
            intent.putExtra("image", "wallpaper");
            ((MainActivity) activity).showInterstitialAd(activity, intent, null);
        }
    }
}
