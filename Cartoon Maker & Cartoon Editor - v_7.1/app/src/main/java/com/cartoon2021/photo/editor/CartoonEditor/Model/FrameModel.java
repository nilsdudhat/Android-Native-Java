package com.cartoon2021.photo.editor.CartoonEditor.Model;

public class FrameModel {
    public int frame;
    public int tsu;

    public FrameModel(int i, int i2) {
        this.tsu = i;
        this.frame = i2;
    }

    public int getTsu() {
        return this.tsu;
    }

    public void setTsu(int i) {
        this.tsu = i;
    }

    public int getFrame() {
        return this.frame;
    }

    public void setFrame(int i) {
        this.frame = i;
    }
}
