package com.udemy.furniture.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.furniture.app.databinding.ItemSellingBinding;
import com.udemy.furniture.app.models.SellingModel;

import java.util.ArrayList;

public class SellingItemAdapter extends RecyclerView.Adapter<SellingItemAdapter.ViewHolder> {

    ArrayList<SellingModel> sellingModelArrayList = new ArrayList<>();

    public SellingItemAdapter() {
    }

    @NonNull
    @Override
    public SellingItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSellingBinding binding = ItemSellingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SellingItemAdapter.ViewHolder holder, int position) {
        SellingModel sellingModel = sellingModelArrayList.get(position);

        holder.binding.imgItem.setImageResource(sellingModel.getImage());
        holder.binding.txtTitle.setText(sellingModel.getTitle());
        holder.binding.txtSubTitle.setText(sellingModel.getSubTitle());
        holder.binding.txtPrice.setText(sellingModel.getPrice());
    }

    @Override
    public int getItemCount() {
        return sellingModelArrayList.size();
    }

    public void setSellingModelArrayList(ArrayList<SellingModel> sellingModelArrayList) {
        this.sellingModelArrayList = sellingModelArrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemSellingBinding binding;

        public ViewHolder(@NonNull ItemSellingBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
