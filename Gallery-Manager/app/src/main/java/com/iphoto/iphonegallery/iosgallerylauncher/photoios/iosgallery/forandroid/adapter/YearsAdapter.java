package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.YearClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class YearsAdapter extends RecyclerView.Adapter<YearsAdapter.MyViewHolder> {

    Activity activity;
    YearClickListener yearClickListener;

    List<Map.Entry<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>>> yearList;

    public YearsAdapter(Activity activity, YearClickListener yearClickListener) {
        this.activity = activity;
        this.yearClickListener = yearClickListener;
        this.yearList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_years_slider, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._30sdp);
        int height = (int) (width * 0.7);

        holder.constraint_main.getLayoutParams().height = height;
        holder.constraint_main.getLayoutParams().width = width;

        String yearName = yearList.get(holder.getBindingAdapterPosition()).getKey();

        holder.txt_year.setText(yearName);

        try {
            HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap = yearList.get(holder.getBindingAdapterPosition()).getValue();

            List<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>> monthList = new ArrayList<>(monthHashMap.entrySet());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("--transition--", "month: " + monthList.get(0).getKey());
                    yearClickListener.onClick(monthList.get(0).getKey());
                }
            });

            HashMap<String, ArrayList<FileModel>> dayHashMap = new HashMap<>(monthList.get(0).getValue());
            List<Map.Entry<String, ArrayList<FileModel>>> dayList = new ArrayList<>(dayHashMap.entrySet());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH); // your own date format

            Collections.sort(dayList, new Comparator<Map.Entry<String, ArrayList<FileModel>>>() {
                @Override
                public int compare(Map.Entry<String, ArrayList<FileModel>> stringArrayListEntry, Map.Entry<String, ArrayList<FileModel>> t1) {
                    return 0;
                }
            });

            ArrayList<FileModel> fileList = dayList.get(0).getValue();

            FileModel fileModel = fileList.get(0);

            File file = new File(fileModel.getPath());

            try {
                if (fileModel.getFileFormat().startsWith("video")) {
                    holder.rl_video_container.setVisibility(View.GONE);
                    holder.rl_video_attrs.setVisibility(View.VISIBLE);
                    holder.img_media.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams layoutParamsVideoContainer = holder.rl_video_container.getLayoutParams();

                    layoutParamsVideoContainer.height = height;
                    layoutParamsVideoContainer.width = width;

                    holder.rl_video_container.setLayoutParams(layoutParamsVideoContainer);
                } else {
                    holder.img_media.setVisibility(View.VISIBLE);
                    holder.rl_video_container.setVisibility(View.GONE);
                    holder.rl_video_attrs.setVisibility(View.GONE);
                }
                Glide.with(activity)
                        .load(file.getAbsoluteFile())
                        .apply(new RequestOptions().override(width, height))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(holder.img_media);

            } catch (Exception e) {
                holder.texture_view.setVisibility(View.GONE);
                holder.rl_video_attrs.setVisibility(View.GONE);
                holder.img_media.setVisibility(View.VISIBLE);

                Glide.with(activity)
                        .load(file.getAbsoluteFile())
                        .apply(new RequestOptions().override(width, height))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(holder.img_media);

                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return yearList.size();
    }

    public void swapYearMap(HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> yearHashMap) {
        List<Map.Entry<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>>> tempYearList = new ArrayList<>(yearHashMap.entrySet());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy"); // your own date format

        // Sort the list
        Collections.sort(tempYearList, new Comparator<Map.Entry<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>>>() {
            public int compare(Map.Entry<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> o1,
                               Map.Entry<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> o2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        this.yearList = new ArrayList<>(tempYearList);
        notifyItemRangeChanged(0, yearList.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_media;
        TextView txt_year;
        TextureView texture_view;
        RelativeLayout rl_video_container;
        RelativeLayout rl_video_attrs;
        ConstraintLayout constraint_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_media = itemView.findViewById(R.id.img_media);
            txt_year = itemView.findViewById(R.id.txt_year);
            texture_view = itemView.findViewById(R.id.texture_view);
            rl_video_container = itemView.findViewById(R.id.rl_video_container);
            rl_video_attrs = itemView.findViewById(R.id.rl_video_attrs);
            constraint_main = itemView.findViewById(R.id.constraint_main);
        }
    }
}
