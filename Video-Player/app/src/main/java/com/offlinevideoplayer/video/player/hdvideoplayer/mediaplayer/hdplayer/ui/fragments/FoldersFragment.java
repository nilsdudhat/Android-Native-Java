package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.fragments;

import static com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils.REQUEST_PERM_DELETE_FOLDER;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters.VideoFolderAdapter;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFoldersViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media.MediaLoader;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.HomeActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.VideoFolderActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment implements VideoFolderAdapter.MoreOptionsClickListener, VideoFolderAdapter.VideoFolderClickListener {

    AppCompatActivity activity;
    SharedPreferences mPrefs;

    SwipeRefreshLayout swipe_refresh_layout;
    RecyclerView rv_video_folders_list;
    VideoFolderAdapter videoFolderListAdapter;

    VideoFoldersViewModel videoFoldersViewModel;
    VideoViewModel videoViewModel;

    FoldersFragment mInstance = null;
    int position;
    File file;
    VideoFolderModel videoFolderModel;
    File[] files;
    String TAG = "--delete_folder--";

    public FoldersFragment() {
    }

    public FoldersFragment getInstance() {
        if (mInstance == null) {
            return new FoldersFragment();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        activity = (AppCompatActivity) requireActivity();

        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layout);
        rv_video_folders_list = view.findViewById(R.id.rv_video_folders_list);

        videoFoldersViewModel = ViewModelProviders.of(activity).get(VideoFoldersViewModel.class);
        videoViewModel = ViewModelProviders.of(activity).get(VideoViewModel.class);

        mPrefs = activity.getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        setUpRecyclerView();

        swipe_refresh_layout.setOnRefreshListener(this::refreshData);

        return view;
    }

    private void refreshData() {
        swipe_refresh_layout.setRefreshing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {

                videoViewModel.deleteAllVideos();
                videoFoldersViewModel.deleteAllVideoFolders();

                videoFoldersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(VideoFoldersViewModel.class);
                videoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(VideoViewModel.class);

                MediaLoader.storeVideoFilesInDatabase(activity, videoViewModel, videoFoldersViewModel);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        videoViewModel.getAllVideos().observe(activity, new Observer<List<VideoModel>>() {
                            @Override
                            public void onChanged(List<VideoModel> videoModels) {
                                if (!videoModels.isEmpty()) {
                                    Utils.videoModelArrayList = videoModels;
                                    Log.d("--list--", "videoFolderModelArrayList: " + Utils.videoFolderModelArrayList.toString());
                                }
                            }
                        });

                        videoFoldersViewModel.getAllVideoFolders().observe(activity, new Observer<List<VideoFolderModel>>() {
                            @Override
                            public void onChanged(List<VideoFolderModel> videoFolderModels) {
                                if (!videoFolderModels.isEmpty()) {
                                    Utils.videoFolderModelArrayList = videoFolderModels;
                                    Log.d("--list--", "videoModelArrayList: " + Utils.videoModelArrayList.toString());
                                }
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

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("--size--", "onPostExecute videoFolderModelArrayList: " + Utils.videoFolderModelArrayList.size());
                                        Log.d("--size--", "onPostExecute videoModelArrayList: " + Utils.videoModelArrayList.size());

                                        setUpRecyclerView();

                                        swipe_refresh_layout.setRefreshing(false);
                                        Utils.refreshFiles = true;

                                        ((HomeActivity) requireActivity()).refreshRecent();
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onMoreOptionsClick(VideoFolderModel videoFolderModel, int position, View view) {

        PopupWindow popupWindow = new PopupWindow(activity);

        LayoutInflater layoutInflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                deleteVideoFolder(videoFolderModel, position);
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
                displayRenameDialog(videoFolderModel, position);
            }
        });

        ll_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                displayDetailsDialogue(videoFolderModel);
            }
        });


//        PopupMenu popupMenu = new PopupMenu(homeActivity, view);
//
//        popupMenu.getMenuInflater().inflate(R.menu.menu_video_options, popupMenu.getMenu());
//        popupMenu.getMenu().getItem(0).setVisible(false);
////        popupMenu.getMenu().getItem(1).setVisible(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q);
//
//        popupMenu.getMenu().getItem(1).setVisible(Utils.isExternalStorageWritable());
//
//        popupMenu.setOnMenuItemClickListener(menuItem -> {
//            switch (menuItem.getItemId()) {
//                case R.id.details:
//                    popupMenu.dismiss();
//
////                        displayDetailsDialogue(videoFolderModel);
//                    break;
//                case R.id.rename:
//                    popupMenu.dismiss();
//
//                    displayRenameDialog(videoFolderModel, position);
//                    break;
//                case R.id.delete:
//                    popupMenu.dismiss();
//
//                    deleteVideoFolder(videoFolderModel, position);
//                    break;
//            }
//            return false;
//        });
//
//        popupMenu.show();
    }

    private void displayDetailsDialogue(VideoFolderModel videoFolderModel) {
        Dialog detailsDialog = new Dialog(activity);
        detailsDialog.setCancelable(true);
        detailsDialog.setContentView(R.layout.dialog_details);
        detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        detailsDialog.show();

        ScrollView card_folder_details = detailsDialog.findViewById(R.id.card_folder_details);
        ScrollView card_video_details = detailsDialog.findViewById(R.id.card_video_details);
        TextView txt_folder_name = detailsDialog.findViewById(R.id.txt_folder_name);
        TextView txt_folder_path = detailsDialog.findViewById(R.id.txt_folder_path);
        TextView txt_files_count = detailsDialog.findViewById(R.id.txt_files_count);
        TextView txt_folder_date = detailsDialog.findViewById(R.id.txt_folder_date);
        TextView txt_folder_size = detailsDialog.findViewById(R.id.txt_folder_size);
        TextView txt_ok = detailsDialog.findViewById(R.id.txt_ok);

        card_folder_details.setVisibility(View.VISIBLE);
        card_video_details.setVisibility(View.GONE);

        String title = videoFolderModel.getTitle();
        String path = videoFolderModel.getPath();
        String count = String.valueOf(videoFolderModel.getFilesCount());
        String date = Utils.getFormattedDate(new File(videoFolderModel.getPath()));
        Log.d("--size--", "displayDetailsDialogue: " + videoFolderModel.getPath());
        String size = Utils.getStringSizeLengthFile(Utils.folderSize(activity, new File(videoFolderModel.getPath())));

        txt_folder_name.setText(String.valueOf(title));
        txt_folder_path.setText(String.valueOf(path));
        txt_files_count.setText(count);
        txt_folder_date.setText(date);
        txt_folder_size.setText(size);

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDialog.dismiss();
            }
        });
    }

    private void displayRenameDialog(VideoFolderModel videoFolderModel, int position) {
        Dialog dialogRename = new Dialog(activity);
        dialogRename.setCancelable(true);
        dialogRename.setContentView(R.layout.dialog_rename);
        dialogRename.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogRename.show();

        EditText edt_rename = dialogRename.findViewById(R.id.edt_rename);
        TextView txt_cancel = dialogRename.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialogRename.findViewById(R.id.txt_ok);

        edt_rename.setText(videoFolderModel.getTitle());

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRename.dismiss();
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_rename.getText().toString().equals(videoFolderModel.getTitle())) {
                    edt_rename.setError("Please enter something else");
                } else if (edt_rename.getText().length() == 0) {
                    edt_rename.setError("This field can't be empty!!");
                } else {
                    dialogRename.dismiss();

                    File parentFile = new File(videoFolderModel.getPath()).getParentFile();

                    File from = new File(videoFolderModel.getPath());
                    Log.d("--rename--", "from: " + from.getAbsolutePath());

                    File to = new File(parentFile, edt_rename.getText().toString());
                    Log.d("--rename--", "to: " + to.getAbsolutePath());

                    if (from.exists()) {
                        boolean renamed = from.renameTo(to);
                        if (renamed) {
                            Log.d("--rename--", "renamed: " + "if");

                            videoFolderModel.setTitle(to.getName());
                            videoFolderModel.setPath(to.getAbsolutePath());

                            videoFoldersViewModel.update(videoFolderModel);
                            videoFolderListAdapter.notifyItemChanged(position, videoFolderModel);
                            videoFolderListAdapter.notifyItemChanged(position);

                            // TODO: Update database and all arrays to be required
                        } else {
                            Log.d("--rename--", "renamed: " + "else");
                            Toast.makeText(activity, "Could not rename File.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "File not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void deleteVideoFolder(VideoFolderModel videoFolderModel, int position) {
        this.position = -1;
        this.file = null;
        this.videoFolderModel = null;
        files = null;

        this.videoFolderModel = videoFolderModel;

        this.position = position;

        file = new File(videoFolderModel.getPath());

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<VideoModel> videoModels = videoViewModel.getListByParentName(file.getName());
                Utils.queriedVideoModels = new ArrayList<>(videoModels);

                if (!Utils.queriedVideoModels.isEmpty()) {
                    files = new File[Utils.queriedVideoModels.size()];

                    ArrayList<Integer> removeItemList = new ArrayList<>();
                    for (int i = 0; i < Utils.queriedVideoModels.size(); i++) {
                        if (!Utils.queriedVideoModels.get(i).getPath().equals(((HomeActivity) requireActivity()).getPlayingVideoPath())) {
                            if (!Utils.queriedVideoModels.get(i).getPath().equals(mPrefs.getString("recent_path", ""))) {
                                files[i] = new File(Utils.queriedVideoModels.get(i).getPath());
                            } else {
                                removeItemList.add(i);
                            }
                        } else {
                            removeItemList.add(i);
                        }
                    }

                    if (!removeItemList.isEmpty()) {
                        for (int i = 0; i < removeItemList.size(); i++) {
                            files = Utils.removeTheElement(files, removeItemList.get(i));
                        }
                    }

                    if (files.length > 0) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                                    Intent intent = new Intent();
                                    Utils.deleteFiles(files, REQUEST_PERM_DELETE_FOLDER, requireActivity(), intent);

                                    Utils.deleteFile = "Folders";
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
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Video inside this folder is currently in use", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void removeFilesFromList() {
        Log.d(TAG, "removeFilesFromList");

        new Thread(new Runnable() {
            @Override
            public void run() {
                VideoFolderModel queriedFolderModel = videoFoldersViewModel.getSingleRowDataByPath(videoFolderModel.getPath());

                if (queriedFolderModel != null) {
                    List<VideoModel> videoModels = videoViewModel.getListByParentName(queriedFolderModel.getTitle());
                    ArrayList<VideoModel> queriedVideoModels = new ArrayList<>(videoModels);

                    if (!queriedVideoModels.isEmpty()) {

                        Log.d(TAG, "Utils.videoModelArrayList -- 1 -- size: " + Utils.videoModelArrayList.size());

                        for (int i = 0; i < queriedVideoModels.size(); i++) {
                            VideoModel queriedVideoModel = queriedVideoModels.get(i);
                            videoViewModel.delete(queriedVideoModel); // Deleting Video from DB

                            for (int j = 0; j < Utils.videoModelArrayList.size(); j++) {
                                if (queriedVideoModels.get(i).getPath().equals(Utils.videoModelArrayList.get(j).getPath())) {
                                    Log.d(TAG, "matched at position: " + j);
                                    Utils.videoModelArrayList.remove(j);
                                    break;
                                }
                            }
                        }

                        Log.d(TAG, "Utils.videoModelArrayList -- 2 -- size: " + Utils.videoModelArrayList.size());
                    }
                    videoFoldersViewModel.delete(queriedFolderModel); // Deleting folder from DB
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Utils.videoFolderModelArrayList.remove(videoFolderModel);
                        videoFolderListAdapter.notifyDataSetChanged();

                        Utils.notifyFilesAdapter = true;
                    }
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.refreshFolder) {
            Log.d("--remove--", "Utils.refreshData: " + "true onResume");
            refreshData();
            Utils.refreshFolder = false;
        }

        if (Utils.notifyFoldersAdapter) {
            videoFolderListAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRecyclerView() {
        if (!Utils.videoFolderModelArrayList.isEmpty()) {

            Log.d("--size--", "setUpRecyclerView: " + Utils.videoFolderModelArrayList.size());
            Log.d("--size--", "setUpRecyclerView: " + Utils.videoModelArrayList.size());

            videoFolderListAdapter = new VideoFolderAdapter(activity, Utils.videoFolderModelArrayList, this, this);

            rv_video_folders_list.setLayoutManager(new GridLayoutManager(activity, 1));
            rv_video_folders_list.setAdapter(videoFolderListAdapter);
        } else {
            // no video files available
        }
    }

    @Override
    public void onVideoClicked(VideoFolderModel videoFolderModel) {
        Intent intent = new Intent(activity, VideoFolderActivity.class);
        intent.putExtra("video_folder_model", videoFolderModel);
        ((HomeActivity) activity).showInterstitialAd(requireActivity(), intent, null);
    }
}