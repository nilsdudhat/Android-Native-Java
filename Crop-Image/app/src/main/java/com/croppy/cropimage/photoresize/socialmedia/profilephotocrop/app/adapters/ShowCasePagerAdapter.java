package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;

import java.util.ArrayList;
import java.util.Objects;

public class ShowCasePagerAdapter extends PagerAdapter {

    // Context object
    Activity activity;

    // Array of images
    ArrayList<Integer> showCaseImageList = new ArrayList<>();

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    public ShowCasePagerAdapter(Activity activity, ArrayList<Integer> showCaseImageList) {
        this.activity = activity;
        this.showCaseImageList = showCaseImageList;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return showCaseImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.item_showcase_pager, container, false);

        ImageView img_showcase = itemView.findViewById(R.id.img_showcase);

        img_showcase.setImageResource(showCaseImageList.get(position));

        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
