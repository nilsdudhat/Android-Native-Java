package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.folders;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.common.ATHToolbarActivity;
import com.kabouzeid.appthemehelper.util.ToolbarContentTintHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.SongFileAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.DeleteSongsDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu.SongMenuHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu.SongsMenuHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.CabHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.LoaderIds;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.DialogAsyncTask;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.UpdateToastMediaScannerCompletionListener;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.WrappedAsyncTaskLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.MainActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.fragments.mainactivity.MainActivityFragment;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.FileUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlayerColorUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PreferenceUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.ViewUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views.BreadCrumbLayout;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class FoldersFragment extends MainActivityFragment implements MainActivity.MainActivityFragmentCallbacks, CabHolder, BreadCrumbLayout.SelectionCallback, SongFileAdapter.Callbacks, AppBarLayout.OnOffsetChangedListener, LoaderManager.LoaderCallbacks<List<File>> {

    private static final int LOADER_ID = LoaderIds.FOLDERS_FRAGMENT;

    protected static final String PATH = "path";
    protected static final String CRUMBS = "crumbs";

    CoordinatorLayout coordinatorLayout;

    View container;

    View empty;

    Toolbar toolbar;

    BreadCrumbLayout breadCrumbs;

    AppBarLayout appbar;

    FastScrollRecyclerView recyclerView;

    private void initViews(View view) {
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        container = view.findViewById(R.id.container);
        empty = view.findViewById(android.R.id.empty);
        toolbar = view.findViewById(R.id.toolbar);
        breadCrumbs = view.findViewById(R.id.bread_crumbs);
        appbar = view.findViewById(R.id.appbar);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private SongFileAdapter adapter;
    private MaterialCab cab;

    public FoldersFragment() {
    }

    public static FoldersFragment newInstance(Context context) {
        return newInstance(PreferenceUtil.getInstance(context).getStartDirectory());
    }

    public static FoldersFragment newInstance(File directory) {
        FoldersFragment frag = new FoldersFragment();
        Bundle b = new Bundle();
        b.putSerializable(PATH, directory);
        frag.setArguments(b);
        return frag;
    }

    public void setCrumb(BreadCrumbLayout.Crumb crumb, boolean addToHistory) {
        if (crumb == null) return;
        saveScrollPosition();
        breadCrumbs.setActiveOrAdd(crumb, false);
        if (addToHistory) {
            breadCrumbs.addHistory(crumb);
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void saveScrollPosition() {
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null) {
            crumb.setScrollPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        }
    }

    @Nullable
    private BreadCrumbLayout.Crumb getActiveCrumb() {
        return breadCrumbs != null && breadCrumbs.size() > 0 ? breadCrumbs.getCrumb(breadCrumbs.getActiveIndex()) : null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CRUMBS, breadCrumbs.getStateWrapper());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            setCrumb(new BreadCrumbLayout.Crumb(FileUtil.safeGetCanonicalFile((File) getArguments().getSerializable(PATH))), true);
        } else {
            breadCrumbs.restoreFromStateWrapper(savedInstanceState.getParcelable(CRUMBS));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getMainActivity().setStatusbarColorAuto();
        getMainActivity().setNavigationbarColorAuto();
        getMainActivity().setTaskDescriptionColorAuto();

//        setUpAppbarColor();
        setUpToolbar();
        setUpBreadCrumbs();
        setUpRecyclerView();
        setUpAdapter();
    }

    private void setUpAppbarColor() {
        int primaryColor = ThemeStore.primaryColor(requireActivity());
        appbar.setBackgroundColor(primaryColor);
        toolbar.setBackgroundColor(primaryColor);
        breadCrumbs.setBackgroundColor(primaryColor);
        breadCrumbs.setActivatedContentColor(ToolbarContentTintHelper.toolbarTitleColor(requireActivity(), primaryColor));
        breadCrumbs.setDeactivatedContentColor(ToolbarContentTintHelper.toolbarSubtitleColor(requireActivity(), primaryColor));
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        requireActivity().setTitle(R.string.app_name);
        getMainActivity().setSupportActionBar(toolbar);
    }

    private void setUpBreadCrumbs() {
        breadCrumbs.setCallback(this);
    }

    private void setUpRecyclerView() {
        ViewUtil.setUpFastScrollRecyclerViewColor(getActivity(), recyclerView, ThemeStore.accentColor(requireActivity()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        appbar.addOnOffsetChangedListener(this);
    }

    private void setUpAdapter() {
        adapter = new SongFileAdapter(getMainActivity(), new LinkedList<>(), R.layout.item_list, this, this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
            }
        });
        recyclerView.setAdapter(adapter);
        checkIsEmpty();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveScrollPosition();
    }

    @Override
    public void onDestroyView() {
        appbar.removeOnOffsetChangedListener(this);
        super.onDestroyView();
    }

    @Override
    public boolean handleBackPress() {
        if (cab != null && cab.isActive()) {
            cab.finish();
            return true;
        }
        if (breadCrumbs.popHistory()) {
            setCrumb(breadCrumbs.lastHistory(), false);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public MaterialCab openCab(int menuRes, MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) cab.finish();
        cab = new MaterialCab(getMainActivity(), R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(PlayerColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor(requireActivity())))
                .start(callback);
        return cab;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_folders, menu);
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(requireActivity(), toolbar, menu, ATHToolbarActivity.getToolbarBackgroundColor(toolbar));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar);
    }

    public static final FileFilter AUDIO_FILE_FILTER = file -> !file.isHidden() && (file.isDirectory() ||
            FileUtil.fileIsMimeType(file, "audio/*", MimeTypeMap.getSingleton()) ||
            FileUtil.fileIsMimeType(file, "application/ogg", MimeTypeMap.getSingleton()));

    @Override
    public void onCrumbSelection(BreadCrumbLayout.Crumb crumb, int index) {
        setCrumb(crumb, true);
    }

    public static File getDefaultStartDirectory() {
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File startFolder;
        if (musicDir.exists() && musicDir.isDirectory()) {
            startFolder = musicDir;
        } else {
            File externalStorage = Environment.getExternalStorageDirectory();
            if (externalStorage.exists() && externalStorage.isDirectory()) {
                startFolder = externalStorage;
            } else {
                startFolder = new File("/"); // root
            }
        }
        return startFolder;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_go_to_start_directory) {
            setCrumb(new BreadCrumbLayout.Crumb(FileUtil.safeGetCanonicalFile(PreferenceUtil.getInstance(requireActivity()).getStartDirectory())), true);
            return true;
        } else if (item.getItemId() == R.id.action_scan) {
            BreadCrumbLayout.Crumb crumb = getActiveCrumb();
            if (crumb != null) {
                new ArrayListPathsAsyncTask(getActivity(), this::scanPaths).execute(new ArrayListPathsAsyncTask.LoadingInfo(crumb.getFile(), AUDIO_FILE_FILTER));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFileSelected(File file) {
        final File canonicalFile = FileUtil.safeGetCanonicalFile(file); // important as we compare the path value later
        if (canonicalFile.isDirectory()) {
            setCrumb(new BreadCrumbLayout.Crumb(canonicalFile), true);
        } else {
            FileFilter fileFilter = pathname -> !pathname.isDirectory() && AUDIO_FILE_FILTER.accept(pathname);
            new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> {
                int startIndex = -1;
                for (int i = 0; i < songs.size(); i++) {
                    if (canonicalFile.getPath().equals(songs.get(i).data)) {
                        startIndex = i;
                        break;
                    }
                }
                if (startIndex > -1) {
                    MusicPlayerRemote.openQueue(songs, startIndex, true);
                } else {
                    Snackbar.make(coordinatorLayout, Html.fromHtml(String.format(getString(R.string.not_listed_in_media_store), canonicalFile.getName())), Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_scan, v -> scanPaths(new String[]{canonicalFile.getPath()}))
                            .setActionTextColor(ThemeStore.accentColor(requireActivity()))
                            .show();
                }
            }).execute(new ListSongsAsyncTask.LoadingInfo(toList(canonicalFile.getParentFile()), fileFilter, getFileComparator()));
        }
    }

    @Override
    public void onMultipleItemAction(MenuItem item, List<File> files) {
        final int itemId = item.getItemId();
        new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> {
            if (!songs.isEmpty()) {
                SongsMenuHelper.handleMenuClick(requireActivity(), songs, itemId);
            }
            if (songs.size() != files.size()) {
                Snackbar.make(coordinatorLayout, R.string.some_files_are_not_listed_in_the_media_store, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_scan, v -> {
                            String[] paths = new String[files.size()];
                            for (int i = 0; i < files.size(); i++) {
                                paths[i] = FileUtil.safeGetCanonicalPath(files.get(i));
                            }
                            scanPaths(paths);
                        })
                        .setActionTextColor(ThemeStore.accentColor(requireActivity()))
                        .show();
            }
        }).execute(new ListSongsAsyncTask.LoadingInfo(files, AUDIO_FILE_FILTER, getFileComparator()));
    }

    private List<File> toList(File file) {
        List<File> files = new ArrayList<>(1);
        files.add(file);
        return files;
    }

    Comparator<File> fileComparator = (lhs, rhs) -> {
        if (lhs.isDirectory() && !rhs.isDirectory()) {
            return -1;
        } else if (!lhs.isDirectory() && rhs.isDirectory()) {
            return 1;
        } else {
            return lhs.getName().compareToIgnoreCase
                    (rhs.getName());
        }
    };

    private Comparator<File> getFileComparator() {
        return fileComparator;
    }

    private boolean manageMotionEvents(MotionEvent event, Dialog dialog) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dialog.dismiss();
                // touch down code
                break;

            case MotionEvent.ACTION_MOVE:
                // touch move code
                break;

            case MotionEvent.ACTION_UP:
                // touch up code
                break;
        }
        return true;
    }

    @Override
    public void onFileMenuClicked(final File file, View view) {

        Dialog dialog = new Dialog(requireActivity(), R.style.CustomDialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return manageMotionEvents(event, dialog);
            }
        });

        if (file.isDirectory()) {
            dialog.setContentView(R.layout.dialog_bottom_folders_directory);

            TextView txt_file_name = dialog.findViewById(R.id.txt_file_name);
            LinearLayout action_play_next = dialog.findViewById(R.id.action_play_next);
            LinearLayout action_add_to_current_playing = dialog.findViewById(R.id.action_add_to_current_playing);
            LinearLayout action_add_to_playlist = dialog.findViewById(R.id.action_add_to_playlist);
            LinearLayout action_delete_from_device = dialog.findViewById(R.id.action_delete_from_device);
            LinearLayout action_scan = dialog.findViewById(R.id.action_scan);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                //Do something
                action_add_to_playlist.setVisibility(View.GONE);
            }

            txt_file_name.setText(file.getName());

            action_play_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickInDirectoryDialog(file, action_play_next.getId());
                }
            });

            action_add_to_current_playing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_add_to_current_playing.getId());
                }
            });

            action_add_to_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_add_to_playlist.getId());
                }
            });

            action_delete_from_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> {
                        if (!songs.isEmpty()) {
                            List<Song> songList = new ArrayList<>();
                            for (int i = 0; i < songs.size(); i++) {
                                if (!MusicPlayerRemote.getCurrentSong().data.equals(songs.get(i).data)) {
                                    songList.add(songs.get(i));
                                } else {
                                    Toast.makeText(requireActivity(), "Could not delete " + songs.get(i).title + " as it is in playing mode now.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!songList.isEmpty()) {
                                DeleteSongsDialog.create(songList).show(requireActivity().getSupportFragmentManager(), "DELETE_SONGS");
                            }
                        } else {
                            Snackbar.make(coordinatorLayout, Html.fromHtml(String.format(getString(R.string.not_listed_in_media_store), file.getName())), Snackbar.LENGTH_LONG)
                                    .setAction(R.string.action_scan, v -> scanPaths(new String[]{FileUtil.safeGetCanonicalPath(file)}))
                                    .setActionTextColor(ThemeStore.accentColor(requireActivity()))
                                    .show();
                        }
                    }).execute(new ListSongsAsyncTask.LoadingInfo(toList(file), AUDIO_FILE_FILTER, getFileComparator()));
                }
            });

            action_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_add_to_playlist.getId());
                }
            });
        } else {
            dialog.setContentView(R.layout.dialog_bottom_folders_song);

            TextView txt_file_name = dialog.findViewById(R.id.txt_file_name);
            LinearLayout action_play_next = dialog.findViewById(R.id.action_play_next);
            LinearLayout action_add_to_current_playing = dialog.findViewById(R.id.action_add_to_current_playing);
            LinearLayout action_add_to_playlist = dialog.findViewById(R.id.action_add_to_playlist);
            LinearLayout action_go_to_album = dialog.findViewById(R.id.action_go_to_album);
            LinearLayout action_go_to_artist = dialog.findViewById(R.id.action_go_to_artist);
            LinearLayout action_share = dialog.findViewById(R.id.action_share);
            LinearLayout action_tag_editor = dialog.findViewById(R.id.action_tag_editor);
            LinearLayout action_details = dialog.findViewById(R.id.action_details);
            LinearLayout action_set_as_ringtone = dialog.findViewById(R.id.action_set_as_ringtone);
            LinearLayout action_delete_from_device = dialog.findViewById(R.id.action_delete_from_device);
            LinearLayout action_scan = dialog.findViewById(R.id.action_scan);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                //Do something
                action_add_to_playlist.setVisibility(View.GONE);
            }

            txt_file_name.setText(file.getName());

            action_play_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_play_next.getId());
                }
            });

            action_add_to_current_playing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_add_to_current_playing.getId());
                }
            });

            action_add_to_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_add_to_playlist.getId());
                }
            });

            action_go_to_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_go_to_album.getId());
                }
            });

            action_go_to_artist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_go_to_artist.getId());
                }
            });

            action_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_share.getId());
                }
            });

            action_tag_editor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_tag_editor.getId());
                }
            });

            action_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_details.getId());
                }
            });

            action_set_as_ringtone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_set_as_ringtone.getId());
                }
            });

            action_delete_from_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    itemClickedInFolderSongDialog(file, action_delete_from_device.getId());
                }
            });

            action_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    new ArrayListPathsAsyncTask(getActivity(), this::scanPaths).execute(new ArrayListPathsAsyncTask.LoadingInfo(file, AUDIO_FILE_FILTER));
                }

                private void scanPaths(String[] strings) {
                    if (getActivity() == null) return;
                    if (strings == null || strings.length < 1) {
                        Toast.makeText(getActivity(), R.string.nothing_to_scan, Toast.LENGTH_SHORT).show();
                    } else {
                        MediaScannerConnection.scanFile(getActivity().getApplicationContext(), strings, null, new UpdateToastMediaScannerCompletionListener(getActivity(), strings));
                    }
                }
            });
        }
        dialog.show();


