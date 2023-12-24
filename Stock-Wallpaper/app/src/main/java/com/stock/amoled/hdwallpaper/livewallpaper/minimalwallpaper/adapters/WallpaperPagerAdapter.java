package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.WallpaperPagerClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.Objects;

public class WallpaperPagerAdapter extends PagerAdapter {

    // Context object
    Activity activity;

    // Array of images
    ArrayList<Wallpaper.Detail> wallpaperList = new ArrayList<>();

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    // Full Screen Wallpaper Click Listener
    WallpaperPagerClickListener wallpaperPagerClickListener;

    // Viewpager Constructor
    public WallpaperPagerAdapter(Activity activity, ArrayList<Wallpaper.Detail> wallpaperList, WallpaperPagerClickListener wallpaperPagerClickListener) {
        this.activity = activity;
        this.wallpaperList = wallpaperList;
        this.wallpaperPagerClickListener = wallpaperPagerClickListener;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return wallpaperList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.item_wallpaper_pager, container, false);

        ImageView img_wallpaper = itemView.findViewById(R.id.img_wallpaper);

        Wallpaper.Detail wallpaperModel = wallpaperList.get(position);

        Bitmap bitmap = null;

        if (!wallpaperModel.isFromAPI()) {
            if (wallpaperModel.isFromAssets()) { // for assets
                bitmap = BitmapUtils.getBitmapFromAsset(activity, wallpaperModel.getName());
            } else { // for internal storage
                Log.d("--path--", "instantiateItem: " + wallpaperModel.getImage());
                bitmap = BitmapUtils.getBitmapFromPath(wallpaperModel.getImage());
            }
        }

        if (bitmap != null) {
            img_wallpaper.setImageBitmap(bitmap);
        } else {
            Glide.with(activity).asBitmap().load(wallpaperModel.getImage()).into(img_wallpaper);
        }

        img_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wallpaperPagerClickListener.onClickListener(wallpaperList.get(position));
            }
        });

        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}