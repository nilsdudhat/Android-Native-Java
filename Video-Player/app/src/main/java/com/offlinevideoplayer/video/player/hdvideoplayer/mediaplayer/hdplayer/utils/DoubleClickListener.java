package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.view.View;

public interface DoubleClickListener {
    void onSingleClick(final View view);

    /**
     * Called when the user make a double click.
     */
    void onDoubleClick(final View view);
}
