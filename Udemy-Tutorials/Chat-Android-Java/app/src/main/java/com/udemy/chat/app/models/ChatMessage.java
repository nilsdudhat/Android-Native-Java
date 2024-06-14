package com.udemy.chat.app.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.firebase.auth.FirebaseAuth;
import com.udemy.chat.app.BR;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.TimeZone;

public class ChatMessage extends BaseObservable {

    @Bindable
    String senderID;
    @Bindable
    String text;
    @Bindable
    long time;
    boolean isMine = false;

    public ChatMessage() {
    }

    public ChatMessage(String senderID, String text, long time) {
        this.senderID = senderID;
        this.text = text;
        this.time = time;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
        notifyPropertyChanged(BR.senderID);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    public boolean isMine() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            isMine = senderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public String convertTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Date date = new Date(getTime());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);
    }
}