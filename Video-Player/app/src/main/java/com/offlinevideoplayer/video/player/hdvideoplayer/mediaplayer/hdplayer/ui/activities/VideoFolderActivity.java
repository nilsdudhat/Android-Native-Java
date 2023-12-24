package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities;

import static com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils.REQUEST_PERM_DELETE_VIDEO_FILE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AdsBaseActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters.VideoListAdapter;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFoldersViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media.MediaLoader;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoFolderActivity extends AdsBaseActivity
        implements VideoListAdapter.MoreOptionsClickListener, VideoListAdapter.PlayVideoClickListener {

    VideoFolderModel videoFolderModel;
    ArrayList<VideoModel> videoModelList = new ArrayList<>();

    ImageView img_back;
    TextView txt_title;

    SwipeRefreshLayout swipe_refresh_layout;
    RecyclerView rv_video_list;
    VideoListAdapter videoListAdapter;

    VideoViewModel videoViewModel;
    VideoFoldersViewModel videoFoldersViewModel;

    SharedPreferences mPrefs;
    boolean isDark = false;
    int position;
    File parentFile;
    VideoModel videoModel;
    ArrayList<VideoModel> listFiles = new ArrayList<>();
    String TAG = "--delete_file--";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        isDark = mPrefs.getBoolean("theme", true);
        Log.d("--theme--", "isDark: " + isDark);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Utils.bottomNavigationBlackColor(VideoFolderActivity.this);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Utils.bottomNavigationWhiteColor(VideoFolderActivity.this);
        }

        setContentView(R.layout.activity_video_folder);

        showBannerAd(VideoFolderActivity.this, findViewById(R.id.ad_banner));

        img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);
        rv_video_list = findViewById(R.id.rv_video_list);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);

        videoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VideoViewModel.class);
        videoFoldersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VideoFoldersViewModel.class);

        img_back.setOnClickListener(v -> {
            onBackPressed();
        });

        videoFolderModel = (VideoFolderModel) getIntent().getSerializableExtra("video_folder_model");

        txt_title.setText(videoFolderModel.getTitle());

        loadData();

        swipe_refresh_layout.setOnRefreshListener(() -> {
            loadData();
        });
    }

    @Override
    public void onBackPressed() {
        mPrefs.edit().putString("recent_path", videoModelList.get(position).getPath()).apply();
        mPrefs.edit().putBoolean("video_orientation", true).apply();
        super.onBackPressed();
    }

    private void loadData() {
        new Thread(() -> {
            swipe_refresh_layout.setRefreshing(true);

            videoModelList = MediaLoader.loadVideoFilesFromFolder(VideoFolderActivity.this, videoFolderModel);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    if (!videoModelList.isEmpty()) {

                        videoListAdapter = new VideoListAdapter(VideoFolderActivity.this, videoModelList, videoViewModel, VideoFolderActivity.this, VideoFolderActivity.this);

                        rv_video_list.setLayoutManager(new LinearLayoutManager(VideoFolderActivity.this, RecyclerView.VERTICAL, false));
                        rv_video_list.setAdapter(videoListAdapter);

                    } else {
                        // no video files available
                        finish();
                    }

                    swipe_refresh_layout.setRefreshing(false);
                }
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PERM_DELETE_VIDEO_FILE) {
                removeFilesFromList();
            }
        }
    }

    @Override
    public void onMoreOptionsClick(VideoModel videoModel, int position, View view) {

        PopupWindow popupWindow = new PopupWindow(VideoFolderActivity.this);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View custom_popup_view = layoutInflater.inflate(R.layout.popup_layout, null);

        LinearLayout ll_delete = custom_popup_view.findViewById(R.id.ll_delete);
        LinearLayout ll_share = custom_popup_view.findViewById(R.id.ll_share);
        LinearLayout ll_rename = custom_popup_view.findViewById(R.id.ll_rename);
        LinearLayout ll_details = custom_popup_view.findViewById(R.id.ll_details);

        ImageView img_delete = custom_popup_view.findViewById(R.id.img_delete);
        ImageView img_share = custom_popup_view.findViewById(R.id.img_share);
        ImageView img_rename = custom_popup_view.findViewById(R.id.img_rename);
        ImageView img_details = custom_popup_view.findViewById(R.id.img_details);

        ll_share.setVisibility(View.GONE);
        ll_rename.setVisibility(View.GONE);
        ll_delete.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ll_rename.setVisibility(View.GONE);
        } else {
            ll_rename.setVisibility(View.VISIBLE);
        }

        custom_popup_view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(custom_popup_view.getMeasuredWidth());
        popupWindow.setHeight(custom_popup_view.getMeasuredHeight());
        popupWindow.setContentView(custom_popup_view);
        popupWindow.update((int) view.getX(), (int) view.getY(), -1, -1);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(view); // set popup on click of view

        boolean isDark = mPrefs.getBoolean("theme", true);
        if (isDark) {
            img_delete.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_light_delete));
            img_share.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_light_share));
            img_rename.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_light_rename));
            img_details.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_light_details));
        } else {
            img_delete.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_dark_delete));
            img_share.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_dark_share));
            img_rename.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_dark_rename));
            img_details.setImageDrawable(ContextCompat.getDrawable(VideoFolderActivity.this, R.drawable.ic_popup_dark_details));

        }

        popupWindow.showAsDropDown(view); // set popup on click of view

        ll_delete.setOnClickListener(v -> {
            popupWindow.dismiss();
            deleteVideoFile(videoModel, position);
        });

        ll_share.setOnClickListener(v -> popupWindow.dismiss());

        ll_rename.setOnClickListener(v -> {
            popupWindow.dismiss();
            displayRenameDialog(videoModel, position);
        });

        ll_details.setOnClickListener(v -> {
            popupWindow.dismiss();
            displayDetailsDialogue(videoModel);
        });


