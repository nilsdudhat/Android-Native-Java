package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player.flat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.kabouzeid.appthemehelper.util.MaterialValueHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicProgressViewUpdateHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.PlayPauseButtonOnClickHandler;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.SimpleOnSeekbarChangeListener;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.service.MusicService;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.MusicServiceFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views.PlayPauseDrawable;

import java.util.Collection;
import java.util.LinkedList;

public class FlatPlayerPlaybackControlsFragment extends MusicServiceFragment implements MusicProgressViewUpdateHelper.Callback {

    ImageButton playPauseButton;
    ImageButton prevButton;
    ImageButton nextButton;
    ImageButton repeatButton;
    ImageButton shuffleButton;
    SeekBar progressSlider;
    TextView songTotalTime;
    TextView songCurrentProgress;

    private void initViews(View view) {
        playPauseButton = view.findViewById(R.id.player_play_pause__button);
        prevButton = view.findViewById(R.id.player_prev_button);
        nextButton = view.findViewById(R.id.player_next_button);
        repeatButton = view.findViewById(R.id.player_repeat_button);
        shuffleButton = view.findViewById(R.id.player_shuffle_button);
        progressSlider = view.findViewById(R.id.player_progress_slider);
        songTotalTime = view.findViewById(R.id.player_song_total_time);
        songCurrentProgress = view.findViewById(R.id.player_song_current_progress);
    }

    private PlayPauseDrawable playPauseDrawable;

    private int lastPlaybackControlsColor;
    private int lastDisabledPlaybackControlsColor;

    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    private AnimatorSet musicControllerAnimationSet;

    private boolean hidden = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flat_player_playback_controls, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMusicControllers();
        updateProgressTextColor();
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
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(getActivity(), true);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(getActivity(), true);
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(getActivity(), false);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(getActivity(), false);
        }

        updateRepeatState();
        updateShuffleState();
        updatePrevNextColor();
        updatePlayPauseColor();
        updateProgressTextColor();
    }

    private void setUpPlayPauseButton() {
        playPauseDrawable = new PlayPauseDrawable(getActivity());
        playPauseButton.setImageDrawable(playPauseDrawable);
        updatePlayPauseColor();
        playPauseButton.setOnClickListener(new PlayPauseButtonOnClickHandler());
        playPauseButton.post(() -> {
            if (playPauseButton != null) {
                playPauseButton.setPivotX(playPauseButton.getWidth() / 2);
                playPauseButton.setPivotY(playPauseButton.getHeight() / 2);
            }
        });
    }

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
            playPauseDrawable.setPause(animate);
        } else {
            playPauseDrawable.setPlay(animate);
        }
    }

    private void setUpMusicControllers() {
        setUpPlayPauseButton();
        setUpPrevNext();
        setUpRepeatButton();
        setUpShuffleButton();
        setUpProgressSlider();
    }

    private void setUpPrevNext() {
        updatePrevNextColor();
        nextButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());
        prevButton.setOnClickListener(v -> MusicPlayerRemote.back());
    }

    private void updateProgressTextColor() {
        int color = MaterialValueHelper.getPrimaryTextColor(getContext(), false);
        songTotalTime.setTextColor(color);
        songCurrentProgress.setTextColor(color);
    }

    private void updatePrevNextColor() {
        nextButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
        prevButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    private void updatePlayPauseColor() {
        playPauseButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    private void setUpShuffleButton() {
        shuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    private void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                shuffleButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            default:
                shuffleButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void setUpRepeatButton() {
        repeatButton.setOnClickListener(v -> MusicPlayerRemote.cycleRepeatMode());
    }

    private void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                repeatButton.setImageResource(R.drawable.ic_repeat_light);
                repeatButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_ALL:
                repeatButton.setImageResource(R.drawable.ic_repeat);
                repeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_THIS:
                repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                repeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    public void show() {
        if (hidden) {
            if (musicControllerAnimationSet == null) {
                TimeInterpolator interpolator = new FastOutSlowInInterpolator();
                final int duration = 300;

                LinkedList<Animator> animators = new LinkedList<>();

                addAnimation(animators, playPauseButton, interpolator, duration, 0);
                addAnimation(animators, nextButton, interpolator, duration, 100);
                addAnimation(animators, prevButton, interpolator, duration, 100);
                addAnimation(animators, shuffleButton, interpolator, duration, 200);
                addAnimation(animators, repeatButton, interpolator, duration, 200);

                musicControllerAnimationSet = new AnimatorSet();
                musicControllerAnimationSet.playTogether(animators);
            } else {
                musicControllerAnimationSet.cancel();
            }
            musicControllerAnimationSet.start();
        }

        hidden = false;
    }

    public void hide() {
        if (musicControllerAnimationSet != null) {
            musicControllerAnimationSet.cancel();
        }
        prepareForAnimation(playPauseButton);
        prepareForAnimation(nextButton);
        prepareForAnimation(prevButton);
        prepareForAnimation(shuffleButton);
        prepareForAnimation(repeatButton);

        hidden = true;
    }

    private static void addAnimation(Collection<Animator> animators, View view, TimeInterpolator interpolator, int duration, int delay) {
        Animator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f);
        scaleX.setInterpolator(interpolator);
        scaleX.setDuration(duration);
        scaleX.setStartDelay(delay);
        animators.add(scaleX);

        Animator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f);
        scaleY.setInterpolator(interpolator);
        scaleY.setDuration(duration);
        scaleY.setStartDelay(delay);
        animators.add(scaleY);
    }

    private static void prepareForAnimation(View view) {
        if (view != null) {
            view.setScaleX(0f);
            view.setScaleY(0f);
        }
    }

    private void setUpProgressSlider() {
        int color = MaterialValueHelper.getPrimaryTextColor(getContext(), false);
        progressSlider.getThumb().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressSlider.getProgressDrawable().mutate().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);

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
    }
}
