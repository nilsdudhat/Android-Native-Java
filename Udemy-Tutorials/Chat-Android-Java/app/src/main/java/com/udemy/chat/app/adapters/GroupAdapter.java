package com.udemy.chat.app.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.chat.app.activities.ChatActivity;
import com.udemy.chat.app.databinding.ItemGroupBinding;
import com.udemy.chat.app.models.ChatGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    List<ChatGroup> chatGroupList = new ArrayList<>();

    public GroupAdapter() {
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupBinding binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, int position) {
        ChatGroup chatGroup = chatGroupList.get(position);
        holder.binding.setChatGroup(chatGroup);

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra("group_name", chatGroup.getGroupName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatGroupList.size();
    }

    public void setChatGroupList(List<ChatGroup> chatGroupList) {
        this.chatGroupList = new ArrayList<>(chatGroupList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemGroupBinding binding;

        public ViewHolder(@NonNull ItemGroupBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}