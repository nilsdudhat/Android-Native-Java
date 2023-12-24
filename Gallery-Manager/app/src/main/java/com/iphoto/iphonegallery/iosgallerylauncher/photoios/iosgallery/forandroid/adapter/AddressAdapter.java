package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.PlaceClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    Activity activity;
    PlaceClickListener placeClickListener;

    List<Map.Entry<String, AddressModel>> addressMapList;

    public AddressAdapter(Activity activity, PlaceClickListener placeClickListener) {
        this.activity = activity;
        this.placeClickListener = placeClickListener;
        this.addressMapList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_place, parent, false);
        return new MyViewHolder(Objects.requireNonNull(view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            if (activity instanceof MainActivity) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                holder.constraint_main.getLayoutParams().width = (int) (displayMetrics.widthPixels/2.5);
            }

            holder.txt_city_name.setText(addressMapList.get(holder.getBindingAdapterPosition()).getKey());
            holder.txt_city_size.setText(String.valueOf(addressMapList.get(holder.getBindingAdapterPosition()).getValue().getFileModelArrayList().size()));

            Glide.with(activity)
                    .load((new File((addressMapList.get(holder.getBindingAdapterPosition()).getValue().getFileModelArrayList().get(0).getPath())).getAbsoluteFile()))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.img_city);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeClickListener.onPlaceClick(addressMapList.get(holder.getBindingAdapterPosition()).getKey(), addressMapList.get(holder.getBindingAdapterPosition()).getValue().getFileModelArrayList());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (activity instanceof MainActivity) {
            if (addressMapList.size() > 6) {
                return 6;
            }
        }
        return addressMapList.size();
    }

    public void swapList(HashMap<String, AddressModel> addressHashMap) {
        List<Map.Entry<String, AddressModel>> tempAddressMapList = new LinkedList<>(addressHashMap.entrySet());

        Collections.sort(tempAddressMapList, new Comparator<Map.Entry<String, AddressModel>>() {
            @Override
            public int compare(Map.Entry<String, AddressModel> lhs, Map.Entry<String, AddressModel> rhs) {
                Integer size1 = lhs.getValue().getFileModelArrayList().size();
                Integer size2 = rhs.getValue().getFileModelArrayList().size();

                return size2.compareTo(size1);
            }
        });

        this.addressMapList = new LinkedList<>(tempAddressMapList);
        notifyItemRangeChanged(0, tempAddressMapList.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_city;
        TextView txt_city_name;
        TextView txt_city_size;
        ConstraintLayout constraint_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_city = itemView.findViewById(R.id.img_city);
            txt_city_name = itemView.findViewById(R.id.txt_city_name);
            txt_city_size = itemView.findViewById(R.id.txt_city_size);
            constraint_main = itemView.findViewById(R.id.constraint_main);
        }
    }
}
