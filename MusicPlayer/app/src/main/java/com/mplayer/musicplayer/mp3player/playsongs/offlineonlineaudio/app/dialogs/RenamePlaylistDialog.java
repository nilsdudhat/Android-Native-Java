package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlaylistsUtil;

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
public class RenamePlaylistDialog extends DialogFragment {

    private static final String PLAYLIST_ID = "playlist_id";

    @NonNull
    public static RenamePlaylistDialog create(long playlistId) {
        RenamePlaylistDialog dialog = new RenamePlaylistDialog();
        Bundle args = new Bundle();
        args.putLong(PLAYLIST_ID, playlistId);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        long playlistId = getArguments().getLong(PLAYLIST_ID);

        MaterialDialog materialDialog = new MaterialDialog.Builder(requireActivity())
                .title(R.string.rename_playlist_title)
                .positiveText(R.string.rename_action)
                .negativeText(android.R.string.cancel)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(getString(R.string.playlist_name_empty), PlaylistsUtil.getNameForPlaylist(requireActivity(), playlistId), false,
                        (dialog, charSequence) -> {
                            final String name = charSequence.toString().trim();
                            if (!name.isEmpty()) {
                                long playlistId1 = getArguments().getLong(PLAYLIST_ID);
                                PlaylistsUtil.renamePlaylist(requireActivity(), playlistId1, name);
                            }
                        })
                .build();

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }
}