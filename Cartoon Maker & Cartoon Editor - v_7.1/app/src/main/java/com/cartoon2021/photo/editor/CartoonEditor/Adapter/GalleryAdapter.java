package com.cartoon2021.photo.editor.CartoonEditor.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.cartoon2021.photo.editor.GlobalActivities.ImageActivity;
import com.cartoon2021.photo.editor.GlobalActivities.AlbumActivity;
import com.cartoon2021.photo.editor.R;

import org.jetbrains.annotations.NotNull;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public AlbumActivity activity;
    OnDeleteClickListener onDeleteClickListener;

    ArrayList<String> imageGallery = new ArrayList<>();
    SparseBooleanArray mSparseBooleanArray;

    public interface OnDeleteClickListener {
        void onDeleteClick(ArrayList<String> imageGallery, int position);
    }

    public GalleryAdapter(AlbumActivity activity, OnDeleteClickListener onDeleteClickListener, ArrayList<String> arrayList) {
        this.activity = activity;
        this.onDeleteClickListener = onDeleteClickListener;

//        if (AppUtility.isNetworkAvailable(activity)) {
//            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
//                if (imageGallery.size() != 0) {
//                    int counter = Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));
//
//                    for (int i = 0; i < arrayList.size(); i++) {
//                        int k = i % counter;
//
//                        if ((k == 0)) {
//                            arrayList.add(i, null);
//                        }
//                    }
//                }
//            }
//        }

        this.imageGallery = arrayList;
        this.mSparseBooleanArray = new SparseBooleanArray(this.imageGallery.size());
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_gallery, viewGroup, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder myViewHolder, @SuppressLint("RecyclerView") int position) {

//        if (AppUtility.isNetworkAvailable(activity)) {
//            if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
//                if (imageGallery.size() != 0) {
//                    int k = position % Integer.parseInt(AppUtility.getString(activity, Constant.NATIVE_COUNTER, "5"));
//
//                    if ((k == 0)) {
//                        myViewHolder.itemView.findViewById(R.id.main_content).setVisibility(View.GONE);
//                        myViewHolder.itemView.findViewById(R.id.ad_native).setVisibility(View.VISIBLE);
//
//                        AdUtils.setUpGoogleNativeInRecyclerView(activity, myViewHolder.itemView.findViewById(R.id.ad_native));
//                    } else {
//                        myViewHolder.itemView.findViewById(R.id.main_content).setVisibility(View.VISIBLE);
//                        myViewHolder.itemView.findViewById(R.id.ad_native).setVisibility(View.GONE);
//
//                        bindData(myViewHolder, position);
//                    }
//                }
//            } else {
//                bindData(myViewHolder, position);
//            }
//        }
        bindData(myViewHolder, position);
    }

    private void bindData(RecyclerView.ViewHolder myViewHolder, int position) {

        ViewHolder viewHolderInfo = (ViewHolder) myViewHolder;

        String path = imageGallery.get(position);
        String img_name = path.substring(path.lastIndexOf("/") + 1);
        viewHolderInfo.img_path.setText(img_name);
        viewHolderInfo.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(activity, ImageActivity.class);
                imgIntent.putExtra("path", imageGallery.get(position));
                activity.startActivity(imgIntent);
            }
        });

        viewHolderInfo.img_delete.setOnClickListener(v -> {
            onDeleteClickListener.onDeleteClick(imageGallery, position);
        });

        Glide.with(activity).load(imageGallery.get(position)).into(viewHolderInfo.imgIcon);
        System.gc();
    }

    private void removeItem(ArrayList<String> imagegallary, int i) {

    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public int getItemCount() {
        return imageGallery.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgIcon;
        private final ImageView img_delete;
        private final TextView img_path;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            img_path = itemView.findViewById(R.id.img_path);
        }
    }
}
