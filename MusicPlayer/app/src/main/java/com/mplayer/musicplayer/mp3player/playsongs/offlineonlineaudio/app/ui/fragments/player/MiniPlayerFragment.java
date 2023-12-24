package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.util.ATHUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.SongGlideRequest;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicProgressViewUpdateHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.PlayPauseButtonOnClickHandler;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.MusicServiceFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views.PlayPauseDrawable;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MiniPlayerFragment extends MusicServiceFragment implements MusicProgressViewUpdateHelper.Callback {

    TextView miniPlayerTitle;
    TextView miniPlayerText;
    ImageView miniPlayerPlayPauseButton;
    MaterialProgressBar progressBar;
    ImageView imgThumbnail;

    private void initViews(View view) {
        miniPlayerTitle = view.findViewById(R.id.mini_player_title);
        miniPlayerText = view.findViewById(R.id.mini_player_text);
        miniPlayerPlayPauseButton = view.findViewById(R.id.mini_player_play_pause_button);
        progressBar = view.findViewById(R.id.progress_bar);
        imgThumbnail = view.findViewById(R.id.img_thumbnail);
    }

    private PlayPauseDrawable miniPlayerPlayPauseDrawable;

    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mini_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackground(new ColorDrawable(Color.TRANSPARENT));
        initViews(view);

        view.setOnTouchListener(new FlingPlayBackController(getActivity()));
        setUpMiniPlayer();
    }

    private void setUpMiniPlayer() {
        setUpPlayPauseButton();
        progressBar.setSupportProgressTintList(ColorStateList.valueOf(ThemeStore.accentColor(requireActivity())));
    }

    private void setUpPlayPauseButton() {
        miniPlayerPlayPauseDrawable = new PlayPauseDrawable(requireActivity());
        miniPlayerPlayPauseButton.setImageDrawable(miniPlayerPlayPauseDrawable);
        miniPlayerPlayPauseButton.setColorFilter(ATHUtil.resolveColor(requireActivity(), R.attr.iconColor, ThemeStore.textColorSecondary(requireActivity())), PorterDuff.Mode.SRC_IN);
        miniPlayerPlayPauseButton.setOnClickListener(new PlayPauseButtonOnClickHandler());
    }

    private void updateSongTitle() {
        miniPlayerTitle.setText(MusicPlayerRemote.getCurrentSong().title);
        miniPlayerText.setText(MusicPlayerRemote.getCurrentSong().artistName);
    }

    @Override
    public void onServiceConnected() {
        updateSongTitle();
        updateThumbnail();
        updatePlayPauseDrawableState(false);
    }

    private void updateThumbnail() {
        SongGlideRequest.Builder.from(Glide.with(requireContext()), MusicPlayerRemote.getCurrentSong())
                .checkIgnoreMediaStore(requireContext()).build()
                .into(imgThumbnail);
    }

    @Override
    public void onPlayingMetaChanged() {
        updateSongTitle();
        updateThumbnail();
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState(true);
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        progressBar.setMax(total);
        progressBar.setProgress(progress);
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

    private static class FlingPlayBackController implements View.OnTouchListener {

        GestureDetector flingPlayBackController;

        public FlingPlayBackController(Context context) {
            flingPlayBackController = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (Math.abs(velocityX) > Math.abs(velocityY)) {
                        if (velocityX < 0) {
                            MusicPlayerRemote.playNextSong();
                            return true;
                        } else if (velocityX > 0) {
                            MusicPlayerRemote.playPreviousSong();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return flingPlayBackController.onTouchEvent(event);
        }
    }

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
            miniPlayerPlayPauseDrawable.setPause(animate);
        } else {
            miniPlayerPlayPauseDrawable.setPlay(animate);
        }
    }
}
