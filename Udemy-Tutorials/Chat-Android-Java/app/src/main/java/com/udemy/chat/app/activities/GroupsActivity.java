package com.udemy.chat.app.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udemy.chat.app.R;
import com.udemy.chat.app.adapters.GroupAdapter;
import com.udemy.chat.app.databinding.ActivityGroupsBinding;
import com.udemy.chat.app.databinding.DialogGroupNameBinding;
import com.udemy.chat.app.models.ChatGroup;
import com.udemy.chat.app.mvvm.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    ActivityGroupsBinding binding;

    List<ChatGroup> chatGroupList = new ArrayList<>();

    GroupAdapter groupAdapter;

    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(GroupsActivity.this).get(MyViewModel.class);
        viewModel.getGroupList().observe(GroupsActivity.this, chatGroups -> {
            chatGroupList = new ArrayList<>(chatGroups);

            setUpRecyclerView();
        });

        binding.btnAdd.setOnClickListener(v -> showCreateGroupNameDialog());
    }

    private void showCreateGroupNameDialog() {
        Dialog dialog = new Dialog(GroupsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        DialogGroupNameBinding dialogBinding = DialogGroupNameBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.btnCreateGroup.setOnClickListener(v -> {
            dialog.dismiss();

            String groupName = dialogBinding.edtGroupName.getText().toString();
            if (TextUtils.isEmpty(groupName)) {
                dialogBinding.edtGroupName.setError("Please enter Group Name");
                return;
            }

            viewModel.createNewGroup(groupName);
        });
    }

    private void setUpRecyclerView() {
        if (binding.rvChatGroups.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(GroupsActivity.this, LinearLayoutManager.VERTICAL, false);
            binding.rvChatGroups.setLayoutManager(layoutManager);
        }

        if (groupAdapter == null) {
            groupAdapter = new GroupAdapter();
            binding.rvChatGroups.setAdapter(groupAdapter);
        }

        groupAdapter.setChatGroupList(chatGroupList);
    }
}