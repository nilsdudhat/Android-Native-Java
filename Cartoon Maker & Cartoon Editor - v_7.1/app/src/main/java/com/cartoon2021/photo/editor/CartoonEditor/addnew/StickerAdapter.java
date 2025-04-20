package com.cartoon2021.photo.editor.CartoonEditor.addnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import com.cartoon2021.photo.editor.R;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.SimpleViewHolder> {

    public static int[] photolab = {R.drawable.colorpainnt0, R.drawable.colorpainnt1, R.drawable.colorpainnt2, R.drawable.colorpainnt3, R.drawable.colorpainnt4, R.drawable.colorpainnt5, R.drawable.colorpainnt6, R.drawable.colorpainnt7, R.drawable.colorpainnt8, R.drawable.colorpainnt9, R.drawable.colorpainnt10, R.drawable.colorpainnt11, R.drawable.colorpainnt12, R.drawable.colorpainnt13, R.drawable.colorpainnt14, R.drawable.colorpainnt15, R.drawable.colorpainnt16, R.drawable.colorpainnt17, R.drawable.colorpainnt18, R.drawable.colorpainnt19, R.drawable.colorpainnt20};
    public Context context;
    public int[] imagedata;
    public onItemClick itemClick;
    private LayoutInflater mInflater;
    ArrayList<String> strings = new ArrayList<>();
    public int type = 1;

    public interface onItemClick {
        void onItemClick(int i, int i2);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public ImageView album_image;

        public SimpleViewHolder(View view) {
            super(view);
            this.album_image = (ImageView) view.findViewById(R.id.img_sticker);
        }
    }

    public StickerAdapter(Context context2, onItemClick onitemclick, int i) {
        this.itemClick = onitemclick;
        this.context = context2;
        this.type = i;
        if (i != 1 && i != 2) {
            if (i == 3) {
                this.imagedata = photolab;
            } else if (i == 4) {
                try {
                    this.strings = new ArrayList<>(Arrays.asList(context2.getAssets().list("effect")));
                } catch (Exception unused) {
                    unused.printStackTrace();
                }
            }
        }
    }

    @NonNull
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.mInflater = LayoutInflater.from(this.context);
        return new SimpleViewHolder(this.mInflater.inflate(R.layout.item_effects, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, @SuppressLint("RecyclerView") int i) {
        if (this.type != 4) {
            Glide.with(this.context).load(this.imagedata[i]).into(simpleViewHolder.album_image);
        } else {
            try {
                AssetManager assets = this.context.getAssets();
                simpleViewHolder.album_image.setImageDrawable(Drawable.createFromStream(assets.open("effect/" + this.strings.get(i)), (String) null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        simpleViewHolder.album_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (itemClick != null) {
                    itemClick.onItemClick(i, type);
                }
            }
        });
    }

    public int getItemCount() {
        if (this.type != 4) {
            return this.imagedata.length;
        }
        return this.strings.size();
    }
}