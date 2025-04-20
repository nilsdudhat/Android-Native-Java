package com.xbeat.videostatus.statusmaker.Database;

import android.content.ContentValues;
import android.database.Cursor;

public class Favourites {

    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "favourites";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "id";
    // These fields can be anything you want.
    public static final String COL_CREATED = "created";
    public static final String COL_HEIGHT = "height";
    public static final String COL_IS_HOT = "is_hot";
    public static final String COL_IS_NEW = "is_new";
    public static final String COL_THUMB_URL = "thumb_url";
    public static final String COL_TITLE = "title";
    public static final String COL_VIDEO_URL = "video_url";
    public static final String COL_WIDTH = "width";
    public static final String COL_ZIP = "zip";
    public static final String COL_ZIP_URL = "zip_url";
    public static final String COL_VIDEO_ID = "video_id";

    // For database projection so order is consistent
    public static final String[] FIELDS
            = {COL_ID, COL_CREATED, COL_HEIGHT, COL_IS_HOT,
            COL_IS_NEW, COL_THUMB_URL, COL_TITLE, COL_VIDEO_URL,
            COL_WIDTH, COL_ZIP, COL_ZIP_URL, COL_VIDEO_ID};

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_CREATED + " TEXT NOT NULL DEFAULT '',"
                    + COL_HEIGHT + " TEXT NOT NULL DEFAULT '',"
                    + COL_IS_HOT + " TEXT NOT NULL DEFAULT '',"
                    + COL_IS_NEW + " TEXT NOT NULL DEFAULT '',"
                    + COL_THUMB_URL + " TEXT NOT NULL DEFAULT '',"
                    + COL_TITLE + " TEXT NOT NULL DEFAULT ''"
                    + COL_VIDEO_URL + " TEXT NOT NULL DEFAULT ''"
                    + COL_WIDTH + " TEXT NOT NULL DEFAULT ''"
                    + COL_ZIP + " TEXT NOT NULL DEFAULT ''"
                    + COL_ZIP_URL + " TEXT NOT NULL DEFAULT ''"
                    + COL_VIDEO_ID + " TEXT NOT NULL DEFAULT ''"
                    + ")";

    // Fields corresponding to database columns
    public String id = "id";
    public String created = "created";
    public String height = "height";
    public String is_hot = "is_hot";
    public String is_new = "is_new";
    public String thumb_url = "thumb_url";
    public String title = "title";
    public String video_url = "video_url";
    public String width = "width";
    public String zip = "zip";
    public String zip_url = "zip_url";
    public String video_id = "video_id";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public Favourites() {
    }

    /**
     * Convert information from the database into a Calender object.
     */
    public Favourites(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getString(0);
        this.created = cursor.getString(1);
        this.height = cursor.getString(2);
        this.is_hot = cursor.getString(3);
        this.is_new = cursor.getString(4);
        this.thumb_url = cursor.getString(5);
        this.title = cursor.getString(6);
        this.video_url = cursor.getString(7);
        this.width = cursor.getString(8);
        this.zip = cursor.getString(9);
        this.zip_url = cursor.getString(10);
        this.video_id = cursor.getString(11);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_CREATED, created);
        values.put(COL_HEIGHT, height);
        values.put(COL_IS_HOT, is_hot);
        values.put(COL_IS_NEW, is_new);
        values.put(COL_THUMB_URL, thumb_url);
        values.put(COL_TITLE, title);
        values.put(COL_VIDEO_URL, video_url);
        values.put(COL_WIDTH, width);
        values.put(COL_ZIP, zip);
        values.put(COL_ZIP_URL, zip_url);
        values.put(COL_VIDEO_ID, video_id);

        return values;
    }
}
