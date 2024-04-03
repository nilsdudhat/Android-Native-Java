package com.udemy.contactmanager.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.contactmanager.app.R;
import com.udemy.contactmanager.app.database.Contact;
import com.udemy.contactmanager.app.databinding.ItemContactBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    List<Contact> contactList = new ArrayList<>();

    public ContactAdapter() {
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.binding.setContact(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setContactList(List<Contact> contactList) {
        int previousLength = this.contactList.size();
        this.contactList = new ArrayList<>(contactList);
        notifyItemRangeChanged(0, Math.max(contactList.size(), previousLength));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemContactBinding binding;

        public ViewHolder(@NonNull ItemContactBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
