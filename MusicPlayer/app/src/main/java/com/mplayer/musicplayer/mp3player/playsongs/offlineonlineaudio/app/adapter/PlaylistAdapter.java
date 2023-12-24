package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.MyApplication;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.base.MediaEntryViewHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.adapter.base.MultiSelectAdapter;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.ClearSmartPlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.DeletePlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu.PlaylistMenuHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu.SongsMenuHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.CabHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.PlaylistSongLoader;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.misc.WeakContextAsyncTask;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.CustomPlaylist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Playlist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.smartplaylist.LastAddedPlaylist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.smartplaylist.SmartPlaylist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.NavigationUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.PlaylistsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends MultiSelectAdapter<PlaylistAdapter.ViewHolder, Playlist> {

    private static final int SMART_PLAYLIST = 0;
    private static final int DEFAULT_PLAYLIST = 1;

    protected final AdsBaseActivity activity;
    protected List<Playlist> dataSet;
    protected int itemLayoutRes;

    public PlaylistAdapter(AdsBaseActivity activity, List<Playlist> dataSet, @LayoutRes int itemLayoutRes, @Nullable CabHolder cabHolder) {
        super(activity, cabHolder, R.menu.menu_playlists_selection);
        this.activity = activity;
        this.dataSet = dataSet;
        this.itemLayoutRes = itemLayoutRes;
        setHasStableIds(true);
    }

    public List<Playlist> getDataSet() {
        return dataSet;
    }

    public void swapDataSet(List<Playlist> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).id;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false);
        return createViewHolder(view, viewType);
    }

    protected ViewHolder createViewHolder(View view, int viewType) {
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Playlist playlist = dataSet.get(position);

        if (playlist.name.equalsIgnoreCase("favourites")) {
            holder.text.setVisibility(View.GONE);

            if (holder.image != null) {
                int iconPadding = activity.getResources().getDimensionPixelSize(R.dimen.list_item_image_icon_padding);
                holder.image.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
            }
        } else if (playlist.name.equalsIgnoreCase("recently played")) {
            holder.text.setVisibility(View.GONE);

            if (holder.image != null) {
                int iconPadding = activity.getResources().getDimensionPixelSize(R.dimen.list_item_image_icon_padding);
                holder.image.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
            }
        } else if (playlist.name.equalsIgnoreCase("my top tracks")) {
            holder.text.setVisibility(View.GONE);

            if (holder.image != null) {
                int iconPadding = activity.getResources().getDimensionPixelSize(R.dimen.list_item_image_icon_padding);
                holder.image.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
            }
        } else {
            holder.text.setVisibility(View.VISIBLE);
            holder.image.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_playlist));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List list = (List) PlaylistSongLoader.getPlaylistSongList(activity, playlist.id);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("---size---", "run: " + list.size());
                            holder.text.setText(list.size() + " Songs");
                        }
                    });
                }
            }).start();
        }

        holder.itemView.setActivated(isChecked(playlist));

        if (holder.title != null) {
            holder.title.setText(playlist.name);
        }

        if (holder.getAdapterPosition() == getItemCount() - 1) {
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.GONE);
                holder.separator.setVisibility(View.GONE);
            }
        } else {
            if (holder.shortSeparator != null && !(dataSet.get(position) instanceof SmartPlaylist)) {
                holder.shortSeparator.setVisibility(View.GONE);
                holder.separator.setVisibility(View.VISIBLE);
            }
        }

        if (holder.image != null) {
            holder.image.setImageResource(getIconRes(playlist));
        }
    }

    private int getIconRes(Playlist playlist) {
        if (playlist instanceof SmartPlaylist) {
            return ((SmartPlaylist) playlist).iconRes;
        }
        return MusicUtil.isFavoritePlaylist(activity, playlist) ? R.drawable.ic_favorite : R.drawable.ic_playlist;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) instanceof SmartPlaylist ? SMART_PLAYLIST : DEFAULT_PLAYLIST;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    protected Playlist getIdentifier(int position) {
        return dataSet.get(position);
    }

    @Override
    protected String getName(Playlist playlist) {
        return playlist.name;
    }

    @Override
    protected void onMultipleItemAction(@NonNull MenuItem menuItem, @NonNull List<Playlist> selection) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_delete_playlist) {
            for (int i = 0; i < selection.size(); i++) {
                Playlist playlist = selection.get(i);
                if (playlist instanceof SmartPlaylist) {
                    SmartPlaylist smartPlaylist = (SmartPlaylist) playlist;
                    ClearSmartPlaylistDialog.create(smartPlaylist).show(activity.getSupportFragmentManager(), "CLEAR_PLAYLIST_" + smartPlaylist.name);
                    selection.remove(playlist);
                    i--;
                }
            }
            if (selection.size() > 0) {
                DeletePlaylistDialog.create(selection).show(activity.getSupportFragmentManager(), "DELETE_PLAYLIST");
            }
        } else if (itemId == R.id.action_save_playlist) {
            if (selection.size() == 1) {
                PlaylistMenuHelper.handleMenuClick(activity, selection.get(0), menuItem);
            } else {
                new SavePlaylistsAsyncTask(activity).execute(selection);
            }
        } else {
            SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.getItemId());
        }
    }

    private static class SavePlaylistsAsyncTask extends WeakContextAsyncTask<List<Playlist>, String, String> {
        public SavePlaylistsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(List<Playlist>... params) {
            int successes = 0;
            int failures = 0;

            String dir = "";

            for (Playlist playlist : params[0]) {
                try {
                    dir = PlaylistsUtil.savePlaylist(MyApplication.getInstance().getApplicationContext(), playlist).getParent();
                    successes++;
                } catch (IOException e) {
                    failures++;
                    e.printStackTrace();
                }
            }

            return failures == 0
                    ? String.format(MyApplication.getInstance().getApplicationContext().getString(R.string.saved_x_playlists_to_x), successes, dir)
                    : String.format(MyApplication.getInstance().getApplicationContext().getString(R.string.saved_x_playlists_to_x_failed_to_save_x), successes, dir, failures);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, string, Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    private List<Song> getSongList(@NonNull List<Playlist> playlists) {
        final List<Song> songs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            if (playlist instanceof CustomPlaylist) {
                songs.addAll(((CustomPlaylist) playlist).getSongs(activity));
            } else {
                songs.addAll(PlaylistSongLoader.getPlaylistSongList(activity, playlist.id));
            }
        }
        return songs;
    }

    public class ViewHolder extends MediaEntryViewHolder {

        public ViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);

            if (itemViewType == SMART_PLAYLIST) {
                if (shortSeparator != null) {
                    shortSeparator.setVisibility(View.GONE);
                }
//                itemView.setBackgroundColor(ATHUtil.resolveColor(activity, R.attr.cardBackgroundColor));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    itemView.setElevation(activity.getResources().getDimensionPixelSize(R.dimen.card_elevation));
                }
            }

            if (menu != null) {
                menu.setOnClickListener(view -> {
                    final PopupMenu popupMenu = new PopupMenu(activity, view);

                    final Playlist playlist = dataSet.get(getAdapterPosition());

                    popupMenu.inflate(getItemViewType() == SMART_PLAYLIST ? R.menu.menu_item_smart_playlist : R.menu.menu_item_playlist);
                    if (playlist instanceof LastAddedPlaylist) {
                        popupMenu.getMenu().findItem(R.id.action_clear_playlist).setVisible(false);
                    }
                    if (getItemViewType() == DEFAULT_PLAYLIST) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            popupMenu.getMenu().findItem(R.id.action_delete_playlist).setVisible(false);
                        }
                    }
                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.action_clear_playlist) {
                            if (playlist instanceof SmartPlaylist) {
                                ClearSmartPlaylistDialog.create((SmartPlaylist) playlist).show(activity.getSupportFragmentManager(), "CLEAR_SMART_PLAYLIST_" + playlist.name);
                                return true;
                            }
                        }
                        return PlaylistMenuHelper.handleMenuClick(
                                activity, dataSet.get(getAdapterPosition()), item);
                    });
                    popupMenu.show();
                });
            }
        }

        @Override
        public void onClick(View view) {
            if (isInQuickSelectMode()) {
                toggleChecked(getAdapterPosition());
            } else {
                Playlist playlist = dataSet.get(getAdapterPosition());
                NavigationUtil.goToPlaylist(activity, playlist);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            toggleChecked(getAdapterPosition());
            return true;
        }
    }
}