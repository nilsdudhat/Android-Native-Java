package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "video_folders_table")
public class VideoFolderModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String path;

    private int filesCount;

    public VideoFolderModel(String title, String path, int filesCount) {
        this.title = title;
        this.path = path;
        this.filesCount = filesCount;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }
}
