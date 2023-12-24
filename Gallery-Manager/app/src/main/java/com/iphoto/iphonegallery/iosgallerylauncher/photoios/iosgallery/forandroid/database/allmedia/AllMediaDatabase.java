package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {MediaModel.class}, version = 1, exportSchema = false)
public abstract class AllMediaDatabase extends RoomDatabase {

    private static AllMediaDatabase allMediaDatabase;

    public abstract AllMediaDao allMediaDao();

    public static synchronized AllMediaDatabase getInstance(Context context) {
        if (allMediaDatabase == null) {
            allMediaDatabase =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AllMediaDatabase.class,
                            "all_media").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return allMediaDatabase;
    }

    private static final Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDBAsyncTask(allMediaDatabase).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDBAsyncTask(AllMediaDatabase allMediaDatabase) {
            AllMediaDao allMediaDao = allMediaDatabase.allMediaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
