package com.udemy.journal.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.journal.app.databinding.ItemJournalBinding;
import com.udemy.journal.app.models.Journal;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    Context context;
    List<Journal> journalList = new ArrayList<>();

    public JournalAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public JournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemJournalBinding binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.ViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.binding.setJournal(journal);
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public void setJournalList(List<Journal> journalList) {
        this.journalList = new ArrayList<>(journalList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemJournalBinding binding;

        public ViewHolder(@NonNull ItemJournalBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}