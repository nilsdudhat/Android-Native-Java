package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ForYouModel implements Parcelable {

    String format;
    String title;
    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    public ForYouModel(String format, String title, ArrayList<FileModel> fileModelArrayList) {
        this.format = format;
        this.title = title;
        this.fileModelArrayList = new ArrayList<>(fileModelArrayList);
    }

    protected ForYouModel(Parcel in) {
        format = in.readString();
        title = in.readString();
        fileModelArrayList = in.createTypedArrayList(FileModel.CREATOR);
    }

    public static final Creator<ForYouModel> CREATOR = new Creator<ForYouModel>() {
        @Override
        public ForYouModel createFromParcel(Parcel in) {
            return new ForYouModel(in);
        }

        @Override
        public ForYouModel[] newArray(int size) {
            return new ForYouModel[size];
        }
    };

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<FileModel> getFileModelArrayList() {
        return fileModelArrayList;
    }

    public void setFileModelArrayList(ArrayList<FileModel> fileModelArrayList) {
        this.fileModelArrayList = fileModelArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(format);
        parcel.writeString(title);
        parcel.writeTypedList(fileModelArrayList);
    }
}
