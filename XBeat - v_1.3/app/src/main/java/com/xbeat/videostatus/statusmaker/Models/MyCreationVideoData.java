package com.xbeat.videostatus.statusmaker.Models;

public class MyCreationVideoData {
    String fileName;
    String filePath;
    int height;
    int width;

    public MyCreationVideoData(String str, String str2, int i, int i2) {
        this.fileName = str;
        this.filePath = str2;
        this.height = i;
        this.width = i2;
    }

    public int getHeight() {
        return this.height;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public int getWidth() {
        return this.width;
    }
}
