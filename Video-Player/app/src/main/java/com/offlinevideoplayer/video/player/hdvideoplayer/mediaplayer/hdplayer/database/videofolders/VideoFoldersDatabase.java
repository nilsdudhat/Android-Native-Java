package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {VideoFolderModel.class}, version = 1)
public abstract class VideoFoldersDatabase extends RoomDatabase {

    private static VideoFoldersDatabase videoFoldersDatabase;
    private static final Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDBAsyncTask(videoFoldersDatabase).execute();
        }
    };

    public static synchronized VideoFoldersDatabase getInstance(Context context) {
        if (videoFoldersDatabase == null) {
            videoFoldersDatabase =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            VideoFoldersDatabase.class,
                            "video_folders_table").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return videoFoldersDatabase;
    }

    public abstract VideoFoldersDao videoFoldersDao();

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDBAsyncTask(VideoFoldersDatabase videoFoldersDatabase) {
            VideoFoldersDao videoFoldersDao = videoFoldersDatabase.videoFoldersDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
