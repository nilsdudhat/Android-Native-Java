package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {

    Activity activity;
    List<VideoModel> videoModelArrayList = new ArrayList<>();

    MoreOptionsClickListener moreOptionsClickListener;
    PlayVideoClickListener playVideoClickListener;

    SharedPreferences mPrefs;

    VideoViewModel videoViewModel;

    public VideoListAdapter(Activity activity, List<VideoModel> videoModelArrayList, VideoViewModel videoViewModel,
                            MoreOptionsClickListener moreOptionsClickListener,
                            PlayVideoClickListener playVideoClickListener) {

        this.activity = activity;
        this.videoModelArrayList = videoModelArrayList;
        this.moreOptionsClickListener = moreOptionsClickListener;
        this.playVideoClickListener = playVideoClickListener;
        this.videoViewModel = videoViewModel;

        mPrefs = activity.getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_video_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        VideoModel videoModel = videoModelArrayList.get(holder.getAdapterPosition());

        if (mPrefs.getBoolean("display_recent_video", true)) {
            if (Utils.pageSelection.equalsIgnoreCase("Videos")) {
                String recent_path = mPrefs.getString("recent_path", "");
                if (!(recent_path.equalsIgnoreCase("")) && (recent_path.equals(videoModel.getPath()))) {
                    Log.d("--recent_path_adapter--", "files adapter: " + recent_path);

                    Utils.selectedItem = holder.getAdapterPosition();
                    holder.constraint_video_holder.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_stroke_rectangle));
                } else {
                    holder.constraint_video_holder.setBackground(ContextCompat.getDrawable(activity, android.R.color.transparent));
                }
            } else {
                holder.constraint_video_holder.setBackground(ContextCompat.getDrawable(activity, android.R.color.transparent));
            }
        } else {
            holder.constraint_video_holder.setBackground(ContextCompat.getDrawable(activity, android.R.color.transparent));
        }

        holder.txt_file_name.setText(videoModel.getTitle());

        Glide.with(activity).load(new File(videoModel.getPath())).placeholder(R.drawable.ic_icon).fitCenter().into(holder.video_thumbnail);

        if (videoModel.getDuration() != null) {
            holder.video_duration.setText(Utils.convertMillieToHMmSs(Integer.parseInt(videoModel.getDuration())));
        }

        holder.txt_date.setText(videoModel.getDate());

        holder.txt_file_size.setText(Utils.getStringSizeLengthFile(Long.parseLong(videoModel.getSize())));

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionsClickListener.onMoreOptionsClick(videoModel, holder.getAdapterPosition(), holder.img_more);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideoClickListener.onVideoClick(holder.getAdapterPosition(), videoModelArrayList);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (videoModelArrayList != null) {
            return videoModelArrayList.size();
        } else {
            return 0;
        }
    }

    public interface PlayVideoClickListener {
        void onVideoClick(int position, List<VideoModel> videoModel);
    }

    public interface MoreOptionsClickListener {
        void onMoreOptionsClick(VideoModel videoModel, int position, View view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_file_name;
        ImageView video_thumbnail;
        TextView video_duration;
        TextView txt_date;
        TextView txt_file_size;
        ImageView img_more;
        ConstraintLayout constraint_video_holder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_file_name = itemView.findViewById(R.id.txt_file_name);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            video_duration = itemView.findViewById(R.id.video_duration);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_file_size = itemView.findViewById(R.id.txt_file_size);
            img_more = itemView.findViewById(R.id.img_more);
            constraint_video_holder = itemView.findViewById(R.id.constraint_video_holder);
        }
    }

}
