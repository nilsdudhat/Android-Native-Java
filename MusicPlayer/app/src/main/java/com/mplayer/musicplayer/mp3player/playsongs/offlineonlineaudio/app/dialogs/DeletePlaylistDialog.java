package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Playlist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlaylistsUtil;

import java.util.ArrayList;
import java.util.List;

public class DeletePlaylistDialog extends DialogFragment {

    @NonNull
    public static DeletePlaylistDialog create(Playlist playlist) {
        List<Playlist> list = new ArrayList<>();
        list.add(playlist);
        return create(list);
    }

    @NonNull
    public static DeletePlaylistDialog create(List<Playlist> playlists) {
        DeletePlaylistDialog dialog = new DeletePlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("playlists", new ArrayList<>(playlists));
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //noinspection unchecked
        final List<Playlist> playlists = getArguments().getParcelableArrayList("playlists");
        int title;
        CharSequence content;
        //noinspection ConstantConditions
        if (playlists.size() > 1) {
            title = R.string.delete_playlists_title;
            content = Html.fromHtml(getString(R.string.delete_x_playlists, playlists.size()));
        } else {
            title = R.string.delete_playlist_title;
            content = Html.fromHtml(getString(R.string.delete_playlist_x, playlists.get(0).name));
        }

        MaterialDialog materialDialog = new MaterialDialog.Builder(requireActivity())
                .title(title)
                .content(content)
                .positiveText(R.string.delete_action)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    if (getActivity() == null)
                        return;
                    PlaylistsUtil.deletePlaylists(getActivity(), playlists);
                })
                .build();

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }
}
