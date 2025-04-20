package com.cartoon2021.photo.editor.CartoonEditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.cartoon2021.photo.editor.R;
import com.cartoon2021.photo.editor.CartoonEditor.Model.FrameModel;

import java.util.ArrayList;

public class FrameAdapter extends BaseAdapter {
    ArrayList<FrameModel> arrayList;
    Context context;

    public long getItemId(int i) {
        return (long) i;
    }

    public FrameAdapter(Context context2, ArrayList<FrameModel> arrayList2) {
        this.context = context2;
        this.arrayList = arrayList2;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item, (ViewGroup) null);
        ((ImageView) inflate.findViewById(R.id.list_item)).setImageResource(this.arrayList.get(i).getTsu());
        return inflate;
    }
}
