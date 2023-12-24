package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressDetailModel implements Parcelable {

    String addressLine = "";
    String areaName = "";
    String cityName = "";
    String stateName = "";
    String countryName = "";
    String postalCode = "";

    public AddressDetailModel() {
    }

    public AddressDetailModel(Parcel in) {
        addressLine = in.readString();
        areaName = in.readString();
        cityName = in.readString();
        stateName = in.readString();
        countryName = in.readString();
        postalCode = in.readString();
    }

    public static final Creator<AddressDetailModel> CREATOR = new Creator<AddressDetailModel>() {
        @Override
        public AddressDetailModel createFromParcel(Parcel in) {
            return new AddressDetailModel(in);
        }

        @Override
        public AddressDetailModel[] newArray(int size) {
            return new AddressDetailModel[size];
        }
    };

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(addressLine);
        parcel.writeString(areaName);
        parcel.writeString(cityName);
        parcel.writeString(stateName);
        parcel.writeString(countryName);
        parcel.writeString(postalCode);
    }
}
