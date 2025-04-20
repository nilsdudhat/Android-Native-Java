package com.xbeat.videostatus.statusmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xbeat.videostatus.statusmaker.Activities.VideoMakingActivity;
import com.xbeat.videostatus.statusmaker.Models.ImageJsonData;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ImageJsonData> imageList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvParentLayout;
        ImageView ivPlus;
        ImageView ivVideoImage;

        public MyViewHolder(View view) {
            super(view);
            this.cvParentLayout = (CardView) view.findViewById(R.id.cv_parent_layout);
            this.ivPlus = (ImageView) view.findViewById(R.id.iv_plus);
            this.ivVideoImage = (ImageView) view.findViewById(R.id.iv_video_image);
        }
    }

    public ImageListAdapter(Context context2, ArrayList<ImageJsonData> arrayList) {
        this.context = context2;
        this.imageList = arrayList;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_layout_image_list, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        ImageView imageView;
        int i2;
        if (this.imageList.get(i).getSelectedPath() == null) {
            Glide.with(this.context).load(this.imageList.get(i).getImagePath()).into(myViewHolder.ivVideoImage);
            imageView = myViewHolder.ivPlus;
            i2 = 0;
        } else {
            Glide.with(this.context).load(this.imageList.get(i).getSelectedPath()).into(myViewHolder.ivVideoImage);
            imageView = myViewHolder.ivPlus;
            i2 = 8;
        }
        imageView.setVisibility(i2);
        myViewHolder.cvParentLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VideoMakingActivity.videoMakingActivity.getNewImage(myViewHolder.getAdapterPosition());
            }
        });
    }

    public int getItemCount() {
        return this.imageList.size();
    }
}
