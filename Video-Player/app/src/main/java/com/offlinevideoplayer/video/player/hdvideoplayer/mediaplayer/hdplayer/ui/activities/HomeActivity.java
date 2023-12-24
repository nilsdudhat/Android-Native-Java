package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities;

import static com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils.REQUEST_PERM_DELETE_FOLDER;
import static com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils.REQUEST_PERM_DELETE_VIDEO_FILE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.navigation.NavigationView;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.AdsIntegration.AdsBaseActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.adapters.ViewPagerFragmentAdapter;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFoldersViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media.MediaLoader;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.fragments.FoldersFragment;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.fragments.VideosFragment;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AdsBaseActivity {

    public static boolean isActivityOpen = false;
    ImageView img_menu;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    SharedPreferences mPrefs;
    boolean isDark = false;
    ViewPager2 viewPager;
    TextView txt_videos;
    TextView txt_folders;
    ImageView bg_videos;
    ImageView bg_folders;
    CoordinatorLayout coordinatior_layout;
    ConstraintLayout constraint_holder;
    TextView txt_no_videos;
    AppBarLayout appbar;
    LinearLayout title_bar;
    ImageView img_search;
    EditText edt_search;
    TextView txt_cancel;
    ConstraintLayout constraint_recent;
    ImageView recent_thumbnail;
    SeekBar seek_recent;
    TextView txt_recent_duration;
    TextView txt_recent_file_name;
    String recent_path = "";
    VideoViewModel videoViewModel;
    VideoFoldersViewModel videoFoldersViewModel;
    VideosFragment videosFragment;
    FoldersFragment foldersFragment;
    ProgressDialog progressDialog;
    RelativeLayout player_container;
    TextView txt_file_name;
    ImageView exo_prev;
    ImageView exo_play;
    ImageView exo_pause;
    ImageView exo_next;
    boolean isVisible = false;
    int position = 0;
    ArrayList<VideoModel> playerVideoList = new ArrayList<>();
    SimpleExoPlayer simpleExoPlayer;
    PlayerView playerView;
    String playingVideoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mPrefs = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        isDark = mPrefs.getBoolean("theme", true);
        Log.d("--theme--", "isDark: " + isDark);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Utils.bottomNavigationBlackColor(HomeActivity.this);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Utils.bottomNavigationWhiteColor(HomeActivity.this);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        loadInterstitialAd(HomeActivity.this);

        txt_file_name = findViewById(R.id.txt_file_name);
        exo_prev = findViewById(R.id.exo_prev);
        exo_play = findViewById(R.id.exo_play);
        exo_pause = findViewById(R.id.exo_pause);
        exo_next = findViewById(R.id.exo_next);
        playerView = findViewById(R.id.exo_player);
        player_container = findViewById(R.id.player_container);

        txt_videos = findViewById(R.id.txt_videos_tab);
        txt_folders = findViewById(R.id.txt_folders);

        viewPager = findViewById(R.id.view_pager);
        coordinatior_layout = findViewById(R.id.coordinatior_layout);

        constraint_holder = findViewById(R.id.constraint_holder);
        txt_no_videos = findViewById(R.id.txt_no_videos);

        appbar = findViewById(R.id.appbar);
        title_bar = findViewById(R.id.title_bar);
        img_search = findViewById(R.id.img_search);
        edt_search = findViewById(R.id.edt_search);
        txt_cancel = findViewById(R.id.txt_cancel);
        img_menu = findViewById(R.id.img_menu);

        recent_thumbnail = findViewById(R.id.recent_thumbnail);
        constraint_recent = findViewById(R.id.constraint_recent);
        seek_recent = findViewById(R.id.seek_recent);
        txt_recent_duration = findViewById(R.id.txt_recent_duration);
        txt_recent_file_name = findViewById(R.id.txt_recent_file_name);

        bg_videos = findViewById(R.id.bg_videos);
        bg_folders = findViewById(R.id.bg_folders);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.d("width_player", "height: " + height);
        Log.d("width_player", "width: " + width);

        int playerHeight = (width * width) / height;

        Log.d("width_player", "playerHeight: " + playerHeight);

        if (isDark) {
            img_menu.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.ic_menu_black_theme));
            img_search.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.ic_search_dark));
        } else {
            img_menu.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.ic_menu_white_theme));
            img_search.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.ic_search_light));
        }

        videoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VideoViewModel.class);
        videoFoldersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VideoFoldersViewModel.class);

        exo_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position < playerVideoList.size()) {
                    initializePlayer(position, playerVideoList);
                    videosFragment.highlightVideoFromHomeActivity(position, playerVideoList);
                } else {
                    position--;
                }
            }
        });

        exo_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position >= 0) {
                    initializePlayer(position, playerVideoList);
                    videosFragment.highlightVideoFromHomeActivity(position, playerVideoList);
                } else {
                    position++;
                }
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAd(HomeActivity.this, new Intent(HomeActivity.this, SearchActivity.class), null);
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading Data");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressDialog.create();
        }
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (Utils.videoModelArrayList.isEmpty()) {
            getVideoData();
        } else {
            progressDialog.dismiss();
            setUpViewPager();
        }

        videosSelected();

        setupDrawer();

        Utils.hideSoftKeyboard(HomeActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityOpen = true;
        isVisible = true;
        Log.d("isActivityOpen", "onResume: isActivityOpen " + isActivityOpen);

        Log.d("--theme--", "theme: " + mPrefs.getBoolean("theme", true));

        mPrefs.edit().putBoolean("video_orientation", true).apply();
        mPrefs.edit().putBoolean("isActivityOpen", true).apply();

        if (isVisible) {
            if (mPrefs.getBoolean("theme", true) != isDark) {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
            }
        }

        if (!Utils.videoModelArrayList.isEmpty()) {
            if (mPrefs.getBoolean("display_recent_video", true)) {
                constraint_recent.setVisibility(View.VISIBLE);
                showRecent();
            } else {
                constraint_recent.setVisibility(View.GONE);
            }
            constraint_holder.setVisibility(View.VISIBLE);
            txt_no_videos.setVisibility(View.GONE);
        } else {
            constraint_holder.setVisibility(View.GONE);
            txt_no_videos.setVisibility(View.VISIBLE);
        }

    }

    private void showRecent() {
        recent_path = mPrefs.getString("recent_path", "");
        Log.d("--path--", "showRecent path: " + recent_path);

        if (!recent_path.equals("")) {
            if (new File(recent_path).exists()) {
                constraint_recent.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    VideoModel videoModel = videoViewModel.getSingleDataByPath(recent_path);

                    if (videoModel != null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void run() {
                                Glide.with(HomeActivity.this).load(new File(videoModel.getPath())).placeholder(R.drawable.ic_icon).fitCenter().into(recent_thumbnail);
                                txt_recent_file_name.setText(videoModel.getTitle());

                                try {
                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                    retriever.setDataSource(HomeActivity.this, Uri.parse(videoModel.getPath()));
                                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                    long timeInMilliSec = Long.parseLong(time);
                                    Log.d("--duration--", "timeInMilliSec: " + timeInMilliSec);

                                    retriever.release();

                                    seek_recent.setMax((int) timeInMilliSec);

                                    long seekPosition = mPrefs.getLong("seek_position", 0);
                                    seek_recent.setProgress((int) seekPosition);

                                    Log.d("--path--", "show recent progress: " + seekPosition);

                                    long remainingTime = timeInMilliSec - seekPosition;

                                    txt_recent_duration.setText(Utils.convertMillisToMinutes(String.valueOf(remainingTime)));

                                    seek_recent.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {
                                            return true;
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                constraint_recent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        int position = 0;

                                        for (int i = 0; i < Utils.videoModelArrayList.size(); i++) {
                                            File file = new File(Utils.videoModelArrayList.get(i).getPath());
                                            if (file.getAbsolutePath().equals(videoModel.getPath())) {
                                                position = i;
                                                break;
                                            }
                                        }

                                        itemClickedInVideosFragment(position, Utils.videoModelArrayList);
                                    }
                                });
                                constraint_recent.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }).start();
            } else {
                constraint_recent.setVisibility(View.GONE);
            }
        } else {
            constraint_recent.setVisibility(View.GONE);
        }
    }

    private void getVideoData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                videoViewModel.deleteAllVideos();
                videoFoldersViewModel.deleteAllVideoFolders();

                MediaLoader.storeVideoFilesInDatabase(HomeActivity.this, videoViewModel, videoFoldersViewModel);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        videoViewModel.getAllVideos().observe(HomeActivity.this, videoModels -> {
                            if (!videoModels.isEmpty()) {
                                Utils.videoModelArrayList = videoModels;
                                Log.d("--list--", "videoFolderModelArrayList: " + Utils.videoFolderModelArrayList.toString());
                            }
                        });

                        videoFoldersViewModel.getAllVideoFolders().observe(HomeActivity.this, videoFolderModels -> {
                            if (!videoFolderModels.isEmpty()) {
                                Utils.videoFolderModelArrayList = videoFolderModels;
                                Log.d("--list--", "videoModelArrayList: " + Utils.videoModelArrayList.toString());
                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();

                                        setUpViewPager();

                                        videosSelected();

                                        Log.d("--size--", "onPostExecute: " + Utils.videoFolderModelArrayList.size());
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        }).start();
    }

    private void setUpViewPager() {
        videosFragment = new VideosFragment().getInstance();
        foldersFragment = new FoldersFragment().getInstance();

        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(HomeActivity.this);
        viewPagerFragmentAdapter.addFragment(videosFragment);
        viewPagerFragmentAdapter.addFragment(foldersFragment);
        viewPager.setAdapter(viewPagerFragmentAdapter);

        if (viewPager.getCurrentItem() == 0) {
            Utils.pageSelection = "Videos";
        } else {
            Utils.pageSelection = "Folders";
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    videosSelected();
                } else {
                    foldersSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        txt_folders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foldersSelected();
            }
        });

        txt_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videosSelected();
            }
        });
    }

    private void foldersSelected() {
        Utils.pageSelection = "Folders";

        int color = MaterialColors.getColor(HomeActivity.this, R.attr.opposite_light_color, Color.GRAY);

        txt_videos.setTextColor(color);
        txt_folders.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.white));

        bg_videos.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.transparent));
        bg_folders.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.bg_selected));

        bg_folders.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));

        viewPager.setCurrentItem(1);
    }

    private void videosSelected() {
        Utils.pageSelection = "Videos";

        int color = MaterialColors.getColor(HomeActivity.this, R.attr.opposite_light_color, Color.GRAY);

        txt_videos.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        txt_folders.setTextColor(color);

        bg_videos.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.bg_selected));
        bg_folders.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.transparent));

        bg_videos.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));

        viewPager.setCurrentItem(0);
    }

    private void setupDrawer() {
        nav_view = findViewById(R.id.nav_view);
        drawer_layout = findViewById(R.id.drawer_layout);

        TextView txt_version_name = nav_view.findViewById(R.id.txt_version_name);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            txt_version_name.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            txt_version_name.setVisibility(View.GONE);
            e.printStackTrace();
        }

