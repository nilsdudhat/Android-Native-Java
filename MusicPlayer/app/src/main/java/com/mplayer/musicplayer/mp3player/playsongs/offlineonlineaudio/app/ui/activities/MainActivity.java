package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.mindinventory.midrawer.MIDrawerView;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.DeleteSongsDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.ScanMediaFolderChooserDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.SearchQueryHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.AlbumLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.ArtistLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.PlaylistSongLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.service.MusicService;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.base.SlidingMusicPanelActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.folders.FoldersFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.library.LibraryFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PreferenceUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SlidingMusicPanelActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int APP_INTRO_REQUEST = 100;

    private static final int LIBRARY = 0;
    private static final int FOLDERS = 1;

    NavigationView navigationView;
    MIDrawerView drawerLayout;
    LinearLayout navLibrary;
    LinearLayout navFolders;
    LinearLayout nav_scan_music;
    LinearLayout nav_settings;
    LinearLayout nav_rate_us;
    LinearLayout nav_share_app;
    LinearLayout nav_privacy_policy;
    LinearLayout nav_more_apps;
    TextView txtLibrary;
    TextView txtFolders;
    ImageView imgLibrary;
    ImageView imgFolders;

    @Nullable
    MainActivityFragmentCallbacks currentFragment;

    private boolean blockRequestPermissions;

    private void initViews() {
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navLibrary = findViewById(R.id.nav_library);
        navFolders = findViewById(R.id.nav_folders);
        nav_scan_music = findViewById(R.id.nav_scan_music);
        nav_settings = findViewById(R.id.nav_settings);
        nav_rate_us = findViewById(R.id.nav_rate_us);
        nav_share_app = findViewById(R.id.nav_share_app);
        nav_privacy_policy = findViewById(R.id.nav_privacy_policy);
        nav_more_apps = findViewById(R.id.nav_more_apps);
        txtLibrary = findViewById(R.id.txt_library);
        txtFolders = findViewById(R.id.txt_folders);
        imgLibrary = findViewById(R.id.img_library);
        imgFolders = findViewById(R.id.img_folders);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawUnderStatusbar();
        initViews();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            navigationView.setFitsSystemWindows(false); // for header to go below statusbar
        }

        setUpDrawerLayout();

        if (savedInstanceState == null) {
            setMusicChooser(PreferenceUtil.getInstance(this).getLastMusicChooser());
        } else {
            restoreCurrentFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setMusicChooser(int key) {

        PreferenceUtil.getInstance(this).setLastMusicChooser(key);
        switch (key) {
            case LIBRARY:
                setLibraryAsCurrentFragment();
                break;
            case FOLDERS:
                setFolderAsCurrentFragment();
                break;
        }
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, null).commit();
        currentFragment = (MainActivityFragmentCallbacks) fragment;
    }

    private void restoreCurrentFragment() {
        currentFragment = (MainActivityFragmentCallbacks) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_INTRO_REQUEST) {
            blockRequestPermissions = false;
            if (!hasPermissions()) {
                requestPermissions();
            }
        }

        if (requestCode == 9876) {
            if (resultCode == Activity.RESULT_OK) {
                for (int i = 0; i < DeleteSongsDialog.songs.size(); i++) {
                    MusicPlayerRemote.removeFromQueue(DeleteSongsDialog.songs.get(i));
                }
            }
        }
    }

    @Override
    protected void requestPermissions() {
        if (!blockRequestPermissions) super.requestPermissions();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        super.onPanelSlide(panel, slideOffset);

        statusBarView.setVisibility(View.GONE);
    }

    @Override
    protected View createContentView() {
        @SuppressLint("InflateParams")
        View contentView = getLayoutInflater().inflate(R.layout.activity_main_drawer_layout, null);
        ViewGroup drawerContent = contentView.findViewById(R.id.drawer_content_container);
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content));
        return contentView;
    }

    private void setUpDrawerLayout() {

        drawerLayout.setMIDrawerListener(new MIDrawerView.MIDrawerEvents() {
            @Override
            public void onDrawerOpened(@NonNull View view) {
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
            }
        });

        drawerLayout.setScrimColor(Color.TRANSPARENT);

        navLibrary.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                PreferenceUtil.getInstance(MainActivity.this).setLastMusicChooser(LIBRARY);
                setLibraryAsCurrentFragment();
            }
        });

        navFolders.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                PreferenceUtil.getInstance(MainActivity.this).setLastMusicChooser(FOLDERS);
                setFolderAsCurrentFragment();
            }
        });

        nav_scan_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);

                new Handler().postDelayed(() -> {
                    ScanMediaFolderChooserDialog dialog = ScanMediaFolderChooserDialog.create();
                    dialog.show(getSupportFragmentManager(), "SCAN_MEDIA_FOLDER_CHOOSER");
                }, 200);
            }
        });

        nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                drawerLayout.closeDrawer(GravityCompat.START);
                showInterstitialAd(MainActivity.this, new Intent(MainActivity.this, SettingsActivity.class), null);
            }
        });

        nav_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);

                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }
            }
        });

        nav_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        nav_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);

                Util.openBrowser(MainActivity.this, "https://www.bluemoonmobileapps.com/privacy.html");
            }
        });

        nav_more_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);

                Util.openBrowser(MainActivity.this, "https://play.google.com/store/apps/dev?id=4710251405227521498");
            }
        });
    }

    private void setFolderAsCurrentFragment() {
        TypedValue typedValueText = new TypedValue();
        getTheme().resolveAttribute(R.attr.textColor, typedValueText, true);
        int textColor = ContextCompat.getColor(MainActivity.this, typedValueText.resourceId);

        TypedValue typedValueIcon = new TypedValue();
        getTheme().resolveAttribute(R.attr.textColor, typedValueIcon, true);
        int iconColor = ContextCompat.getColor(MainActivity.this, typedValueIcon.resourceId);

        navFolders.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.selected_drawer_navigation));
        txtFolders.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
        imgFolders.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);

        txtLibrary.setTextColor(textColor);
        imgLibrary.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        navLibrary.setBackgroundColor(Color.TRANSPARENT);

        setCurrentFragment(FoldersFragment.newInstance(this));
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setLibraryAsCurrentFragment() {
        TypedValue typedValueText = new TypedValue();
        getTheme().resolveAttribute(R.attr.textColor, typedValueText, true);
        int textColor = ContextCompat.getColor(MainActivity.this, typedValueText.resourceId);

        TypedValue typedValueIcon = new TypedValue();
        getTheme().resolveAttribute(R.attr.textColor, typedValueIcon, true);
        int iconColor = ContextCompat.getColor(MainActivity.this, typedValueIcon.resourceId);

        navLibrary.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.selected_drawer_navigation));
        txtLibrary.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
        imgLibrary.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);

        txtFolders.setTextColor(textColor);
        imgFolders.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        navFolders.setBackgroundColor(Color.TRANSPARENT);

        setCurrentFragment(LibraryFragment.newInstance());
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        handlePlaybackIntent(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
            } else {
                drawerLayout.openDrawer(navigationView);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleBackPress() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
            return true;
        }
        return super.handleBackPress() || (currentFragment != null && currentFragment.handleBackPress());
    }

    private void handlePlaybackIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        Uri uri = intent.getData();
        String mimeType = intent.getType();
        boolean handled = false;

        if (intent.getAction() != null && intent.getAction().equals(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)) {
            final List<Song> songs = SearchQueryHelper.getSongs(this, intent.getExtras());
            if (MusicPlayerRemote.getShuffleMode() == MusicService.SHUFFLE_MODE_SHUFFLE) {
                MusicPlayerRemote.openAndShuffleQueue(songs, true);
            } else {
                MusicPlayerRemote.openQueue(songs, 0, true);
            }
            handled = true;
        }

        if (uri != null && uri.toString().length() > 0) {
            MusicPlayerRemote.playFromUri(uri);
            handled = true;
        } else if (MediaStore.Audio.Playlists.CONTENT_TYPE.equals(mimeType)) {
            final long id = parseIdFromIntent(intent, "playlistId", "playlist");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                List<Song> songs = new ArrayList<>(PlaylistSongLoader.getPlaylistSongList(this, id));
                MusicPlayerRemote.openQueue(songs, position, true);
                handled = true;
            }
        } else if (MediaStore.Audio.Albums.CONTENT_TYPE.equals(mimeType)) {
            final long id = parseIdFromIntent(intent, "albumId", "album");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                MusicPlayerRemote.openQueue(AlbumLoader.getAlbum(this, id).songs, position, true);
                handled = true;
            }
        } else if (MediaStore.Audio.Artists.CONTENT_TYPE.equals(mimeType)) {
            final long id = parseIdFromIntent(intent, "artistId", "artist");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                MusicPlayerRemote.openQueue(ArtistLoader.getArtist(this, id).getSongs(), position, true);
                handled = true;
            }
        }
        if (handled) {
            setIntent(new Intent());
        }
    }

    private long parseIdFromIntent(@NonNull Intent intent, String longKey, String stringKey) {
        long id = intent.getLongExtra(longKey, -1);
        if (id < 0) {
            String idString = intent.getStringExtra(stringKey);
            if (idString != null) {
                try {
                    id = Long.parseLong(idString);
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return id;
    }

    @Override
    public void onPanelExpanded(View view) {
        super.onPanelExpanded(view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onPanelCollapsed(View view) {
        super.onPanelCollapsed(view);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onThemeChanged() {
        super.onThemeChanged();

        recreate();
    }

    public interface MainActivityFragmentCallbacks {
        boolean handleBackPress();
    }
}