package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;

import java.util.ArrayList;

public class ForYouAdapter extends RecyclerView.Adapter<ForYouAdapter.MyViewHolder> {

    Activity activity;

    ArrayList<ForYouModel> dayForYouArrayList = new ArrayList<>();

    ForYouClickListener forYouClickListener;

    public ForYouAdapter(Activity activity, ForYouClickListener forYouClickListener) {
        this.activity = activity;
        this.forYouClickListener = forYouClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_for_you, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._30sdp);
        int height = (int) (displayMetrics.heightPixels * 0.6);

        holder.constraint_for_you.getLayoutParams().height = height;
        holder.constraint_for_you.getLayoutParams().width = width;

        ForYouModel forYouModel = dayForYouArrayList.get(holder.getBindingAdapterPosition());

        ViewCompat.setTransitionName(holder.itemView, String.valueOf(position));

        String title = "";
        switch (forYouModel.getFormat()) {
            case "day":
                title = DateUtils.getTitleOfForYou(forYouModel.getTitle());
                if (DateUtils.isForYouDateFormat(title)) {
                    title = DateUtils.manageFullDateForLast7Days(title);
                }
                break;
            case "month":
                title = DateUtils.getTitleOfForYou(forYouModel.getTitle());
                if (DateUtils.isForYouDateFormat(title)) {
                    title = DateUtils.manageForYouDateForLast7Days(title);
                }
                break;
            case "recent_week":
            case "recent_year":
            case "year":
            case "address":
                title = forYouModel.getTitle();
                break;
        }

        holder.txt_title.setText(title);

        String headerTitle = "";

        switch (forYouModel.getFormat()) {
            case "day":
                headerTitle = "Recent Day\nHighlights";
                break;
            case "month":
                headerTitle = "Recent Month\nHighlights";
                break;
            case "recent_week":
                headerTitle = "Recent Week\nHighlights";
                break;
            case "recent_year":
                headerTitle = "Recent Year\nHighlights";
                break;
            case "year":
                headerTitle = "Highlight of Year";
                break;
            case "address":
                headerTitle = "Highlight of";
                break;
        }

        holder.txt_header.setText(headerTitle);

        ArrayList<FileModel> fileModels = forYouModel.getFileModelArrayList();

        try {
            Glide.with(activity)
                    .load(fileModels.get(0).getPath())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.img_for_you);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forYouClickListener.onClick(holder.itemView, forYouModel.getTitle(), holder.getBindingAdapterPosition(), forYouModel.getFileModelArrayList());
            }
        });
    }

    public void swapList(ArrayList<ForYouModel> dayForYouArrayList) {
        this.dayForYouArrayList = new ArrayList<>(dayForYouArrayList);
        notifyItemRangeChanged(0, this.dayForYouArrayList.size());
    }

    @Override
    public int getItemCount() {
        return dayForYouArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_header;
        ImageView img_for_you;

        ConstraintLayout constraint_for_you;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_header = itemView.findViewById(R.id.txt_header);
            img_for_you = itemView.findViewById(R.id.img_for_you);
            txt_title = itemView.findViewById(R.id.txt_title);
            constraint_for_you = itemView.findViewById(R.id.constraint_for_you);
        }
    }
}
