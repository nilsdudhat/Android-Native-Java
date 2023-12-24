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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.SelectionClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.io.File;
import java.util.ArrayList;

public class SelectionMediaAdapter extends RecyclerView.Adapter<SelectionMediaAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    SelectionClickListener selectionClickListener;
    ArrayList<Integer> selectedItems = new ArrayList<>();

    public SelectionMediaAdapter(Activity activity, SelectionClickListener selectionClickListener) {
        this.activity = activity;
        this.selectionClickListener = selectionClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_selection_media, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FileModel fileModel = fileModelArrayList.get(holder.getBindingAdapterPosition());
        File file = new File(fileModel.getPath());

        if (selectedItems.contains(holder.getBindingAdapterPosition())) {
            holder.img_select.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_selected));
            holder.background_overlay.setVisibility(View.VISIBLE);
        } else {
            holder.img_select.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_not_selected));
            holder.background_overlay.setVisibility(View.GONE);
        }

        try {
            if (fileModel.getFileFormat().startsWith("video")) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionClickListener.onSelect(holder.getBindingAdapterPosition());
            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Dialog dialog = new Dialog(activity, R.style.CustomDialog);
//                dialog.setContentView(R.layout.dialog_image);
//
//                ImageView img_media = dialog.findViewById(R.id.img_media);
//
//                Glide.with(activity).asBitmap().load(fileModel.getPath()).into(img_media);
//
//                dialog.show();
//
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return fileModelArrayList.size();
    }

    public void swapList(ArrayList<FileModel> fileModelArrayList) {
        this.fileModelArrayList = fileModelArrayList;
        notifyItemRangeChanged(0, fileModelArrayList.size());
    }

    public void swapSelectedItems(ArrayList<Integer> selectedItems) {
        this.selectedItems = new ArrayList<>(selectedItems);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_media;
        TextView txt_position;
        TextView txt_duration;
        TextureView texture_view;
        RelativeLayout rl_video_container;
        RelativeLayout rl_video_attrs;
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
            img_select = itemView.findViewById(R.id.img_select);
            background_overlay = itemView.findViewById(R.id.background_overlay);
        }
    }
}
