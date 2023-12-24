package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.AlbumClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AlbumModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    Activity activity;
    String displayType;
    AlbumClickListener albumClickListener;

    ArrayList<AlbumModel> albumList = new ArrayList<>();

    public AlbumAdapter(Activity activity, String displayType, AlbumClickListener albumClickListener) {
        this.activity = activity;
        this.displayType = displayType;
        this.albumClickListener = albumClickListener;

        albumList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_album, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            if (activity instanceof MainActivity) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                holder.constraint_main.getLayoutParams().width = (int) (displayMetrics.widthPixels/2.5);
            }

            String albumName = albumList.get(holder.getBindingAdapterPosition()).getAlbumName();
            ArrayList<FileModel> fileModelArrayList = new ArrayList<>(albumList.get(holder.getBindingAdapterPosition()).getFileModelList());

            holder.txt_album_size.setText(String.valueOf(fileModelArrayList.size()));
            holder.txt_album_name.setText(albumName);

            Log.d("--album--", "onBindViewHolder: " + albumName);

            Glide.with(activity)
                    .load(new File(fileModelArrayList.get(0).getPath()).getAbsoluteFile())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .priority(Priority.IMMEDIATE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.img_album);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    albumClickListener.onAlbumClick(albumName, fileModelArrayList);
                }
            });
        } catch (Exception e) {
            Log.d("--album_exception--", "onBindViewHolder: " + e.getMessage());
            e.printStackTrace();

            Glide.with(activity)
                    .load(R.drawable.ic_placeholder)
                    .into(holder.img_album);
        }
    }

    @Override
    public int getItemCount() {
        if (displayType.equals("AlbumFragment")) {
            return Math.min(albumList.size(), 6);
        } else {
            return albumList.size();
        }
    }

    public void swapList(String albumType, HashMap<String, ArrayList<FileModel>> albumHashMap) {
        albumList = new ArrayList<>();

        if (albumType.equals("My Albums")) {
            String[] albumNameList = {"Favorites", "Recents", "Camera", "Videos", "Download", "Screenshots"};

            for (String s : albumNameList) {
                if (s.equals("Favorites")) {
                    albumList.add(new AlbumModel(s, albumHashMap.get(s)));
                } else {
                    if (albumHashMap.containsKey(s)) {
                        if (!Objects.requireNonNull(albumHashMap.get(s)).isEmpty()) {
                            albumList.add(new AlbumModel(s, albumHashMap.get(s)));
                        }
                    }
                }
            }
        }

        if (albumType.equals("More Albums")) {
            String[] albumNameList = {"Favorites", "Recents", "Camera", "Videos", "Download", "Screenshots"};

            for (String s : albumNameList) {
                albumHashMap.remove(s);
            }

            Object[] keySet = albumHashMap.keySet().toArray();

            for (Object o : keySet) {
                String albumName = (String) o;

                if (albumHashMap.get(albumName) == null) {
                    albumList.add(new AlbumModel(albumName, new ArrayList<>()));
                } else {
                    if (!Objects.requireNonNull(albumHashMap.get(albumName)).isEmpty()) {
                        ArrayList<FileModel> fileModels = new ArrayList<>(Objects.requireNonNull(albumHashMap.get(albumName)));
                        albumList.add(new AlbumModel(albumName, fileModels));
                    }
                }
            }

            Collections.sort(albumList, new Comparator<AlbumModel>() {
                @Override
                public int compare(AlbumModel lhs, AlbumModel rhs) {
                    Integer size1 = lhs.getFileModelList().size();
                    Integer size2 = rhs.getFileModelList().size();

                    return size2.compareTo(size1);
                }
            });
        }

        notifyItemRangeChanged(0, albumList.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_album;
        TextView txt_album_name, txt_album_size;
        ConstraintLayout constraint_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            constraint_main = itemView.findViewById(R.id.constraint_main);
            img_album = itemView.findViewById(R.id.img_album);
            txt_album_size = itemView.findViewById(R.id.txt_album_size);
            txt_album_name = itemView.findViewById(R.id.txt_album_name);
        }
    }
}
