package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.preferences;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.BlacklistFolderChooserDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.provider.BlacklistStore;

import java.io.File;
import java.util.List;

public class BlacklistPreferenceDialog extends DialogFragment implements BlacklistFolderChooserDialog.FolderCallback {

    private List<String> paths;

    public static BlacklistPreferenceDialog newInstance() {
        return new BlacklistPreferenceDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BlacklistFolderChooserDialog blacklistFolderChooserDialog = (BlacklistFolderChooserDialog) getChildFragmentManager().findFragmentByTag("FOLDER_CHOOSER");
        if (blacklistFolderChooserDialog != null) {
            blacklistFolderChooserDialog.setCallback(this);
        }

        refreshBlacklistData();

        MaterialDialog materialDialog = new MaterialDialog.Builder(requireContext())
                .title(R.string.blacklist)
                .positiveText(android.R.string.ok)
                .neutralText(R.string.clear_action)
                .negativeText(R.string.add_action)
                .items(paths)
                .autoDismiss(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence charSequence) {
                        MaterialDialog materialDialog1 = new MaterialDialog.Builder(requireContext())
                                .title(R.string.remove_from_blacklist)
                                .content(Html.fromHtml(getString(R.string.do_you_want_to_remove_from_the_blacklist, charSequence)))
                                .positiveText(R.string.remove_action)
                                .negativeText(android.R.string.cancel)
                                .onPositive((materialDialog12, dialogAction) -> {
                                    BlacklistStore.getInstance(requireContext()).removePath(new File(charSequence.toString()));
                                    refreshBlacklistData();
                                }).show();

                        materialDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        materialDialog1.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                    }
                })
                // clear
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MaterialDialog materialDialog1 = new MaterialDialog.Builder(requireContext())
                                .title(R.string.clear_blacklist)
                                .content(R.string.do_you_want_to_clear_the_blacklist)
                                .positiveText(R.string.clear_action)
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        BlacklistStore.getInstance(requireContext()).clear();
                                        refreshBlacklistData();
                                    }
                                }).show();

                        materialDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        materialDialog1.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                    }
                })
                // add
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BlacklistFolderChooserDialog chooserDialog = BlacklistFolderChooserDialog.create();
                        chooserDialog.setCallback(BlacklistPreferenceDialog.this);
                        chooserDialog.show(getChildFragmentManager(), "FOLDER_CHOOSER");
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }

    private void refreshBlacklistData() {
        paths = BlacklistStore.getInstance(requireContext()).getPaths();

        MaterialDialog dialog = (MaterialDialog) getDialog();
        if (dialog != null) {
            String[] pathArray = new String[paths.size()];
            pathArray = paths.toArray(pathArray);
            dialog.setItems((CharSequence[]) pathArray);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        }
    }

    @Override
    public void onFolderSelection(@NonNull BlacklistFolderChooserDialog folderChooserDialog, @NonNull File file) {
        BlacklistStore.getInstance(requireContext()).addPath(file);
        refreshBlacklistData();
    }
}
