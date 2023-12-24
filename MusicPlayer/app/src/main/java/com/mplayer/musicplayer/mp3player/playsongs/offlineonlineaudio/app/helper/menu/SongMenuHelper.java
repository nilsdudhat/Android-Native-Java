package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.AddToPlaylistDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.DeleteSongsDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.dialogs.SongDetailDialog;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.interfaces.PaletteColorHolder;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Song;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor.AbsTagEditorActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor.SongTagEditorActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.MusicUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.NavigationUtil;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util.RingtoneManager;

public class SongMenuHelper {
    public static final int MENU_RES = R.menu.menu_item_song;
    public static final int Bottom_Res = R.layout.dialog_bottom_song;

    public static boolean handleMenuClick(@NonNull AdsBaseActivity activity, @NonNull Song song, int menuItemId) {
        if (menuItemId == R.id.action_set_as_ringtone) {
            if (RingtoneManager.requiresDialog(activity)) {
                RingtoneManager.showDialog(activity);
            } else {
                RingtoneManager ringtoneManager = new RingtoneManager();
                ringtoneManager.setRingtone(activity, song.id);
            }
            return true;
        } else if (menuItemId == R.id.action_share) {
            activity.startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(song, activity), null));
            return true;
        } else if (menuItemId == R.id.action_delete_from_device) {
            DeleteSongsDialog.create(song).show(activity.getSupportFragmentManager(), "DELETE_SONGS");
            return true;
        } else if (menuItemId == R.id.action_add_to_playlist) {
            AddToPlaylistDialog.create(song).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
            return true;
        } else if (menuItemId == R.id.action_play_next) {
            MusicPlayerRemote.playNext(song);
            return true;
        } else if (menuItemId == R.id.action_add_to_current_playing) {
            MusicPlayerRemote.enqueue(song);
            return true;
        } else if (menuItemId == R.id.action_tag_editor) {
            Intent tagEditorIntent = new Intent(activity, SongTagEditorActivity.class);
            tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_ID, song.id);
            if (activity instanceof PaletteColorHolder)
                tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_PALETTE, ((PaletteColorHolder) activity).getPaletteColor());
            activity.showInterstitialAd(activity, tagEditorIntent, null);
            return true;
        } else if (menuItemId == R.id.action_details) {
            SongDetailDialog.create(song).show(activity.getSupportFragmentManager(), "SONG_DETAILS");
            return true;
        } else if (menuItemId == R.id.action_go_to_album) {
            NavigationUtil.goToAlbum(activity, song.albumId);
            return true;
        } else if (menuItemId == R.id.action_go_to_artist) {
            NavigationUtil.goToArtist(activity, song.artistId);
            return true;
        }
        return false;
    }

    public static abstract class OnClickSongMenu implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private final AdsBaseActivity activity;

        public OnClickSongMenu(@NonNull AdsBaseActivity activity) {
            this.activity = activity;
        }

        public int getMenuRes() {
            return MENU_RES;
        }

        public int getLayoutRes() {
            return Bottom_Res;
        }

        @Override
        public void onClick(View v) {
//            PopupMenu popupMenu = new PopupMenu(activity, v);
//            popupMenu.inflate(getMenuRes());
//            popupMenu.setOnMenuItemClickListener(this);
//            popupMenu.show();

            Dialog dialog = new Dialog(activity, R.style.CustomDialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.setContentView(getLayoutRes());
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return manageMotionEvents(event, dialog);
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });

            TextView txt_song_name = dialog.findViewById(R.id.txt_song_name);
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

            txt_song_name.setText(getSong().title);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                //Do something
                action_add_to_playlist.setVisibility(View.GONE);
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    if (id == R.id.action_set_as_ringtone) {
                        dialog.dismiss();
                        if (RingtoneManager.requiresDialog(activity)) {
                            RingtoneManager.showDialog(activity);
                        } else {
                            RingtoneManager ringtoneManager = new RingtoneManager();
                            ringtoneManager.setRingtone(activity, getSong().id);
                        }
                    } else if (id == R.id.action_share) {
                        dialog.dismiss();
                        activity.startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(getSong(), activity), null));
                    } else if (id == R.id.action_delete_from_device) {
                        dialog.dismiss();
                        if (MusicPlayerRemote.getCurrentSong() == getSong()) {
                            Toast.makeText(activity, "You cannot delete this song as it is playing now.", Toast.LENGTH_SHORT).show();
                        } else {
                            DeleteSongsDialog.create(getSong()).show(activity.getSupportFragmentManager(), "DELETE_SONGS");
                        }
                    } else if (id == R.id.action_add_to_playlist) {
                        dialog.dismiss();
                        AddToPlaylistDialog.create(getSong()).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                    } else if (id == R.id.action_play_next) {
                        dialog.dismiss();
                        MusicPlayerRemote.playNext(getSong());
                    } else if (id == R.id.action_add_to_current_playing) {
                        dialog.dismiss();
                        MusicPlayerRemote.enqueue(getSong());
                    } else if (id == R.id.action_tag_editor) {
                        dialog.dismiss();
                        Intent tagEditorIntent = new Intent(activity, SongTagEditorActivity.class);
                        tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_ID, getSong().id);
                        if (activity instanceof PaletteColorHolder)
                            tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_PALETTE, ((PaletteColorHolder) activity).getPaletteColor());
                        activity.showInterstitialAd(activity, tagEditorIntent, null);
                    } else if (id == R.id.action_details) {
                        dialog.dismiss();
                        SongDetailDialog.create(getSong()).show(activity.getSupportFragmentManager(), "SONG_DETAILS");
                    } else if (id == R.id.action_go_to_album) {
                        dialog.dismiss();
                        NavigationUtil.goToAlbum(activity, getSong().albumId);
                    } else if (id == R.id.action_go_to_artist) {
                        dialog.dismiss();
                        NavigationUtil.goToArtist(activity, getSong().artistId);
                    }
                }
            };

            action_play_next.setOnClickListener(clickListener);
            action_add_to_current_playing.setOnClickListener(clickListener);
            action_add_to_playlist.setOnClickListener(clickListener);
            action_go_to_album.setOnClickListener(clickListener);
            action_go_to_artist.setOnClickListener(clickListener);
            action_share.setOnClickListener(clickListener);
            action_tag_editor.setOnClickListener(clickListener);
            action_details.setOnClickListener(clickListener);
            action_set_as_ringtone.setOnClickListener(clickListener);
            action_delete_from_device.setOnClickListener(clickListener);
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
        public boolean onMenuItemClick(MenuItem item) {
            return false;
//            return handleMenuClick(activity, getSong(), item.getItemId());
        }

        public abstract Song getSong();
    }
}