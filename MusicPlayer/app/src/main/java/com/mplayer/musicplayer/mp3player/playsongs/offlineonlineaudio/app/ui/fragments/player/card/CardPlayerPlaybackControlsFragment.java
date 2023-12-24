package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player.card;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kabouzeid.appthemehelper.util.MaterialValueHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicProgressViewUpdateHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.SimpleOnSeekbarChangeListener;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.service.MusicService;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.MusicServiceFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views.PlayPauseDrawable;

public class CardPlayerPlaybackControlsFragment extends MusicServiceFragment implements MusicProgressViewUpdateHelper.Callback {

    ImageView playPauseFab;
    ImageButton prevButton;
    ImageButton nextButton;
    ImageView repeatButton;
    ImageView shuffleButton;
    SeekBar progressSlider;
    TextView songTotalTime;
    TextView songCurrentProgress;
    TextView txt_song_name;
    TextView txt_artist_name;
    ImageView img_favorite;

    private void initViews(View view) {
        playPauseFab = view.findViewById(R.id.player_play_pause_fab);
        prevButton = view.findViewById(R.id.player_prev_button);
        nextButton = view.findViewById(R.id.player_next_button);
        repeatButton = view.findViewById(R.id.player_repeat_button);
        shuffleButton = view.findViewById(R.id.player_shuffle_button);
        progressSlider = view.findViewById(R.id.player_progress_slider);
        songTotalTime = view.findViewById(R.id.player_song_total_time);
        songCurrentProgress = view.findViewById(R.id.player_song_current_progress);
        txt_song_name = view.findViewById(R.id.txt_song_name);
        txt_artist_name = view.findViewById(R.id.txt_artist_name);
        img_favorite = view.findViewById(R.id.img_favorite);
    }

    private int lastPlaybackControlsColor;
    private int lastDisabledPlaybackControlsColor;

    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_player_playback_controls, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMusicControllers();
        updateProgressTextColor();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Do something
            img_favorite.setVisibility(View.GONE);
        }

        img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MusicUtil.toggleFavorite(requireActivity(), MusicPlayerRemote.getCurrentSong());
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        progressViewUpdateHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        progressViewUpdateHelper.stop();
    }

    @Override
    public void onServiceConnected() {
        updatePlayPauseDrawableState(false);
        updateRepeatState();
        updateShuffleState();
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState(true);
    }

    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    public void setDark(boolean dark) {
        if (dark) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(requireActivity(), true);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(requireActivity(), true);
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(requireActivity(), false);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(requireActivity(), false);
        }

        updateRepeatState();
        updateShuffleState();
        updateProgressTextColor();
    }

    private void setUpPlayPauseFab() {
//        final int fabColor = Color.WHITE;
//        TintHelper.setTintAuto(playPauseFab, fabColor, true);

//        playerFabPlayPauseDrawable = new PlayPauseDrawable(requireActivity());

//        playPauseFab.setImageDrawable(playerFabPlayPauseDrawable); // Note: set the drawable AFTER TintHelper.setTintAuto() was called
//        playPauseFab.setColorFilter(MaterialValueHelper.getPrimaryTextColor(requireContext(), ColorUtil.isColorLight(fabColor)), PorterDuff.Mode.SRC_IN);
        playPauseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayerRemote.isPlaying()) {
                    playPauseFab.setImageResource(R.drawable.ic_play);
                    MusicPlayerRemote.pauseSong();
                } else {
                    playPauseFab.setImageResource(R.drawable.ic_pause);
                    MusicPlayerRemote.resumePlaying();
                }
            }
        });
//        playPauseFab.post(() -> {
//            if (playPauseFab != null) {
//                playPauseFab.setPivotX(playPauseFab.getWidth() / 2);
//                playPauseFab.setPivotY(playPauseFab.getHeight() / 2);
//            }
//        });
    }

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
//            playerFabPlayPauseDrawable.setPause(animate);
            playPauseFab.setImageResource(R.drawable.ic_pause);
        } else {
//            playerFabPlayPauseDrawable.setPlay(animate);
            playPauseFab.setImageResource(R.drawable.ic_play);
        }
    }

    private void setUpMusicControllers() {
        setUpPlayPauseFab();
        setUpPrevNext();
        setUpRepeatButton();
        setUpShuffleButton();
        setUpProgressSlider();
    }

    private void setUpPrevNext() {
        nextButton.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MusicPlayerRemote.playNextSong();
                }
            }).start();
        });
        prevButton.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MusicPlayerRemote.back();
                }
            }).start();
        });
    }

    private void updateSongUtils(Song song) {
        txt_song_name.setText(song.title);
        txt_artist_name.setText(song.artistName);
//        if (MusicUtil.isFavorite(requireContext(), song)) {
//            img_favorite.setImageResource(R.drawable.ic_favorite);
//        } else {
//            img_favorite.setImageResource(R.drawable.ic_like_stroke);
//        }
    }

    private void updateProgressTextColor() {
        int color = MaterialValueHelper.getPrimaryTextColor(requireContext(), false);
        songTotalTime.setTextColor(color);
        songCurrentProgress.setTextColor(color);
    }

    private void setUpShuffleButton() {
        shuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    private void updateShuffleState() {
        if (MusicPlayerRemote.getShuffleMode() == MusicService.SHUFFLE_MODE_NONE) {
            shuffleButton.setImageResource(R.drawable.ic_shuffle_light);
        } else {
            shuffleButton.setImageResource(R.drawable.ic_shuffle);
        }
    }

    private void setUpRepeatButton() {
        repeatButton.setOnClickListener(v -> MusicPlayerRemote.cycleRepeatMode());
    }

    private void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                repeatButton.setImageResource(R.drawable.ic_repeat_light);
                break;
            case MusicService.REPEAT_MODE_ALL:
                repeatButton.setImageResource(R.drawable.ic_repeat);
                break;
            case MusicService.REPEAT_MODE_THIS:
                repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                break;
        }
    }

    public void show() {
        playPauseFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(360f)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public void hide() {
        if (playPauseFab != null) {
            playPauseFab.setScaleX(0f);
            playPauseFab.setScaleY(0f);
            playPauseFab.setRotation(0f);
        }
    }

    private void setUpProgressSlider() {

        progressSlider.setOnSeekBarChangeListener(new SimpleOnSeekbarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress);
                    onUpdateProgressViews(MusicPlayerRemote.getSongProgressMillis(), MusicPlayerRemote.getSongDurationMillis());
                }
            }
        });
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        progressSlider.setMax(total);
        progressSlider.setProgress(progress);
        songTotalTime.setText(MusicUtil.getReadableDurationString(total));
        songCurrentProgress.setText(MusicUtil.getReadableDurationString(progress));

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                updateSongUtils(MusicPlayerRemote.getCurrentSong());
            }
        });
    }
}
