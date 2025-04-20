package com.xbeat.videostatus.statusmaker.Models;

import com.google.gson.annotations.SerializedName;

public class ModelVideoListDataByCategory {
    @SerializedName("data")
    private ModelVideoRefactorByCategory Data;
    @SerializedName("status")
    private boolean Status;
    @SerializedName("message")
    private String message;

    public ModelVideoRefactorByCategory getData() {
        return this.Data;
    }

    public boolean getStatus() {
        return this.Status;
    }

    public String getMessage() {
        return this.message;
    }
}
