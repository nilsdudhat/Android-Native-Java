package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models;

import java.util.Vector;

public class PhoneAlbumModel {

    private Vector<PhonePhotoModel> albumPhotos;
    private String coverUri;
    private int id;
    private String name;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getCoverUri() {
        return this.coverUri;
    }

    public void setCoverUri(String str) {
        this.coverUri = str;
    }

    public Vector<PhonePhotoModel> getAlbumPhotos() {
        if (this.albumPhotos == null) {
            this.albumPhotos = new Vector<>();
        }
        return this.albumPhotos;
    }

    public void setAlbumPhotos(Vector<PhonePhotoModel> vector) {
        this.albumPhotos = vector;
    }
}
