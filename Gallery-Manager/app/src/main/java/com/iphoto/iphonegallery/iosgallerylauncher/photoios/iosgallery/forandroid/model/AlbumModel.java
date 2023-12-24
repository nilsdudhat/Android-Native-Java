package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AlbumModel implements Parcelable {

    String albumName;
    ArrayList<FileModel> fileModelList = new ArrayList<>();

    public AlbumModel(String albumName, ArrayList<FileModel> fileModelList) {
        this.albumName = albumName;
        this.fileModelList = fileModelList;
    }

    protected AlbumModel(Parcel in) {
        albumName = in.readString();
        fileModelList = in.createTypedArrayList(FileModel.CREATOR);
    }

    public static final Creator<AlbumModel> CREATOR = new Creator<AlbumModel>() {
        @Override
        public AlbumModel createFromParcel(Parcel in) {
            return new AlbumModel(in);
        }

        @Override
        public AlbumModel[] newArray(int size) {
            return new AlbumModel[size];
        }
    };

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<FileModel> getFileModelList() {
        return fileModelList;
    }

    public void setFileModelList(ArrayList<FileModel> fileModelList) {
        this.fileModelList = fileModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(albumName);
        parcel.writeTypedList(fileModelList);
    }
}
