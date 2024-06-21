package com.udemy.recyclerview.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.recyclerview.app.databinding.ItemCardBinding;
import com.udemy.recyclerview.app.models.PlanetModel;

import java.util.ArrayList;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    ArrayList<PlanetModel> planetModelArrayList = new ArrayList<>();

    public CardAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanetModel planetModel = planetModelArrayList.get(position);

        holder.binding.txtName.setText(planetModel.getPlanetName());
        holder.binding.txtGravity.setText(
                String.format(Locale.US, "Gravity: %.2f N/Kg", planetModel.getGravity()));
        holder.binding.txtDistance.setText(
                String.format(Locale.US, "Distance from Sun: %d Million KM", planetModel.getDistanceFromSun()));
        holder.binding.txtDiameter.setText(
                String.format(Locale.US, "Diameter: %d KM", planetModel.getDiameter()));
    }

    @Override
    public int getItemCount() {
        return planetModelArrayList.size();
    }

    public void setPlanetModelArrayList(ArrayList<PlanetModel> planetModelArrayList) {
        this.planetModelArrayList = planetModelArrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemCardBinding binding;

        public ViewHolder(@NonNull ItemCardBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
