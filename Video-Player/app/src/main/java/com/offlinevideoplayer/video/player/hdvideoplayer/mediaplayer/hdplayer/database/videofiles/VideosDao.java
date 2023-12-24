package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VideosDao {

    @Insert
    void insert(VideoModel videoModel);

    @Update
    void update(VideoModel videoModel);

    @Delete
    void delete(VideoModel videoModel);

    @Query("SELECT EXISTS(SELECT * FROM videos_table WHERE path = :path)")
    boolean isVideoModelExist(String path);

    @Query("DELETE FROM videos_table")
    void deleteAllVideos();

    @Query("SELECT * FROM videos_table WHERE path = :path")
    VideoModel getSingleDataByPath(String path);

    @Query("SELECT * FROM videos_table WHERE parentName = :parentName")
    List<VideoModel> getListByParentName(String parentName);

    @Query("SELECT * FROM videos_table ORDER BY date DESC")
    LiveData<List<VideoModel>> getVideos();
}