//        PopupMenu popupMenu = new PopupMenu(VideoFolderActivity.this, view);
//
//        popupMenu.getMenuInflater().inflate(R.menu.menu_video_options, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.rename:
//                        popupMenu.dismiss();
//
//                        displayRenameDialog(videoModel, position);
//                        break;
//                    case R.id.details:
//                        popupMenu.dismiss();
//
//                        displayDetailsDialogue(videoModel);
//                        break;
//                    case R.id.delete:
//                        popupMenu.dismiss();
//
//                        deleteVideoFile(videoModel, position);
//                        break;
//                }
//                return false;
//            }
//        });
//
//        popupMenu.show();
    }

    private void displayRenameDialog(VideoModel videoModel, int position) {
        Dialog dialogRename = new Dialog(VideoFolderActivity.this);
        dialogRename.setCancelable(true);
        dialogRename.setContentView(R.layout.dialog_rename);
        dialogRename.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogRename.show();

        EditText edt_rename = dialogRename.findViewById(R.id.edt_rename);
        TextView txt_cancel = dialogRename.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialogRename.findViewById(R.id.txt_ok);

        edt_rename.setText(videoModel.getTitle());

        txt_cancel.setOnClickListener(view -> dialogRename.dismiss());

        txt_ok.setOnClickListener(view -> {
            if (edt_rename.getText().toString().equals(videoModel.getTitle())) {
                edt_rename.setError("Please enter something else");
            } else if (edt_rename.getText().toString().length() == 0) {
                edt_rename.setError("This field can't be empty!!");
            } else {
                dialogRename.dismiss();

                File parentFile = new File(videoModel.getPath()).getParentFile();

                File from = new File(videoModel.getPath());
                Log.d("--rename--", "from: " + from.getAbsolutePath());

                File to = new File(parentFile, edt_rename.getText().toString() + ".mp4");
                Log.d("--rename--", "to: " + to.getAbsolutePath());

                try {
                    Uri uriFrom = Uri.fromFile(from);
                    Intent intentFrom = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriFrom);
                    sendBroadcast(intentFrom);

                    boolean renamed = from.renameTo(to);
                    if (renamed) {
                        Log.d("--rename--", "renamed: " + "if");

                        String fileName = to.getName();
                        int pos = fileName.lastIndexOf(".");
                        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
                            fileName = fileName.substring(0, pos);
                        }

                        String finalFileName = fileName;

                        new Thread(() -> {
                            VideoModel queriedModel = videoViewModel.getSingleDataByPath(videoModel.getPath());

                            if (queriedModel != null) {

                                queriedModel.setTitle(finalFileName);
                                queriedModel.setPath(to.getAbsolutePath());

                                videoViewModel.update(queriedModel);

                                new Handler(Looper.getMainLooper()).post(() -> {

                                    videoListAdapter.notifyItemChanged(position, queriedModel);
                                    videoListAdapter.notifyItemChanged(position);

                                    Utils.videoModelArrayList.set(position, queriedModel);

                                    Uri uriTo = Uri.fromFile(to);
                                    Intent intentTo = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriTo);
                                    sendBroadcast(intentTo);
                                });
                            }
                        }).start();

                        // TODO: Update database and all arrays to be required
                    } else {
                        Log.d("--rename--", "renamed: " + "else");
                        Toast.makeText(VideoFolderActivity.this, "Could not rename File.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) {
                    Log.d("--catch--", "onClick: " + exception.getCause());
                    exception.printStackTrace();
                }
            }
        });
    }

    private void displayDetailsDialogue(@NonNull VideoModel videoModel) {
        Dialog detailsDialog = new Dialog(VideoFolderActivity.this);
        detailsDialog.setCancelable(true);
        detailsDialog.setContentView(R.layout.dialog_details);
        detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        detailsDialog.show();

        ScrollView card_folder_details = detailsDialog.findViewById(R.id.card_folder_details);
        ScrollView card_video_details = detailsDialog.findViewById(R.id.card_video_details);
        TextView txt_file_name = detailsDialog.findViewById(R.id.txt_file_name);
        TextView txt_file_path = detailsDialog.findViewById(R.id.txt_file_path);
        TextView txt_file_date = detailsDialog.findViewById(R.id.txt_file_date);
        TextView txt_file_size = detailsDialog.findViewById(R.id.txt_file_size);
        TextView txt_file_resolution = detailsDialog.findViewById(R.id.txt_file_resolution);
        TextView txt_file_length = detailsDialog.findViewById(R.id.txt_file_length);
        TextView txt_ok = detailsDialog.findViewById(R.id.txt_ok);

        card_folder_details.setVisibility(View.GONE);
        card_video_details.setVisibility(View.VISIBLE);

        txt_file_name.setText(videoModel.getTitle());

        txt_file_path.setText(videoModel.getPath());

        txt_file_date.setText(videoModel.getDate());

        txt_file_size.setText(Utils.getStringSizeLengthFile(Long.parseLong(videoModel.getSize())));

        txt_file_resolution.setText(videoModel.getResolution());

        if (videoModel.getDuration() != null) {
            txt_file_length.setText(Utils.convertMillieToHMmSs(Integer.parseInt(videoModel.getDuration())));
        }

        txt_ok.setOnClickListener(view -> detailsDialog.dismiss());
    }

    private void deleteVideoFile(@NonNull VideoModel videoModel, int position) {

        if (!videoModel.getPath().equals(mPrefs.getString("recent_path", ""))) {

            Log.d(TAG, "VideoModel: " + videoModel);
            this.videoModel = videoModel;

            Log.d(TAG, "position: " + position);
            this.position = position;

            File file = new File(videoModel.getPath());
            Log.d(TAG, "file: " + file.getAbsolutePath());

            parentFile = file.getParentFile();
            Log.d(TAG, "parentFile: " + Objects.requireNonNull(parentFile).getAbsolutePath());

            new Thread(() -> {
                List<VideoModel> videoModels = videoViewModel.getListByParentName(parentFile.getName());
                listFiles = new ArrayList<>(videoModels);
                Log.d(TAG, "listFiles: " + listFiles.toString());

                if (!listFiles.isEmpty()) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                Log.d(TAG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.R");
                                Intent intent = new Intent();
                                Utils.deleteFiles(new File[]{file}, REQUEST_PERM_DELETE_VIDEO_FILE, VideoFolderActivity.this, intent);
                            } else {
                                Dialog dialog = new Dialog(VideoFolderActivity.this);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.dialog_delete);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    dialog.create();
                                }

                                TextView txt_title = dialog.findViewById(R.id.txt_title);
                                TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
                                TextView txt_ok = dialog.findViewById(R.id.txt_ok);

                                txt_title.setText("Do you want to delete this folder?");
                                txt_cancel.setOnClickListener(v -> {
                                    dialog.dismiss();
                                });

                                txt_ok.setOnClickListener(v -> {
                                    dialog.dismiss();

                                    file.delete();
                                    MediaScannerConnection.scanFile(VideoFolderActivity.this,
                                            new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {

                                            });
                                    removeFilesFromList();
                                });
                                dialog.show();
                            }
                        }
                    });
                }
            }).start();
        } else {
            Toast.makeText(this, "You cannot delete video file that is recent mode", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFilesFromList() {
        Log.d(TAG, "removeFilesFromList");

        new Thread(() -> {
            VideoModel queriedVideoModel = videoViewModel.getSingleDataByPath(videoModel.getPath());

            if (queriedVideoModel != null) {
                videoViewModel.delete(queriedVideoModel);
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                    if (Utils.videoModelArrayList.get(i).getPath().equals(videoModel.getPath())) {
                        VideoModel videoModel = Utils.videoModelArrayList.get(i);
                        Utils.videoModelArrayList.remove(videoModel);
                        Utils.refreshFiles = true;
                        break;
                    }
                }

                videoModelList.remove(videoModel);
                videoListAdapter.notifyItemRemoved(position);
                videoListAdapter.notifyItemRangeChanged(position, videoModelList.size());

                if (listFiles != null && listFiles.size() == 1) {
                    String name = parentFile.getName();

                    Log.d(TAG, "name: " + name);

                    for (int i = 0; i < Utils.videoFolderModelArrayList.size(); i++) {
                        if (Utils.videoFolderModelArrayList.get(i).getTitle().equals(name)) {
                            Utils.videoFolderModelArrayList.remove(i);
                            Log.d(TAG, "position: " + i);
                            Utils.refreshFolder = true;
                            break;
                        }
                    }

                    finish();
                } else {
                    updateFolderModel(parentFile);
                }

                Utils.refreshFiles = true;
                Utils.refreshFolder = true;
            });
        }).start();
    }

    private void updateFolderModel(File file) {
        new Thread(() -> {
            VideoFolderModel videoFolderModel = videoFoldersViewModel.getSingleRowDataByPath(file.getAbsolutePath());

            if (videoFolderModel != null) {

                int files_count = Utils.getFilesVideoCount(VideoFolderActivity.this, file.getName());

                videoFolderModel.setFilesCount(files_count);

                videoFoldersViewModel.update(videoFolderModel);
            }
        }).start();
    }

    @Override
    public void onVideoClick(int position, List<VideoModel> videoModelList) {

        mPrefs.edit().putString("recent_path", videoModelList.get(position).getPath()).apply();

        Log.d("--recent_path--", "onVideoClick path: " + mPrefs.getString("recent_path", ""));

//        Intent intent = new Intent(VideoFolderActivity.this, CustomPlayerActivity.class);
        Intent intent = new Intent(VideoFolderActivity.this, VideoPlayerActivity.class);

        intent.putExtra("is_from_mini_player", false);
        mPrefs.edit().putInt("position", position).apply();
        intent.putExtra("list", (Serializable) videoModelList);

        showInterstitialAd(VideoFolderActivity.this, intent, null);
    }
}