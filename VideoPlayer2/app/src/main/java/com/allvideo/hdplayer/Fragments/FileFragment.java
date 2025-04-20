package com.allvideo.hdplayer.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allvideo.hdplayer.Activities.MainActivity;
import com.allvideo.hdplayer.Activities.VideoFolderActivity;
import com.allvideo.hdplayer.Adapters.VideoAdapter;
import com.allvideo.hdplayer.AdsIntegration.AppUtility;
import com.allvideo.hdplayer.AdsIntegration.Constant;
import com.allvideo.hdplayer.Custom.Utils;
import com.allvideo.hdplayer.Models.VideoModel;
import com.allvideo.hdplayer.R;

import java.io.File;
import java.util.ArrayList;

public class FileFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;

    ImageView img_no_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_file, container, false);

        recyclerView = view.findViewById(R.id.files_recycler_view);
        img_no_data = view.findViewById(R.id.img_no_data);

        return view;
    }

    private void setRecyclerView() {
        if (Utils.videoModelArrayList.size() > 0) {
            img_no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            videoAdapter = new VideoAdapter(requireActivity(), Utils.videoModelArrayList, new VideoAdapter.OnDeleteVideoFile() {
                @Override
                public void onDelete(String path, int position) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deleteFile(path, position);
                        }
                    });
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

            recyclerView.setAdapter(videoAdapter);
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            recyclerView.setVisibility(View.GONE);
            img_no_data.setVisibility(View.VISIBLE);
        }
    }

    public void deleteFile(String path, int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Do you want to delete this video file?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, which) -> {
                    Log.d("--delete--", "onDelete path: " + path);

                    String realPath = getRealPathFromURI(Uri.parse(path));

                    File fileDelete = new File(realPath);
                    if (fileDelete.exists()) {

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            Toast.makeText(requireContext(), "Sorry, your mobile do not supports this feature", Toast.LENGTH_SHORT).show();
                        } else {
                            Utils.videoModelArrayList.remove(position);
                            videoAdapter.notifyItemRemoved(position);

                            if (delete(requireContext(), fileDelete)) {
                                Toast.makeText(requireContext(), "File deleted successfully!", Toast.LENGTH_SHORT).show();
                                Log.d("--delete--", "File deleted: " + fileDelete.getAbsolutePath());
                            } else {
                                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                            if (Utils.videoModelArrayList.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                img_no_data.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                img_no_data.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public boolean delete(final Context context, final File file) {

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
        return !file.exists();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = requireContext().getContentResolver().query(contentURI, null, null, null, null);
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
    public void onPause() {

        if (Utils.videoModelArrayList.size() > 0) {
            for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                if (Utils.videoModelArrayList.get(i) == null) {
                    Utils.videoModelArrayList.remove(i);
                }
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        setRecyclerView();
    }
}