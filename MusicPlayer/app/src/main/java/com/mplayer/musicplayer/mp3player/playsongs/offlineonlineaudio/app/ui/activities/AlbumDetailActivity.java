package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.kabouzeid.appthemehelper.util.ColorUtil;
import com.kabouzeid.appthemehelper.util.MaterialValueHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.song.AlbumSongAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.AddToPlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.DeleteSongsDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SleepTimerDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.ColoredTarget;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.glide.SongGlideRequest;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.CabHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.LoaderIds;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.PaletteColorHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.lastfm.rest.LastFMRestClient;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.lastfm.rest.model.LastFmAlbum;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.AlbumLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.SimpleObservableScrollViewCallbacks;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.WrappedAsyncTaskLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Album;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.base.SlidingMusicPanelActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor.AbsTagEditorActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor.AlbumTagEditorActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.NavigationUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlayerColorUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PreferenceUtil;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Be careful when changing things in this Activity!
 */
public class AlbumDetailActivity extends SlidingMusicPanelActivity implements PaletteColorHolder, CabHolder, LoaderManager.LoaderCallbacks<Album> {

    private static final int TAG_EDITOR_REQUEST = 2001;
    private static final int LOADER_ID = LoaderIds.ALBUM_DETAIL_ACTIVITY;

    public static final String EXTRA_ALBUM_ID = "extra_album_id";

    private Album album;

    ObservableRecyclerView recyclerView;
    ImageView albumArtImageView;
    Toolbar toolbar;
    View headerView;
    View headerOverlay;
    TextView artistTextView;
    TextView durationTextView;
    TextView songCountTextView;
    TextView albumYearTextView;
    LinearLayout llShuffle;
    LinearLayout llPlay;

    private void initViews() {
        recyclerView = findViewById(R.id.list);
        albumArtImageView = findViewById(R.id.image);
        toolbar = findViewById(R.id.toolbar);
        headerView = findViewById(R.id.header);
        headerOverlay = findViewById(R.id.header_overlay);
        artistTextView = findViewById(R.id.artist_text);
        durationTextView = findViewById(R.id.duration_text);
        songCountTextView = findViewById(R.id.song_count_text);
        albumYearTextView = findViewById(R.id.album_year_text);
        llShuffle = findViewById(R.id.ll_shuffle);
        llPlay = findViewById(R.id.ll_play);
    }

    private AlbumSongAdapter adapter;

    private MaterialCab cab;
    private int headerViewHeight;
    private int toolbarColor;

    @Nullable
    private Spanned wiki;
    private MaterialDialog wikiDialog;
    private LastFMRestClient lastFMRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawUnderStatusbar();

        initViews();

        lastFMRestClient = new LastFMRestClient(this);

        setUpObservableListViewParams();
        setUpToolBar();
        setUpViews();

        getSupportLoaderManager().initLoader(LOADER_ID, getIntent().getExtras(), this);

        llShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Song> songs = adapter.getDataSet();
                MusicPlayerRemote.openAndShuffleQueue(songs, true);
            }
        });

        llPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Song> songs = adapter.getDataSet();
                MusicPlayerRemote.openQueue(songs, 0, true);
            }
        });
    }

    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_album_detail);
    }

    private final SimpleObservableScrollViewCallbacks observableScrollViewCallbacks = new SimpleObservableScrollViewCallbacks() {
        @Override
        public void onScrollChanged(int scrollY, boolean b, boolean b2) {
            scrollY += headerViewHeight;

            // Change alpha of overlay
//            float headerAlpha = Math.max(0, Math.min(1, (float) 2 * scrollY / headerViewHeight));
//            headerOverlay.setBackgroundColor(ColorUtil.withAlpha(toolbarColor, headerAlpha));

            // Translate name text
            headerView.setTranslationY(Math.max(-scrollY, -headerViewHeight));
            headerOverlay.setTranslationY(Math.max(-scrollY, -headerViewHeight));
//            albumArtImageView.setTranslationY(Math.max(-scrollY, -headerViewHeight));
        }
    };

    private void setUpObservableListViewParams() {
        headerViewHeight = getResources().getDimensionPixelSize(R.dimen.detail_header_height);
    }

    private void setUpViews() {
        setUpRecyclerView();
        setUpSongsAdapter();
        artistTextView.setOnClickListener(v -> {
            if (album != null) {
                NavigationUtil.goToArtist(AlbumDetailActivity.this, album.getArtistId());
            }
        });
        setColors(DialogUtils.resolveColor(this, R.attr.defaultFooterColor));
    }

    private void loadAlbumCover() {
        SongGlideRequest.Builder.from(Glide.with(this), getAlbum().safeGetFirstSong())
                .checkIgnoreMediaStore(this)
                .generatePalette(this).buildWithDisc()
                .dontAnimate()
                .into(new ColoredTarget(albumArtImageView) {
                    @Override
                    public void onColorReady(int color) {
                        setColors(color);
                    }
                });
    }

    private void setColors(int color) {
        toolbarColor = color;
//        headerView.setBackgroundColor(color);

//        setNavigationBarColor(color);
//        setTaskDescriptionColor(color);

//        toolbar.setBackgroundColor(color);
        setSupportActionBar(toolbar); // needed to auto readjust the toolbar content color
//        setStatusbarColor(color);

        int secondaryTextColor = MaterialValueHelper.getSecondaryTextColor(this, ColorUtil.isColorLight(color));
//        artistIconImageView.setColorFilter(secondaryTextColor, PorterDuff.Mode.SRC_IN);
//        durationIconImageView.setColorFilter(secondaryTextColor, PorterDuff.Mode.SRC_IN);
//        songCountIconImageView.setColorFilter(secondaryTextColor, PorterDuff.Mode.SRC_IN);
//        albumYearIconImageView.setColorFilter(secondaryTextColor, PorterDuff.Mode.SRC_IN);
//        artistTextView.setTextColor(MaterialValueHelper.getPrimaryTextColor(this, ColorUtil.isColorLight(color)));
//        durationTextView.setTextColor(secondaryTextColor);
//        songCountTextView.setTextColor(secondaryTextColor);
//        albumYearTextView.setTextColor(secondaryTextColor);
    }

    @Override
    public int getPaletteColor() {
        return toolbarColor;
    }

    private void setUpRecyclerView() {
        setUpRecyclerViewPadding();
        recyclerView.setScrollViewCallbacks(observableScrollViewCallbacks);
        final View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.post(() -> observableScrollViewCallbacks.onScrollChanged(-headerViewHeight, false, false));
    }

    private void setUpRecyclerViewPadding() {
        recyclerView.setPadding(0, headerViewHeight, 0, 0);
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpSongsAdapter() {
        Log.d("--list--", "setUpSongsAdapter: " + getAlbum().songs.size());
        adapter = new AlbumSongAdapter(this, getAlbum().songs, R.layout.item_list, false, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() == 0) finish();
            }
        });
    }

    private void reload() {
        getSupportLoaderManager().restartLoader(LOADER_ID, getIntent().getExtras(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadWiki() {
        loadWiki(Locale.getDefault().getLanguage());
    }

    private void loadWiki(@Nullable final String lang) {
        wiki = null;

        lastFMRestClient.getApiService()
                .getAlbumInfo(getAlbum().getTitle(), getAlbum().getArtistName(), lang)
                .enqueue(new Callback<LastFmAlbum>() {
                    @Override
                    public void onResponse(@NonNull Call<LastFmAlbum> call, @NonNull Response<LastFmAlbum> response) {
                        final LastFmAlbum lastFmAlbum = response.body();
                        if (lastFmAlbum != null && lastFmAlbum.getAlbum() != null && lastFmAlbum.getAlbum().getWiki() != null) {
                            final String wikiContent = lastFmAlbum.getAlbum().getWiki().getContent();
                            if (wikiContent != null && !wikiContent.trim().isEmpty()) {
                                wiki = Html.fromHtml(wikiContent);
                            }
                        }

                        // If the "lang" parameter is set and no wiki is given, retry with default language
                        if (wiki == null && lang != null) {
                            loadWiki(null);
                            return;
                        }

                        if (!PreferenceUtil.isAllowedToDownloadMetadata(AlbumDetailActivity.this)) {
                            if (wiki != null) {
                                wikiDialog.setContent(wiki);
                            } else {
                                wikiDialog.dismiss();
                                Toast.makeText(AlbumDetailActivity.this, getResources().getString(R.string.wiki_unavailable), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LastFmAlbum> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final List<Song> songs = adapter.getDataSet();
        if (id == R.id.action_sleep_timer) {
            new SleepTimerDialog().show(getSupportFragmentManager(), "SET_SLEEP_TIMER");
            return true;
        } else if (id == R.id.action_equalizer) {
            NavigationUtil.openEqualizer(this);
            return true;
        } else if (id == R.id.action_shuffle_album) {
            MusicPlayerRemote.openAndShuffleQueue(songs, true);
            return true;
        } else if (id == R.id.action_play_next) {
            MusicPlayerRemote.playNext(songs);
            return true;
        } else if (id == R.id.action_add_to_current_playing) {
            MusicPlayerRemote.enqueue(songs);
            return true;
        } else if (id == R.id.action_add_to_playlist) {
            AddToPlaylistDialog.create(songs).show(getSupportFragmentManager(), "ADD_PLAYLIST");
            return true;
        } else if (id == R.id.action_delete_from_device) {
            DeleteSongsDialog.create(songs).show(getSupportFragmentManager(), "DELETE_SONGS");
            return true;
        } else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else if (id == R.id.action_tag_editor) {
            Intent intent = new Intent(this, AlbumTagEditorActivity.class);
            intent.putExtra(AbsTagEditorActivity.EXTRA_ID, getAlbum().getId());
            startActivityForResult(intent, TAG_EDITOR_REQUEST);
            return true;
        } else if (id == R.id.action_go_to_artist) {
            NavigationUtil.goToArtist(this, getAlbum().getArtistId());
            return true;
        } else if (id == R.id.action_wiki) {
            if (wikiDialog == null) {
                wikiDialog = new MaterialDialog.Builder(this)
                        .title(album.getTitle())
                        .positiveText(android.R.string.ok)
                        .build();
            }
            if (PreferenceUtil.isAllowedToDownloadMetadata(this)) {
                if (wiki != null) {
                    wikiDialog.setContent(wiki);
                    wikiDialog.show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.wiki_unavailable), Toast.LENGTH_SHORT).show();
                }
            } else {
                wikiDialog.show();
                loadWiki();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_EDITOR_REQUEST) {
            reload();
            setResult(RESULT_OK);
        }
    }

    @NonNull
    @Override
    public MaterialCab openCab(int menuRes, @NonNull final MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) cab.finish();
        cab = new MaterialCab(this, R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(PlayerColorUtil.shiftBackgroundColorForLightText(getPaletteColor()))
                .start(new MaterialCab.Callback() {
                    @Override
                    public boolean onCabCreated(MaterialCab materialCab, Menu menu) {
                        return callback.onCabCreated(materialCab, menu);
                    }

                    @Override
                    public boolean onCabItemClicked(MenuItem menuItem) {
                        return callback.onCabItemClicked(menuItem);
                    }

                    @Override
                    public boolean onCabFinished(MaterialCab materialCab) {
                        return callback.onCabFinished(materialCab);
                    }
                });
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

        reload();
    }

    @Override
    public void setStatusbarColor(int color) {
        super.setStatusbarColor(color);
        setLightStatusBar(false);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        super.onPanelSlide(panel, slideOffset);

        statusBarView.setVisibility(View.VISIBLE);
    }

    private void setAlbum(Album album) {
        this.album = album;
        loadAlbumCover();

        if (PreferenceUtil.isAllowedToDownloadMetadata(this)) {
            loadWiki();
        }

        getSupportActionBar().setTitle(album.getTitle());
        artistTextView.setText(album.getArtistName());
        songCountTextView.setText(MusicUtil.getSongCountString(this, album.getSongCount()));
        durationTextView.setText(MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(this, album.songs)));
        albumYearTextView.setText(MusicUtil.getYearString(album.getYear()));

        adapter.swapDataSet(album.songs);
    }

    private Album getAlbum() {
        if (album == null) album = new Album();
        return album;
    }

    @Override
    public Loader<Album> onCreateLoader(int id, Bundle args) {
        return new AsyncAlbumLoader(this, args.getLong(EXTRA_ALBUM_ID));
    }

    @Override
    public void onLoadFinished(Loader<Album> loader, Album data) {
        setAlbum(data);
    }

    @Override
    public void onLoaderReset(Loader<Album> loader) {
        this.album = new Album();
        adapter.swapDataSet(album.songs);
    }

    private static class AsyncAlbumLoader extends WrappedAsyncTaskLoader<Album> {
        private final long albumId;

        public AsyncAlbumLoader(Context context, long albumId) {
            super(context);
            this.albumId = albumId;
        }

        @Override
        public Album loadInBackground() {
            return AlbumLoader.getAlbum(getContext(), albumId);
        }
    }
}