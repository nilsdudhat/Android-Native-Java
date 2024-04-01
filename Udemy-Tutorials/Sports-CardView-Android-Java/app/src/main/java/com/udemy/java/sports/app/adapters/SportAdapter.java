package com.udemy.java.sports.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.java.sports.app.R;
import com.udemy.java.sports.app.SportClickListener;
import com.udemy.java.sports.app.models.Sport;

import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {

    List<Sport> sportList;

    SportClickListener sportClickListener;

    public SportAdapter(List<Sport> sportList, SportClickListener sportClickListener) {
        this.sportList = sportList;
        this.sportClickListener = sportClickListener;
    }

    @NonNull
    @Override
    public SportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportAdapter.ViewHolder holder, int position) {
        Sport sport = sportList.get(position);

        holder.imgSport.setImageResource(sport.getSportImage());
        holder.txtTitle.setText(sport.getSportName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportClickListener.onClick(holder.itemView, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSport;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSport = itemView.findViewById(R.id.img_sport);
            txtTitle = itemView.findViewById(R.id.txt_title);
        }
    }
}
