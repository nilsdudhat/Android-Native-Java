package com.udemy.recyclerview.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.recyclerview.app.databinding.ItemSingleBinding;
import com.udemy.recyclerview.app.models.SingleModel;

import java.util.ArrayList;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.ViewHolder> {

    public interface SingleItemClickListener {
        void onSingleItemClick(int position);
    }

    ArrayList<SingleModel> singleModelArrayList = new ArrayList<>();
    SingleItemClickListener singleItemClickListener;

    public SingleAdapter(SingleItemClickListener singleItemClickListener) {
        this.singleItemClickListener = singleItemClickListener;
    }

    @NonNull
    @Override
    public SingleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSingleBinding binding = ItemSingleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleAdapter.ViewHolder holder, int position) {
        SingleModel singleModel = singleModelArrayList.get(position);
        holder.binding.txtName.setText(singleModel.getName());
        holder.binding.check.setChecked(singleModel.isChecked());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleItemClickListener.onSingleItemClick(holder.getAdapterPosition());
            }
        });

        holder.binding.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleItemClickListener.onSingleItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleModelArrayList.size();
    }

    public void setSingleModelArrayList(ArrayList<SingleModel> singleModelArrayList) {
        this.singleModelArrayList = singleModelArrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemSingleBinding binding;

        public ViewHolder(@NonNull ItemSingleBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
