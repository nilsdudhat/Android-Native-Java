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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class AlbumMediaListAdapter extends RecyclerView.Adapter<AlbumMediaListAdapter.MyViewHolder> {

    Activity activity;
    MediaAdapterClickListener mediaAdapterClickListener;

    String albumName;

    ArrayList<FileModel> albumFileList = new ArrayList<>();

    ArrayList<Integer> selectedItems = new ArrayList<>();
    boolean isMultipleSelection = false;

    public AlbumMediaListAdapter(Activity activity, String albumName, MediaAdapterClickListener mediaAdapterClickListener) {
        this.activity = activity;
        this.albumName = albumName;
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

        FileModel fileModel = albumFileList.get(currentPosition);
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

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaAdapterClickListener.onClick(myViewHolder.itemView, albumFileList, currentPosition, "Album", albumName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFileList.size();
    }

    public void swapList(ArrayList<FileModel> allMediaFilesList) {
        this.albumFileList = new ArrayList<>(allMediaFilesList);

        Collections.sort(albumFileList, new Comparator<FileModel>() {
            final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

            @Override
            public int compare(FileModel lhs, FileModel rhs) {
                try {
                    return Objects.requireNonNull(f.parse(rhs.getDateModified())).compareTo(f.parse(lhs.getDateModified()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

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
