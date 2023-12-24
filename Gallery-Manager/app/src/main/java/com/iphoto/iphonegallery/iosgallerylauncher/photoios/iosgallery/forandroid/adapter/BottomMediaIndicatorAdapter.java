package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BottomMediaIndicatorAdapter extends RecyclerView.Adapter<BottomMediaIndicatorAdapter.MyViewHolder> {

    Activity activity;

    List<FileModel> fileModelList = new ArrayList<>();

    int currentPosition;

    BottomIndicatorClicked bottomIndicatorClicked;

    public BottomMediaIndicatorAdapter(Activity activity, BottomIndicatorClicked bottomIndicatorClicked) {
        this.activity = activity;
        this.bottomIndicatorClicked = bottomIndicatorClicked;
    }

    public interface BottomIndicatorClicked {
        void onBottomIndicatorClicked(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_media_indicator, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FileModel fileModel = fileModelList.get(holder.getBindingAdapterPosition());

        File file = new File(fileModel.getPath());

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomIndicatorClicked.onBottomIndicatorClicked(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileModelList.size();
    }

    public void swapList(List<FileModel> fileModelList) {
        this.fileModelList = new ArrayList<>(fileModelList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_media;
        ConstraintLayout media_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_media = itemView.findViewById(R.id.img_media);
            media_container = itemView.findViewById(R.id.media_container);
        }
    }
}
