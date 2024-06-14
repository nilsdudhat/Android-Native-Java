package com.udemy.chat.app.mvvm;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.udemy.chat.app.models.ChatGroup;
import com.udemy.chat.app.models.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void signUpAnonymousUser() {
        repository.firebaseAnonymousAuthentication();
    }

    public String getCurrentUserID() {
        return repository.getUserID();
    }

    public void signOut() {
        repository.signOut();
    }

    public MutableLiveData<List<ChatGroup>> getGroupList() {
        return repository.getChatGroupLiveData();
    }

    public MutableLiveData<List<ChatMessage>> getChatList(String groupName) {
        return repository.getChatMessageLiveData(groupName);
    }

    public void createNewGroup(String groupName) {
        repository.createNewGroup(groupName);
    }

    public void sendChatMessage(String message, String groupName) {
        if ((message == null) || TextUtils.isEmpty(message.trim())) {
            Toast.makeText(repository.context, "Please enter message first", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.sendChatMessage(message, groupName);
    }
}
