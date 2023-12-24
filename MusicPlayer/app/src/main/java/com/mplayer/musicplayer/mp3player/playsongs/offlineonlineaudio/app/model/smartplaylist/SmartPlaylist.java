package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.CustomPlaylist;

public abstract class SmartPlaylist extends CustomPlaylist {

    @DrawableRes
    public final int iconRes;

    public SmartPlaylist(final String name, final int iconRes) {
        super(-Math.abs(31 * name.hashCode() + (iconRes * name.hashCode() * 31 * 31)), name);
        this.iconRes = iconRes;
    }

    public SmartPlaylist() {
        super();
        this.iconRes = R.drawable.ic_queue_music_white_24dp;
    }

    public abstract void clear(Context context);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + iconRes;
        return result;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (super.equals(obj)) {
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SmartPlaylist other = (SmartPlaylist) obj;
            return iconRes == other.iconRes;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.iconRes);
    }

    protected SmartPlaylist(Parcel in) {
        super(in);
        this.iconRes = in.readInt();
    }
}
