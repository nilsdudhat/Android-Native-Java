package com.udemy.chat.app.mvvm;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udemy.chat.app.activities.GroupsActivity;
import com.udemy.chat.app.models.ChatGroup;
import com.udemy.chat.app.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * it acts as bridge between the ViewModel and Data Source
 */
public class Repository {

    Context context;
    FirebaseAuth firebaseAuth;
    MutableLiveData<List<ChatGroup>> chatGroupLiveData;
    MutableLiveData<List<ChatMessage>> chatMessageLiveData;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference chatReference;

    public Repository(Context context) {
        this.context = context;
        chatGroupLiveData = new MutableLiveData<>();
        chatMessageLiveData = new MutableLiveData<>();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(); // the root reference
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageLiveData(String groupName) {
        chatReference = firebaseDatabase.getReference(groupName);

        List<ChatMessage> list = new ArrayList<>();

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    list.add(chatMessage);
                }

                chatMessageLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatMessageLiveData;
    }

    public MutableLiveData<List<ChatGroup>> getChatGroupLiveData() {
        List<ChatGroup> list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatGroup chatGroup = new ChatGroup(dataSnapshot.getKey());
                    list.add(chatGroup);
                }

                chatGroupLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatGroupLiveData;
    }

    public void firebaseAnonymousAuthentication() {
        firebaseAuth
                .signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // authentication is successful
                            Intent intent = new Intent(context, GroupsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // get firebase user id
    public String getUserID() {
        return firebaseAuth.getUid();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public void createNewGroup(String groupName) {
        databaseReference.child(groupName).setValue("");
    }

    public void sendChatMessage(String message, String groupName) {
        DatabaseReference chatReference = firebaseDatabase.getReference(groupName);

        ChatMessage chatMessage = new ChatMessage(
                getUserID(),
                message,
                System.currentTimeMillis()
        );

        String randomKey = chatReference.push().getKey();
        if (randomKey != null) {
            chatReference.child(randomKey).setValue(chatMessage);
        }
    }
}
