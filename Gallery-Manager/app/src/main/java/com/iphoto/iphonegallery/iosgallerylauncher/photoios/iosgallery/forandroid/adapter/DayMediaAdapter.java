package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.DayClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DayMediaAdapter extends RecyclerView.Adapter<DayMediaAdapter.MyViewHolder> {

    ArrayList<FileModel> displayList = new ArrayList<>();
    ArrayList<FileModel> fileModelList = new ArrayList<>();

    Activity activity;
    DayClickListener dayClickListener;
    MediaAdapterClickListener mediaAdapterClickListener;

    public DayMediaAdapter(Activity activity, MediaAdapterClickListener mediaAdapterClickListener) {
        this.activity = activity;
        this.mediaAdapterClickListener = mediaAdapterClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_media, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if ((position > 2) && (position != (fileModelList.size() - 1))) {
            holder.rl_last_item.setVisibility(View.VISIBLE);

            String strRemaining = "+" + (fileModelList.size() - 4);
            holder.txt_remaining.setText(strRemaining);
        }

        FileModel fileModel = displayList.get(holder.getBindingAdapterPosition());
        File file = new File(fileModel.getPath());
        String fileFormat = fileModel.getFileFormat();

        holder.itemView.setOnClickListener(v -> {
//                dayClickListener.onDayClicked(holder.getBindingAdapterPosition(), fileModelList);
            mediaAdapterClickListener.onClick(holder.itemView, fileModelList, holder.getBindingAdapterPosition(), "Day", fileModelList.get(holder.getBindingAdapterPosition()).getDateModified());
        });

        try {
            if (fileFormat.startsWith("video")) {
                holder.rl_video_container.setVisibility(View.GONE);
                holder.rl_video_attrs.setVisibility(View.VISIBLE);
                holder.img_media.setVisibility(View.VISIBLE);
            } else {
                holder.img_media.setVisibility(View.VISIBLE);
                holder.rl_video_container.setVisibility(View.GONE);
                holder.rl_video_attrs.setVisibility(View.GONE);
            }
            Glide.with(activity)
                    .load(file.getAbsoluteFile())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.img_media);

        } catch (Exception e) {
            holder.rl_video_container.setVisibility(View.GONE);
            holder.rl_video_attrs.setVisibility(View.GONE);
            holder.img_media.setVisibility(View.VISIBLE);

            Glide.with(activity)
                    .load(fileModel.getPath())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.img_media);

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    public void swapList(List<FileModel> displayList, List<FileModel> fileModelList) {
        this.displayList = new ArrayList<>(displayList);
        notifyItemRangeInserted(0, displayList.size());

        this.fileModelList = new ArrayList<>(fileModelList);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_media;
        TextView txt_position;
        RelativeLayout rl_last_item;
        TextView txt_remaining;
        TextureView texture_view;
        RelativeLayout rl_video_container;
        RelativeLayout rl_video_attrs;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_media = itemView.findViewById(R.id.img_media);
            txt_position = itemView.findViewById(R.id.txt_position);
            rl_last_item = itemView.findViewById(R.id.rl_last_item);
            txt_remaining = itemView.findViewById(R.id.txt_remaining);
            texture_view = itemView.findViewById(R.id.texture_view);
            rl_video_container = itemView.findViewById(R.id.rl_video_container);
            rl_video_attrs = itemView.findViewById(R.id.rl_video_attrs);
        }
    }
}
