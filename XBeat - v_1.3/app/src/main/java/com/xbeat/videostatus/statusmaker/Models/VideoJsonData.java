package com.xbeat.videostatus.statusmaker.Models;

import org.json.JSONArray;

public class VideoJsonData {

    String Name;
    JSONArray PostFix;
    JSONArray PreFix;
    String VideoPath;

    public VideoJsonData(String str, String str2, JSONArray jSONArray, JSONArray jSONArray2) {
        this.Name = str;
        this.VideoPath = str2;
        this.PreFix = jSONArray;
        this.PostFix = jSONArray2;
    }

    public JSONArray getPostFix() {
        return this.PostFix;
    }

    public JSONArray getPreFix() {
        return this.PreFix;
    }

    public String getVideoPath() {
        return this.VideoPath;
    }
}
