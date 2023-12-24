package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment.library;

import android.app.Activity;
import android.content.Intent;
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

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.DaysAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentDaysBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MultiSelectListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.FullScreenMediaActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.SelectionMediaActivity;
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

public class DaysFragment extends Fragment implements MultiSelectListener, MediaAdapterClickListener {

    FragmentDaysBinding binding;

    Activity activity;

    DaysFragment fragment;

    DaysAdapter daysAdapter;

    HashMap<String, ArrayList<FileModel>> dayHashMap;

    String monthToDayTransition = null;

    MutableLiveData<HashMap<String, ArrayList<FileModel>>> dayLiveData;

    public DaysFragment getInstance() {
        if (fragment == null) {
            fragment = new DaysFragment();
        }
        return fragment;
    }

    public DaysFragment() {
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
        binding = FragmentDaysBinding.inflate(inflater, container, false);

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) activity).showProgressDialog();

        setUpRecyclerView();

        if (dayLiveData == null) {
            dayLiveData = new MutableLiveData<>();
        }

        dayLiveData.observe((MainActivity) activity, new Observer<HashMap<String, ArrayList<FileModel>>>() {
            @Override
            public void onChanged(HashMap<String, ArrayList<FileModel>> dayChangeHashMap) {
                dayHashMap = new HashMap<>(dayChangeHashMap);
                daysAdapter.swapDayHashMap(dayHashMap);

                if (dayHashMap.isEmpty()) {
                    binding.rvDays.setVisibility(View.GONE);
                    binding.txtNoMedia.setVisibility(View.VISIBLE);
                } else {
                    if (monthToDayTransition != null) {
                        if (dayHashMap.containsKey(monthToDayTransition)) {
                            List<Map.Entry<String, ArrayList<FileModel>>> dayList = new ArrayList<>(dayHashMap.entrySet());

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH); // your own date format

                            // Sort the list
                            Collections.sort(dayList, new Comparator<Map.Entry<String, ArrayList<FileModel>>>() {
                                public int compare(Map.Entry<String, ArrayList<FileModel>> o1,
                                                   Map.Entry<String, ArrayList<FileModel>> o2) {
                                    try {
                                        return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        return 0;
                                    }
                                }
                            });

                            for (int i = 0; i < dayList.size(); i++) {
                                if (dayList.get(i).getKey().equals(monthToDayTransition)) {
                                    Objects.requireNonNull(binding.rvDays.getLayoutManager()).scrollToPosition(i);

                                    monthToDayTransition = null;
                                    break;
                                }
                            }
                        }
                    }

                    setVisibility(dayHashMap);

                    ((MainActivity) activity).dismissProgressDialog();
                }
            }
        });
    }

    public void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        binding.rvDays.setLayoutManager(linearLayoutManager);

        daysAdapter = new DaysAdapter(activity, this, this);

        binding.rvDays.setAdapter(null);
        binding.rvDays.setAdapter(daysAdapter);
    }

    private void setVisibility(HashMap<String, ArrayList<FileModel>> dayMapList) {
        ((MainActivity) activity).dismissProgressDialog();

        Log.d("-set_visibility-", "DayModel setVisibility: " + dayMapList.size());
        if (!dayMapList.isEmpty()) {
            binding.rvDays.setVisibility(View.VISIBLE);
            binding.txtNoMedia.setVisibility(View.GONE);
        } else {
            binding.rvDays.setVisibility(View.GONE);
            binding.txtNoMedia.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMultiSelect(ArrayList<FileModel> fileModelArrayList) {
        Intent intent = new Intent(requireContext(), SelectionMediaActivity.class);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
//        intent.putExtra("fileModelArrayList", fileModelArrayList);;
        ((MainActivity) activity).showInterstitialAd(activity, intent, null);
    }

    @Override
    public void onClick(View view, ArrayList<FileModel> fileModelArrayList, int position, String mediaType, String subMediaType) {
        Intent intent = new Intent(activity, FullScreenMediaActivity.class);

//        intent.putParcelableArrayListExtra("fileModelArrayList", fileModelArrayList);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
        intent.putExtra("position", position);
        intent.putExtra("mediaType", mediaType);
        intent.putExtra("subMediaType", subMediaType);

        ((MainActivity) activity).showInterstitialAd(activity, intent, null);
    }

    public void monthClicked(String day) {
        Log.d("--month_to_day--", "day: " + day);

        monthToDayTransition = day;

        if (dayHashMap != null) {
            if (dayHashMap.containsKey(monthToDayTransition)) {
                List<Map.Entry<String, ArrayList<FileModel>>> dayList = new ArrayList<>(dayHashMap.entrySet());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH); // your own date format

                // Sort the list
                Collections.sort(dayList, new Comparator<Map.Entry<String, ArrayList<FileModel>>>() {
                    public int compare(Map.Entry<String, ArrayList<FileModel>> o1,
                                       Map.Entry<String, ArrayList<FileModel>> o2) {
                        try {
                            return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                for (int i = 0; i < dayList.size(); i++) {
                    if (dayList.get(i).getKey().equals(monthToDayTransition)) {
                        Objects.requireNonNull(binding.rvDays.getLayoutManager()).scrollToPosition(i);

                        monthToDayTransition = null;
                        break;
                    }
                }
            }
        }
    }

    public void refreshData(ArrayList<MediaModel> mediaModels) {

        if (fragment == null) {
            fragment = new DaysFragment().getInstance();
        }

        Log.d("--days--", "onChanged: " + mediaModels.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

                dayHashMap = new HashMap<>(MediaLoader.getDayHashMap(mediaModelArrayList));
                Log.d("--day_list--", "run: " + dayHashMap.size());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (dayLiveData == null) {
                            dayLiveData = new MutableLiveData<>();
                        }
                        dayLiveData.setValue(dayHashMap);
                    }
                });
            }
        }).start();
    }
}
