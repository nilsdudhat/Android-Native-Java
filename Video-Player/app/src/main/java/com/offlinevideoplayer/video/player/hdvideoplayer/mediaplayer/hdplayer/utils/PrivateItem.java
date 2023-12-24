package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

public class PrivateItem {

    public String oldPath;
    public String name;

    public PrivateItem(String oldPath, String name) {
        this.oldPath = oldPath;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }
}
