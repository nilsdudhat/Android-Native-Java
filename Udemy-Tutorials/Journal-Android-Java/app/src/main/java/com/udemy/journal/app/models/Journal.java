package com.udemy.journal.app.models;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.udemy.journal.app.BR;

public class Journal extends BaseObservable {

    @Bindable
    String title;
    @Bindable
    String thoughts;
    @Bindable
    String imageUrl;
    @Bindable
    String userID;
    @Bindable
    Timestamp timeAdded;
    @Bindable
    String userName;

    public Journal() {
    }

    public Journal(String title, String thoughts, String imageUrl, String userID, Timestamp timeAdded, String userName) {
        this.title = title;
        this.thoughts = thoughts;
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
        notifyPropertyChanged(BR.thoughts);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view)
                .load(imageUrl)
                .fitCenter()
                .into(view);
    }

    @BindingAdapter({"timeAdded"})
    public static void displayTimeAdded(TextView textView, Timestamp timeAdded) {
        String timeDifference = String.valueOf(DateUtils.getRelativeTimeSpanString(timeAdded.getSeconds() * 1000));
        textView.setText(timeDifference);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
