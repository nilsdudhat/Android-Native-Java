package com.udemy.java.market.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.java.market.app.R;
import com.udemy.java.market.app.interfaces.ItemClickListener;
import com.udemy.java.market.app.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Item> itemList;

    ItemClickListener itemClickListener;

    public ItemAdapter(List<Item> itemList, ItemClickListener itemClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }

    /**
     * responsible for creating new view holders for your items
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new ViewHolder(view);
    }

    /**
     * binds the data from your dataset to views within the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.imgItem.setImageResource(item.getItemImg());
        holder.txtTitle.setText(item.getItemName());
        holder.txtDesc.setText(item.getItemDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(holder.itemView, holder.getAdapterPosition());
                }
            }
        });
    }

    /**
     * returns the total number of items in your dataset
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * holds references to the views within the item layout
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItem;
        TextView txtTitle, txtDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.img_item);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDesc = itemView.findViewById(R.id.txt_desc);
        }
    }
}
