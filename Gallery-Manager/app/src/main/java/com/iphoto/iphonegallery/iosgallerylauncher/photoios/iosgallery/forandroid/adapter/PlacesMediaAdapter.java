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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.io.File;
import java.util.ArrayList;

public class PlacesMediaAdapter extends RecyclerView.Adapter<PlacesMediaAdapter.MyViewHolder> {

    Activity activity;
    String place;
    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    MediaAdapterClickListener mediaAdapterClickListener;

    ArrayList<Integer> selectedItems = new ArrayList<>();
    boolean isMultipleSelection = false;

    public PlacesMediaAdapter(Activity activity, String place, MediaAdapterClickListener mediaAdapterClickListener) {
        this.activity = activity;
        this.place = place;
        this.mediaAdapterClickListener = mediaAdapterClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_media_with_selection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        int currentPosition = myViewHolder.getBindingAdapterPosition();

        ViewCompat.setTransitionName(myViewHolder.itemView, String.valueOf(currentPosition));

        if (isMultipleSelection) {
            myViewHolder.selection_holder.setVisibility(View.VISIBLE);

            if (selectedItems.contains(currentPosition)) {
                myViewHolder.img_select.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_selected));
                myViewHolder.background_overlay.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.img_select.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_not_selected));
                myViewHolder.background_overlay.setVisibility(View.GONE);
            }
        } else {
            myViewHolder.selection_holder.setVisibility(View.GONE);
        }

        myViewHolder.txt_position.setText(String.valueOf(currentPosition));

        FileModel fileModel = fileModelArrayList.get(currentPosition);
        File file = new File(fileModel.getPath());

        try {
            if (fileModel.getFileFormat().startsWith("video")) {
                myViewHolder.rl_video_container.setVisibility(View.GONE);
                myViewHolder.rl_video_attrs.setVisibility(View.VISIBLE);
                myViewHolder.img_media.setVisibility(View.VISIBLE);
//                myViewHolder.txt_duration.setText(fileModel.getDuration());
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

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaAdapterClickListener.onClick(myViewHolder.itemView, fileModelArrayList, currentPosition, "Place", place);
            }
        });
    }

    public void swapList(ArrayList<FileModel> allMediaFilesList) {
        this.fileModelArrayList = new ArrayList<>(allMediaFilesList);
        notifyItemRangeChanged(0, allMediaFilesList.size());
    }

    public void swapSelectedList(ArrayList<Integer> selectedItems, boolean isMultipleSelection) {
        this.selectedItems = new ArrayList<>(selectedItems);
        this.isMultipleSelection = isMultipleSelection;
    }

    public void notifySingleSelection(ArrayList<Integer> selectedItems, int position) {
        this.selectedItems = new ArrayList<>(selectedItems);

        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return fileModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_media;
        TextView txt_position;
        TextView txt_duration;
        TextureView texture_view;
        RelativeLayout rl_video_container;
        RelativeLayout rl_video_attrs;
        ConstraintLayout selection_holder;
        ImageView img_select;
        ImageView background_overlay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_media = itemView.findViewById(R.id.img_media);
            txt_position = itemView.findViewById(R.id.txt_position);
            txt_duration = itemView.findViewById(R.id.txt_duration);
            texture_view = itemView.findViewById(R.id.texture_view);
            rl_video_container = itemView.findViewById(R.id.rl_video_container);
            rl_video_attrs = itemView.findViewById(R.id.rl_video_attrs);
            selection_holder = itemView.findViewById(R.id.selection_holder);
            img_select = itemView.findViewById(R.id.img_select);
            background_overlay = itemView.findViewById(R.id.background_overlay);
        }
    }
}
