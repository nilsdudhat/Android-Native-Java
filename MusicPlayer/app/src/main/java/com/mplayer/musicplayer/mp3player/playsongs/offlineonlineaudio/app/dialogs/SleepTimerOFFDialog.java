package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;

public class SleepTimerOFFDialog extends DialogFragment {

    TextView txtTitle;
    Button btnDone;
    private MaterialDialog materialDialog;

    private void initViews(View view) {
        txtTitle = view.findViewById(R.id.txtTitle);
        btnDone = view.findViewById(R.id.btnDone);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(requireActivity())
                .customView(R.layout.dialog_sleep_timer_off, false)
                .build();

        if (requireActivity() == null || materialDialog.getCustomView() == null) {
            return materialDialog;
        }

        initViews(materialDialog.getCustomView());

        txtTitle.setTextColor(ContextCompat.getColor(requireContext(), com.kabouzeid.appthemehelper.R.color.md_white_1000));
//        btnDone.getBackground().setColorFilter(ThemeStore.accentColor(requireActivity()), PorterDuff.Mode.SRC_ATOP);
        btnDone.setTextColor(getResources().getColor(R.color.button_text_color));

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }
}
