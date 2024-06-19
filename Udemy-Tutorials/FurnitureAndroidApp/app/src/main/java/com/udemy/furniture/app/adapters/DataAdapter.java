package com.udemy.furniture.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.furniture.app.databinding.ItemDataBinding;
import com.udemy.furniture.app.models.DataModel;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    ArrayList<DataModel> dataArrayList = new ArrayList<>();

    public DataAdapter() {
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDataBinding binding = ItemDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        DataModel dataModel = dataArrayList.get(position);

        holder.binding.imgFurniture.setImageResource(dataModel.getImage());
        holder.binding.txtTitle.setText(dataModel.getName());
        holder.binding.txtSubTitle.setText(dataModel.getVersion());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setDataArrayList(ArrayList<DataModel> dataArrayList) {
        this.dataArrayList = dataArrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDataBinding binding;

        public ViewHolder(@NonNull ItemDataBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}