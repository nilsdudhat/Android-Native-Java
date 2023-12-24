package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.AddToPlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.CreatePlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SleepTimerDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SongDetailDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SongShareDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.PaletteColorHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor.AbsTagEditorActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor.SongTagEditorActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.MusicServiceFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.NavigationUtil;

public abstract class PlayerFragment extends MusicServiceFragment implements Toolbar.OnMenuItemClickListener, PaletteColorHolder {

    private Callbacks callbacks;
    private static boolean isToolbarShown = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callbacks = (Callbacks) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.getClass().getSimpleName() + " must implement " + Callbacks.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final Song song = MusicPlayerRemote.getCurrentSong();
        int itemId = item.getItemId();
        if (itemId == R.id.action_sleep_timer) {
            new SleepTimerDialog().show(getFragmentManager(), "SET_SLEEP_TIMER");
            return true;
        } else if (itemId == R.id.action_toggle_favorite) {
            toggleFavorite(song);
            return true;
        } else if (itemId == R.id.action_share) {
            SongShareDialog.create(song).show(getFragmentManager(), "SHARE_SONG");
            return true;
        } else if (itemId == R.id.action_equalizer) {
            NavigationUtil.openEqualizer(requireActivity());
            return true;
        } else if (itemId == R.id.action_add_to_playlist) {
            AddToPlaylistDialog.create(song).show(getFragmentManager(), "ADD_PLAYLIST");
            return true;
        } else if (itemId == R.id.action_clear_playing_queue) {
            MusicPlayerRemote.clearQueue();
            return true;
        } else if (itemId == R.id.action_save_playing_queue) {
            CreatePlaylistDialog.create(MusicPlayerRemote.getPlayingQueue()).show(requireActivity().getSupportFragmentManager(), "ADD_TO_PLAYLIST");
            return true;
        } else if (itemId == R.id.action_tag_editor) {
            Intent intent = new Intent(getActivity(), SongTagEditorActivity.class);
            intent.putExtra(AbsTagEditorActivity.EXTRA_ID, song.id);
            ((AdsBaseActivity) requireActivity()).showInterstitialAd(requireActivity(), intent, null);
            return true;
        } else if (itemId == R.id.action_details) {
            SongDetailDialog.create(song).show(getChildFragmentManager(), "SONG_DETAIL");
            return true;
        } else if (itemId == R.id.action_go_to_album) {
            NavigationUtil.goToAlbum((AdsBaseActivity) requireActivity(), song.albumId);
            return true;
        } else if (itemId == R.id.action_go_to_artist) {
            NavigationUtil.goToArtist((AdsBaseActivity) requireActivity(), song.artistId);
            return true;
        }
        return false;
    }

    protected void toggleFavorite(Song song) {
        MusicUtil.toggleFavorite(requireActivity(), song);
    }

    protected boolean isToolbarShown() {
        return isToolbarShown;
    }

    protected void setToolbarShown(boolean toolbarShown) {
        isToolbarShown = toolbarShown;
    }

    protected void showToolbar(@Nullable final View toolbar) {
        if (toolbar == null) return;

        setToolbarShown(true);

        toolbar.setVisibility(View.VISIBLE);
        toolbar.animate().alpha(1f).setDuration(PlayerAlbumCoverFragment.VISIBILITY_ANIM_DURATION);
    }

    protected void hideToolbar(@Nullable final View toolbar) {
        if (toolbar == null) return;

        setToolbarShown(false);

        toolbar.animate().alpha(0f).setDuration(PlayerAlbumCoverFragment.VISIBILITY_ANIM_DURATION).withEndAction(() -> toolbar.setVisibility(View.GONE));
    }

    protected void toggleToolbar(@Nullable final View toolbar) {
        if (isToolbarShown()) {
            hideToolbar(toolbar);
        } else {
            showToolbar(toolbar);
        }
    }

    protected void checkToggleToolbar(@Nullable final View toolbar) {
        if (toolbar != null && !isToolbarShown() && toolbar.getVisibility() != View.GONE) {
            hideToolbar(toolbar);
        } else if (toolbar != null && isToolbarShown() && toolbar.getVisibility() != View.VISIBLE) {
            showToolbar(toolbar);
        }
    }

    protected String getUpNextAndQueueTime() {
        final long duration = MusicPlayerRemote.getQueueDurationMillis(MusicPlayerRemote.getPosition());

        return MusicUtil.buildInfoString(
            getResources().getString(R.string.up_next),
            MusicUtil.getReadableDurationString(duration)
        );
    }

    public abstract void onShow();

    public abstract void onHide();

    public abstract boolean onBackPressed();

    public Callbacks getCallbacks() {
        return callbacks;
    }

    public interface Callbacks {
        void onPaletteColorChanged();
    }
}
