package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialcab.MaterialCab;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.common.ATHToolbarActivity;
import com.kabouzeid.appthemehelper.util.ToolbarContentTintHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.MusicLibraryPagerAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.CreatePlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SleepTimerDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SleepTimerOFFDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.SortOrder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.CabHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.SongLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.service.MusicService;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.MainActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.SearchActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.MainActivityFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.pager.AlbumsFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.pager.ArtistsFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.pager.LibraryPagerCustomGridSizeFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.pager.PlaylistsFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.pager.SongsFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlayerColorUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PreferenceUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.Util;

public class LibraryFragment extends MainActivityFragment implements CabHolder, MainActivity.MainActivityFragmentCallbacks, ViewPager.OnPageChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    Toolbar toolbar;
    TabLayout tabs;
    AppBarLayout appbar;
    ViewPager pager;

    private MusicLibraryPagerAdapter pagerAdapter;
    private MaterialCab cab;

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tabs = view.findViewById(R.id.tabs);
        appbar = view.findViewById(R.id.appbar);
        pager = view.findViewById(R.id.pager);
    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    public LibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        PreferenceUtil.getInstance(requireActivity()).unregisterOnSharedPreferenceChangedListener(this);
        super.onDestroyView();
        pager.removeOnPageChangeListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PreferenceUtil.getInstance(requireActivity()).registerOnSharedPreferenceChangedListener(this);
