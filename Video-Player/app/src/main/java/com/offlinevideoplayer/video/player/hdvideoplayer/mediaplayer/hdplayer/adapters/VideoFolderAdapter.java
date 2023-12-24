package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;

import java.util.ArrayList;
import java.util.List;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {

    AppCompatActivity activity;

    MoreOptionsClickListener moreOptionsClickListener;

    List<VideoFolderModel> videoFolderModels = new ArrayList<>();

    VideoFolderClickListener videoFolderClickListener;

    public VideoFolderAdapter(AppCompatActivity activity, List<VideoFolderModel> videoFolderModelArrayList, MoreOptionsClickListener moreOptionsClickListener, VideoFolderClickListener videoFolderClickListener) {
        this.activity = activity;
        this.videoFolderModels = videoFolderModelArrayList;
        this.moreOptionsClickListener = moreOptionsClickListener;
        this.videoFolderClickListener = videoFolderClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_folder_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        VideoFolderModel videoFolderModel = videoFolderModels.get(holder.getAdapterPosition());

        holder.txt_folder_name.setText(videoFolderModel.getTitle());
        holder.txt_files_count.setText(String.valueOf(videoFolderModel.getFilesCount()));
//        Glide.with(homeActivity).load(new File(loadFolderThumbnail(videoFolderModel))).placeholder(R.drawable.ic_icon).into(holder.img_folder_thumbnail);

        holder.img_more.setOnClickListener(v -> {
            moreOptionsClickListener.onMoreOptionsClick(videoFolderModel, holder.getAdapterPosition(), holder.img_more);
        });

        holder.itemView.setOnClickListener(v -> {
            videoFolderClickListener.onVideoClicked(videoFolderModels.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return videoFolderModels.size();
    }

    @SuppressLint("Range")
    private String loadFolderThumbnail(VideoFolderModel videoFolderModel) {

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + videoFolderModel.getTitle() + "%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        } else {
            return null;
        }
    }

    public interface VideoFolderClickListener {
        void onVideoClicked(VideoFolderModel videoFolderModel);
    }

    public interface MoreOptionsClickListener {
        void onMoreOptionsClick(VideoFolderModel videoFolderModel, int position, View view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_folder_name;
        TextView txt_files_count;
        ImageView img_folder_thumbnail;
        ImageView img_more;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_folder_name = itemView.findViewById(R.id.txt_folder_name);
            txt_files_count = itemView.findViewById(R.id.txt_files_count);
            img_folder_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            img_more = itemView.findViewById(R.id.img_more);
        }
    }
/*
    public void updateList(List<VideoFolderModel> videoFolderModels) {
        this.videoFolderModels = new ArrayList<>(videoFolderModels);
        notifyDataSetChanged();
    }*/
}
