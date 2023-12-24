package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.kabouzeid.appthemehelper.util.ATHUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.palette.BitmapPaletteTarget;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.palette.BitmapPaletteWrapper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlayerColorUtil;

public abstract class ColoredTarget extends BitmapPaletteTarget {
    public ColoredTarget(ImageView view) {
        super(view);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        onColorReady(getDefaultFooterColor());
    }

    @Override
    public void onResourceReady(BitmapPaletteWrapper resource, GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);
        onColorReady(PlayerColorUtil.getColor(resource.getPalette(), getDefaultFooterColor()));
    }

    protected int getDefaultFooterColor() {
        return ATHUtil.resolveColor(getView().getContext(), R.attr.defaultFooterColor);
    }

    protected int getAlbumArtistFooterColor() {
        return ATHUtil.resolveColor(getView().getContext(), com.kabouzeid.appthemehelper.R.attr.cardBackgroundColor);
    }

    public abstract void onColorReady(int color);
}
