package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class CustomPlaylist extends Playlist {

    public CustomPlaylist(long id, String name) {
        super(id, name);
    }

    public CustomPlaylist() {
    }

    public CustomPlaylist(Parcel in) {
        super(in);
    }

    @NonNull
    public abstract List<Song> getSongs(Context context);
}
