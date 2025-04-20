package com.xbeat.videostatus.statusmaker.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.xbeat.videostatus.statusmaker.AdUtils.AdUtils;
import com.xbeat.videostatus.statusmaker.AdUtils.AppUtility;
import com.xbeat.videostatus.statusmaker.AdUtils.Constant;
import com.xbeat.videostatus.statusmaker.AdUtils.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchVideoListAdapter extends RecyclerView.Adapter<SearchVideoListAdapter.MyViewHolder> implements Filterable {

    Activity activity;
    ArrayList<ModelVideoList> videoList = new ArrayList<>();
    ArrayList<ModelVideoList> videoListFiltered = new ArrayList<>();
    VideoSelectListener videoSelectListener;

    public interface VideoSelectListener {
        void onVideoSelectListener(int i, ModelVideoList modelVideoList);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mainLayout;
        TextView textVideoTitle;
        RelativeLayout ad_mini_native;

        public MyViewHolder(View view) {
            super(view);
            this.textVideoTitle = (TextView) view.findViewById(R.id.text_video_title);
            this.mainLayout = (ConstraintLayout) view.findViewById(R.id.main_layout);
            this.ad_mini_native = view.findViewById(R.id.ad_mini_native);
        }
    }

    public SearchVideoListAdapter(ArrayList<ModelVideoList> arrayList, Activity activity, VideoSelectListener videoSelectListener2) {
        this.activity = activity;
        this.videoList = arrayList;
        this.videoSelectListener = videoSelectListener2;

        if (AppUtility.isNetworkAvailable(activity)) {
            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
                int counter = Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

                for (int i = 0; i < arrayList.size(); i++) {
                    int k = i % counter;

                    if ((k == 0)) {
                        arrayList.add(i, null);
                    }
                }
            }
        }

        this.videoListFiltered = arrayList;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.row_layout_search_list, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        if (AppUtility.isNetworkAvailable(activity)) {
            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
                int k = position % Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

                if ((k == 0)) {
                    myViewHolder.mainLayout.setVisibility(View.GONE);
                    myViewHolder.ad_mini_native.setVisibility(View.VISIBLE);

                    AdUtils.setUpGoogleMiniNativeInRecyclerView(activity, myViewHolder.ad_mini_native);
                } else {
                    myViewHolder.mainLayout.setVisibility(View.VISIBLE);
                    myViewHolder.ad_mini_native.setVisibility(View.GONE);

                    bindData(myViewHolder, position);
                }
            } else {
                myViewHolder.ad_mini_native.setVisibility(View.GONE);
                bindData(myViewHolder, position);
            }
        }
    }

    private void bindData(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.mainLayout.setVisibility(View.VISIBLE);
        myViewHolder.ad_mini_native.setVisibility(View.GONE);

        if (videoListFiltered.get(position) != null) {
            myViewHolder.textVideoTitle.setText(this.videoListFiltered.get(position).getTitle());

            myViewHolder.mainLayout.setOnClickListener(new DebounceClickListener(2000) {
                @Override
                public void onDebouncedClick(View v) {
                    if (videoSelectListener != null) {
                        videoSelectListener.onVideoSelectListener(myViewHolder.getAdapterPosition(), videoListFiltered.get(myViewHolder.getAdapterPosition()));
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.videoListFiltered.size();
    }

    public Filter getFilter() {
        return new Filter() {
            public FilterResults performFiltering(CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                if (charSequence2.isEmpty()) {
                    videoListFiltered = videoList;
                } else {
                    for (int i = 0; i < videoList.size(); i++) {
                        if (videoList.get(i) == null) {
                            videoList.remove(i);
                        }
                    }
                    ArrayList<ModelVideoList> arrayList = new ArrayList<>();
                    Iterator<ModelVideoList> it = videoList.iterator();
                    while (it.hasNext()) {
                        ModelVideoList next = it.next();
                        if (next.getTitle().toLowerCase().contains(charSequence2.toLowerCase())) {
                            arrayList.add(next);
                        }
                    }
                    videoListFiltered = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = videoListFiltered;
                return filterResults;
            }

            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                videoListFiltered = (ArrayList) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
