package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FileModel implements Parcelable {

    String id;
    String path;
    String dateModified;
    String fileFormat;
    String duration;
    String size;

    public FileModel(String id, String path, String date_modified, String file_format, String duration, String size) {
        this.id = id;
        this.path = path;
        this.dateModified = date_modified;
        this.fileFormat = file_format;
        this.duration = duration;
        this.size = size;
    }

    protected FileModel(Parcel in) {
        id = in.readString();
        path = in.readString();
        dateModified = in.readString();
        fileFormat = in.readString();
        duration = in.readString();
        size = in.readString();
    }

    public static final Creator<FileModel> CREATOR = new Creator<FileModel>() {
        @Override
        public FileModel createFromParcel(Parcel in) {
            return new FileModel(in);
        }

        @Override
        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(path);
        parcel.writeString(dateModified);
        parcel.writeString(fileFormat);
        parcel.writeString(duration);
        parcel.writeString(size);
    }
}
