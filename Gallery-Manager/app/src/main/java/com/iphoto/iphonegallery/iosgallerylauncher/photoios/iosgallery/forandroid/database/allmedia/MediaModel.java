package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "all_media_table")
public class MediaModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    String fileId;
    String path;
    String dateModified;
    String fileFormat;
    String duration;
    String size;

    public MediaModel(String fileId, String path, String dateModified, String fileFormat, String duration, String size) {
        this.fileId = fileId;
        this.path = path;
        this.dateModified = dateModified;
        this.fileFormat = fileFormat;
        this.duration = duration;
        this.size = size;
    }

    protected MediaModel(Parcel in) {
        id = in.readInt();
        fileId = in.readString();
        path = in.readString();
        dateModified = in.readString();
        fileFormat = in.readString();
        duration = in.readString();
        size = in.readString();
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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
        parcel.writeInt(id);
        parcel.writeString(fileId);
        parcel.writeString(path);
        parcel.writeString(dateModified);
        parcel.writeString(fileFormat);
        parcel.writeString(duration);
        parcel.writeString(size);
    }
}
