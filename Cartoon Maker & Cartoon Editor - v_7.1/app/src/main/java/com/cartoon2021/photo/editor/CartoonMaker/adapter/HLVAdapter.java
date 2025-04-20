package com.cartoon2021.photo.editor.CartoonMaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cartoon2021.photo.editor.R;

import java.util.ArrayList;

public class HLVAdapter extends BaseAdapter {
    ArrayList<Integer> alImage;
    private Context context;
    private LayoutInflater mInflater;

    public long getItemId(int i) {
        return (long) i;
    }

    public HLVAdapter(Context context2, ArrayList<Integer> arrayList) {
        this.mInflater = LayoutInflater.from(context2);
        this.context = context2;
        this.alImage = arrayList;
    }

    public int getCount() {
        return this.alImage.size();
    }

    public Object getItem(int i) {
        return this.alImage.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.hlv_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imgThumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imgThumbnail.setImageResource(this.alImage.get(i));
        return view;
    }

    private static class ViewHolder {
        public ImageView imgThumbnail;

        private ViewHolder() {
        }
    }
}
