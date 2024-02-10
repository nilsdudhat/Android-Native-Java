package com.udemy.planet.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlanetAdapter extends ArrayAdapter<PlanetModel> {
    // Custom Layouts --> PlanetAdapter
    // Custom Objects --> ArrayAdapter<PlanetModel>

    // Constructor
    public PlanetAdapter(@NonNull Context context, ArrayList<PlanetModel> planetModelArrayList) {
        super(context, R.layout.adapter_planet, planetModelArrayList);
    }

    // getView() --> used to create and return a view for specific item in a list
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the object of model for the current position
        PlanetModel planetModel = getItem(position);

        if (planetModel != null) {
            // inflate the layout
            MyViewHolder viewHolder;
            View view;
            if (convertView == null) {
                viewHolder = new MyViewHolder();
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.adapter_planet, parent, false);

                viewHolder.img_planet = convertView.findViewById(R.id.img_planet);
                viewHolder.txt_planet_name = convertView.findViewById(R.id.txt_planet_name);
                viewHolder.txt_number_moons = convertView.findViewById(R.id.txt_number_moons);

                convertView.setTag(viewHolder);
            } else {
                // the view is recycled
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            view = convertView;

            viewHolder.img_planet.setImageResource(planetModel.getPlanetImage());
            viewHolder.txt_planet_name.setText(planetModel.getPlanetName());
            viewHolder.txt_number_moons.setText(new StringBuilder().append(planetModel.getMoonsCount()).append(" Moons"));

            return view;
        }
        return super.getView(position, convertView, parent);
    }

    // ViewHolder Class --> used to cache reference to the views within an item layout,
    //                      so that they don't need to be repeatedly looked up during scrolling,
    //                      without ViewHolder the adapter will have to find view by it's id everytime,
    //                      to access the view within the layout,
    //                      which can be expensive in terms of performance
    private static class MyViewHolder {
        TextView txt_planet_name;
        TextView txt_number_moons;
        ImageView img_planet;
    }
}
