package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.ui.activities.tageditor;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.kabouzeid.appthemehelper.util.ToolbarContentTintHelper;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;
import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.loader.SongLoader;

import org.jaudiotagger.tag.FieldKey;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SongTagEditorActivity extends AbsTagEditorActivity implements TextWatcher {

    EditText songTitle;
    EditText albumTitle;
    EditText artist;
    EditText genre;
    EditText year;
    EditText trackNumber;
    EditText lyrics;

    private void initViews() {
        songTitle = findViewById(R.id.title1);
        albumTitle = findViewById(R.id.title2);
        artist = findViewById(R.id.artist);
        genre = findViewById(R.id.genre);
        year = findViewById(R.id.year);
        trackNumber = findViewById(R.id.image_text);
        lyrics = findViewById(R.id.lyrics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();

        setNoImageMode();
        setUpViews();

        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.action_tag_editor);
    }

    private void setUpViews() {
        fillViewsWithFileTags();
        songTitle.addTextChangedListener(this);
        albumTitle.addTextChangedListener(this);
        artist.addTextChangedListener(this);
        genre.addTextChangedListener(this);
        year.addTextChangedListener(this);
        trackNumber.addTextChangedListener(this);
        lyrics.addTextChangedListener(this);
    }

    private void fillViewsWithFileTags() {
        songTitle.setText(getSongTitle());
        albumTitle.setText(getAlbumTitle());
        artist.setText(getArtistName());
        genre.setText(getGenreName());
        year.setText(getSongYear());
        trackNumber.setText(getTrackNumber());
        lyrics.setText(getLyrics());
    }

    @Override
    protected void loadCurrentImage() {

    }

    @Override
    protected void getImageFromLastFM() {

    }

    @Override
    protected void searchImageOnWeb() {

    }

    @Override
    protected void deleteImage() {

    }

    @Override
    protected void save() {
        Map<FieldKey, String> fieldKeyValueMap = new EnumMap<>(FieldKey.class);
        fieldKeyValueMap.put(FieldKey.TITLE, songTitle.getText().toString());
        fieldKeyValueMap.put(FieldKey.ALBUM, albumTitle.getText().toString());
        fieldKeyValueMap.put(FieldKey.ARTIST, artist.getText().toString());
        fieldKeyValueMap.put(FieldKey.GENRE, genre.getText().toString());
        fieldKeyValueMap.put(FieldKey.YEAR, year.getText().toString());
        fieldKeyValueMap.put(FieldKey.TRACK, trackNumber.getText().toString());
        fieldKeyValueMap.put(FieldKey.LYRICS, lyrics.getText().toString());
        writeValuesToFiles(fieldKeyValueMap, null);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_song_tag_editor;
    }

    @NonNull
    @Override
    protected List<String> getSongPaths() {
        List<String> paths = new ArrayList<>(1);
        paths.add(SongLoader.getSong(this, getId()).data);
        return paths;
    }

    @Override
    protected void loadImageFromFile(Uri imageFilePath) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        dataChanged();
    }

    @Override
    protected void setColors(int color) {
        super.setColors(color);
        int toolbarTitleColor = ToolbarContentTintHelper.toolbarTitleColor(this, color);
        songTitle.setTextColor(toolbarTitleColor);
        albumTitle.setTextColor(toolbarTitleColor);
    }
}
