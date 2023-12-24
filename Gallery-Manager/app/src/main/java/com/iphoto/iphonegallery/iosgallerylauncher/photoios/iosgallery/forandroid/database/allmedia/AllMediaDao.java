package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AllMediaDao {

    @Insert
    void insertMedia(MediaModel mediaModel);

    @Update
    void updateMedia(MediaModel mediaModel);

    @Delete
    void deleteMedia(MediaModel mediaModel);

    @Query("SELECT EXISTS(SELECT * FROM all_media_table WHERE path = :path)")
    int isMediaExist(String path);

    @Query("DELETE FROM all_media_table WHERE id NOT IN (SELECT MIN(id) FROM all_media_table GROUP BY path)")
    void deleteDuplicates();

    @Query("SELECT * FROM all_media_table WHERE path = :path")
    MediaModel getMediaByPath(String path);

    @Query("DELETE FROM all_media_table")
    void deleteAllMedia();

    @Query("SELECT * FROM all_media_table ORDER BY dateModified DESC")
    LiveData<List<MediaModel>> getAllMediaData();
}
