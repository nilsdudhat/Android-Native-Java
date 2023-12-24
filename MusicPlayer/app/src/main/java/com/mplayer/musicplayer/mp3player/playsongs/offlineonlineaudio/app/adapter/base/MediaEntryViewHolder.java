package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.base;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;

public class MediaEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public ImageView image;
    public TextView imageText;
    public TextView title;
    public TextView text;
    public View menu;
    public View separator;
    public View shortSeparator;
    public View dragView;
    public View paletteColorContainer;

    public MediaEntryViewHolder(View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);
        imageText = itemView.findViewById(R.id.image_text);
        title = itemView.findViewById(R.id.title);
        text = itemView.findViewById(R.id.text);
        menu = itemView.findViewById(R.id.menu);
        separator = itemView.findViewById(R.id.separator);
        shortSeparator = itemView.findViewById(R.id.short_separator);
        dragView = itemView.findViewById(R.id.drag_view);
        paletteColorContainer = itemView.findViewById(R.id.palette_color_container);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    protected void setImageTransitionName(@NonNull String transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && image != null) {
            image.setTransitionName(transitionName);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
