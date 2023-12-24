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
import androidx.recyclerview.widget.GridLayoutManager;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AllMediaAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentAllBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.EventListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.FullScreenMediaActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.SelectionMediaActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class AllFragment extends Fragment implements MediaAdapterClickListener, EventListener {

    AllFragment fragment;

    Activity activity;

    FragmentAllBinding binding;

    private AllMediaAdapter allMediaAdapter;

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    MutableLiveData<ArrayList<FileModel>> allLiveData;

    public AllFragment getInstance() {
        if (fragment == null) {
            fragment = new AllFragment();
        }
        return fragment;
    }

    public AllFragment() {
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
        binding = FragmentAllBinding.inflate(inflater, container, false);

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();

        ((MainActivity) activity).showProgressDialog();

        binding.frameSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SelectionMediaActivity.class);
                Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
//                intent.putExtra("fileModelArrayList", fileModelArrayList);
                ((MainActivity) activity).showInterstitialAd(activity, intent, null);
            }
        });

        if (allLiveData == null) {
            allLiveData = new MutableLiveData<>();
        }

        allLiveData.observe((MainActivity) activity, new Observer<ArrayList<FileModel>>() {
            @Override
            public void onChanged(ArrayList<FileModel> fileChangeList) {
                fileModelArrayList = new ArrayList<>(fileChangeList);

                if (fileModelArrayList.isEmpty()) {
                    binding.dataContainer.setVisibility(View.GONE);
                    binding.txtNoMedia.setVisibility(View.VISIBLE);
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH); // your own date format

                    Collections.sort(fileModelArrayList, new Comparator<FileModel>() {
                        @Override
                        public int compare(FileModel fileModel1, FileModel fileModel2) {
                            try {
                                return Objects.requireNonNull(simpleDateFormat.parse(fileModel2.getDateModified())).compareTo(simpleDateFormat.parse(fileModel1.getDateModified()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    binding.dataContainer.setVisibility(View.VISIBLE);
                    binding.txtNoMedia.setVisibility(View.GONE);
                }
                ((MainActivity) activity).dismissProgressDialog();
                allMediaAdapter.swapList(fileModelArrayList);
            }
        });
    }

    private void setUpRecyclerView() {
        WrapperGridLayoutManager gridLayoutManager = new WrapperGridLayoutManager(requireContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        gridLayoutManager.setReverseLayout(true);
        binding.rvAllMedia.setLayoutManager(gridLayoutManager);

        allMediaAdapter = new AllMediaAdapter(activity, this, this);
        binding.rvAllMedia.setAdapter(null);
        binding.rvAllMedia.setAdapter(allMediaAdapter);
    }

    @Override
    public void onClick(View view, ArrayList<FileModel> fileModelArrayList, int position, String mediaType, String mediaSubType) {
        Intent intent = new Intent(activity, FullScreenMediaActivity.class);

        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);

        intent.putExtra("position", position);
        intent.putExtra("mediaType", mediaType);
        intent.putExtra("subMediaType", mediaSubType);

        ((MainActivity) activity).showInterstitialAd(activity, intent, null);
    }

    @Override
    public void onEvent(int i) {
        if (!fileModelArrayList.isEmpty()) {
            if (i > 0) {
                binding.txtDate.setText(DateUtils.manageDateForLast7Days(DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModelArrayList.get(i).getDateModified()), "dd MMMM, yyyy"));
            }
        }
    }

    public void refreshData(ArrayList<MediaModel> mediaModels) {

        if (fragment == null) {
            fragment = new AllFragment().getInstance();
        }

        Log.d("--all--", "onChanged: " + mediaModels.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

                try {
                    fileModelArrayList = new ArrayList<>();

                    for (int i = 0; i < mediaModelArrayList.size(); i++) {
                        MediaModel mediaModel = mediaModelArrayList.get(i);

                        FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
                        fileModelArrayList.add(fileModel);
                    }

                    if (allLiveData == null) {
                        allLiveData = new MutableLiveData<>();
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            allLiveData.setValue(fileModelArrayList);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
