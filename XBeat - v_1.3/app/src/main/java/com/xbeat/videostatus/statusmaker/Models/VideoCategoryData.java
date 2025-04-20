package com.xbeat.videostatus.statusmaker.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoCategoryData implements Serializable {

    @SerializedName("image_url")
    String ImageUrl;

    @SerializedName("name")
    String Name;

    int drawableCount;

    @SerializedName("id")
    int f128Id;

    String f13704d;

    public VideoCategoryData(int id, String name, int drawableCount, String str2) {
        this.f128Id = id;
        this.Name = name;
        this.drawableCount = drawableCount;
        this.f13704d = str2;
    }

    public String mo12260a() {
        return this.f13704d;
    }

    public int getId() {
        return this.f128Id;
    }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public String getName() {
        return this.Name;
    }

    public int getDrawableCount() {
        return this.drawableCount;
    }

    public void setDrawableCount(int i) {
        this.drawableCount = i;
    }
}
