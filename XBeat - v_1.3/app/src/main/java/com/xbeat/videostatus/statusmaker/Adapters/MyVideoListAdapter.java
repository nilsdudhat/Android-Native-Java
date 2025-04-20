package com.xbeat.videostatus.statusmaker.Adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xbeat.videostatus.statusmaker.AdUtils.AdUtils;
import com.xbeat.videostatus.statusmaker.AdUtils.AppUtility;
import com.xbeat.videostatus.statusmaker.AdUtils.Constant;
import com.xbeat.videostatus.statusmaker.Customs.Globals;
import com.xbeat.videostatus.statusmaker.Models.MyCreationVideoData;
import com.xbeat.videostatus.statusmaker.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class MyVideoListAdapter extends RecyclerView.Adapter<MyVideoListAdapter.MyViewHolder> {
    private final Activity activity;
    public ArrayList<MyCreationVideoData> videoList;
    VideoSelectListener videoSelectListener;

    public interface VideoSelectListener {
        void onVideoSelectListener(int i, MyCreationVideoData myCreationVideoData);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout cvVideoList;
        ConstraintLayout layoutVideoName;
        public ImageView videoListThumb;
        TextView tv_video_name;
        RelativeLayout ad_native;
        RelativeLayout imageRelative;

        public MyViewHolder(View view) {
            super(view);
            this.layoutVideoName = (ConstraintLayout) view.findViewById(R.id.layout_video_name);
            this.videoListThumb = (ImageView) view.findViewById(R.id.video_list_thumb);
            this.cvVideoList = (RelativeLayout) view.findViewById(R.id.cv_video_list);
            this.tv_video_name = (TextView) view.findViewById(R.id.tv_video_name);
            this.ad_native = view.findViewById(R.id.ad_native);
            this.imageRelative = view.findViewById(R.id.imageRelative);
        }
    }

    public MyVideoListAdapter(ArrayList<MyCreationVideoData> arrayList,
                              Activity activity, VideoSelectListener videoSelectListener2) {
        this.activity = activity;
        this.videoSelectListener = videoSelectListener2;

        if (AppUtility.isNetworkAvailable(activity)) {
            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
                int counter = Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

                for (int i = 0; i < arrayList.size(); i++) {
                    int k = i % counter;

                    if ((k == 0)) {
                        arrayList.add(i, null);
                    }
                }
            }
        }

        this.videoList = arrayList;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.row_layout_video_list, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        if (AppUtility.isNetworkAvailable(activity)) {
            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
                int k = position % Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

                if ((k == 0)) {
                    myViewHolder.imageRelative.setVisibility(View.GONE);
                    myViewHolder.ad_native.setVisibility(View.VISIBLE);

                    AdUtils.setUpGoogleNativeInRecyclerView(activity, myViewHolder.ad_native);
                } else {
                    myViewHolder.imageRelative.setVisibility(View.VISIBLE);
                    myViewHolder.ad_native.setVisibility(View.GONE);

                    bindData(myViewHolder, position);
                }
            } else {
                myViewHolder.ad_native.setVisibility(View.GONE);
                bindData(myViewHolder, position);
            }
        }
    }

    private void bindData(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.imageRelative.setVisibility(View.VISIBLE);
        myViewHolder.ad_native.setVisibility(View.GONE);

        MyCreationVideoData myCreationVideoData = this.videoList.get(position);
//        myViewHolder.layoutVideoName.setVisibility(View.GONE);
        int density = (Resources.getSystem().getDisplayMetrics().widthPixels / 2) - Globals.getDensity(8.0d);
        myViewHolder.videoListThumb.setLayoutParams(new RelativeLayout.LayoutParams(density, (myCreationVideoData.getHeight() * (density - Globals.getDensity(5.0d))) / myCreationVideoData.getWidth()));
        Glide.with(this.activity).load(Uri.fromFile(new File(myCreationVideoData.getFilePath()))).into(myViewHolder.videoListThumb);
        String filename = myCreationVideoData.getFilePath().substring(myCreationVideoData.getFilePath().lastIndexOf("/") + 1);
        myViewHolder.tv_video_name.setText(filename);
        myViewHolder.cvVideoList.setOnClickListener(view -> {
            if (videoSelectListener != null) {
                videoSelectListener.onVideoSelectListener(myViewHolder.getAdapterPosition(), MyVideoListAdapter.this.videoList.get(myViewHolder.getAdapterPosition()));
            }
        });
    }

    public int getItemCount() {
        return this.videoList.size();
    }
}
