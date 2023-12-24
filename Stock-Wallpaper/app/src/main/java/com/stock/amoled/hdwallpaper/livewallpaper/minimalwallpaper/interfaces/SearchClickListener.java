package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;

import java.util.ArrayList;

public interface SearchClickListener {
    void onSearchClickListener(int position, ArrayList<Wallpaper.Detail> wallpaperModelArrayList);
}
