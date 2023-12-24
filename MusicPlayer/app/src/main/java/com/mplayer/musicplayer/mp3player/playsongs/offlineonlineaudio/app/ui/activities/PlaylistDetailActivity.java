package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.song.OrderablePlaylistSongAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.song.PlaylistSongAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.song.SongAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu.PlaylistMenuHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.CabHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.LoaderIds;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.PlaylistLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.PlaylistSongLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.WrappedAsyncTaskLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.CustomPlaylist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Playlist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.base.SlidingMusicPanelActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlayerColorUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlaylistsUtil;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailActivity extends SlidingMusicPanelActivity implements CabHolder, LoaderManager.LoaderCallbacks<List<Song>> {

    private static final int LOADER_ID = LoaderIds.PLAYLIST_DETAIL_ACTIVITY;

    @NonNull
    public static String EXTRA_PLAYLIST = "extra_playlist";

    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView empty;

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        empty = findViewById(android.R.id.empty);
    }

    private Playlist playlist;

    private MaterialCab cab;
    private SongAdapter adapter;

    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawUnderStatusbar();

        playlist = getIntent().getExtras().getParcelable(EXTRA_PLAYLIST);

        initViews();

        setUpRecyclerView();

        setUpToolbar();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_playlist_detail);
    }

    private void setUpRecyclerView() {
//        ViewUtil.setUpFastScrollRecyclerViewColor(this, ((FastScrollRecyclerView) recyclerView), ThemeStore.accentColor(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (playlist instanceof CustomPlaylist) {
            adapter = new PlaylistSongAdapter(this, new ArrayList<>(), R.layout.item_list, false, this);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerViewDragDropManager = new RecyclerViewDragDropManager();
            final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
            adapter = new OrderablePlaylistSongAdapter(this, new ArrayList<>(), R.layout.item_list, false, this, (fromPosition, toPosition) -> {
                if (PlaylistsUtil.moveItem(PlaylistDetailActivity.this, playlist.id, fromPosition, toPosition)) {
                    Song song = adapter.getDataSet().remove(fromPosition);
                    adapter.getDataSet().add(toPosition, song);
                    adapter.notifyItemMoved(fromPosition, toPosition);
                }
            });
            wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(adapter);

            recyclerView.setAdapter(wrappedAdapter);
            recyclerView.setItemAnimator(animator);

            recyclerViewDragDropManager.attachRecyclerView(recyclerView);
        }

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
            }
        });
    }

    private void setUpToolbar() {
//        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTitle(playlist.name);
    }

    private void setToolbarTitle(String title) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(playlist instanceof CustomPlaylist ? R.menu.menu_smart_playlist_detail : R.menu.menu_playlist_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_shuffle_playlist) {
            MusicPlayerRemote.openAndShuffleQueue(adapter.getDataSet(), true);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return PlaylistMenuHelper.handleMenuClick(this, playlist, item);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        super.onPanelSlide(panel, slideOffset);

        statusBarView.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public MaterialCab openCab(final int menu, final MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) cab.finish();
        cab = new MaterialCab(this, R.id.cab_stub)
                .setMenu(menu)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(PlayerColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor(this)))
                .start(callback);
        return cab;
    }

    @Override
    public void onBackPressed() {
        if (cab != null && cab.isActive()) cab.finish();
        else {
            recyclerView.stopScroll();
            super.onBackPressed();
        }
    }

    @Override
    public void onMediaStoreChanged() {
        super.onMediaStoreChanged();

        if (!(playlist instanceof CustomPlaylist)) {
            // Playlist deleted
            if (!PlaylistsUtil.doesPlaylistExist(this, playlist.id)) {
                finish();
                return;
            }

            // Playlist renamed
            final String playlistName = PlaylistsUtil.getNameForPlaylist(this, playlist.id);
            if (!playlistName.equals(playlist.name)) {
                playlist = PlaylistLoader.getPlaylist(this, playlist.id);
                setToolbarTitle(playlist.name);
            }
        }

        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void checkIsEmpty() {
        empty.setVisibility(
                adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE
        );
    }

    @Override
    public void onPause() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.cancelDrag();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.release();
            recyclerViewDragDropManager = null;
        }

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        adapter = null;

        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new AsyncPlaylistSongLoader(this, playlist);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Song>> loader, List<Song> data) {
        if (adapter != null)
            adapter.swapDataSet(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Song>> loader) {
        if (adapter != null)
            adapter.swapDataSet(new ArrayList<>());
    }

    private static class AsyncPlaylistSongLoader extends WrappedAsyncTaskLoader<List<Song>> {
        private final Playlist playlist;

        public AsyncPlaylistSongLoader(Context context, Playlist playlist) {
            super(context);
            this.playlist = playlist;
        }

        @Override
        public List<Song> loadInBackground() {
            if (playlist instanceof CustomPlaylist) {
                return ((CustomPlaylist) playlist).getSongs(getContext());
            } else {
                //noinspection unchecked
                return (List) PlaylistSongLoader.getPlaylistSongList(getContext(), playlist.id);
            }
        }
    }
}
