package com.cartoon2021.photo.editor.CartoonEditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.cartoon2021.photo.editor.R;

public class StickerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> stiker;

    public long getItemId(int i) {
        return (long) i;
    }

    public StickerAdapter(Context context2, ArrayList<Integer> arrayList) {
        this.context = context2;
        this.stiker = arrayList;
    }

    public int getCount() {
        return this.stiker.size();
    }

    public Object getItem(int i) {
        return this.stiker.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_sticker, viewGroup, false);
        }
        Glide.with(this.context).load(this.stiker.get(i)).into((ImageView) view.findViewById(R.id.iv_list_stker));
        return view;
    }
}
