package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.MainActivity;

public abstract class MainActivityFragment extends Fragment {

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
