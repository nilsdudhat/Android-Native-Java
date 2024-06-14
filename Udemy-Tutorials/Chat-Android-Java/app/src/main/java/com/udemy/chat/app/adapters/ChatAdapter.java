package com.udemy.chat.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.chat.app.databinding.ItemChatBinding;
import com.udemy.chat.app.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;

    List<ChatMessage> chatMessageList = new ArrayList<>();

    public ChatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = ItemChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessageList.get(position);
        holder.binding.setChatMessage(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = new ArrayList<>(chatMessageList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemChatBinding binding;

        public ViewHolder(@NonNull ItemChatBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