//        getMainActivity().setStatusbarColorAuto();
//        getMainActivity().setNavigationbarColorAuto();
//        getMainActivity().setTaskDescriptionColorAuto();

        setUpToolbar();
        setUpViewPager();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (PreferenceUtil.LIBRARY_CATEGORIES.equals(key)) {
            Fragment current = getCurrentFragment();
            pagerAdapter.setCategoryInfos(PreferenceUtil.getInstance(requireActivity()).getLibraryCategoryInfos());
            pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
            int position = pagerAdapter.getItemPosition(current);
            if (position < 0) position = 0;
            pager.setCurrentItem(position);
            PreferenceUtil.getInstance(requireContext()).setLastPage(position);

            updateTabVisibility();
        }
    }

    private void setUpToolbar() {
//        int primaryColor = ThemeStore.primaryColor(requireActivity());
//        appbar.setBackgroundColor(primaryColor);
//        toolbar.setBackgroundColor(primaryColor);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        requireActivity().setTitle(R.string.app_name);
        getMainActivity().setSupportActionBar(toolbar);
    }

    private void setUpViewPager() {
        pagerAdapter = new MusicLibraryPagerAdapter(requireActivity(), getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);

        tabs.setupWithViewPager(pager);

//        int primaryColor = ThemeStore.primaryColor(requireActivity());
//        int normalColor = ToolbarContentTintHelper.toolbarSubtitleColor(requireActivity(), primaryColor);
//        int selectedColor = ToolbarContentTintHelper.toolbarTitleColor(requireActivity(), primaryColor);
//        TabLayoutUtil.setTabIconColors(tabs, normalColor, selectedColor);
//        tabs.setTabTextColors(normalColor, selectedColor);
//        tabs.setSelectedTabIndicatorColor(ThemeStore.accentColor(requireActivity()));

        updateTabVisibility();

        if (PreferenceUtil.getInstance(requireContext()).rememberLastTab()) {
            pager.setCurrentItem(PreferenceUtil.getInstance(requireContext()).getLastPage());
        }
        pager.addOnPageChangeListener(this);

        for (int i = 0; i < tabs.getTabCount(); i++) {
            View tab = ((ViewGroup) tabs.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 10, 10, 10);
            tab.requestLayout();
        }
    }

    private void updateTabVisibility() {
        // hide the tab bar when only a single tab is visible
        tabs.setVisibility(pagerAdapter.getCount() == 1 ? View.GONE : View.VISIBLE);
    }

    public Fragment getCurrentFragment() {
        return pagerAdapter.getFragment(pager.getCurrentItem());
    }

    private boolean isPlaylistPage() {
        return getCurrentFragment() instanceof PlaylistsFragment;
    }

    @NonNull
    @Override
    public MaterialCab openCab(final int menuRes, final MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) cab.finish();
        cab = new MaterialCab(getMainActivity(), R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(PlayerColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor(requireActivity())))
                .start(callback);
        return cab;
    }

    public void addOnAppBarOffsetChangedListener(AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        appbar.addOnOffsetChangedListener(onOffsetChangedListener);
    }

    public void removeOnAppBarOffsetChangedListener(AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        appbar.removeOnOffsetChangedListener(onOffsetChangedListener);
    }

    public int getTotalAppBarScrollingRange() {
        return appbar.getTotalScrollRange();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (pager == null) return;
//        Util.setMenuBackground(requireActivity());
        inflater.inflate(R.menu.menu_main, menu);
        if (isPlaylistPage()) {
            menu.add(0, R.id.action_new_playlist, 0, R.string.new_playlist_title);
        }
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof LibraryPagerCustomGridSizeFragment && currentFragment.isAdded()) {
            LibraryPagerCustomGridSizeFragment libraryRecyclerViewCustomGridSizeFragment = (LibraryPagerCustomGridSizeFragment) currentFragment;

            MenuItem gridSizeItem = menu.findItem(R.id.action_grid_size);
            if (Util.isLandscape(getResources())) {
                gridSizeItem.setTitle(R.string.action_grid_size_land);
            }
            setUpGridSizeMenu(libraryRecyclerViewCustomGridSizeFragment, gridSizeItem.getSubMenu());

            menu.findItem(R.id.action_colored_footers).setChecked(libraryRecyclerViewCustomGridSizeFragment.usePalette());
            menu.findItem(R.id.action_colored_footers).setEnabled(libraryRecyclerViewCustomGridSizeFragment.canUsePalette());

            setUpSortOrderMenu(libraryRecyclerViewCustomGridSizeFragment, menu.findItem(R.id.action_sort_order).getSubMenu());
        } else {
            menu.removeItem(R.id.action_grid_size);
            menu.removeItem(R.id.action_colored_footers);
            menu.removeItem(R.id.action_sort_order);
        }
        Activity activity = getActivity();
        if (activity == null) return;
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(getActivity(), toolbar, menu, ATHToolbarActivity.getToolbarBackgroundColor(toolbar));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Activity activity = getActivity();
        if (activity == null) return;
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(activity, toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (pager == null) return false;
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof LibraryPagerCustomGridSizeFragment) {
            LibraryPagerCustomGridSizeFragment absLibraryRecyclerViewCustomGridSizeFragment = (LibraryPagerCustomGridSizeFragment) currentFragment;
            if (item.getItemId() == R.id.action_colored_footers) {
                item.setChecked(!item.isChecked());
                absLibraryRecyclerViewCustomGridSizeFragment.setAndSaveUsePalette(item.isChecked());
                return true;
            }
            if (handleGridSizeMenuItem(absLibraryRecyclerViewCustomGridSizeFragment, item)) {
                return true;
            }
            if (handleSortOrderMenuItem(absLibraryRecyclerViewCustomGridSizeFragment, item)) {
                return true;
            }
        }

        int id = item.getItemId();
        if (id == R.id.action_shuffle_all) {
            MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(requireActivity()), true);
            return true;
        } else if (id == R.id.action_new_playlist) {
            CreatePlaylistDialog.create().show(getChildFragmentManager(), "CREATE_PLAYLIST");
            return true;
        } else if (id == R.id.action_search) {
            ((AdsBaseActivity) requireActivity()).showInterstitialAd(requireActivity(), new Intent(getActivity(), SearchActivity.class), null);
            return true;
        } else if (id == R.id.action_sleep_timer) {
            if (MusicService.playback.isPlaying()) {
                new SleepTimerDialog().show(getChildFragmentManager(), "SET_SLEEP_TIMER");
            } else {
                new SleepTimerOFFDialog().show(getChildFragmentManager(), "SET_SLEEP_TIMER");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpGridSizeMenu(@NonNull LibraryPagerCustomGridSizeFragment fragment, @NonNull SubMenu gridSizeMenu) {
        switch (fragment.getGridSize()) {
            case 1:
                gridSizeMenu.findItem(R.id.action_grid_size_1).setChecked(true);
                break;
            case 2:
                gridSizeMenu.findItem(R.id.action_grid_size_2).setChecked(true);
                break;
            case 3:
                gridSizeMenu.findItem(R.id.action_grid_size_3).setChecked(true);
                break;
            case 4:
                gridSizeMenu.findItem(R.id.action_grid_size_4).setChecked(true);
                break;
            case 5:
                gridSizeMenu.findItem(R.id.action_grid_size_5).setChecked(true);
                break;
            case 6:
                gridSizeMenu.findItem(R.id.action_grid_size_6).setChecked(true);
                break;
            case 7:
                gridSizeMenu.findItem(R.id.action_grid_size_7).setChecked(true);
                break;
            case 8:
                gridSizeMenu.findItem(R.id.action_grid_size_8).setChecked(true);
                break;
        }
        int maxGridSize = fragment.getMaxGridSize();
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(R.id.action_grid_size_8).setVisible(false);
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(R.id.action_grid_size_7).setVisible(false);
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(R.id.action_grid_size_6).setVisible(false);
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(R.id.action_grid_size_5).setVisible(false);
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(R.id.action_grid_size_4).setVisible(false);
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(R.id.action_grid_size_3).setVisible(false);
        }
    }

    private boolean handleGridSizeMenuItem(@NonNull LibraryPagerCustomGridSizeFragment fragment, @NonNull MenuItem item) {
        int gridSize = 0;
        int itemId = item.getItemId();
        if (itemId == R.id.action_grid_size_1) {
            gridSize = 1;
        } else if (itemId == R.id.action_grid_size_2) {
            gridSize = 2;
        } else if (itemId == R.id.action_grid_size_3) {
            gridSize = 3;
        } else if (itemId == R.id.action_grid_size_4) {
            gridSize = 4;
        } else if (itemId == R.id.action_grid_size_5) {
            gridSize = 5;
        } else if (itemId == R.id.action_grid_size_6) {
            gridSize = 6;
        } else if (itemId == R.id.action_grid_size_7) {
            gridSize = 7;
        } else if (itemId == R.id.action_grid_size_8) {
            gridSize = 8;
        }
        if (gridSize > 0) {
            item.setChecked(true);
            fragment.setAndSaveGridSize(gridSize);
            toolbar.getMenu().findItem(R.id.action_colored_footers).setEnabled(fragment.canUsePalette());
            return true;
        }
        return false;
    }

    private void setUpSortOrderMenu(@NonNull LibraryPagerCustomGridSizeFragment fragment, @NonNull SubMenu sortOrderMenu) {
        String currentSortOrder = fragment.getSortOrder();
        sortOrderMenu.clear();

        if (fragment instanceof AlbumsFragment) {
            sortOrderMenu.add(0, R.id.action_album_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_A_Z));
            sortOrderMenu.add(0, R.id.action_album_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_Z_A));
            sortOrderMenu.add(0, R.id.action_album_sort_order_artist, 2, R.string.sort_order_artist)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_ARTIST));
            sortOrderMenu.add(0, R.id.action_album_sort_order_year, 3, R.string.sort_order_year)
                    .setChecked(currentSortOrder.equals(SortOrder.AlbumSortOrder.ALBUM_YEAR));
        } else if (fragment instanceof ArtistsFragment) {
            sortOrderMenu.add(0, R.id.action_artist_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SortOrder.ArtistSortOrder.ARTIST_A_Z));
            sortOrderMenu.add(0, R.id.action_artist_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SortOrder.ArtistSortOrder.ARTIST_Z_A));
        } else if (fragment instanceof SongsFragment) {
            sortOrderMenu.add(0, R.id.action_song_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_A_Z));
            sortOrderMenu.add(0, R.id.action_song_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_Z_A));
            sortOrderMenu.add(0, R.id.action_song_sort_order_artist, 2, R.string.sort_order_artist)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_ARTIST));
            sortOrderMenu.add(0, R.id.action_song_sort_order_album, 3, R.string.sort_order_album)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_ALBUM));
            sortOrderMenu.add(0, R.id.action_song_sort_order_year, 4, R.string.sort_order_year)
                    .setChecked(currentSortOrder.equals(SortOrder.SongSortOrder.SONG_YEAR));
        }

        sortOrderMenu.setGroupCheckable(0, true, true);
    }

    private boolean handleSortOrderMenuItem(@NonNull LibraryPagerCustomGridSizeFragment fragment, @NonNull MenuItem item) {
        String sortOrder = null;
        if (fragment instanceof AlbumsFragment) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_album_sort_order_asc) {
                sortOrder = SortOrder.AlbumSortOrder.ALBUM_A_Z;
            } else if (itemId == R.id.action_album_sort_order_desc) {
                sortOrder = SortOrder.AlbumSortOrder.ALBUM_Z_A;
            } else if (itemId == R.id.action_album_sort_order_artist) {
                sortOrder = SortOrder.AlbumSortOrder.ALBUM_ARTIST;
            } else if (itemId == R.id.action_album_sort_order_year) {
                sortOrder = SortOrder.AlbumSortOrder.ALBUM_YEAR;
            }
        } else if (fragment instanceof ArtistsFragment) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_artist_sort_order_asc) {
                sortOrder = SortOrder.ArtistSortOrder.ARTIST_A_Z;
            } else if (itemId == R.id.action_artist_sort_order_desc) {
                sortOrder = SortOrder.ArtistSortOrder.ARTIST_Z_A;
            }
        } else if (fragment instanceof SongsFragment) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_song_sort_order_asc) {
                sortOrder = SortOrder.SongSortOrder.SONG_A_Z;
            } else if (itemId == R.id.action_song_sort_order_desc) {
                sortOrder = SortOrder.SongSortOrder.SONG_Z_A;
            } else if (itemId == R.id.action_song_sort_order_artist) {
                sortOrder = SortOrder.SongSortOrder.SONG_ARTIST;
            } else if (itemId == R.id.action_song_sort_order_album) {
                sortOrder = SortOrder.SongSortOrder.SONG_ALBUM;
            } else if (itemId == R.id.action_song_sort_order_year) {
                sortOrder = SortOrder.SongSortOrder.SONG_YEAR;
            }
        }

        if (sortOrder != null) {
            item.setChecked(true);
            fragment.setAndSaveSortOrder(sortOrder);
            return true;
        }

        return false;
    }

    @Override
    public boolean handleBackPress() {
        if (cab != null && cab.isActive()) {
            cab.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        PreferenceUtil.getInstance(requireActivity()).setLastPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
