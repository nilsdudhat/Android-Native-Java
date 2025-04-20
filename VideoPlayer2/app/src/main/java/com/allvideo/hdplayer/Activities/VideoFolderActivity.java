package com.allvideo.hdplayer.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allvideo.hdplayer.Adapters.FolderVideoAdapter;
import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.Custom.Utils;
import com.allvideo.hdplayer.Models.VideoModel;
import com.allvideo.hdplayer.R;

import java.io.File;
import java.util.ArrayList;

public class VideoFolderActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    public static ArrayList<VideoModel> videoFilesArrayList = new ArrayList<>();

    FolderVideoAdapter folderVideoAdapter;

    String folderName;
    Toolbar toolbar;

    ProgressDialog progressDialog;

    ImageView img_no_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);

        AdUtils.showInterstitial(VideoFolderActivity.this);

        progressDialog = new ProgressDialog(VideoFolderActivity.this);
        progressDialog.setMessage("Loading Videos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        img_no_data = findViewById(R.id.img_no_data);
        recyclerView = findViewById(R.id.folder_video_recyclerview);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        folderName = getIntent().getStringExtra("folderName");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(folderName);

        gettingVideos();
    }

    private void setRecyclerView() {
        if (videoFilesArrayList.size() > 0) {
            img_no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            folderVideoAdapter = new FolderVideoAdapter(VideoFolderActivity.this, videoFilesArrayList, new FolderVideoAdapter.OnDeleteListener() {
                @Override
                public void onDelete(String path, int position) {
                    new AlertDialog.Builder(VideoFolderActivity.this)
                            .setTitle("Do you want to delete this video file?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        deleteFile(path, position);
                                        dialog.dismiss();
                                    }
                                });
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .create().show();
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(VideoFolderActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(folderVideoAdapter);
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            recyclerView.setVisibility(View.GONE);
            img_no_data.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void gettingVideos() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (folderName != null) {
                    videoFilesArrayList.clear();
                    videoFilesArrayList = getAllVideos(VideoFolderActivity.this, folderName);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                setRecyclerView();
                progressDialog.dismiss();
            }
        }.execute();
    }

    public ArrayList<VideoModel> getAllVideos(Context context, String folderName) {

        ArrayList<VideoModel> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        @SuppressLint("Recycle")
        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);
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
                    mmr.setDataSource(VideoFolderActivity.this, uriPath);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int millSecond = Integer.parseInt(durationStr);

                    String duration = convertMillieToHMmSs(millSecond); // use this duration

                    Log.e("path", path);
                    if (!Utils.folderArrayList.contains(folderName))
                        Utils.folderArrayList.add(folderName);

                    VideoModel videoModel = new VideoModel(id, path, title, fileName, size, dateAdded, resolution, duration);
                    tempVideoFiles.add(videoModel);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        return tempVideoFiles;
    }

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

    private void deleteFile(String path, int position) {
        Log.d("--delete--", "onDelete path: " + path);

        String realPath = getRealPathFromURI(Uri.parse(path));

        Log.d("--delete--", "onDelete realPath: " + realPath);

        File fileDelete = new File(realPath);
        if (fileDelete.exists()) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                Toast.makeText(VideoFolderActivity.this, "Sorry, your mobile do not supports this feature", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                    if (path.equals(Utils.videoModelArrayList.get(i).getPath())) {
                        Utils.videoModelArrayList.remove(i);
                        break;
                    }
                }

                videoFilesArrayList.remove(position);
                folderVideoAdapter.notifyItemRemoved(position);

                if (delete(VideoFolderActivity.this, fileDelete)) {
                    Toast.makeText(VideoFolderActivity.this, "File deleted successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("--delete--", "File deleted: " + fileDelete.getAbsolutePath());
                } else {
                    Toast.makeText(VideoFolderActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

                if (videoFilesArrayList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    img_no_data.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    img_no_data.setVisibility(View.VISIBLE);
                }
            }
        } else {
            Toast.makeText(VideoFolderActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    int REQUEST_PERM_DELETE = 1020;

    public boolean delete(final Context context, final File file) {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//            PendingIntent pi = MediaStore.createDeleteRequest(getContentResolver(), (Collection<Uri>) file);
//
//            try {
//                startIntentSenderForResult(pi.getIntentSender(), REQUEST_PERM_DELETE, null, 0, 0, 0);
//            } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
//        } else {

            final String where = MediaStore.MediaColumns.DATA + "=?";
            final String[] selectionArgs = new String[]{
                    file.getAbsolutePath()
            };
            final ContentResolver contentResolver = context.getContentResolver();
            final Uri filesUri = MediaStore.Files.getContentUri("external");

            contentResolver.delete(filesUri, where, selectionArgs);

            if (file.exists()) {

                contentResolver.delete(filesUri, where, selectionArgs);
            }
//        }
        return !file.exists();
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}