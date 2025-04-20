package com.xbeat.videostatus.statusmaker.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelVideoList implements Serializable {
    @SerializedName("created")
    private long Created;
    @SerializedName("height")
    private int Height = 50;
    @SerializedName("is_hot")
    private boolean IsHot;
    @SerializedName("is_new")
    private boolean IsNew;
    @SerializedName("thumb_url")
    private String ThumbUrl;
    @SerializedName("title")
    private String Title;
    @SerializedName("video_url")
    private String VideoUrl;
    @SerializedName("width")
    private int Width = 100;
    @SerializedName("zip")
    private String Zip = "";
    @SerializedName("zip_url")
    private String ZipUrl;
    @SerializedName("id")
    private int Id;

    public void setCreated(long created) {
        Created = created;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public void setHot(boolean hot) {
        IsHot = hot;
    }

    public void setNew(boolean aNew) {
        IsNew = aNew;
    }

    public void setThumbUrl(String thumbUrl) {
        ThumbUrl = thumbUrl;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public void setZipUrl(String zipUrl) {
        ZipUrl = zipUrl;
    }

    public void setId(int id) {
        Id = id;
    }

    public long getCreated() {
        return this.Created;
    }

    public int getHeight() {
        return this.Height;
    }

    public int getId() {
        return this.Id;
    }

    public String getThumbUrl() {
        return this.ThumbUrl;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getVideoUrl() {
        return this.VideoUrl;
    }

    public int getWidth() {
        return this.Width;
    }

    public String getZip() {
        return this.Zip;
    }

    public String getZipUrl() {
        return this.ZipUrl;
    }

    public boolean getIsHot() {
        return this.IsHot;
    }

    public boolean getIsNew() {
        return this.IsNew;
    }
}
