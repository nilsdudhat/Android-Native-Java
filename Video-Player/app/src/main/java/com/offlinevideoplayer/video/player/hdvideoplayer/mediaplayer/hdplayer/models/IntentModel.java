package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.models;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;

import java.io.Serializable;
import java.util.ArrayList;

public class IntentModel implements Serializable {

    private int position;

    private ArrayList<VideoModel> videoModelList;

    public IntentModel(int position, ArrayList<VideoModel> videoModelList) {
        this.position = position;
        this.videoModelList = videoModelList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<VideoModel> getVideoModelList() {
        return videoModelList;
    }

    public void setVideoModelList(ArrayList<VideoModel> videoModelList) {
        this.videoModelList = videoModelList;
    }
}
