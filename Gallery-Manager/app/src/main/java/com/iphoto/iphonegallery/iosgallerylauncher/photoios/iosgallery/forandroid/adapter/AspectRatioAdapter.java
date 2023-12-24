package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.RatioModel;
import com.steelkiwi.cropiwa.AspectRatio;

import java.util.Arrays;
import java.util.List;

public class AspectRatioAdapter extends RecyclerView.Adapter<AspectRatioAdapter.ViewHolder> {

    public int lastSelectedView;
    public OnNewSelectedListener listener;
    public List<RatioModel> ratios;
    public RatioModel selectedRatio;
    Activity activity;

    public AspectRatioAdapter(Activity activity) {
        this.activity = activity;

        List<RatioModel> asList = Arrays.asList(
                new RatioModel(10, 10, R.drawable.ic_crop_free, "Free"),
                new RatioModel(5, 4, R.drawable.ic_crop_free, "5:4"),
                new RatioModel(1, 1, R.drawable.ic_instagram_4_5, "1:1"),
                new RatioModel(4, 3, R.drawable.ic_crop_free, "4:3"),
                new RatioModel(4, 5, R.drawable.ic_instagram_4_5, "4:5"),
                new RatioModel(1, 2, R.drawable.ic_crop_free, "1:2"),
                new RatioModel(9, 16, R.drawable.ic_instagram_4_5, "Story"),
                new RatioModel(16, 7, R.drawable.ic_movie, "Movie"),
                new RatioModel(2, 3, R.drawable.ic_crop_free, "2:3"),
                new RatioModel(4, 3, R.drawable.ic_fb_cover, "Post"),
                new RatioModel(16, 6, R.drawable.ic_fb_cover, "Cover"),
                new RatioModel(16, 9, R.drawable.ic_crop_free, "16:9"),
                new RatioModel(3, 2, R.drawable.ic_crop_free, "3:2"),
                new RatioModel(2, 3, R.drawable.ic_pinterest, "Post"),
                new RatioModel(16, 9, R.drawable.ic_crop_youtube, "Cover"),
                new RatioModel(9, 16, R.drawable.ic_crop_free, "9:16"),
                new RatioModel(3, 4, R.drawable.ic_crop_free, "3:4"),
                new RatioModel(16, 8, R.drawable.ic_crop_post_twitter, "Post"),
                new RatioModel(16, 5, R.drawable.ic_crop_post_twitter, "Header"),
                new RatioModel(0, 0, R.drawable.ic_circle, "Profile"),
                new RatioModel(-1, -1, R.drawable.ic_sq_circle, "Cir. Sq."),
                new RatioModel(10, 16, R.drawable.ic_crop_free, "A4"),
                new RatioModel(10, 16, R.drawable.ic_crop_free, "A5"));
        this.ratios = asList;
        this.selectedRatio = asList.get(0);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_crop, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RatioModel ratioModel = this.ratios.get(i);
        viewHolder.ratioView.setImageResource(ratioModel.getSelectedIem());
        if (i == this.lastSelectedView) {
            viewHolder.text_view_filter_name.setText(ratioModel.getName());
            viewHolder.relativeLayoutCrop.setBackgroundResource(R.drawable.background_item_selected);
            viewHolder.text_view_filter_name.setTextColor(MaterialColors.getColor(activity, R.attr.main_bg, Color.GRAY));
            viewHolder.ratioView.setColorFilter(MaterialColors.getColor(activity, R.attr.main_bg, Color.GRAY));
            return;
        }
        viewHolder.text_view_filter_name.setText(ratioModel.getName());
        viewHolder.relativeLayoutCrop.setBackgroundResource(R.drawable.background_item);
        viewHolder.text_view_filter_name.setTextColor(MaterialColors.getColor(activity, R.attr.txt_light, Color.GRAY));
        viewHolder.ratioView.setColorFilter(MaterialColors.getColor(activity, R.attr.icon_light, Color.GRAY));
    }

    public int getItemCount() {
        return this.ratios.size();
    }

    public void setListener(OnNewSelectedListener onNewSelectedListener) {
        this.listener = onNewSelectedListener;
    }

    public interface OnNewSelectedListener {
        void onNewAspectRatioSelected(AspectRatio aspectRatio);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ratioView;
        public ConstraintLayout relativeLayoutCrop;
        TextView text_view_filter_name;

        public ViewHolder(View view) {
            super(view);
            this.ratioView = (ImageView) view.findViewById(R.id.image_view_aspect_ratio);
            this.text_view_filter_name = (TextView) view.findViewById(R.id.text_view_filter_name);
            relativeLayoutCrop = (ConstraintLayout) view.findViewById(R.id.relativeLayoutCropper);
            relativeLayoutCrop.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (lastSelectedView != getBindingAdapterPosition()) {
                selectedRatio = ratios.get(getBindingAdapterPosition());
                lastSelectedView = getBindingAdapterPosition();
                if (listener != null) {
                    listener.onNewAspectRatioSelected(selectedRatio);
                }
                notifyDataSetChanged();
            }
        }
    }
}
