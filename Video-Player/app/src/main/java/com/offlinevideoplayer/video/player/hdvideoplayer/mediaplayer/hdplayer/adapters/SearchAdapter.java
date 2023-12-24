package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.SearchActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    ArrayList<VideoModel> videoModelList = new ArrayList<>();
    ArrayList<VideoModel> filteredVideoList = new ArrayList<>();
    SearchActivity homeActivity;
    SearchListener searchListener;
    SearchedItemClicked searchedItemClicked;

    public SearchAdapter(SearchActivity homeActivity, ArrayList<VideoModel> videoModelList, SearchListener searchListener, SearchedItemClicked searchedItemClicked) {
        this.homeActivity = homeActivity;
        this.videoModelList = videoModelList;
        this.filteredVideoList = videoModelList;
        this.searchListener = searchListener;
        this.searchedItemClicked = searchedItemClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.item_video_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        VideoModel videoModel = filteredVideoList.get(holder.getAdapterPosition());

        holder.txt_file_name.setText(videoModel.getTitle());

        Glide.with(homeActivity).load(new File(videoModel.getPath())).placeholder(R.drawable.ic_icon).fitCenter().into(holder.video_thumbnail);

        Log.d("--time--", "duration: " + videoModel.getDuration());

        if (videoModel.getDuration() != null) {
            holder.video_duration.setText(Utils.convertMillieToHMmSs(Integer.parseInt(videoModel.getDuration())));
        }

        holder.txt_date.setText("Date Modified: " + videoModel.getDate());

        int file_size = (int) new File(videoModel.getPath()).length();
        holder.txt_file_size.setText(Utils.getStringSizeLengthFile(Long.parseLong(videoModel.getSize())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedItemClicked.onSearchedItemClick(videoModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (filteredVideoList != null) {
            return filteredVideoList.size();
        } else {
            return 0;
        }
    }

    public Filter getFilter() {
        return new Filter() {
            public FilterResults performFiltering(CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                if (charSequence2.isEmpty()) {
                    filteredVideoList = videoModelList;
                } else {
                    ArrayList<VideoModel> arrayList = new ArrayList<>();
                    for (VideoModel next : videoModelList) {
                        if (next.getTitle().toLowerCase().contains(charSequence2.toLowerCase())) {
                            arrayList.add(next);
                        }
                    }
                    filteredVideoList = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredVideoList;
                return filterResults;
            }

            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredVideoList = (ArrayList<VideoModel>) filterResults.values;
                searchListener.searchedItem(!filteredVideoList.isEmpty());
                notifyDataSetChanged();
            }
        };
    }

    public interface SearchListener {
        void searchedItem(boolean isResultFound);
    }

    public interface SearchedItemClicked {
        void onSearchedItemClick(VideoModel videoModel);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_file_name;
        ImageView video_thumbnail;
        TextView video_duration;
        TextView txt_date;
        TextView txt_file_size;
        TextView txt_file_resolution;
        ImageView img_more;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_file_name = itemView.findViewById(R.id.txt_file_name);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            video_duration = itemView.findViewById(R.id.video_duration);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_file_size = itemView.findViewById(R.id.txt_file_size);
            txt_file_resolution = itemView.findViewById(R.id.txt_file_resolution);
            img_more = itemView.findViewById(R.id.img_more);
            img_more.setVisibility(View.GONE);
        }
    }
}