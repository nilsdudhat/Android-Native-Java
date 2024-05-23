package com.udemy.phonebook.app.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.udemy.phonebook.app.BR;

public class User extends BaseObservable {

    @Bindable
    String userName;
    @Bindable
    int phoneNumber;
    @Bindable
    String groupUser;

    // when dealing with Firebase, always create no-arguments constructor
    public User() {
    }

    public User(String userName, int phoneNumber, String groupUser) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.groupUser = groupUser;
        notifyPropertyChanged(BR._all);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    public String getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(String groupUser) {
        this.groupUser = groupUser;
        notifyPropertyChanged(BR.groupUser);
    }
}