package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models;

public class PhonePhotoModel {
    private String albumName;

    private int id;
    private String photoUri;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String str) {
        this.albumName = str;
    }

    public String getPhotoUri() {
        return this.photoUri;
    }

    public void setPhotoUri(String str) {
        this.photoUri = str;
    }
}
