package com.xbeat.videostatus.statusmaker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelVideoRefactorByCategory {
    @SerializedName("templates")
    private ArrayList<ModelVideoList> TemplatesList;

    public ArrayList<ModelVideoList> getTemplatesList() {
        return this.TemplatesList;
    }
}
