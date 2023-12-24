package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaPagerClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.VideoClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaPagerAdapter extends PagerAdapter {

    // Context object
    Activity activity;

    // Array of images
    List<FileModel> fileModelList;

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    VideoClickListener videoClickListener;
    MediaPagerClickListener mediaPagerClickListener;

    // Viewpager Constructor
    public MediaPagerAdapter(Activity activity, VideoClickListener videoClickListener, MediaPagerClickListener mediaPagerClickListener) {
        this.activity = activity;
        this.fileModelList = new ArrayList<>();
        this.videoClickListener = videoClickListener;
        this.mediaPagerClickListener = mediaPagerClickListener;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return fileModelList.size();
    }

    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_media_pager, container, false);

        TouchImageView img_media = itemView.findViewById(R.id.img_media);
        ImageView img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        ImageView img_play_video = itemView.findViewById(R.id.img_play_video);

        try {
            FileModel fileModel = fileModelList.get(position);

            File file = new File(fileModel.getPath());

            if (fileModel.getFileFormat().startsWith("video")) {
                img_play_video.setVisibility(View.VISIBLE);
                img_thumbnail.setVisibility(View.VISIBLE);
                img_media.setVisibility(View.GONE);

                Glide.with(activity)
                        .load(file.getAbsoluteFile())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(img_thumbnail);
            } else {
                img_play_video.setVisibility(View.GONE);
                img_thumbnail.setVisibility(View.GONE);
                img_media.setVisibility(View.VISIBLE);

                Glide.with(activity)
                        .load(file.getAbsoluteFile())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(img_media);
            }

            img_media.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPagerClickListener.onPagerClick(position);
                }
            });

            img_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPagerClickListener.onPagerClick(position);
                }
            });

            img_play_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoClickListener.onVideoClick(position);
                }
            });

            Objects.requireNonNull(container).addView(itemView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public void swapList(ArrayList<FileModel> fileModelArrayList) {
        this.fileModelList = new ArrayList<>(fileModelArrayList);
        notifyDataSetChanged();
        Log.d("--renamed_list--", "swapList: " + new Gson().toJson(fileModelArrayList));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}