package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer;

public class EqualizerDataList {

    String name;
    boolean selectItem;

    public EqualizerDataList(String name, boolean selectItem) {
        this.name = name;
        this.selectItem = selectItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectItem() {
        return selectItem;
    }

    public void setSelectItem(boolean selectItem) {
        this.selectItem = selectItem;
    }
}
