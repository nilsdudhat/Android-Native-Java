package com.xbeat.videostatus.statusmaker.Models;

import org.json.JSONArray;

public class ImageJsonData {
    int Height;
    String ImagePath;
    String Name;
    JSONArray Postfix;
    JSONArray Prefix;
    String SelectedPath = null;
    int Width;

    public ImageJsonData(String str, int i, int i2, String str2, JSONArray jSONArray, JSONArray jSONArray2) {
        this.Name = str;
        this.Height = i2;
        this.Width = i;
        this.ImagePath = str2;
        this.Prefix = jSONArray;
        this.Postfix = jSONArray2;
    }

    public int getHeight() {
        return this.Height;
    }

    public void setSelectedPath(String str) {
        this.SelectedPath = str;
    }

    public String getImagePath() {
        return this.ImagePath;
    }

    public String getSelectedPath() {
        return this.SelectedPath;
    }

    public int getWidth() {
        return this.Width;
    }

    public JSONArray getPostfix() {
        return this.Postfix;
    }

    public JSONArray getPrefix() {
        return this.Prefix;
    }
}
