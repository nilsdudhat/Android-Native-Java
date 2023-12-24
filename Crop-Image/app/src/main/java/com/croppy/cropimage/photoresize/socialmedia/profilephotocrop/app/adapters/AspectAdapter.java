package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;
import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.models.RatioModel;
import com.steelkiwi.cropiwa.AspectRatio;

import java.util.Arrays;
import java.util.List;

public class AspectAdapter extends RecyclerView.Adapter<AspectAdapter.ViewHolder> {
    public int lastSelectedView;
    public OnNewSelectedListener listener;
    public List<RatioModel> ratios;
    public RatioModel selectedRatio;

    public AspectAdapter() {
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

    public AspectAdapter(boolean z) {
        List<RatioModel> asList = Arrays.asList(new RatioModel(5, 4, R.drawable.ic_crop_free, "5:4"), new RatioModel(1, 1, R.drawable.ic_instagram_4_5, "1:1"), new RatioModel(4, 3, R.drawable.ic_crop_free, "4:3"), new RatioModel(4, 5, R.drawable.ic_instagram_4_5, "4:5"), new RatioModel(1, 2, R.drawable.ic_crop_free, "1:2"), new RatioModel(9, 16, R.drawable.ic_instagram_4_5, "Story"), new RatioModel(16, 7, R.drawable.ic_movie, "Movie"), new RatioModel(2, 3, R.drawable.ic_crop_free, "2:3"), new RatioModel(4, 3, R.drawable.ic_fb_cover, "Post"), new RatioModel(16, 6, R.drawable.ic_fb_cover, "Cover"), new RatioModel(16, 9, R.drawable.ic_crop_free, "16:9"), new RatioModel(3, 2, R.drawable.ic_crop_free, "3:2"), new RatioModel(2, 3, R.drawable.ic_pinterest, "Post"), new RatioModel(16, 9, R.drawable.ic_crop_youtube, "Cover"), new RatioModel(9, 16, R.drawable.ic_crop_free, "9:16"), new RatioModel(3, 4, R.drawable.ic_crop_free, "3:4"), new RatioModel(16, 8, R.drawable.ic_crop_post_twitter, "Post"), new RatioModel(16, 5, R.drawable.ic_crop_post_twitter, "Header"), new RatioModel(10, 16, R.drawable.ic_crop_free, "A4"), new RatioModel(10, 16, R.drawable.ic_crop_free, "A5"));
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
            viewHolder.text_view_filter_name.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.ratioView.setColorFilter(Color.parseColor("#FFFFFF"));
            return;
        }
        viewHolder.text_view_filter_name.setText(ratioModel.getName());
        viewHolder.relativeLayoutCrop.setBackgroundResource(R.drawable.background_item);
        viewHolder.text_view_filter_name.setTextColor(Color.parseColor("#111111"));
        viewHolder.ratioView.setColorFilter(Color.parseColor("#111111"));
    }

    public void setLastSelectedView(int i) {
        this.lastSelectedView = i;
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
            if (lastSelectedView != getAdapterPosition()) {
                selectedRatio = ratios.get(getAdapterPosition());
                lastSelectedView = getAdapterPosition();
                if (listener != null) {
                    listener.onNewAspectRatioSelected(selectedRatio);
                }
                notifyDataSetChanged();
            }
        }
    }
}
