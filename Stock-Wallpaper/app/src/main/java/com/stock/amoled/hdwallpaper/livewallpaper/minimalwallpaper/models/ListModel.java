package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ListModel implements Serializable {

    int position = 0;
    ArrayList<Wallpaper.Detail> wallpaperList = new ArrayList<>();

    public ListModel(int position, ArrayList<Wallpaper.Detail> wallpaperList) {
        this.position = position;
        this.wallpaperList = wallpaperList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Wallpaper.Detail> getWallpaperList() {
        return wallpaperList;
    }

    public void setWallpaperList(ArrayList<Wallpaper.Detail> wallpaperList) {
        this.wallpaperList = wallpaperList;
    }
}
