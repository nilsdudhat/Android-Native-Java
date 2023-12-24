package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.fragments;

import static com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils.REQUEST_PERM_DELETE_VIDEO_FILE;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters.VideoListAdapter;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFoldersViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media.MediaLoader;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.HomeActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideosFragment extends Fragment implements VideoListAdapter.MoreOptionsClickListener, VideoListAdapter.PlayVideoClickListener {

    public VideosFragment mInstance = null;
    AppCompatActivity activity;
    SwipeRefreshLayout swipe_refresh_layout;
    RecyclerView rv_video_list;
    VideoListAdapter videoListAdapter;
    VideoViewModel videoViewModel;
    VideoFoldersViewModel videoFoldersViewModel;
    SharedPreferences mPrefs;
    int position;
    File parentFile;
    VideoModel videoModel;
    ArrayList<VideoModel> videoModelList = new ArrayList<>();
    private int recentPosition = 0;

    public VideosFragment() {
    }

    public VideosFragment getInstance() {
        if (mInstance == null) {
            mInstance = new VideosFragment();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        rv_video_list = view.findViewById(R.id.rv_video_list);
        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layout);

        activity = (AppCompatActivity) requireActivity();

        mPrefs = activity.getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        videoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(VideoViewModel.class);
        videoFoldersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(VideoFoldersViewModel.class);

        setUpRecyclerView();

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        return view;
    }

    private void refreshData() {

        new Thread(() -> {
            swipe_refresh_layout.setRefreshing(true);

            videoViewModel.deleteAllVideos();
            videoFoldersViewModel.deleteAllVideoFolders();

            MediaLoader.storeVideoFilesInDatabase(activity, videoViewModel, videoFoldersViewModel);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    videoViewModel.getAllVideos().observe(activity, videoModels -> {
                        if (!videoModels.isEmpty()) {
                            Utils.videoModelArrayList = videoModels;
                            Log.d("--list--", "videoFolderModelArrayList: " + Utils.videoFolderModelArrayList.toString());
                        }
                    });

                    videoFoldersViewModel.getAllVideoFolders().observe(activity, videoFolderModels -> {
                        if (!videoFolderModels.isEmpty()) {
                            Utils.videoFolderModelArrayList = videoFolderModels;
                            Log.d("--list--", "videoModelArrayList: " + Utils.videoModelArrayList.toString());
                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            new Handler(Looper.getMainLooper()).post(() -> {

                                Log.d("--size--", "onPostExecute: " + Utils.videoFolderModelArrayList.size());

                                setUpRecyclerView();

                                swipe_refresh_layout.setRefreshing(false);
                                Utils.refreshFolder = true;

                                ((HomeActivity) activity).refreshRecent();
                            });
                        }
                    }).start();
                }
            });
        }).start();
    }

    public ArrayList<VideoModel> getVideoModelList() {
        return videoModelList;
    }

    private void setUpRecyclerView() {
        if (!Utils.videoModelArrayList.isEmpty()) {

            videoListAdapter = new VideoListAdapter(activity, Utils.videoModelArrayList, videoViewModel, this, this);

            rv_video_list.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
            rv_video_list.setAdapter(videoListAdapter);
        } else {
            // no video files available
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.refreshFiles) {
            setUpRecyclerView();
            Utils.refreshFiles = false;
        }

        if (Utils.notifyFilesAdapter) {
            videoListAdapter.notifyDataSetChanged();
        }

        if (!Utils.videoModelArrayList.isEmpty()) {
            highlightRecentPosition();
        }


    }

    public void highlightRecentPosition() {
        videoListAdapter.notifyItemChanged(recentPosition);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String recent_path = mPrefs.getString("recent_path", "");
                Log.d("--recent_path--", "highlightRecentPosition: " + recent_path);

                if (mPrefs.getBoolean("scroll_down_to_highlight", true)) {
                    if (recent_path.length() != 0) {
                        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(requireContext()) {
                            @Override
                            protected int getVerticalSnapPreference() {
                                return LinearSmoothScroller.SNAP_TO_START;
                            }
                        };

                        activity.runOnUiThread(() -> {
                            for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                                if (recent_path.equals(Utils.videoModelArrayList.get(i).getPath())) {
                                    smoothScroller.setTargetPosition(i);
                                    recentPosition = i;

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Objects.requireNonNull(rv_video_list.getLayoutManager()).startSmoothScroll(smoothScroller);
                                            videoListAdapter.notifyItemChanged(recentPosition);
                                        }
                                    });

                                    break;
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public void onMoreOptionsClick(VideoModel videoModel, int position, View view) {

        PopupWindow popupWindow = new PopupWindow(activity);

        LayoutInflater layoutInflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom_popup_view = layoutInflater.inflate(R.layout.popup_layout, null);

        LinearLayout ll_delete = custom_popup_view.findViewById(R.id.ll_delete);
        LinearLayout ll_share = custom_popup_view.findViewById(R.id.ll_share);
        LinearLayout ll_rename = custom_popup_view.findViewById(R.id.ll_rename);
        LinearLayout ll_details = custom_popup_view.findViewById(R.id.ll_details);


        ImageView img_delete = custom_popup_view.findViewById(R.id.img_delete);
        ImageView img_share = custom_popup_view.findViewById(R.id.img_share);
        ImageView img_rename = custom_popup_view.findViewById(R.id.img_rename);
        ImageView img_details = custom_popup_view.findViewById(R.id.img_details);


        ll_share.setVisibility(View.GONE);
        ll_delete.setVisibility(View.VISIBLE);
        ll_rename.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ll_rename.setVisibility(View.GONE);
        } else {
            ll_rename.setVisibility(View.VISIBLE);
        }

        boolean isDark = mPrefs.getBoolean("theme", true);
        if (isDark) {
            img_delete.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_light_delete));
            img_share.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_light_share));
            img_rename.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_light_rename));
            img_details.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_light_details));
        } else {
            img_delete.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_dark_delete));
            img_share.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_dark_share));
            img_rename.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_dark_rename));
            img_details.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_popup_dark_details));
        }

        custom_popup_view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(custom_popup_view.getMeasuredWidth());
        popupWindow.setHeight(custom_popup_view.getMeasuredHeight());
        popupWindow.setContentView(custom_popup_view);
        popupWindow.update((int) view.getX(), (int) view.getY(), -1, -1);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(view); // set popup on click of view

        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                deleteVideoFile(videoModel, position);
            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ll_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                displayRenameDialog(videoModel, position);
            }
        });

        ll_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                displayDetailsDialogue(videoModel);
            }

        });

