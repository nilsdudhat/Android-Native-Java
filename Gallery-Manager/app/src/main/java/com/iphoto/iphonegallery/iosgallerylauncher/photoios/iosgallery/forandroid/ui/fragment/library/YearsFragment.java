package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.library;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.YearsAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentYearsBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.YearClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.LibraryFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class YearsFragment extends Fragment implements YearClickListener {

    YearsFragment fragment;
    Activity activity;

    FragmentYearsBinding  binding;

    YearsAdapter yearsAdapter;

    MutableLiveData<HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>>> yearLiveData;

    public YearsFragment getInstance() {
        if (fragment == null) {
            fragment = new YearsFragment();
        }
        return fragment;
    }

    public YearsFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    CacheUtils.deleteCache(activity);
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentYearsBinding.inflate(inflater, container, false);

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();

        ((MainActivity) activity).showProgressDialog();

        if (yearLiveData == null) {
            yearLiveData = new MutableLiveData<>();
        }

        yearLiveData.observe((MainActivity) activity, new Observer<HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>>>() {
            @Override
            public void onChanged(HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> yearHashMap) {
                yearsAdapter.swapYearMap(yearHashMap);

                setVisibility(yearHashMap);

                ((MainActivity) activity).dismissProgressDialog();
            }
        });
    }

    private void setVisibility(HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> yearHashMap) {
        ((MainActivity) activity).dismissProgressDialog();

        Log.d("-set_visibility-", "YearModel setVisibility: " + yearHashMap.size());
        if (!yearHashMap.isEmpty()) {
            binding.rvYears.setVisibility(View.VISIBLE);
            binding.txtNoMedia.setVisibility(View.GONE);
        } else {
            binding.rvYears.setVisibility(View.GONE);
            binding.txtNoMedia.setVisibility(View.VISIBLE);
        }
    }

    public void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        binding.rvYears.setLayoutManager(linearLayoutManager);

        yearsAdapter = new YearsAdapter(activity, this);

        binding.rvYears.setAdapter(null);
        binding.rvYears.setAdapter(yearsAdapter);
    }

    @Override
    public void onClick(String month) {
        ((LibraryFragment) requireParentFragment()).yearToMonthTransition(month);
    }

    public void refreshData(ArrayList<MediaModel> mediaModels) {

        if (fragment == null) {
            fragment = new YearsFragment().getInstance();
        }

        Log.d("--months--", "onChanged: " + mediaModels.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

                HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> yearHashMap = new HashMap<>(MediaLoader.getYearHashMap(mediaModelArrayList));

                Log.d("--year_list--", "run: " + yearHashMap.size());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (yearLiveData == null) {
                            yearLiveData = new MutableLiveData<>();
                        }
                        yearLiveData.setValue(yearHashMap);
                    }
                });
            }
        }).start();
    }
}
