package com.xbeat.videostatus.statusmaker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xbeat.videostatus.statusmaker.Models.ModelVideoList;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favourites_data";
    public static final String TABLE_FAVOURITES = "favourites";

    // These field is autogenerate
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

    public static String getCreated = "";
    public static String getHeight = "";
    public static String getIs_Hot = "";
    public static String getIs_New = "";
    public static String getThumb_Url = "";
    public static String getTitle = "";
    public static String getVideo_Url = "";
    public static String getWidth = "";
    public static String getZip = "";
    public static String getZip_Url = "";
    public static String getVideo_Id = "";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CALENDER_TABLE_TABLE = "create table favourites " +
                "(id integer primary key," +
                " created text, height text, is_hot text, is_new text, thumb_url text, title text, video_url text, width text, zip text, zip_url text, video_id text)";
        sqLiteDatabase.execSQL(CREATE_CALENDER_TABLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // code to add the new contact

    public boolean insertCalender(
            String created, String height, String is_hot, String is_new,
            String thumb_url, String title, String video_url, String width,
            String zip, String zip_url, String video_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("created", created);
        contentValues.put("height", height);
        contentValues.put("is_hot", is_hot);
        contentValues.put("is_new", is_new);
        contentValues.put("thumb_url", thumb_url);
        contentValues.put("title", title);
        contentValues.put("video_url", video_url);
        contentValues.put("width", width);
        contentValues.put("zip", zip);
        contentValues.put("zip_url", zip_url);
        contentValues.put("video_id", video_id);
        db.insert(TABLE_FAVOURITES, null, contentValues);
        return true;
    }

    // below is the method for deleting our course.
    public void deleteItem(String courseName) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_FAVOURITES, "video_id=?", new String[]{courseName});
        db.close();
    }

    public boolean checkIsDataAlreadyInDBorNot(String TableName,
                                                      String dbField, String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbField + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<ModelVideoList> getModelArrayList(Context context) {
        List<ModelVideoList> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String qry = ("SELECT * FROM [favourites]");

            Cursor cur = db.rawQuery(qry, null);

            Log.d("--fillValue--", "qry: " + qry);
            Log.d("--fillValue--", "count: " + cur.getCount());

            while (cur.moveToNext()) {
                ModelVideoList getSet = new ModelVideoList();
                getSet.setCreated(cur.getLong(cur.getColumnIndex("created")));
                getSet.setHeight(Integer.parseInt(cur.getString(cur.getColumnIndex("height"))));
                getSet.setHot(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("is_hot"))));
                getSet.setNew(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("is_new"))));
                getSet.setThumbUrl(cur.getString(cur.getColumnIndex("thumb_url")));
                getSet.setTitle(cur.getString(cur.getColumnIndex("title")));
                getSet.setVideoUrl(cur.getString(cur.getColumnIndex("video_url")));
                getSet.setWidth(Integer.parseInt(cur.getString(cur.getColumnIndex("width"))));
                getSet.setZip(cur.getString(cur.getColumnIndex("zip")));
                getSet.setZipUrl(cur.getString(cur.getColumnIndex("zip_url")));
                getSet.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("video_id"))));

                list.add(getSet);
            }
        } catch (Exception e) {
            Log.d("--fillValue--", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // code to get the single contact
    public Cursor getCalender(Context context, String video_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favourites where video_id=" + video_id + "", null);

        if (res.moveToFirst() && (res.getCount() > 0)) {
            getCreated = res.getString(res.getColumnIndex("created"));
            getHeight = res.getString(res.getColumnIndex("height"));
            getIs_Hot = res.getString(res.getColumnIndex("is_hot"));
            getIs_New = res.getString(res.getColumnIndex("is_new"));
            getThumb_Url = res.getString(res.getColumnIndex("thumb_url"));
            getTitle = res.getString(res.getColumnIndex("title"));
            getVideo_Url = res.getString(res.getColumnIndex("video_url"));
            getWidth = res.getString(res.getColumnIndex("width"));
            getZip = res.getString(res.getColumnIndex("zip"));
            getZip_Url = res.getString(res.getColumnIndex("zip_url"));
            getVideo_Id = res.getString(res.getColumnIndex("video_id"));

//            txt_no_data.setVisibility(View.GONE);
//            ll_data.setVisibility(View.VISIBLE);
        } else {
//            getSteps = "0";
//            getMetric = "miles";
//            getDistance = "0.000";
//            getCalories = "0";
//            getTime = "0";
//
//            txt_no_data.setVisibility(View.VISIBLE);
//            ll_data.setVisibility(View.GONE);

//            Toast.makeText(context, "No data available on date " + fullDate, Toast.LENGTH_LONG).show();
        }
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_FAVOURITES);
        return numRows;
    }

    // code to update the single calender
    public int updateFavourites(
            int id, String created, String height, String is_hot,
            String is_new, String thumb_url, String title, String video_url,
            String width, String zip, String zip_url, String video_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
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

        // updating row
        return db.update(TABLE_FAVOURITES, values, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Deleting single calender
    public int deleteCalender() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVOURITES, null, null);
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVOURITES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // code to get all contacts in a list view
    public ArrayList<ModelVideoList> getAllCalender() {
        ArrayList<ModelVideoList> array_list = new ArrayList<ModelVideoList>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favourites", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
//            array_list.add(res.getString(res.getColumnIndex(COL_TITLE)));
            ModelVideoList obj = new ModelVideoList();
            array_list.add(obj);
            res.moveToNext();
        }
        return array_list;
    }
}