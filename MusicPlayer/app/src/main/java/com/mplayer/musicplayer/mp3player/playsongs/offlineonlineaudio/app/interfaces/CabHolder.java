package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces;

import androidx.annotation.NonNull;

import com.afollestad.materialcab.MaterialCab;

public interface CabHolder {

    @NonNull
    MaterialCab openCab(final int menuRes, final MaterialCab.Callback callback);
}
