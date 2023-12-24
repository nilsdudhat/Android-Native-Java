package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.models;

import java.io.Serializable;

public class FolderModel implements Serializable {

    public String bucket;
    public String data;
    public String size;
    public String bid;
    public String count;
    public String date;
    public String newTag;
    public long folderSize;


    public FolderModel() {
    }

    public FolderModel(String bucket, String data, String size, String bid) {
        this.bucket = bucket;
        this.data = data;
        this.size = size;
        this.bid = bid;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String DATE) {
        this.date = DATE;
    }

    public long getFolderSize() {
        return folderSize;
    }

    public void setFolderSize(long folderSize) {
        this.folderSize = folderSize;
    }
}
