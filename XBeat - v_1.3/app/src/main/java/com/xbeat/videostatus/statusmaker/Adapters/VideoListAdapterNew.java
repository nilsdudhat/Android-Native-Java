package com.xbeat.videostatus.statusmaker.Adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xbeat.videostatus.statusmaker.AdUtils.AdUtils;
import com.xbeat.videostatus.statusmaker.AdUtils.AppUtility;
import com.xbeat.videostatus.statusmaker.AdUtils.Constant;
import com.xbeat.videostatus.statusmaker.AdUtils.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Customs.Globals;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;
import com.xbeat.videostatus.statusmaker.R;

import java.util.Arrays;
import java.util.List;

public class VideoListAdapterNew extends RecyclerView.Adapter<VideoListAdapterNew.MyViewHolder> {
    private static final List<Integer> integerList = Arrays.asList(-16732441, -589767, -539392, -5563878, -4361542, -1012819, -566180, -40820, -11873182, -11724117, -7202133, -18611);
    Activity activity;
    public List<ModelVideoList> videoList;
    VideoSelectListenerNew videoSelectListener;

    public interface VideoSelectListenerNew {
        void onVideoSelectListenerNew(int i, ModelVideoList modelVideoList);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cvVideoList;
        public FrameLayout d;
        RelativeLayout imageRelative;
        RelativeLayout ad_native;
        ImageView ivBackNewHot;
        ImageView ivNew;
        ConstraintLayout layoutVideoName;
        TextView tvVideoName;
        ImageView videoListThumb;

        public MyViewHolder(View view) {
            super(view);
            tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
            cvVideoList = (RelativeLayout) view.findViewById(R.id.cv_video_list);
            imageRelative = (RelativeLayout) view.findViewById(R.id.imageRelative);
            ad_native = (RelativeLayout) view.findViewById(R.id.ad_native);
            ivBackNewHot = (ImageView) view.findViewById(R.id.ivBackNewHot);
            ivNew = (ImageView) view.findViewById(R.id.ivNew);
            layoutVideoName = (ConstraintLayout) view.findViewById(R.id.layout_video_name);
            videoListThumb = (ImageView) view.findViewById(R.id.video_list_thumb);
        }
    }

    public VideoListAdapterNew(List<ModelVideoList> arrayList, Activity activity, VideoSelectListenerNew videoSelectListener2) {
        this.activity = activity;
        this.videoSelectListener = videoSelectListener2;

        if (AppUtility.isNetworkAvailable(activity)) {
            int counter = Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

            for (int i = 0; i < arrayList.size(); i++) {
                int k = i % counter;

                if ((k == 0)) {
                    arrayList.add(i, null);
                }
            }
        }

        this.videoList = arrayList;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_video_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    public int getItemViewType(int i) {
        return this.videoList.get(i) != null ? 1 : 0;
    }

    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        if (AppUtility.isNetworkAvailable(activity)) {
            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
                int k = position % Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));

                if ((k == 0)) {
                    myViewHolder.imageRelative.setVisibility(View.GONE);
                    myViewHolder.ad_native.setVisibility(View.VISIBLE);

                    AdUtils.setUpGoogleNativeInRecyclerView(activity, myViewHolder.ad_native);
                } else {
                    myViewHolder.imageRelative.setVisibility(View.VISIBLE);
                    myViewHolder.ad_native.setVisibility(View.GONE);

                    bindData(myViewHolder, position);
                }
            } else {
                myViewHolder.ad_native.setVisibility(View.GONE);
                bindData(myViewHolder, position);
            }
        }
    }

    private void bindData(MyViewHolder myViewHolder, int position) {
        int itemViewType = myViewHolder.getItemViewType();

        if (itemViewType == 1) {

            ModelVideoList modelVideoList = this.videoList.get(position);
            myViewHolder.tvVideoName.setText(modelVideoList.getTitle());

            int density = (Resources.getSystem().getDisplayMetrics().widthPixels / 2) - Globals.getDensity(8.0d);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(density, (modelVideoList.getHeight() * (density - Globals.getDensity(5.0d))) / modelVideoList.getWidth());

            myViewHolder.videoListThumb.setLayoutParams(layoutParams);

            int size = position % integerList.size();

            if (modelVideoList.getIsHot()) {
                myViewHolder.ivBackNewHot.setVisibility(View.GONE);
                myViewHolder.ivNew.setVisibility(View.GONE);
                myViewHolder.ivBackNewHot.setColorFilter(integerList.get(size));
            } else if (modelVideoList.getIsNew()) {
                myViewHolder.ivBackNewHot.setVisibility(View.GONE);
                myViewHolder.ivNew.setVisibility(View.GONE);
                myViewHolder.ivBackNewHot.setColorFilter(integerList.get(size));
            } else {
                myViewHolder.ivBackNewHot.setVisibility(View.GONE);
                myViewHolder.ivNew.setVisibility(View.GONE);
            }

            Glide.with(this.activity).load(modelVideoList.getThumbUrl()).into(myViewHolder.videoListThumb);

            myViewHolder.cvVideoList.setOnClickListener(new DebounceClickListener(2000) {
                @Override
                public void onDebouncedClick(View v) {
                    if (videoSelectListener != null) {
                        videoSelectListener.onVideoSelectListenerNew(myViewHolder.getAdapterPosition(), videoList.get(myViewHolder.getAdapterPosition()));
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.videoList.size();
    }
}
