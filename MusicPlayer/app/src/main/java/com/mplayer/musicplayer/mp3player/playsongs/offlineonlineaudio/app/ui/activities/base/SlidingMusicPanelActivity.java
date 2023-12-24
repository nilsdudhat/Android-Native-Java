package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.base;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player.MiniPlayerFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player.NowPlayingScreen;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player.PlayerFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.player.card.CardPlayerFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PreferenceUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.ViewUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views.StatusBarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Do not use {@link #setContentView(int)}. Instead wrap your layout with
 * {@link #wrapSlidingMusicPanel(int)} first and then return it in {@link #createContentView()}
 */
public abstract class SlidingMusicPanelActivity extends MusicServiceActivity implements SlidingUpPanelLayout.PanelSlideListener, CardPlayerFragment.Callbacks {

    SlidingUpPanelLayout slidingUpPanelLayout;
    CardView cardMiniPlayer;
    CardView cardPlayer;
    public StatusBarView statusBarView;

    private void initViews() {
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        cardMiniPlayer = findViewById(R.id.card_mini_player);
        cardPlayer = findViewById(R.id.card_player);
        statusBarView = findViewById(R.id.status_bar);
    }

    private int navigationBarColor;
    private int taskColor;
    private boolean lightStatusBar;

    private NowPlayingScreen currentNowPlayingScreen;
    private PlayerFragment playerFragment;
    private MiniPlayerFragment miniPlayerFragment;

    private ValueAnimator navigationBarColorAnimator;
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContentView());

        initViews();

        currentNowPlayingScreen = PreferenceUtil.getInstance(this).getNowPlayingScreen();
        Fragment fragment = new CardPlayerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_container, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();

        playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment_container);
        miniPlayerFragment = (MiniPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.mini_player_fragment);

        //noinspection ConstantConditions
        miniPlayerFragment.requireView().setOnClickListener(v -> expandPanel());

        slidingUpPanelLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                slidingUpPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                switch (getPanelState()) {
                    case EXPANDED:
                        onPanelSlide(slidingUpPanelLayout, 1);
                        onPanelExpanded(slidingUpPanelLayout);
                        break;
                    case COLLAPSED:
                        onPanelCollapsed(slidingUpPanelLayout);
                        break;
                    default:
                        cardPlayer.setAlpha(0);
                        playerFragment.onHide();
                        break;
                }
            }
        });
        slidingUpPanelLayout.addPanelSlideListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentNowPlayingScreen != PreferenceUtil.getInstance(this).getNowPlayingScreen()) {
            postRecreate();
        }
    }

    public void setAntiDragView(View antiDragView) {
        slidingUpPanelLayout.setAntiDragView(antiDragView);
    }

    protected abstract View createContentView();

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        if (!MusicPlayerRemote.getPlayingQueue().isEmpty()) {
            slidingUpPanelLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    slidingUpPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    hideBottomBar(false);
                }
            });
        } // don't call hideBottomBar(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    @Override
    public void onQueueChanged() {
        super.onQueueChanged();
        hideBottomBar(MusicPlayerRemote.getPlayingQueue().isEmpty());
    }

    @Override
    public void onPanelSlide(View panel, @FloatRange(from = 0, to = 1) float slideOffset) {
        setMiniPlayerAlphaProgress(slideOffset);
        if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
        super.setNavigationBarColor((int) argbEvaluator.evaluate(slideOffset, navigationBarColor, playerFragment.getPaletteColor()));
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        switch (newState) {
            case COLLAPSED:
                onPanelCollapsed(panel);
                break;
            case EXPANDED:
                onPanelExpanded(panel);
                break;
            case ANCHORED:
                collapsePanel(); // this fixes a bug where the panel would get stuck for some reason
                break;
        }
    }

    public void onPanelCollapsed(View panel) {
        // restore values
        super.setLightStatusBar(lightStatusBar);
        super.setTaskDescriptionColor(taskColor);
        super.setNavigationBarColor(navigationBarColor);

        playerFragment.setMenuVisibility(false);
        playerFragment.setUserVisibleHint(false);
        cardPlayer.setAlpha(0);
        playerFragment.onHide();
    }

    public void onPanelExpanded(View panel) {
        // setting fragments values
        int playerFragmentColor = playerFragment.getPaletteColor();
        super.setLightStatusBar(false);
        super.setTaskDescriptionColor(playerFragmentColor);
        super.setNavigationBarColor(playerFragmentColor);

        playerFragment.setMenuVisibility(true);
        playerFragment.setUserVisibleHint(true);
        cardPlayer.setAlpha(1);
        playerFragment.onShow();
    }

    private void setMiniPlayerAlphaProgress(@FloatRange(from = 0, to = 1) float progress) {
        if (cardMiniPlayer == null) return;
        if (cardPlayer == null) return;
        float alpha = 1 - progress;
        cardPlayer.setAlpha(progress);
        cardMiniPlayer.setAlpha(alpha);
        // necessary to make the views below clickable
        cardMiniPlayer.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
//        playerFragment.getView().setVisibility(progress == 1 ? View.VISIBLE : View.GONE);
    }

    public SlidingUpPanelLayout.PanelState getPanelState() {
        return slidingUpPanelLayout == null ? null : slidingUpPanelLayout.getPanelState();
    }

    public void collapsePanel() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public void expandPanel() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void hideBottomBar(final boolean hide) {
        if (hide) {
            slidingUpPanelLayout.setPanelHeight(0);
            collapsePanel();
        } else {
            slidingUpPanelLayout.setPanelHeight(getResources().getDimensionPixelSize(R.dimen.mini_player_height));
        }
    }

    protected View wrapSlidingMusicPanel(@LayoutRes int resId) {
        @SuppressLint("InflateParams")
        View slidingMusicPanelLayout = getLayoutInflater().inflate(R.layout.sliding_music_panel_layout, null);
        ViewGroup contentContainer = slidingMusicPanelLayout.findViewById(R.id.content_container);
        getLayoutInflater().inflate(resId, contentContainer);
        return slidingMusicPanelLayout;
    }

    @Override
    public void onBackPressed() {
        if (!handleBackPress())
            super.onBackPressed();
    }

    public boolean handleBackPress() {
        if (slidingUpPanelLayout.getPanelHeight() != 0 && playerFragment.onBackPressed())
            return true;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            collapsePanel();
            return true;
        }
        return false;
    }

    @Override
    public void onPaletteColorChanged() {
        if (getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            int playerFragmentColor = playerFragment.getPaletteColor();
            super.setTaskDescriptionColor(playerFragmentColor);
            animateNavigationBarColor(playerFragmentColor);
        }
    }

    @Override
    public void setLightStatusBar(boolean enabled) {
        lightStatusBar = enabled;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setLightStatusBar(enabled);
        }
    }

    @Override
    public void setNavigationBarColor(int color) {
        this.navigationBarColor = color;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
            super.setNavigationBarColor(color);
        }
    }

    private void animateNavigationBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
            navigationBarColorAnimator = ValueAnimator
                    .ofArgb(getWindow().getNavigationBarColor(), color)
                    .setDuration(ViewUtil.ANIM_TIME);
            navigationBarColorAnimator.setInterpolator(new PathInterpolator(0.4f, 0f, 1f, 1f));
            navigationBarColorAnimator.addUpdateListener(animation -> SlidingMusicPanelActivity.super.setNavigationBarColor((Integer) animation.getAnimatedValue()));
            navigationBarColorAnimator.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel(); // just in case
    }

    @Override
    public void setTaskDescriptionColor(@ColorInt int color) {
        this.taskColor = color;
        if (getPanelState() == null || getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setTaskDescriptionColor(color);
        }
    }

    @Override
    protected View getSnackBarContainer() {
        return findViewById(R.id.content_container);
    }

    public SlidingUpPanelLayout getSlidingUpPanelLayout() {
        return slidingUpPanelLayout;
    }

    public MiniPlayerFragment getMiniPlayerFragment() {
        return miniPlayerFragment;
    }

    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }
}