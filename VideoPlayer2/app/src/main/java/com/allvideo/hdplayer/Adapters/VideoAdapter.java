package com.allvideo.hdplayer.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allvideo.hdplayer.Activities.MyPlayerActivity;
import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.AdsIntegration.AppUtility;
import com.allvideo.hdplayer.AdsIntegration.Constant;
import com.allvideo.hdplayer.Models.VideoModel;
import com.allvideo.hdplayer.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<VideoModel> videoArrayList = new ArrayList<>();

    OnDeleteVideoFile onDeleteVideoFile;

    public interface OnDeleteVideoFile {
        void onDelete(String path, int position);
    }

    public VideoAdapter(Activity activity, ArrayList<VideoModel> videoArrayList, OnDeleteVideoFile onDeleteVideoFile) {
        this.activity = activity;
        this.onDeleteVideoFile = onDeleteVideoFile;

        if (AppUtility.isNetworkAvailable(activity)) {
            int counter = Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

            for (int i = 0; i < videoArrayList.size(); i++) {
                int k = i % counter;

                if ((k == 0)) {
                    videoArrayList.add(i, null);
                }
            }
        }

        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int k = position % Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

        if ((k == 0)) {
            holder.ll_video_item.setVisibility(View.GONE);
            holder.ad_mini_native.setVisibility(View.VISIBLE);

            AdUtils.showMiniNativeAds(activity, holder.ad_mini_native);
        } else {
            holder.ll_video_item.setVisibility(View.VISIBLE);
            holder.ad_mini_native.setVisibility(View.GONE);

            try {
                holder.text_file_name.setText(videoArrayList.get(position).getTitle());
                holder.video_duration.setText(videoArrayList.get(position).getDuration());
                Glide.with(activity).load(new File(videoArrayList.get(position).getPath())).into(holder.video_thumbnail);

                String path = videoArrayList.get(position).getPath();
                File file = new File(path);

                String parentFileName = Objects.requireNonNull(file.getParentFile()).getName();
                holder.txt_parent_file.setText(parentFileName);

                int file_size = (int) file.length();
                holder.text_file_size.setText(getStringSizeLengthFile(file_size));

                holder.txt_file_resolution.setText(videoArrayList.get(position).getResolution());
                Log.d("--resolution--", "onBindViewHolder txt_file_resolution: " + videoArrayList.get(position).getResolution());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MyPlayerActivity.class);
                        intent.putExtra("path", videoArrayList.get(position).getPath());
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(intent);
                            }
                        }, 500);
                    }
                });

                holder.img_more.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(activity, holder.img_more);
                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                        onDeleteVideoFile.onDelete(path, position);
                        return true;
                    });
                    popupMenu.show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;

        if (size < sizeMb)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView video_thumbnail, img_more;
        TextView video_duration, text_file_name, text_file_size, txt_file_resolution, txt_parent_file;
        RelativeLayout ad_mini_native;
        LinearLayout ll_video_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_more = itemView.findViewById(R.id.img_more);
            text_file_size = itemView.findViewById(R.id.text_file_size);
            video_duration = itemView.findViewById(R.id.video_duration);
            text_file_name = itemView.findViewById(R.id.text_file_name);
            txt_parent_file = itemView.findViewById(R.id.txt_parent_file);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            txt_file_resolution = itemView.findViewById(R.id.txt_file_resolution);
            ll_video_item = itemView.findViewById(R.id.ll_video_item);
            ad_mini_native = itemView.findViewById(R.id.ad_mini_native);
        }
    }
}
