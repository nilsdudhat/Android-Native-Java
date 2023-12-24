package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.helper.WrapperGridLayoutManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.DayClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaAdapterClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MultiSelectListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> implements DayClickListener, MediaAdapterClickListener {

    Activity activity;

    DayClickListener dayClickListener;
    MediaAdapterClickListener mediaAdapterClickListener;
    MultiSelectListener multiSelectListener;

    List<Map.Entry<String, ArrayList<FileModel>>> dayMapList;

    public DaysAdapter(Activity activity, MediaAdapterClickListener mediaAdapterClickListener, MultiSelectListener multiSelectListener) {
        this.activity = activity;
        this.mediaAdapterClickListener = mediaAdapterClickListener;
        this.multiSelectListener = multiSelectListener;
        this.dayMapList = new LinkedList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d("--day--", "day: " + dayMapList.get(holder.getBindingAdapterPosition()).getKey());

        ArrayList<FileModel> fileModelArrayList = dayMapList.get(holder.getBindingAdapterPosition()).getValue();
        FileModel fileModel = fileModelArrayList.get(0);

        List<FileModel> displayList = new ArrayList<>();

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            if (i < 4) {
                displayList.add(fileModelArrayList.get(i));
            }
        }

        holder.txt_date.setText(DateUtils.manageDateForLast7Days(DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModel.getDateModified()), "dd MMMM, yyyy"));

        WrapperGridLayoutManager gridLayoutManager;
        if (displayList.size() == 4) {
            gridLayoutManager = new WrapperGridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
            });
        } else if (displayList.size() == 3) {
            gridLayoutManager = new WrapperGridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });
        } else {
            gridLayoutManager = new WrapperGridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false);
        }

        DayMediaAdapter dayMediaAdapter = new DayMediaAdapter(activity, this);

        holder.rv_media.setLayoutManager(gridLayoutManager);
        holder.rv_media.setAdapter(dayMediaAdapter);

        dayMediaAdapter.swapList(displayList, fileModelArrayList);

        holder.frame_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectListener.onMultiSelect(fileModelArrayList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dayMapList.size();
    }

    public void swapDayHashMap(HashMap<String, ArrayList<FileModel>> dayMapList) {
        List<Map.Entry<String, ArrayList<FileModel>>> tempDayMapList = new LinkedList<>(dayMapList.entrySet());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH); // your own date format

        // Sort the list
        Collections.sort(tempDayMapList, new Comparator<Map.Entry<String, ArrayList<FileModel>>>() {
            public int compare(Map.Entry<String, ArrayList<FileModel>> o1,
                               Map.Entry<String, ArrayList<FileModel>> o2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getKey())).compareTo(simpleDateFormat.parse(o1.getKey()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        this.dayMapList = new LinkedList<>(tempDayMapList);
        notifyItemRangeChanged(0, dayMapList.size());
    }

    @Override
    public void onDayClicked(int position, ArrayList<FileModel> fileModelList) {
        dayClickListener.onDayClicked(position, fileModelList);
    }

    @Override
    public void onClick(View view, ArrayList<FileModel> fileModelArrayList, int position, String mediaType, String subMediaType) {
        mediaAdapterClickListener.onClick(view, fileModelArrayList, position, mediaType, subMediaType);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_media;
        TextView txt_date;
        FrameLayout frame_select;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rv_media = itemView.findViewById(R.id.rv_media);
            txt_date = itemView.findViewById(R.id.txt_date);
            frame_select = itemView.findViewById(R.id.frame_select);
        }
    }
}
