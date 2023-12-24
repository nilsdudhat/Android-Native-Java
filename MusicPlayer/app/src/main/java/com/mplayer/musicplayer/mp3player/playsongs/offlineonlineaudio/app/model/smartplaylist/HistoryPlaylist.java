package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.TopAndRecentlyPlayedTracksLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.provider.HistoryStore;

import java.util.List;

public class HistoryPlaylist extends SmartPlaylist {

    public HistoryPlaylist(@NonNull Context context) {
        super(context.getString(R.string.history), R.drawable.ic_recent);
    }

    @NonNull
    @Override
    public List<Song> getSongs(@NonNull Context context) {
        return TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
    }

    @Override
    public void clear(@NonNull Context context) {
        HistoryStore.getInstance(context).clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected HistoryPlaylist(Parcel in) {
        super(in);
    }

    public static final Creator<HistoryPlaylist> CREATOR = new Creator<HistoryPlaylist>() {
        public HistoryPlaylist createFromParcel(Parcel source) {
            return new HistoryPlaylist(source);
        }

        public HistoryPlaylist[] newArray(int size) {
            return new HistoryPlaylist[size];
        }
    };
}
