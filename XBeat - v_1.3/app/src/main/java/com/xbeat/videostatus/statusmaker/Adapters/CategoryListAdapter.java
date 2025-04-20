package com.xbeat.videostatus.statusmaker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xbeat.videostatus.statusmaker.Customs.DebounceClickListener;
import com.xbeat.videostatus.statusmaker.Models.VideoCategoryData;
import com.xbeat.videostatus.statusmaker.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    public ArrayList<VideoCategoryData> categoryList;
    CategorySelectListener categorySelectListener;
    Context context;

    public interface CategorySelectListener {
        void onCategorySelectListener(VideoCategoryData videoCategoryData);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutMain;
        TextView text_category_title;
        ImageView img_category;
        RelativeLayout rl_animation_view;

        public MyViewHolder(View view) {
            super(view);

            this.text_category_title = (TextView) view.findViewById(R.id.txt_category_name);
            this.layoutMain = (RelativeLayout) view.findViewById(R.id.layout_main);
            img_category = view.findViewById(R.id.img_category);
            rl_animation_view = view.findViewById(R.id.rl_animation_view);
        }
    }

    @SuppressLint("LongLogTag")
    public CategoryListAdapter(Context context2, ArrayList<VideoCategoryData> arrayList, CategorySelectListener categorySelectListener2) {
        this.context = context2;
        this.categoryList = arrayList;
        this.categorySelectListener = categorySelectListener2;
        Log.d("categoryarraylistlength", String.valueOf(categoryList.size()));
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_layout_category_list, viewGroup, false));
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        VideoCategoryData videoCategoryData = categoryList.get(i);

        final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.wobble);
        myViewHolder.rl_animation_view.setAnimation(animShake);

        myViewHolder.text_category_title.setText(videoCategoryData.getName());
        Glide.with(context).asBitmap().load(videoCategoryData.getImageUrl()).into(myViewHolder.img_category);

        myViewHolder.layoutMain.setOnClickListener(new DebounceClickListener(2000) {
            @Override
            public void onDebouncedClick(View v) {
                categorySelectListener.onCategorySelectListener(categoryList.get(myViewHolder.getAdapterPosition()));
            }
        });
    }

    public int getItemCount() {
        return this.categoryList.size();
    }
}