//        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
//        if (file.isDirectory()) {
//            popupMenu.inflate(R.menu.menu_item_directory);
//            popupMenu.setOnMenuItemClickListener(item -> {
//                final int itemId = item.getItemId();
//                switch (itemId) {
//                    case R.id.action_play_next:
//                    case R.id.action_add_to_current_playing:
//                    case R.id.action_add_to_playlist:
//                    case R.id.action_delete_from_device:
//                        itemClickInDirectoryDialog(file, itemId);
//                        return true;
//                    case R.id.action_set_as_start_directory:
//                        PreferenceUtil.getInstance(requireActivity()).setStartDirectory(file);
//                        Toast.makeText(getActivity(), String.format(getString(R.string.new_start_directory), file.getPath()), Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.action_scan:
//                        new ArrayListPathsAsyncTask(getActivity(), this::scanPaths).execute(new ArrayListPathsAsyncTask.LoadingInfo(file, AUDIO_FILE_FILTER));
//                        return true;
//                }
//                return false;
//            });
//        } else {
//            popupMenu.inflate(R.menu.menu_item_file);
//            popupMenu.setOnMenuItemClickListener(item -> {
//                final int itemId = item.getItemId();
//                switch (itemId) {
//                    case R.id.action_play_next:
//                    case R.id.action_add_to_current_playing:
//                    case R.id.action_add_to_playlist:
//                    case R.id.action_go_to_album:
//                    case R.id.action_go_to_artist:
//                    case R.id.action_share:
//                    case R.id.action_tag_editor:
//                    case R.id.action_details:
//                    case R.id.action_set_as_ringtone:
//                    case R.id.action_delete_from_device:
//                        itemClickedInFolderSongDialog(file, itemId);
//                        return true;
//                    case R.id.action_scan:
//                        scanPaths(new String[]{FileUtil.safeGetCanonicalPath(file)});
//                        return true;
//                }
//                return false;
//            });
//        }
//        popupMenu.show();
    }

    private void itemClickInDirectoryDialog(File file, int id) {
        new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> {
            if (!songs.isEmpty()) {
                SongsMenuHelper.handleMenuClick(requireActivity(), songs, id);
            }
        }).execute(new ListSongsAsyncTask.LoadingInfo(toList(file), AUDIO_FILE_FILTER, getFileComparator()));
    }

    private void itemClickedInFolderSongDialog(File file, int itemId) {
        new ListSongsAsyncTask(getActivity(), null, (songs, extra) -> {
            if (!songs.isEmpty()) {
                SongMenuHelper.handleMenuClick((AdsBaseActivity) requireActivity(), songs.get(0), itemId);
            } else {
                Snackbar.make(coordinatorLayout, Html.fromHtml(String.format(getString(R.string.not_listed_in_media_store), file.getName())), Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_scan, v -> scanPaths(new String[]{FileUtil.safeGetCanonicalPath(file)}))
                        .setActionTextColor(ThemeStore.accentColor(requireActivity()))
                        .show();
            }
        }).execute(new ListSongsAsyncTask.LoadingInfo(toList(file), AUDIO_FILE_FILTER, getFileComparator()));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        container.setPadding(container.getPaddingLeft(), container.getPaddingTop(), container.getPaddingRight(), appbar.getTotalScrollRange() + verticalOffset);
    }

    private void checkIsEmpty() {
        if (empty != null) {
            empty.setVisibility(adapter == null || adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void scanPaths(@Nullable String[] toBeScanned) {
        if (getActivity() == null) return;
        if (toBeScanned == null || toBeScanned.length < 1) {
            Toast.makeText(getActivity(), R.string.nothing_to_scan, Toast.LENGTH_SHORT).show();
        } else {
            MediaScannerConnection.scanFile(getActivity().getApplicationContext(), toBeScanned, null, new UpdateToastMediaScannerCompletionListener(getActivity(), toBeScanned));
        }
    }

    private void updateAdapter(@NonNull List<File> files) {
        adapter.swapDataSet(files);
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null && recyclerView != null) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(crumb.getScrollPosition(), 0);
        }
    }

    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        return new AsyncFileLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<File>> loader, List<File> data) {
        updateAdapter(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<File>> loader) {
        updateAdapter(new LinkedList<>());
    }

    private static class AsyncFileLoader extends WrappedAsyncTaskLoader<List<File>> {
        private WeakReference<FoldersFragment> fragmentWeakReference;

        public AsyncFileLoader(FoldersFragment foldersFragment) {
            super(foldersFragment.getActivity());
            fragmentWeakReference = new WeakReference<>(foldersFragment);
        }

        @Override
        public List<File> loadInBackground() {
            FoldersFragment foldersFragment = fragmentWeakReference.get();
            File directory = null;
            if (foldersFragment != null) {
                BreadCrumbLayout.Crumb crumb = foldersFragment.getActiveCrumb();
                if (crumb != null) {
                    directory = crumb.getFile();
                }
            }
            if (directory != null) {
                List<File> files = FileUtil.listFiles(directory, AUDIO_FILE_FILTER);
                Collections.sort(files, foldersFragment.getFileComparator());
                return files;
            } else {
                return new LinkedList<>();
            }
        }
    }

    private static class ListSongsAsyncTask extends ListingFilesDialogAsyncTask<ListSongsAsyncTask.LoadingInfo, Void, List<Song>> {
        private WeakReference<Context> contextWeakReference;
        private WeakReference<OnSongsListedCallback> callbackWeakReference;
        private final Object extra;

        public ListSongsAsyncTask(Context context, Object extra, OnSongsListedCallback callback) {
            super(context, 500);
            this.extra = extra;
            contextWeakReference = new WeakReference<>(context);
            callbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkCallbackReference();
            checkContextReference();
        }

        @Override
        protected List<Song> doInBackground(LoadingInfo... params) {
            try {
                LoadingInfo info = params[0];
                List<File> files = FileUtil.listFilesDeep(info.files, info.fileFilter);

                if (isCancelled() || checkContextReference() == null || checkCallbackReference() == null)
                    return null;

                Collections.sort(files, info.fileComparator);

                Context context = checkContextReference();
                if (isCancelled() || context == null || checkCallbackReference() == null)
                    return null;

                return FileUtil.matchFilesWithMediaStore(context, files);
            } catch (Exception e) {
                e.printStackTrace();
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            OnSongsListedCallback callback = checkCallbackReference();
            if (songs != null && callback != null)
                callback.onSongsListed(songs, extra);
        }

        private Context checkContextReference() {
            Context context = contextWeakReference.get();
            if (context == null) {
                cancel(false);
            }
            return context;
        }

        private OnSongsListedCallback checkCallbackReference() {
            OnSongsListedCallback callback = callbackWeakReference.get();
            if (callback == null) {
                cancel(false);
            }
            return callback;
        }

        public static class LoadingInfo {
            public final Comparator<File> fileComparator;
            public final FileFilter fileFilter;
            public final List<File> files;

            public LoadingInfo(@NonNull List<File> files, @NonNull FileFilter fileFilter, @NonNull Comparator<File> fileComparator) {
                this.fileComparator = fileComparator;
                this.fileFilter = fileFilter;
                this.files = files;
            }
        }

        public interface OnSongsListedCallback {
            void onSongsListed(@NonNull List<Song> songs, Object extra);
        }
    }

    public static class ArrayListPathsAsyncTask extends ListingFilesDialogAsyncTask<ArrayListPathsAsyncTask.LoadingInfo, String, String[]> {
        private WeakReference<OnPathsListedCallback> onPathsListedCallbackWeakReference;

        public ArrayListPathsAsyncTask(Context context, OnPathsListedCallback callback) {
            super(context, 500);
            onPathsListedCallbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkCallbackReference();
        }

        @Override
        protected String[] doInBackground(LoadingInfo... params) {
            try {
                if (isCancelled() || checkCallbackReference() == null) return null;

                LoadingInfo info = params[0];

                final String[] paths;

                if (info.file.isDirectory()) {
                    List<File> files = FileUtil.listFilesDeep(info.file, info.fileFilter);

                    if (isCancelled() || checkCallbackReference() == null) return null;

                    paths = new String[files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        File f = files.get(i);
                        paths[i] = FileUtil.safeGetCanonicalPath(f);

                        if (isCancelled() || checkCallbackReference() == null) return null;
                    }
                } else {
                    paths = new String[1];
                    paths[0] = FileUtil.safeGetCanonicalPath(info.file);
                }

                return paths;
            } catch (Exception e) {
                e.printStackTrace();
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] paths) {
            super.onPostExecute(paths);
            OnPathsListedCallback callback = checkCallbackReference();
            if (callback != null && paths != null) {
                callback.onPathsListed(paths);
            }
        }

        private OnPathsListedCallback checkCallbackReference() {
            OnPathsListedCallback callback = onPathsListedCallbackWeakReference.get();
            if (callback == null) {
                cancel(false);
            }
            return callback;
        }

        public static class LoadingInfo {
            public final File file;
            public final FileFilter fileFilter;

            public LoadingInfo(File file, FileFilter fileFilter) {
                this.file = file;
                this.fileFilter = fileFilter;
            }
        }

        public interface OnPathsListedCallback {
            void onPathsListed(@NonNull String[] paths);
        }
    }

    private static abstract class ListingFilesDialogAsyncTask<Params, Progress, Result> extends DialogAsyncTask<Params, Progress, Result> {
        public ListingFilesDialogAsyncTask(Context context) {
            super(context);
        }

        public ListingFilesDialogAsyncTask(Context context, int showDelay) {
            super(context, showDelay);
        }

        @Override
        protected Dialog createDialog(@NonNull Context context) {
            return new MaterialDialog.Builder(context)
                    .title(R.string.listing_files)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .cancelListener(dialog -> cancel(false))
                    .dismissListener(dialog -> cancel(false))
                    .negativeText(android.R.string.cancel)
                    .onNegative((dialog, which) -> cancel(false))
                    .show();
        }
    }
}
