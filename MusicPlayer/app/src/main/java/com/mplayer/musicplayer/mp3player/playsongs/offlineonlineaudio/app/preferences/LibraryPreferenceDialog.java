package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.preferences;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.CategoryInfoAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.CategoryInfo;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class LibraryPreferenceDialog extends DialogFragment {
    public static LibraryPreferenceDialog newInstance() {
        return new LibraryPreferenceDialog();
    }

    private CategoryInfoAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = requireActivity().getLayoutInflater().inflate(R.layout.preference_dialog_library_categories, null);

        List<CategoryInfo> categoryInfos;
        if (savedInstanceState != null) {
            categoryInfos = savedInstanceState.getParcelableArrayList(PreferenceUtil.LIBRARY_CATEGORIES);
        } else {
            categoryInfos = PreferenceUtil.getInstance(requireContext()).getLibraryCategoryInfos();
        }
        adapter = new CategoryInfoAdapter(categoryInfos);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.attachToRecyclerView(recyclerView);

        MaterialDialog materialDialog = new MaterialDialog.Builder(requireContext())
                .title(R.string.library_categories)
                .customView(view, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .neutralText(R.string.reset_action)
                .autoDismiss(false)
                .onNeutral((dialog, action) -> adapter.setCategoryInfos(PreferenceUtil.getInstance(requireContext()).getDefaultLibraryCategoryInfos()))
                .onNegative((dialog, action) -> dismiss())
                .onPositive((dialog, action) -> {
                    updateCategories(adapter.getCategoryInfos());
                    dismiss();
                })
                .build();

        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        materialDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        return materialDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PreferenceUtil.LIBRARY_CATEGORIES, new ArrayList<>(adapter.getCategoryInfos()));
    }

    private void updateCategories(List<CategoryInfo> categories) {
        if (getSelected(categories) == 0) return;

        PreferenceUtil.getInstance(requireContext()).setLibraryCategoryInfos(categories);
    }

    private int getSelected(List<CategoryInfo> categories) {
        int selected = 0;
        for (CategoryInfo categoryInfo : categories) {
            if (categoryInfo.visible)
                selected++;
        }
        return selected;
    }
}
