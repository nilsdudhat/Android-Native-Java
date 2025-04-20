package com.allvideo.hdplayer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.allvideo.hdplayer.Adapters.FolderAdapter;
import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.Custom.Utils;
import com.allvideo.hdplayer.R;

public class FolderFragment extends Fragment {

    RecyclerView recyclerView_folders;
    FolderAdapter folderAdapter;

    ImageView img_no_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        img_no_data = view.findViewById(R.id.img_no_data);
        recyclerView_folders = view.findViewById(R.id.recyclerview_folders);

        AdUtils.showMiniNativeAds(requireActivity(), view.findViewById(R.id.ad_mini_native));

        return view;
    }

    private void setUpRecyclerView() {
        if (Utils.folderArrayList != null && Utils.folderArrayList.size() > 0 && Utils.videoModelArrayList != null) {
            img_no_data.setVisibility(View.GONE);
            recyclerView_folders.setVisibility(View.VISIBLE);

            folderAdapter = new FolderAdapter(Utils.folderArrayList, Utils.videoModelArrayList, getActivity());
            recyclerView_folders.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            recyclerView_folders.setAdapter(folderAdapter);
            recyclerView_folders.setNestedScrollingEnabled(false);
        } else {
            img_no_data.setVisibility(View.VISIBLE);
            recyclerView_folders.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpRecyclerView();
    }
}