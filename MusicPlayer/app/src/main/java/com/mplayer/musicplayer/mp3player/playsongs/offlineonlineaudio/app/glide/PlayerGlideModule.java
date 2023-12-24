package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.artistimage.ArtistImage;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.artistimage.ArtistImageLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.audiocover.AudioFileCover;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.audiocover.AudioFileCoverLoader;

import java.io.InputStream;

public class PlayerGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(AudioFileCover.class, InputStream.class, new AudioFileCoverLoader.Factory());
        glide.register(ArtistImage.class, InputStream.class, new ArtistImageLoader.Factory());
    }
}
