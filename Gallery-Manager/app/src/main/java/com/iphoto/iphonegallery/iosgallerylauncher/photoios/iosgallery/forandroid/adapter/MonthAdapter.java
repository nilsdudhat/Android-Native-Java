package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MonthClickListener;
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

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyViewHolder> {

    Activity activity;
    MonthClickListener monthClickListener;

    List<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>> monthMapList;

    public MonthAdapter(Activity activity, MonthClickListener monthClickListener) {
        this.activity = activity;
        this.monthClickListener = monthClickListener;
        this.monthMapList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_months, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String monthName = monthMapList.get(holder.getBindingAdapterPosition()).getKey();

        HashMap<String, ArrayList<FileModel>> dayHashMap = monthMapList.get(holder.getBindingAdapterPosition()).getValue();

        holder.txt_month.setText(monthName);

        List<Map.Entry<String, ArrayList<FileModel>>> dayList = new ArrayList<>(dayHashMap.entrySet());
        FileModel fileModel = dayList.get(0).getValue().get(0);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthClickListener.onClick(dayList.get(0).getKey());
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._30sdp);
        int height = (int) (width * 0.7);

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
    }

    @Override
    public int getItemCount() {
        return monthMapList.size();
    }

    public void swapMonthMap(HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap) {
        List<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>> tempMonthMapList = new ArrayList<>(monthHashMap.entrySet());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH); // your own date format

        // Sort the list
        Collections.sort(tempMonthMapList, new Comparator<Map.Entry<String, HashMap<String, ArrayList<FileModel>>>>() {
            public int compare(Map.Entry<String, HashMap<String, ArrayList<FileModel>>> o1,
                               Map.Entry<String, HashMap<String, ArrayList<FileModel>>> o2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        this.monthMapList = new ArrayList<>(tempMonthMapList);
        notifyItemRangeChanged(0, monthMapList.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_media;
        TextView txt_month;
        TextureView texture_view;
        RelativeLayout rl_video_container;
        RelativeLayout rl_video_attrs;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_media = itemView.findViewById(R.id.img_media);
            txt_month = itemView.findViewById(R.id.txt_month);
            texture_view = itemView.findViewById(R.id.texture_view);
            rl_video_container = itemView.findViewById(R.id.rl_video_container);
            rl_video_attrs = itemView.findViewById(R.id.rl_video_attrs);
        }
    }
}
