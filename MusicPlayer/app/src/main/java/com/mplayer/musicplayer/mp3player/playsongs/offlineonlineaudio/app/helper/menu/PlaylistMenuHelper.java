package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.MyApplication;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.AddToPlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.DeletePlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.RenamePlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.PlaylistSongLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.WeakContextAsyncTask;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.CustomPlaylist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Playlist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlaylistsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMenuHelper {
    public static boolean handleMenuClick(@NonNull AppCompatActivity activity, @NonNull final Playlist playlist, @NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_play) {
            MusicPlayerRemote.openQueue(new ArrayList<>(getPlaylistSongs(activity, playlist)), 0, true);
            return true;
        } else if (itemId == R.id.action_play_next) {
            MusicPlayerRemote.playNext(new ArrayList<>(getPlaylistSongs(activity, playlist)));
            return true;
        } else if (itemId == R.id.action_add_to_current_playing) {
            MusicPlayerRemote.enqueue(new ArrayList<>(getPlaylistSongs(activity, playlist)));
            return true;
        } else if (itemId == R.id.action_add_to_playlist) {
            AddToPlaylistDialog.create(new ArrayList<>(getPlaylistSongs(activity, playlist))).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
            return true;
        } else if (itemId == R.id.action_rename_playlist) {
            RenamePlaylistDialog.create(playlist.id).show(activity.getSupportFragmentManager(), "RENAME_PLAYLIST");
            return true;
        } else if (itemId == R.id.action_delete_playlist) {
            DeletePlaylistDialog.create(playlist).show(activity.getSupportFragmentManager(), "DELETE_PLAYLIST");
            return true;
        } else if (itemId == R.id.action_save_playlist) {
            new SavePlaylistAsyncTask(activity).execute(playlist);
            return true;
        }
        return false;
    }

    @NonNull
    private static List<? extends Song> getPlaylistSongs(@NonNull Activity activity, Playlist playlist) {
        return playlist instanceof CustomPlaylist ?
                ((CustomPlaylist) playlist).getSongs(activity) :
                PlaylistSongLoader.getPlaylistSongList(activity, playlist.id);
    }


    private static class SavePlaylistAsyncTask extends WeakContextAsyncTask<Playlist, String, String> {
        public SavePlaylistAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Playlist... params) {
            try {
                return String.format(MyApplication.getInstance().getApplicationContext().getString(R.string.saved_playlist_to), PlaylistsUtil.savePlaylist(MyApplication.getInstance().getApplicationContext(), params[0]));
            } catch (IOException e) {
                e.printStackTrace();
                return String.format(MyApplication.getInstance().getApplicationContext().getString(R.string.failed_to_save_playlist), e);
            }
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, string, Toast.LENGTH_LONG).show();
            }
        }
    }
}
