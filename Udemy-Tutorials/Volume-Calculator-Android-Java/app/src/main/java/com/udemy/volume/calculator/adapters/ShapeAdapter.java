package com.udemy.volume.calculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.udemy.volume.calculator.R;
import com.udemy.volume.calculator.models.Shape;

import java.util.ArrayList;

/**
 * This class will be responsible for creating the view for each item and binding data to it
 */
public class ShapeAdapter extends ArrayAdapter<Shape> {

    public ShapeAdapter(@NonNull Context context, @NonNull ArrayList<Shape> shapeArrayList) {
        super(context, R.layout.item_grid, shapeArrayList);
    }

    /** getView method:
     * used to create and return a view for a specific item in listing
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the object of model for the current position
        Shape shapeMode = getItem(position);

        if (shapeMode != null) {
            // inflate the layout
            MyViewHolder viewHolder;
            View view;
            if (convertView == null) {
                viewHolder = new MyViewHolder();
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_grid, parent, false);

                viewHolder.imgShape = convertView.findViewById(R.id.img_shape);
                viewHolder.txtShapeName = convertView.findViewById(R.id.txt_shape_name);

                convertView.setTag(viewHolder);
            } else {
                // the view is recycled
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            view = convertView;

            viewHolder.imgShape.setImageResource(shapeMode.getImgShape());
            viewHolder.txtShapeName.setText(shapeMode.getShapeName());

            return view;
        }
        return super.getView(position, convertView, parent);
    }

    /** View Holder:
     * Used to cache references to the views within an item layout
     */
    private static class MyViewHolder {
        TextView txtShapeName;
        ImageView imgShape;
    }
}
