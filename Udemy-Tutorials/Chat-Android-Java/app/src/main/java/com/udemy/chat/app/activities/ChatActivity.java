package com.udemy.chat.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udemy.chat.app.R;
import com.udemy.chat.app.adapters.ChatAdapter;
import com.udemy.chat.app.databinding.ActivityChatBinding;
import com.udemy.chat.app.models.ChatMessage;
import com.udemy.chat.app.mvvm.MyViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ChatAdapter chatAdapter;
    List<ChatMessage> chatMessageList = new ArrayList<>();

    String groupName;

    MyViewModel myViewModel;

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnSend.setOnClickListener(v -> {
            myViewModel.sendChatMessage(binding.edtMessage.getText().toString(), groupName);
            binding.edtMessage.getText().clear();
        });

        getChatMessages();
    }

    private void getChatMessages() {
        groupName = getIntent().getStringExtra("group_name");

        myViewModel = new ViewModelProvider(ChatActivity.this).get(MyViewModel.class);

        myViewModel.getChatList(groupName).observe(ChatActivity.this, chatMessages -> {
            chatMessageList = new ArrayList<>(chatMessages);
            setUpRecyclerView();
        });
    }

    private void setUpRecyclerView() {
        if (binding.rvMessages.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, true);
            binding.rvMessages.setLayoutManager(layoutManager);
        }
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(ChatActivity.this);
            binding.rvMessages.setAdapter(chatAdapter);
        }
        Collections.reverse(chatMessageList);
        chatAdapter.setChatMessageList(chatMessageList);
    }
}