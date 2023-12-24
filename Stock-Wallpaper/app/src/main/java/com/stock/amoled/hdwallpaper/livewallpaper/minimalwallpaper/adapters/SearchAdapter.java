package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.R;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.SearchClickListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.SearchListener;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities.SearchActivity;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils.BitmapUtils;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    ArrayList<Wallpaper.Detail> wallpaperModelArrayList = new ArrayList<>();
    ArrayList<Wallpaper.Detail> filteredWallpaperList = new ArrayList<>();
    SearchActivity searchActivity;
    SearchListener searchListener;
    SearchClickListener searchClickListener;

    public SearchAdapter(SearchActivity searchActivity, ArrayList<Wallpaper.Detail> wallpaperModelArrayList, SearchListener searchListener, SearchClickListener searchClickListener) {
        this.searchActivity = searchActivity;
        this.wallpaperModelArrayList = new ArrayList<>(wallpaperModelArrayList);
        this.searchListener = searchListener;
        this.searchClickListener = searchClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(searchActivity).inflate(R.layout.item_wallpaper, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        searchActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        holder.container_wallpaper.getLayoutParams().height = (int) (displayMetrics.heightPixels/2.5);

        Wallpaper.Detail wallpaperModel = filteredWallpaperList.get(holder.getAdapterPosition());

        Bitmap bitmap = null;

        if (!wallpaperModel.isFromAPI()) {
            if (wallpaperModel.isFromAssets()) { // for assets
                bitmap = BitmapUtils.getBitmapFromAsset(searchActivity, wallpaperModel.getName());
            } else { // for internal storage
                bitmap = BitmapUtils.getBitmapFromPath(wallpaperModel.getImage());
            }
        }

        if (bitmap != null) {
            holder.img_thumbnail.setImageBitmap(bitmap);
        } else {
            Glide.with(searchActivity).asBitmap().load(wallpaperModel.getImage()).into(holder.img_thumbnail);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClickListener.onSearchClickListener(holder.getAdapterPosition(), filteredWallpaperList);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (filteredWallpaperList != null) {
            return filteredWallpaperList.size();
        } else {
            return 0;
        }
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

    public Filter getFilter() {
        return new Filter() {
            public FilterResults performFiltering(CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                if (charSequence2.isEmpty()) {
                    filteredWallpaperList = wallpaperModelArrayList;
                } else {
                    ArrayList<Wallpaper.Detail> arrayList = new ArrayList<>();
                    for (Wallpaper.Detail next : wallpaperModelArrayList) {
                        if (next.getName().toLowerCase().contains(charSequence2.toLowerCase())) {
                            arrayList.add(next);
                        }
                    }
                    filteredWallpaperList = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredWallpaperList;
                return filterResults;
            }

            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredWallpaperList = (ArrayList<Wallpaper.Detail>) filterResults.values;
                searchListener.searchedItem(!filteredWallpaperList.isEmpty());
                notifyDataSetChanged();
            }
        };
    }
}