//        PopupMenu popupMenu = new PopupMenu(homeActivity, view);
//
//        popupMenu.getMenuInflater().inflate(R.menu.menu_video_options, popupMenu.getMenu());
//
//        popupMenu.getMenu().getItem(1).setVisible(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q);
//
//        popupMenu.setOnMenuItemClickListener(menuItem -> {
//            switch (menuItem.getItemId()) {
//                case R.id.details:
//                    popupMenu.dismiss();
//
//                    displayDetailsDialogue(videoModel);
//                    break;
//                case R.id.rename:
//                    popupMenu.dismiss();
//
//                    displayRenameDialog(videoModel, position);
//                    break;
//                case R.id.delete:
//                    popupMenu.dismiss();
//
//                    deleteVideoFile(videoModel, position);
//                    break;
//            }
//            return false;
//        });
//
//        popupMenu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                    Toast.makeText(activity, "Permission granted!", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Intent.ACTION_CALL);
                    startActivity(in);
                } else {
                    //If user presses deny
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void displayRenameDialog(VideoModel videoModel, int position) {
        Dialog dialogRename = new Dialog(activity);
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
                    Uri tempURI = Uri.fromFile(from);
                    Uri uriFrom = Uri.parse(tempURI.toString().replace("///", "//"));
                    Intent intentFrom = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriFrom);
                    activity.sendBroadcast(intentFrom);

                    Log.d("--rename--", "uriFrom: " + uriFrom.toString());

                    /*from.setWritable(true);
                    boolean renamed = from.renameTo(to);
                    if (renamed) {
                        renameSuccess(videoModel, position, to);
                    } else {
                        Log.d("--rename--", "renamed: " + "else");
                        Toast.makeText(activity, "Could not rename File.", Toast.LENGTH_SHORT).show();
                    }*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (rename(uriFrom, edt_rename.getText().toString())) {
                            renameSuccess(videoModel, position, to);
                        } else {
                            Log.d("--rename--", "renamed: " + "else");
                            Toast.makeText(activity, "Could not rename File.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        boolean renamed = from.renameTo(to);
                        if (renamed) {
                            renameSuccess(videoModel, position, to);
                        } else {
                            Log.d("--rename--", "renamed: " + "else");
                            Toast.makeText(activity, "Could not rename File.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception exception) {
                    Log.d("--catch--", "onClick: " + exception.getCause());
                    exception.printStackTrace();
                }
            }
        });
    }

    /**
     * Rename file.
     *
     * @param uri    - filepath.
     * @param rename - the name you want to replace with original.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public boolean rename(Uri uri, String rename) {
        try {
            //create content values with new name and update
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, rename);

            ((HomeActivity) activity).getContentResolver().update(uri, contentValues, null);
            return true;
        } catch (Exception e) {
            Log.d("--rename--", "rename: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private void renameSuccess(VideoModel videoModel, int position, File to) {
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
                    Utils.videoModelArrayList.set(position, queriedModel);

                    videoListAdapter.notifyDataSetChanged();

                    Uri uriTo = Uri.fromFile(to);
                    Intent intentTo = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriTo);
                    activity.sendBroadcast(intentTo);
                });
            }
        }).start();

        // TODO: Update database and all arrays to be required
    }

    private void displayDetailsDialogue(VideoModel videoModel) {
        Dialog detailsDialog = new Dialog(activity);
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

    private void deleteVideoFile(VideoModel videoModel, int position) {
        this.videoModel = videoModel;

        this.position = position;

        Log.d("--delete--", "deleteVideoFile position: " + position);
        Log.d("--delete--", "deleteVideoFile videoModel: " + videoModel.getPath());

        File file = new File(videoModel.getPath());

        parentFile = file.getParentFile();

        if (!file.getAbsolutePath().equals(mPrefs.getString("recent_path", ""))) {
            if (!((HomeActivity) activity).getPlayingVideoPath().equals(file.getAbsolutePath())) {
                new Thread(() -> {
                    List<VideoModel> videoModels = videoViewModel.getListByParentName(parentFile.getName());
                    videoModelList = new ArrayList<>(videoModels);
                    Log.d("--delete--", "deleteVideoFile parentFile: " + parentFile.getName());

                    if (!videoModelList.isEmpty()) {
                        Log.d("--delete--", "deleteVideoFile videoModelList: " + videoModelList.size());
                        new Handler(Looper.getMainLooper()).post(() -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                Log.d("--delete--", "deleteVideoFile Build.VERSION.SDK_INT >= Build.VERSION_CODES.R: ");
                                Intent intent = new Intent();
                                Utils.deleteFiles(new File[]{file}, REQUEST_PERM_DELETE_VIDEO_FILE, activity, intent);

                                Utils.deleteFile = "File";
                            } else {
                                Dialog dialog = new Dialog(activity);
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
                                txt_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                txt_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                        file.delete();
                                        MediaScannerConnection.scanFile(activity,
                                                new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {

                                                });
                                        removeFilesFromList();
                                    }
                                });
                                dialog.show();
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(activity, "Could not delete video... It is in Playing Mode.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "You cannot delete video that is in recent mode.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeFilesFromList() {

        new Thread(() -> {
            VideoModel queriedVideoModel = videoViewModel.getSingleDataByPath(videoModel.getPath());

            if (queriedVideoModel != null) {
                Log.d("--delete--", "removeFilesFromList queriedVideoModel: " + queriedVideoModel.getPath());
                videoViewModel.delete(queriedVideoModel);
            }

            if (videoModelList != null && videoModelList.size() == 1) {
                String name = parentFile.getName();

                for (int i = 0; i < Utils.videoFolderModelArrayList.size(); i++) {
                    if (Utils.videoFolderModelArrayList.get(i).getTitle().equals(name)) {
                        Utils.videoFolderModelArrayList.remove(i);

                        VideoFolderModel queriedFolderModel
                                = videoFoldersViewModel.getSingleRowDataByPath(Utils.videoFolderModelArrayList.get(i).getPath());

                        if (queriedFolderModel != null) {
                            videoFoldersViewModel.delete(queriedFolderModel); // TODO: Query VideoFolderModel Object and remove from video folder database
                        }
                        break;
                    }
                }

                Utils.notifyFoldersAdapter = true;
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                Log.d("--delete--", "removeFilesFromList videoModelArrayList 1: " + Utils.videoModelArrayList.size());
                Utils.videoModelArrayList.remove(videoModel);
                Log.d("--delete--", "removeFilesFromList videoModelArrayList 2: " + Utils.videoModelArrayList.size());
//                videoListAdapter.notifyDataSetChanged();
                videoListAdapter.notifyItemChanged(position);
                videoListAdapter.notifyItemRangeChanged(position, Utils.videoModelArrayList.size());
                Utils.refreshFolder = true;
            });
        }).start();
    }

    @Override
    public void onVideoClick(int position, List<VideoModel> videoModelList) {
        if (activity != null) {
            ((HomeActivity) activity).itemClickedInVideosFragment(position, videoModelList);
        }
    }

    public void highlightVideoFromHomeActivity(int position, List<VideoModel> videoModelList) {

        mPrefs.edit().putString("recent_path", videoModelList.get(position).getPath()).apply();

        Log.d("--recent_path--", "onVideoClick path: " + mPrefs.getString("recent_path", ""));

        Log.d("--prefs--", "last selected Utils.selectedItem: " + Utils.selectedItem);
        Log.d("--prefs--", "last selected position: " + position);

        // This update the last item selected
        videoListAdapter.notifyItemChanged(Utils.selectedItem);

        Utils.selectedItem = position;

        //This update the item selected
        videoListAdapter.notifyItemChanged(position);

        highlightRecentPosition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (activity.isInPictureInPictureMode()) {
                Utils.isInPIPMode = true;
            }
        }
    }
}