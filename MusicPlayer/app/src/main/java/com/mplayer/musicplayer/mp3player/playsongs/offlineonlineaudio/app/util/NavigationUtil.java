package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.AdsIntegration.AdsBaseActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.helper.MusicPlayerRemote;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Genre;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.model.Playlist;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.AlbumDetailActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.ArtistDetailActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.GenreDetailActivity;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.PlaylistDetailActivity;

public class NavigationUtil {

    public static void goToArtist(@NonNull final AdsBaseActivity activity, final long artistId, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, ArtistDetailActivity.class);
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_ID, artistId);

        //noinspection unchecked
        if (sharedElements != null && sharedElements.length > 0) {
            activity.showInterstitialAd(activity, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements).toBundle());
        } else {
            activity.showInterstitialAd(activity, intent, null);
        }
    }

    public static void goToAlbum(@NonNull final AdsBaseActivity activity, final long albumId, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.putExtra(AlbumDetailActivity.EXTRA_ALBUM_ID, albumId);

        //noinspection unchecked
        if (sharedElements != null && sharedElements.length > 0) {
            activity.showInterstitialAd(activity, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements).toBundle());
        } else {
            activity.showInterstitialAd(activity, intent, null);
        }
    }

    public static void goToGenre(@NonNull final AdsBaseActivity activity, final Genre genre, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, GenreDetailActivity.class);
        intent.putExtra(GenreDetailActivity.EXTRA_GENRE, genre);

        activity.showInterstitialAd(activity, intent, null);
    }

    public static void goToPlaylist(@NonNull final AdsBaseActivity activity, final Playlist playlist, @Nullable Pair... sharedElements) {
        final Intent intent = new Intent(activity, PlaylistDetailActivity.class);
        intent.putExtra(PlaylistDetailActivity.EXTRA_PLAYLIST, playlist);

        activity.showInterstitialAd(activity, intent, null);
    }

    public static void openEqualizer(@NonNull final Activity activity) {
        final int sessionId = MusicPlayerRemote.getAudioSessionId();
        if (sessionId == AudioEffect.ERROR_BAD_VALUE) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_audio_ID), Toast.LENGTH_LONG).show();
        } else {
            try {
                final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId);
                effects.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                activity.startActivityForResult(effects, 0);
            } catch (@NonNull final ActivityNotFoundException notFound) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_equalizer), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
