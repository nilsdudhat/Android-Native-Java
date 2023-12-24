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

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.MonthAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentMonthsBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MonthClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.LibraryFragment;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MonthsFragment extends Fragment implements MonthClickListener {

    Activity activity;

    MonthsFragment fragment;

    FragmentMonthsBinding binding;

    MonthAdapter monthAdapter;

    HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap;

    String yearToMonthTransition = null;

    MutableLiveData<HashMap<String, HashMap<String, ArrayList<FileModel>>>> monthLiveData;

    public MonthsFragment getInstance() {
        if (fragment == null) {
            fragment = new MonthsFragment();
        }
        return fragment;
    }

    public MonthsFragment() {
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
        binding = FragmentMonthsBinding.inflate(inflater, container, false);

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();

        ((MainActivity) activity).showProgressDialog();

        monthHashMap = new HashMap<>();

        if (monthLiveData == null) {
            monthLiveData = new MutableLiveData<>();
        }

        monthLiveData.observe((MainActivity) activity, new Observer<HashMap<String, HashMap<String, ArrayList<FileModel>>>>() {
            @Override
            public void onChanged(HashMap<String, HashMap<String, ArrayList<FileModel>>> monthChangeHashMap) {
                monthHashMap = new HashMap<>(monthChangeHashMap);
                monthAdapter.swapMonthMap(monthHashMap);

                if (monthChangeHashMap.isEmpty()) {
                    binding.rvMonths.setVisibility(View.GONE);
                    binding.txtNoMedia.setVisibility(View.VISIBLE);
                } else {
                    if (yearToMonthTransition != null) {
                        if (monthHashMap.containsKey(yearToMonthTransition)) {
                            List<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>> monthList = new ArrayList<>(monthHashMap.entrySet());

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH); // your own date format

                            // Sort the list
                            Collections.sort(monthList, new Comparator<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>>() {
                                public int compare(Map.Entry<String, HashMap<String, ArrayList<FileModel>>> o1,
                                                   Map.Entry<String, HashMap<String, ArrayList<FileModel>>> o2) {
                                    try {
                                        return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        return 0;
                                    }
                                }
                            });

                            for (int i = 0; i < monthList.size(); i++) {
                                if (monthList.get(i).getKey().equals(yearToMonthTransition)) {
                                    Objects.requireNonNull(binding.rvMonths.getLayoutManager()).scrollToPosition(i);

                                    yearToMonthTransition = null;
                                    break;
                                }
                            }
                        }
                    }

                    setVisibility(monthHashMap);
                }
                ((MainActivity) activity).dismissProgressDialog();
            }
        });
    }

    private void setVisibility(HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap) {
        ((MainActivity) activity).dismissProgressDialog();

        Log.d("-set_visibility-", "MonthModel setVisibility: " + monthHashMap.size());
        if (!monthHashMap.isEmpty()) {
            binding.txtNoMedia.setVisibility(View.GONE);
            binding.rvMonths.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoMedia.setVisibility(View.VISIBLE);
            binding.rvMonths.setVisibility(View.GONE);
        }
    }

    public void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        binding.rvMonths.setLayoutManager(linearLayoutManager);

        monthAdapter = new MonthAdapter(activity, this);
        binding.rvMonths.setAdapter(null);
        binding.rvMonths.setAdapter(monthAdapter);
    }

    @Override
    public void onClick(String day) {
        ((LibraryFragment) requireParentFragment()).monthToDayTransition(day);
    }

    public void yearClicked(String month) {
        yearToMonthTransition = month;

        if (monthHashMap != null) {
            if (monthHashMap.containsKey(month)) {
                List<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>> monthList = new ArrayList<>(monthHashMap.entrySet());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH); // your own date format

                // Sort the list
                Collections.sort(monthList, new Comparator<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>>() {
                    public int compare(Map.Entry<String, HashMap<String, ArrayList<FileModel>>> o1,
                                       Map.Entry<String, HashMap<String, ArrayList<FileModel>>> o2) {
                        try {
                            return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                for (int i = 0; i < monthList.size(); i++) {
                    if (monthList.get(i).getKey().equals(month)) {
                        Objects.requireNonNull(binding.rvMonths.getLayoutManager()).scrollToPosition(i);

                        yearToMonthTransition = null;
                        break;
                    }
                }
            }
        }
    }

    public void refreshData(ArrayList<MediaModel> mediaModels) {

        if (fragment == null) {
            fragment = new MonthsFragment().getInstance();
        }

        Log.d("--months--", "onChanged: " + mediaModels.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

                monthHashMap = new HashMap<>(MediaLoader.getMonthHashMap(mediaModelArrayList));

                Log.d("--month_list--", "run: " + monthHashMap.size());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (monthLiveData == null) {
                            monthLiveData = new MutableLiveData<>();
                        }
                        monthLiveData.setValue(monthHashMap);
                    }
                });
            }
        }).start();
    }
}
