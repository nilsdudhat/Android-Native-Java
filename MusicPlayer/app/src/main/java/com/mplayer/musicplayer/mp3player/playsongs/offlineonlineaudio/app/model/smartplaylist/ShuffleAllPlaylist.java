package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.SongLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;

import java.util.List;

public class ShuffleAllPlaylist extends SmartPlaylist {

    public ShuffleAllPlaylist(@NonNull Context context) {
        super(context.getString(R.string.action_shuffle_all), R.drawable.ic_shuffle);
    }

    @NonNull
    @Override
    public List<Song> getSongs(@NonNull Context context) {
        return SongLoader.getAllSongs(context);
    }

    @Override
    public void clear(@NonNull Context context) {
        // Shuffle all is not a real "Smart Playlist"
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ShuffleAllPlaylist(Parcel in) {
        super(in);
    }

    public static final Creator<ShuffleAllPlaylist> CREATOR = new Creator<ShuffleAllPlaylist>() {
        public ShuffleAllPlaylist createFromParcel(Parcel source) {
            return new ShuffleAllPlaylist(source);
        }

        public ShuffleAllPlaylist[] newArray(int size) {
            return new ShuffleAllPlaylist[size];
        }
    };
}
