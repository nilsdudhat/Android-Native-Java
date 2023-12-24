package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouMediaClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.io.File;
import java.util.ArrayList;

public class ForYouMediaAdapter extends SliderViewAdapter<ForYouMediaAdapter.SliderAdapterViewHolder> {

    Activity activity;
    ArrayList<FileModel> fileModelList = new ArrayList<>();
    ForYouMediaClickListener forYouMediaClickListener;

    public ForYouMediaAdapter(Activity activity, ArrayList<FileModel> fileModelArrayList, ForYouMediaClickListener forYouMediaClickListener) {
        this.activity = activity;
        this.fileModelList = fileModelArrayList;
        this.forYouMediaClickListener = forYouMediaClickListener;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_for_you_media, parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapterViewHolder viewHolder, int position) {

        FileModel fileModel = fileModelList.get(position);

        File file = new File(fileModel.getPath());

        try {
            if (fileModel.getFileFormat().startsWith("video")) {
                viewHolder.rl_video_attrs.setVisibility(View.VISIBLE);
                viewHolder.img_media.setVisibility(View.VISIBLE);
            } else {
                viewHolder.img_media.setVisibility(View.VISIBLE);
                viewHolder.rl_video_attrs.setVisibility(View.GONE);
            }
            Glide.with(activity).asBitmap()
                    .load(file.getAbsoluteFile())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(viewHolder.img_media);

        } catch (Exception e) {
            viewHolder.rl_video_attrs.setVisibility(View.GONE);
            viewHolder.img_media.setVisibility(View.VISIBLE);

            Glide.with(activity).asBitmap()
                    .load(file.getAbsoluteFile())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(viewHolder.img_media);

            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forYouMediaClickListener.onClick();
            }
        });
    }

    @Override
    public int getCount() {
        return fileModelList.size();
    }

    public static class SliderAdapterViewHolder extends ViewHolder {

        ConstraintLayout constraint_main;
        ImageView img_media;
        TextureView texture_view;
        RelativeLayout rl_video_attrs;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);

            constraint_main = itemView.findViewById(R.id.constraint_main);
            img_media = itemView.findViewById(R.id.img_media);
            texture_view = itemView.findViewById(R.id.texture_view);
            rl_video_attrs = itemView.findViewById(R.id.rl_video_attrs);
        }
    }
}