//        final ConstraintLayout holder = findViewById(R.id.constraint_holder);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                DisplayMetrics metrics = getResources().getDisplayMetrics();
//                int width = metrics.widthPixels;
//                int height = metrics.heightPixels - metrics.heightPixels/5;
//
//                float scaleFactor = 7f;
//                float slideY = height * slideOffset;
//                holder.setTranslationY(slideY);
//                holder.setScaleX(1 - (slideOffset / scaleFactor));
//                holder.setScaleY(1 - (slideOffset / scaleFactor));
//                super.onDrawerSlide(drawerView, slideOffset);
//            }
//        };
//        drawer_layout.addDrawerListener(toggle);
//        drawer_layout.setScrimColor(Color.TRANSPARENT);
//        toggle.syncState();

        nav_view.findViewById(R.id.nav_more_apps).setOnClickListener(view -> {
            drawer_layout.closeDrawer(GravityCompat.START);
            Utils.openBrowser(HomeActivity.this, "https://play.google.com/store/apps/dev?id=4710251405227521498");
        });

        nav_view.findViewById(R.id.nav_settings).setOnClickListener(view -> {
            drawer_layout.closeDrawer(GravityCompat.START);
            showInterstitialAd(HomeActivity.this, new Intent(HomeActivity.this, SettingsActivity.class), null);
        });

        nav_view.findViewById(R.id.nav_share).

                setOnClickListener(view ->

                {
                    drawer_layout.closeDrawer(GravityCompat.START);
                    Utils.shareApp(HomeActivity.this);
                });

        nav_view.findViewById(R.id.nav_rate).

                setOnClickListener(view ->

                {
                    drawer_layout.closeDrawer(GravityCompat.START);
                    Utils.rateApp(HomeActivity.this);
                });

        nav_view.findViewById(R.id.nav_privacy).

                setOnClickListener(view ->

                {
                    Utils.openBrowser(HomeActivity.this, "http://bluemoonmobileapps.com/privacy.html");
                    drawer_layout.closeDrawer(GravityCompat.START);
                });

        img_menu =

                findViewById(R.id.img_menu);
        img_menu.setOnClickListener(v ->

        {
            drawer_layout.openDrawer(GravityCompat.START);
        });

        float[] isDrawer = {0f};

        drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (isDrawer[0] < slideOffset) {
                    isDrawer[0] = slideOffset;
                    if (!isDark) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.gray));
                            getWindow().setNavigationBarColor(ContextCompat.getColor(HomeActivity.this, R.color.gray));
                        }
                    }
                } else {
                    isDrawer[0] = slideOffset;
                    if (!isDark) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
                            getWindow().setNavigationBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
                        }
                    }
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else if (player_container.getVisibility() == View.VISIBLE) {
            player_container.setVisibility(View.GONE);
            playingVideoPath = "";
            releaseMiniPlayer();
            if (mPrefs.getBoolean("display_recent_video", true)) {
                constraint_recent.setVisibility(View.VISIBLE);
                showRecent();
            } else {
                constraint_recent.setVisibility(View.GONE);
            }
        } else {
            if (title_bar.getVisibility() == View.GONE) {
                edt_search.setText("");
                Utils.hideSoftKeyboard(HomeActivity.this);
                title_bar.setVisibility(View.VISIBLE);
                coordinatior_layout.setVisibility(View.VISIBLE);
            } else {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.SheetDialog);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                bottomSheetDialog.setCancelable(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bottomSheetDialog.create();
                }
                bottomSheetDialog.show();

                TextView txt_no = bottomSheetDialog.findViewById(R.id.txt_no);
                TextView txt_yes = bottomSheetDialog.findViewById(R.id.txt_yes);

//                Objects.requireNonNull(txt_no).getBackground().setAlpha(50);

                txt_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                Objects.requireNonNull(txt_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        finish();
                        System.exit(0);
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPrefs.edit().putBoolean("isActivityOpen", false).apply();

    }

    @Override
    protected void onPause() {
        super.onPause();

        isVisible = false;
        isActivityOpen = true;
        mPrefs.edit().putBoolean("isActivityOpen", true).apply();

        Log.d("isActivityOpen", "onPause: isActivityOpen " + isActivityOpen);


        if (player_container.getVisibility() == View.VISIBLE) {
            player_container.setVisibility(View.GONE);
            playingVideoPath = "";
            releaseMiniPlayer();
        }
        showRecent();
    }

    public void itemClickedInVideosFragment(int position, List<VideoModel> videoModelList) {
        this.position = position;
        this.playerVideoList = new ArrayList<>(videoModelList);

        initializePlayer(position, playerVideoList);

        videosFragment.highlightVideoFromHomeActivity(position, videoModelList);
    }

    private void initializePlayer(int position, ArrayList<VideoModel> videoModelList) {
        appbar.setExpanded(true, true);

        releaseMiniPlayer();

//        // TODO: Maintain Visibility of Previous Icon when there is first index of array == not done
//        if (position == 0) {
//            exo_prev.setVisibility(View.GONE);
//        } else {
//            exo_prev.setVisibility(View.VISIBLE);
//        }
//
//        // TODO: Maintain Visibility of Next Icon when there is last index of array == not done
//        if (position == (arrayList.size() - 1)) {
//            exo_next.setVisibility(View.GONE);
//        } else {
//            exo_next.setVisibility(View.VISIBLE);
//        }

        VideoModel fileModel = videoModelList.get(position);

        String path = fileModel.getPath();


        String filename = new File(path).getName();
        String result = filename.substring(0, filename.lastIndexOf("."));
        txt_file_name.setText(result);

        Uri uri = Uri.parse(path);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        DataSource.Factory factory = new DefaultDataSourceFactory(HomeActivity.this, Util.getUserAgent(HomeActivity.this, String.valueOf(R.string.app_name)));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);


        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        if (path.equals(recent_path)) {
            long seekPosition = mPrefs.getLong("seek_position", 0);
            Log.d("--path--", "initializePlayer path: " + recent_path + seekPosition);
            simpleExoPlayer.seekTo(seekPosition);
        }
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        playingVideoPath = path;

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.EventListener.super.onIsPlayingChanged(isPlaying);

                long seekPosition = simpleExoPlayer.getCurrentPosition();
                mPrefs.edit().putLong("seek_position", seekPosition).apply();
            }
        });

        findViewById(R.id.img_full_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                intent.putExtra("is_from_mini_player", true);
                mPrefs.edit().putLong("seek_position", simpleExoPlayer.getCurrentPosition()).apply();
                mPrefs.edit().putInt("position", position).apply();
                intent.putExtra("list", (Serializable) Utils.videoModelArrayList);
                showInterstitialAd(HomeActivity.this, intent, null);
            }
        });

        player_container.setVisibility(View.VISIBLE);
        if (constraint_recent.getVisibility() == View.VISIBLE) {
            constraint_recent.setVisibility(View.GONE);
        }
        Log.d("--playing_path--", "initializePlayer: " + getPlayingVideoPath());
    }

    public String getPlayingVideoPath() {
        if (player_container.getVisibility() == View.VISIBLE) {
            if (simpleExoPlayer != null && playerView != null) {
                return playingVideoPath;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public void releaseMiniPlayer() {
        if (simpleExoPlayer != null) {

            Log.d("--please_release--", "releaseMiniPlayer: ");
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.stop();
            simpleExoPlayer.seekTo(0);
            simpleExoPlayer.release();
//            simpleExoPlayer.clearclearMediaItems();
            simpleExoPlayer = null;
        }
    }

    public void refreshRecent() {
        if (player_container.getVisibility() == View.VISIBLE) {
            player_container.setVisibility(View.GONE);
            playingVideoPath = "";
            releaseMiniPlayer();
        }
        if (mPrefs.getBoolean("display_recent_video", true)) {
            constraint_recent.setVisibility(View.VISIBLE);
            showRecent();
        } else {
            constraint_recent.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("--delete--", "onActivityResult: ");
        if (resultCode == RESULT_OK) {
            Log.d("--delete--", "onActivityResult resultCode == RESULT_OK: ");
            if (requestCode == REQUEST_PERM_DELETE_VIDEO_FILE) {
                Log.d("--delete--", "onActivityResult requestCode == REQUEST_PERM_DELETE: ");
                videosFragment.removeFilesFromList();
            }
            if (requestCode == REQUEST_PERM_DELETE_FOLDER) {
                Log.d("--delete--", "onActivityResult requestCode == REQUEST_PERM_DELETE_FOLDER: ");
                foldersFragment.removeFilesFromList();
            }
        }
    }
}
