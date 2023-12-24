package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AddressModel implements Parcelable {

    String address;
    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    public AddressModel() {
    }

    public AddressModel(Parcel in) {
        address = in.readString();
        fileModelArrayList = in.createTypedArrayList(FileModel.CREATOR);
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        parcel.writeString(address);
        parcel.writeTypedList(fileModelArrayList);
    }
}
