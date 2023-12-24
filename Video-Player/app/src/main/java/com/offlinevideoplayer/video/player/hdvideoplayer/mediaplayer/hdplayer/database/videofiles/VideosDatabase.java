package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {VideoModel.class}, version = 1)
public abstract class VideosDatabase extends RoomDatabase {

    private static VideosDatabase videosDatabase;
    private static final Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDBAsyncTask(videosDatabase).execute();
        }
    };

    public static synchronized VideosDatabase getInstance(Context context) {
        if (videosDatabase == null) {
            videosDatabase =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            VideosDatabase.class,
                            "videos_table").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return videosDatabase;
    }

    public abstract VideosDao videosDao();

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDBAsyncTask(VideosDatabase videosDatabase) {
            VideosDao videosDao = videosDatabase.videosDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
