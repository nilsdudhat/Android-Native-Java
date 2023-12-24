package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.models;

public class ImageModel {

    String pathList;
    boolean isChecked = false;

    public ImageModel(String pathList) {
        this.pathList = pathList;
    }

    public String getPathList() {
        return pathList;
    }

    public void setPathList(String pathList) {
        this.pathList = pathList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
