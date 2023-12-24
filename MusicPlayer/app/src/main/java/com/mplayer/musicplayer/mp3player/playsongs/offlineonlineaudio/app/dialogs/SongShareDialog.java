package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;

public class SongShareDialog extends DialogFragment {
    @NonNull
    public static SongShareDialog create(final Song song) {
        final SongShareDialog dialog = new SongShareDialog();
        final Bundle args = new Bundle();
        args.putParcelable("song", song);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Song song = getArguments().getParcelable("song");
        final String currentlyListening = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName);

        MaterialDialog materialDialog = new MaterialDialog.Builder(requireActivity())
                .title(R.string.what_do_you_want_to_share)
                .items(getString(R.string.the_audio_file), "\u201C" + currentlyListening + "\u201D")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case 0:
                                startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(song, getContext()), null));
                                break;
                            case 1:
                                requireActivity().startActivity(
                                        Intent.createChooser(
                                                new Intent()
                                                        .setAction(Intent.ACTION_SEND)
                                                        .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                                                        .setType("text/plain"),
                                                null
                                        )
                                );
                                break;
                        }
                    }
                })
                .build();

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }
}
