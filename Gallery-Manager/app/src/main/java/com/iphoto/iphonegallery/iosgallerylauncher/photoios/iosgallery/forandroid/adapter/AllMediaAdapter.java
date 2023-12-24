package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.EventListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils;

import java.io.File;
import java.util.ArrayList;

public class AllMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;

    EventListener eventListener;

    MediaAdapterClickListener mediaAdapterClickListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    public AllMediaAdapter(Activity activity, EventListener eventListener, MediaAdapterClickListener mediaAdapterClickListener) {
        this.activity = activity;
        this.eventListener = eventListener;
        this.mediaAdapterClickListener = mediaAdapterClickListener;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder instanceof MyViewHolder) {
            eventListener.onEvent(holder.getBindingAdapterPosition() - 1);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

//        if (holder instanceof MyViewHolder) {
//            eventListener.onEvent();
//        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.txt_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_media, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            Log.d("--bind_all--", "HeaderViewHolder: " + holder.getBindingAdapterPosition());
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.txt_all_media_details.setText(
                    String.valueOf(PreferenceUtils.getInteger(activity, PreferenceUtils.IMAGE_COUNT)).concat(" Photos, ")
                            .concat(String.valueOf(PreferenceUtils.getInteger(activity, PreferenceUtils.VIDEO_COUNT))).concat(" Videos"));
        } else if (holder instanceof MyViewHolder) {
            Log.d("--bind_all--", "MyViewHolder: " + holder.getBindingAdapterPosition());
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            int currentPosition = myViewHolder.getBindingAdapterPosition() - 1;

            ViewCompat.setTransitionName(myViewHolder.itemView, String.valueOf(currentPosition));

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaAdapterClickListener.onClick(myViewHolder.itemView, fileModelArrayList, currentPosition, "All", null);
                }
            });
            myViewHolder.txt_position.setText(String.valueOf(currentPosition));

            FileModel fileModel = fileModelArrayList.get(currentPosition);
            File file = new File(fileModel.getPath());

            try {
                if (fileModel.getFileFormat().startsWith("video")) {
                    myViewHolder.rl_video_container.setVisibility(View.GONE);
                    myViewHolder.rl_video_attrs.setVisibility(View.VISIBLE);
                    myViewHolder.img_media.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.img_media.setVisibility(View.VISIBLE);
                    myViewHolder.rl_video_container.setVisibility(View.GONE);
                    myViewHolder.rl_video_attrs.setVisibility(View.GONE);
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
                        .into(myViewHolder.img_media);

            } catch (Exception e) {
                myViewHolder.rl_video_container.setVisibility(View.GONE);
                myViewHolder.rl_video_attrs.setVisibility(View.GONE);
                myViewHolder.img_media.setVisibility(View.VISIBLE);

                Glide.with(activity)
                        .load(fileModel.getPath())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(myViewHolder.img_media);

                e.printStackTrace();
            }
        }
    }

    public void swapList(ArrayList<FileModel> fileModelArrayList) {
        this.fileModelArrayList = new ArrayList<>(fileModelArrayList);
        notifyItemRangeChanged(0, fileModelArrayList.size() + 1);
    }

    @Override
    public int getItemCount() {
        Log.d("--model_list--", "getItemCount: " + fileModelArrayList.size());
        return fileModelArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_media;
        TextView txt_position;
        TextureView texture_view;
        RelativeLayout rl_video_container;
        RelativeLayout rl_video_attrs;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_media = itemView.findViewById(R.id.img_media);
            txt_position = itemView.findViewById(R.id.txt_position);
            texture_view = itemView.findViewById(R.id.texture_view);
            rl_video_container = itemView.findViewById(R.id.rl_video_container);
            rl_video_attrs = itemView.findViewById(R.id.rl_video_attrs);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_all_media_details;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_all_media_details = itemView.findViewById(R.id.txt_all_media_details);
        }
    }
}
