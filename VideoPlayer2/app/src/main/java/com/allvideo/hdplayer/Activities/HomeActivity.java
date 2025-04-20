package com.allvideo.hdplayer.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.BuildConfig;
import com.allvideo.hdplayer.Custom.Utils;
import com.allvideo.hdplayer.Models.VideoModel;
import com.allvideo.hdplayer.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ImageView img_start;

    ProgressDialog progressDialog;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading Videos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AdUtils.showBanner(HomeActivity.this, findViewById(R.id.ad_banner));
        AdUtils.showMiniNativeAds(HomeActivity.this, findViewById(R.id.ad_mini_native));

        img_start = findViewById(R.id.img_start);

        img_start.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.card_1).setOnClickListener(v -> {
//            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
//            intent.putExtra("web_view", "quareka");
//            startActivity(intent);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://317.win.qureka.com/"));
            startActivity(browserIntent);
        });
        findViewById(R.id.card_2).setOnClickListener(v -> {
//            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
//            intent.putExtra("web_view", "quareka");
//            startActivity(intent);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://317.win.qureka.com/"));
            startActivity(browserIntent);
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Utils.videoModelArrayList = getAllVideos(HomeActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                progressDialog.dismiss();
            }
        }.execute();
    }

    public ArrayList<VideoModel> getAllVideos(Context context) {

        ArrayList<VideoModel> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        @SuppressLint("Recycle")
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
//                String duration = convertMillieToHMmSs(Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))));

                try {
                    Uri uriPath = Uri.parse(path);
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(HomeActivity.this, uriPath);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int millSecond = Integer.parseInt(durationStr);

                    String duration = convertMillieToHMmSs(millSecond); // use this duration

                    Log.e("path", path);
                    int slashFirstIndex = path.lastIndexOf("/");
                    String subString = path.substring(0, slashFirstIndex);
                    int index = subString.lastIndexOf("/");
                    String folderName = subString.substring(index + 1, slashFirstIndex);
                    if (!Utils.folderArrayList.contains(folderName)) {
                        Utils.folderArrayList.add(folderName);
                    }

                    VideoModel videoModel = new VideoModel(id, path, title, fileName, size, dateAdded, resolution, duration);
                    tempVideoFiles.add(videoModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        return tempVideoFiles;
    }

    @SuppressLint("DefaultLocale")
    public static String convertMillieToHMmSs(int j) {
        long j2 = j / 1000;
        long j3 = j2 % 60;
        long j4 = (j2 / 60) % 60;
        long j5 = (j2 / 3600) % 24;
        if (j5 > 0) {
            return String.format("%02d:%02d:%02d", j5, j4, j3);
        }
        return String.format("%02d:%02d", j4, j3);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        int i = preferences.getInt("rate", 0);
        if (i == 10) {
            i = 0;
            editor.putInt("rate", i);
            rate_app();
        } else {
            i++;
            editor.putInt("rate", i);
            exit();
        }
        editor.apply();
    }

    private void rate_app() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(HomeActivity.this, R.style.NewDialog);
        View sheetView = getLayoutInflater().inflate(R.layout.rate_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.show();

        mBottomSheetDialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                finish();
            }
        });

        mBottomSheetDialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
            }
        });
    }

    private void exit() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(HomeActivity.this, R.style.NewDialog);
        View sheetView = this.getLayoutInflater().inflate(R.layout.exit_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        mBottomSheetDialog.show();

        mBottomSheetDialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                finishAffinity();
            }
        });
    }
}
