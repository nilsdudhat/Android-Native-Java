package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VideoFoldersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoFolderModel videoFolderModel);

    @Update
    void update(VideoFolderModel videoFolderModel);

    @Delete
    void delete(VideoFolderModel videoFolderModel);

    @Query("SELECT * FROM video_folders_table WHERE path = :path")
    VideoFolderModel getSingleRowData(String path);

    @Query("DELETE FROM video_folders_table")
    void deleteAllVideoFolders();

    @Query("SELECT * FROM video_folders_table ORDER BY title ASC")
    LiveData<List<VideoFolderModel>> getAllVideoFolders();
}
