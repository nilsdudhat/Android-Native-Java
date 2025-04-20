package com.cartoon2021.photo.editor.CartoonMaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cartoon2021.photo.editor.R;

public class ImojiPartAdapter extends BaseAdapter {
    public static int defaultFree = 500;
    int[] arraydata;
    Context context;
    public int selectedPosition = -1;

    public long getItemId(int i) {
        return (long) i;
    }

    public ImojiPartAdapter(Context context2, int[] iArr) {
        this.context = context2;
        this.arraydata = iArr;
    }

    public int getCount() {
        return this.arraydata.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = layoutInflater.inflate(R.layout.grid_single, viewGroup, false);
            viewHolder.imageView = (ImageView) view2.findViewById(R.id.grid_image);
            viewHolder.lockedImageView = (ImageView) view2.findViewById(R.id.lockedImageView);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setImageResource(this.arraydata[i]);
        if (this.selectedPosition == i) {
            viewHolder.imageView.setBackgroundColor(Color.parseColor("#89ADD9"));
        } else {
            viewHolder.imageView.setBackgroundColor(0);
        }
        if (defaultFree < i) {
            viewHolder.lockedImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lockedImageView.setVisibility(View.GONE);
        }
        return view2;
    }

    private static class ViewHolder {
        ImageView imageView;
        ImageView lockedImageView;

        private ViewHolder() {
        }
    }
}
