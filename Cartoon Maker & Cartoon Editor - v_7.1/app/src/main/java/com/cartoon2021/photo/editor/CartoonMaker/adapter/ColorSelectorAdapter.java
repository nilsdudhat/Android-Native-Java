package com.cartoon2021.photo.editor.CartoonMaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cartoon2021.photo.editor.R;

public class ColorSelectorAdapter extends BaseAdapter {
    public String[] array = {"#ffffff", "#000000", "#f9e7cf", "#e6cda5", "#b48a3e", "#feeeee", "#da2d02", "#e88949", "#f5d03a", "#139d3d", "#0bb6a3", "#8fa6f6", "#c28ff6", "#794b29", "#fecc97", "#dfc181", "#b88864", "#694225", "#feb185", "#cdac69", "#a9724b", "#5e3718", "#c08e73", "#c9a760", "#925d35", "#bea181", "#c7aca1", "#e2c2b5"};
    Context context;

    public ColorSelectorAdapter(Context context2) {
        this.context = context2;
    }

    public int getCount() {
        return this.array.length;
    }

    public Object getItem(int i) {
        return this.array.length;
    }

    public long getItemId(int i) {
        return (long) this.array.length;
    }

    public int[] hex2Rgb(String str) {
        return new int[]{Integer.valueOf(str.substring(1, 3), 16), Integer.valueOf(str.substring(3, 5), 16), Integer.valueOf(str.substring(5, 7), 16)};
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.color_layout, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imagecolor);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setBackgroundColor(Color.argb(255, hex2Rgb(this.array[i])[0], hex2Rgb(this.array[i])[1], hex2Rgb(this.array[i])[2]));
        return view;
    }

    private class ViewHolder {
        ImageView imageView;

        private ViewHolder() {
        }
    }
}
