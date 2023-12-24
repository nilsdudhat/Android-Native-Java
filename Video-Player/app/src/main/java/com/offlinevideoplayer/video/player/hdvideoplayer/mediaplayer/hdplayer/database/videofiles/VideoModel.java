package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "videos_table")
public class VideoModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String duration;

    private String path;

    private String parentName;

    private String date;

    private String resolution;

    private String size;

    private boolean isChecked = false;

    public VideoModel() {
    }

    public VideoModel(String title, String duration, String path, String parentName, String date, String resolution, String size) {
        this.title = title;
        this.path = path;
        this.parentName = parentName;
        this.duration = duration;
        this.date = date;

        // nilesh

        this.resolution = resolution;
        this.size = size;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
