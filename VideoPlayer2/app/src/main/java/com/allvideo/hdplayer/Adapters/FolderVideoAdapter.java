package com.allvideo.hdplayer.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
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

public class FolderVideoAdapter extends RecyclerView.Adapter<FolderVideoAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();

    OnDeleteListener onDeleteListener;

    public interface OnDeleteListener {
        void onDelete(String path, int position);
    }

    public FolderVideoAdapter(Activity activity, ArrayList<VideoModel> videoModelArrayList, OnDeleteListener onDeleteListener) {
        this.activity = activity;
        this.onDeleteListener = onDeleteListener;

        if (AppUtility.isNetworkAvailable(activity)) {

            int counter = Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

            for (int i = 0; i < videoModelArrayList.size(); i++) {
                int k = i % counter;

                if ((k == 0)) {
                    videoModelArrayList.add(i, null);
                }
            }
        }

        this.videoModelArrayList = videoModelArrayList;
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

        if (k == 0) {
            holder.ll_video_item.setVisibility(View.GONE);
            holder.ad_mini_native.setVisibility(View.VISIBLE);

            AdUtils.showMiniNativeAds(activity, holder.ad_mini_native);
        } else {
            holder.ad_mini_native.setVisibility(View.GONE);
            holder.ll_video_item.setVisibility(View.VISIBLE);

            try {
                holder.text_file_name.setText(videoModelArrayList.get(position).getTitle());
                holder.video_duration.setText(videoModelArrayList.get(position).getDuration());
                Glide.with(activity).load(new File(videoModelArrayList.get(position).getPath())).into(holder.video_thumbnail);

                String path = videoModelArrayList.get(position).getPath();
                File file = new File(path);

                String parentFileName = Objects.requireNonNull(file.getParentFile()).getName();
                holder.txt_parent_file.setText(parentFileName);

                int file_size = (int) file.length();
                holder.text_file_size.setText(getStringSizeLengthFile(file_size));

                holder.txt_file_resolution.setText(videoModelArrayList.get(position).getResolution());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MyPlayerActivity.class);
                        intent.putExtra("path", videoModelArrayList.get(position).getPath());
//                        intent.putExtra("position", position);
//                        intent.putExtra("sender", "FolderIsSending");
                        new Handler().postDelayed(new Runnable() {
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

                        onDeleteListener.onDelete(path, position);

                        return true;
                    });
                    popupMenu.show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scanFileForDelete(String path, int position) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();

            videoModelArrayList.remove(position);
            notifyDataSetChanged();
            notifyItemRemoved(position);

        } else {
            Toast.makeText(activity, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getStringSizeLengthFile(long size) {

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
        return videoModelArrayList.size();
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
