package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;

import java.util.Collections;
import java.util.List;


public final class TrackSelectionViewFragment extends Fragment implements TrackSelectionView.TrackSelectionListener {

    /* package */ boolean isDisabled;
    /* package */ List<DefaultTrackSelector.SelectionOverride> overrides;
    private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;
    private int rendererIndex;
    private boolean allowAdaptiveSelections;
    private boolean allowMultipleOverrides;

    public TrackSelectionViewFragment() {
        // Retain instance across activity re-creation to prevent losing access to init data.
        setRetainInstance(true);
    }

    public void init(
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo,
            int rendererIndex,
            boolean initialIsDisabled,
            @Nullable DefaultTrackSelector.SelectionOverride initialOverride,
            boolean allowAdaptiveSelections,
            boolean allowMultipleOverrides) {
        this.mappedTrackInfo = mappedTrackInfo;
        this.rendererIndex = rendererIndex;
        this.isDisabled = initialIsDisabled;
        this.overrides = initialOverride == null ? Collections.emptyList() : Collections.singletonList(initialOverride);
        this.allowAdaptiveSelections = allowAdaptiveSelections;
        this.allowMultipleOverrides = allowMultipleOverrides;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context contexttheam = new ContextThemeWrapper(getActivity(), R.style.fragtheam);
        LayoutInflater localinfale = inflater.cloneInContext(contexttheam);
        View rootView = localinfale.inflate(R.layout.exo_track_selection_dialog, container, /* attachToRoot= */ false);
        TrackSelectionView trackSelectionView = rootView.findViewById(R.id.exo_track_selection_view);
        trackSelectionView.setShowDisableOption(true);
        trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides);
        trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections);
        trackSelectionView.init(mappedTrackInfo, rendererIndex, isDisabled, overrides, /* listener= */ this);
        return rootView;
    }

    @Override
    public void onTrackSelectionChanged(boolean isDisabled, List<DefaultTrackSelector.SelectionOverride> overrides) {
        this.isDisabled = isDisabled;
        this.overrides = overrides;
    }
}