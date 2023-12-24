package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.PlaylistSong;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlaylistsUtil;

import java.util.ArrayList;
import java.util.List;

public class RemoveFromPlaylistDialog extends DialogFragment {

    @NonNull
    public static RemoveFromPlaylistDialog create(PlaylistSong song) {
        List<PlaylistSong> list = new ArrayList<>();
        list.add(song);
        return create(list);
    }

    @NonNull
    public static RemoveFromPlaylistDialog create(List<PlaylistSong> songs) {
        RemoveFromPlaylistDialog dialog = new RemoveFromPlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", new ArrayList<>(songs));
        dialog.setArguments(args);
        return dialog;
    }

    @SuppressLint("StringFormatInvalid")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //noinspection unchecked
        final List<PlaylistSong> songs = getArguments().getParcelableArrayList("songs");
        int title;
        CharSequence content;
        if (songs.size() > 1) {
            title = R.string.remove_songs_from_playlist_title;
            content = Html.fromHtml(getString(R.string.remove_x_songs_from_playlist, songs.size()));
        } else {
            title = R.string.remove_song_from_playlist_title;
            content = Html.fromHtml(getString(R.string.remove_song_x_from_playlist, songs.get(0).title));
        }

        MaterialDialog materialDialog = new MaterialDialog.Builder(requireActivity())
                .title(title)
                .content(content)
                .positiveText(R.string.remove_action)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    if (getActivity() == null)
                        return;
                    PlaylistsUtil.removeFromPlaylist(getActivity(), songs);
                })
                .build();

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }
}
