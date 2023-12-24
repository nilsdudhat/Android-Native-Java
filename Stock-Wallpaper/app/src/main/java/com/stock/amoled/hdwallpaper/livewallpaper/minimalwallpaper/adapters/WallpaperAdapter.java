package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.WallpaperClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.BitmapUtils;

import java.util.ArrayList;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.MyViewHolder> {

    Activity activity;

    ArrayList<Wallpaper.Detail> wallpaperList = new ArrayList<>();

    WallpaperClickListener wallpaperClickListener;

    public WallpaperAdapter(Activity activity, WallpaperClickListener wallpaperClickListener) {
        this.activity = activity;
        this.wallpaperClickListener = wallpaperClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_wallpaper, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        holder.container_wallpaper.getLayoutParams().height = (int) (displayMetrics.heightPixels/2.5);

        Wallpaper.Detail wallpaperModel = wallpaperList.get(holder.getAdapterPosition());

        Bitmap bitmap = null;

        if (!wallpaperModel.isFromAPI()) {
            if (wallpaperModel.isFromAssets()) { // for assets
                bitmap = BitmapUtils.getBitmapFromAsset(activity, wallpaperModel.getName());
            }
        }

        if (bitmap != null) {
            holder.img_thumbnail.setImageBitmap(bitmap);
        } else {
            Glide.with(activity).asBitmap().load(wallpaperModel.getImage()).into(holder.img_thumbnail);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // find this click in HomeFragment
                wallpaperClickListener.onWallpaperClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    /**
     * this will refresh whole adapter and refresh every item of recyclerview
     */
    public void swapWallpaperList(ArrayList<Wallpaper.Detail> wallpaperList) {
        this.wallpaperList = new ArrayList<>(wallpaperList);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container_wallpaper;
        ImageView img_thumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
            container_wallpaper = itemView.findViewById(R.id.container_wallpaper);
        }
    }
}
