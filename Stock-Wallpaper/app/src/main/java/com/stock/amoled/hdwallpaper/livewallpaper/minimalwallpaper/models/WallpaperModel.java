package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models;

import java.io.Serializable;

public class WallpaperModel implements Serializable {

    String id;
    String path;
    String name;
    String dateModified;
    String fileFormat;
    String duration;
    String size;
    boolean isFromAPI = false;
    boolean isFromAssets = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isFromAPI() {
        return isFromAPI;
    }

    public void setFromAPI(boolean fromAPI) {
        isFromAPI = fromAPI;
    }

    public boolean isFromAssets() {
        return isFromAssets;
    }

    public void setFromAssets(boolean fromAssets) {
        isFromAssets = fromAssets;
    }
}
