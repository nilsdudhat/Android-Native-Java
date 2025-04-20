package com.cartoon2021.photo.editor.CartoonEditor.TextDemo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cartoon2021.photo.editor.R;
import java.util.ArrayList;

public class FontList_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Typeface> stickers;
    TextView txtFont;

    public FontList_Adapter(Context context2, ArrayList<Typeface> arrayList) {
        this.context = context2;
        this.stickers = arrayList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public long getItemId(int i) {
        return i;
    }

    public int getCount() {
        return this.stickers.size();
    }

    public Object getItem(int i) {
        return this.stickers.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.inflater.inflate(R.layout.item_font_collage, null);
        }
        this.txtFont = view.findViewById(R.id.menu_item_title);
        this.txtFont.setTypeface(this.stickers.get(i));
        System.gc();
        return view;
    }
}
