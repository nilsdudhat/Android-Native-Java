package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.R;

import java.util.ArrayList;

public class ExtensionAdapter extends RecyclerView.Adapter<ExtensionAdapter.MyViewHolder> {

    Activity activity;

    ArrayList<String> extensionList = new ArrayList<>();

    int extensionIndex = 6;

    SelectExtension selectExtension;

    public interface SelectExtension {
        void onSelection(int position);
    }

    public ExtensionAdapter(Activity activity, SelectExtension selectExtension) {
        this.activity = activity;
        this.selectExtension = selectExtension;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extension, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_extension.setText(extensionList.get(holder.getAdapterPosition()));

        if (extensionIndex == holder.getAdapterPosition()) {
            holder.txt_extension.setBackgroundResource(R.drawable.bg_card_black);
            holder.txt_extension.setTextColor(ContextCompat.getColor(activity, R.color.white));
        } else {
            holder.txt_extension.setBackgroundResource(R.drawable.bg_card_white);
            holder.txt_extension.setTextColor(ContextCompat.getColor(activity, R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectExtension.onSelection(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return extensionList.size();
    }

    public void swapList(ArrayList<String> extensionList) {
        this.extensionList = new ArrayList<>(extensionList);
        notifyItemRangeChanged(0, extensionList.size());
    }

    public void swapSelectedExtension(int extensionIndex) {
        this.extensionIndex = extensionIndex;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_extension;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_extension = itemView.findViewById(R.id.txt_extension);
        }
    }
}
