package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.song;

import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kabouzeid.appthemehelper.ThemeStore;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.CabHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;

import java.util.List;

public class ShuffleButtonSongAdapter extends OffsetSongAdapter {

    public ShuffleButtonSongAdapter(AdsBaseActivity activity, List<Song> dataSet, @LayoutRes int itemLayoutRes, boolean usePalette, @Nullable CabHolder cabHolder) {
        super(activity, dataSet, itemLayoutRes, usePalette, cabHolder);
    }

    @Override
    protected SongAdapter.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == OFFSET_ITEM) {
            int accentColor = ThemeStore.accentColor(activity);
            if (holder.title != null) {
                holder.title.setText(activity.getResources().getString(R.string.action_shuffle_all).toUpperCase());
                holder.title.setTextColor(accentColor);
                holder.title.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            }
            if (holder.text != null) {
                holder.text.setVisibility(View.GONE);
            }
            if (holder.menu != null) {
                holder.menu.setVisibility(View.GONE);
            }
            if (holder.image != null) {
                final int padding = activity.getResources().getDimensionPixelSize(R.dimen.default_item_margin) / 2;
                holder.image.setPadding(padding, padding, padding, padding);
                holder.image.setColorFilter(accentColor);
                holder.image.setImageResource(R.drawable.ic_shuffle);
            }
            if (holder.separator != null) {
                holder.separator.setVisibility(View.VISIBLE);
            }
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.GONE);
            }
        } else {
            super.onBindViewHolder(holder, position - 1);
        }
    }

    public class ViewHolder extends OffsetSongAdapter.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (getItemViewType() == OFFSET_ITEM) {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true);
                return;
            }
            super.onClick(v);
        }
    }
}
