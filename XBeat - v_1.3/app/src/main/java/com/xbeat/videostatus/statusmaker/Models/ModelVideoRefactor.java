package com.xbeat.videostatus.statusmaker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelVideoRefactor {
    @SerializedName("categories")
    private ArrayList<VideoCategoryData> CategoriesList;
    @SerializedName("templates")
    private ArrayList<ModelVideoList> TemplatesList;

    public ArrayList<VideoCategoryData> getCategoriesList() {
        return this.CategoriesList;
    }

    public ArrayList<ModelVideoList> getTemplatesList() {
        return this.TemplatesList;
    }
}
