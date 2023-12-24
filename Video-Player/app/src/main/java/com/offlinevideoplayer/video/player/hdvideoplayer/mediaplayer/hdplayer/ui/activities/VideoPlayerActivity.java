package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.EqualizerDataList;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.VerticalSeekBar;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq.BassBoosts;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq.Equalizers;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq.Loud;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.eq.Virtualizers;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.BackgroundSoundService;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.DoubleClick;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.DoubleClickListener;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.FilePicker;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.MyBroadcastReceiver;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.MyService;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.OnSwipeTouchListener;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PlayerService;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PrefUtil;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PreferenceUtil;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.PrivateItem;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.TrackSelectionDialog;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.floating;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "audiotracksd";
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final int SUBTITLEGET = 25;
    private static int sw = 15;
    private static MediaSource videoSource;
    private static ArrayList<EqualizerDataList> equalizerListData;
    DefaultTrackSelector trackSelector;
    int seekArcProgress;
    MediaSource[] videoSources;
    DataSource.Factory dataSourceFactory;
    CheckBox subcheck;
    boolean subbool = false;
    Format subtitleFormat;
    MediaSource subtitleSource;
    MergingMediaSource mergedSource;
    ImageView adclosebtn;
    FrameLayout adfrm, vadfrm;
    SharedPreferences appPreferences;
    VideoViewModel videoViewModel;
    private ConstraintLayout control_layout;
    private ImageView crop, volume;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;
    private SimpleExoPlayer player;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            player.stop();
        }
    };
    private PlayerView playerView;
    private ImageView rotate, equalizer;
    private ImageView lock;
    private ImageView setting_menu_control;
    private HorizontalScrollView control_menu_bar;
    private ImageView unlock;
    private ImageView back;
    private ImageView repeat;
    private ImageView img_quick_quit, exo_next, exo_prev;
    private ImageView popup;
    private ImageView playlist, takePicture, subtitle, brightness, share, imgMore;
    private TextView title;
    private TextView tvolume, tvoltitl;
    private TextView pspeed;
    private TextView dspeed;
    private Boolean lockstatus = false;
    private Boolean repeatstatus = false;
    private Boolean swdecoder = false;
    private Boolean batterseen = false;
    private int position = -1;
    private int view = 0;
    private boolean db = true;
    private int width;
    private float brightnessv;
    private float speedv;
    private WindowManager.LayoutParams attributes;
    private SeekBar vseekBar;
    private AudioManager audioManager;
    private PlaybackParameters parameters;
    private long current;
    private PlayerService service;
    private boolean serviceBound;
    private BottomSheetDialog bottomSheetDialog;
    private List<VideoModel> videolist;
    private boolean checktrue = false;
    private boolean defualt = false;
    private boolean dislis = false;
    private VerticalSeekBar[] seekBarFinal = new VerticalSeekBar[5];
    private VerticalSeekBar seekBar;
    private TextView textView;
    private MyAdapter myAdapter;
    private RecyclerView rvEqualizer;
    private PreferenceUtil util;
    private int reverbSetting = 0;
    private PresetReverb presetReverb;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private int orientation;
    private boolean isOrientation = true;
    private ConstraintLayout sheetmain;
    private PictureInPictureParams.Builder mPictureInPictureParamsBuilder;
    private boolean isShowingTrackSelectionDialog = false;
    private boolean isOpen = false;
    private boolean isPIPMode;
    //    private AdView mAdView;
    private boolean adbool = true;
    private boolean isloaded = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        registerReceiver(broadcastReceiver, new IntentFilter("Stop_play_video"));

        control_layout = findViewById(R.id.control_layout);
        adclosebtn = findViewById(R.id.adclosebtn);
        adfrm = findViewById(R.id.adfrm);
        vadfrm = findViewById(R.id.vadfrm);
        exo_next = findViewById(R.id.exo_next);
        exo_prev = findViewById(R.id.exo_prev);
        title = findViewById(R.id.title);

        videoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(VideoPlayerActivity.this.getApplication()).create(VideoViewModel.class);


        if (PreferenceUtil.getInstance(VideoPlayerActivity.this).getBatterylock()) {
            batterseen = true;
        } else {
            batterseen = false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
        }


        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        display.getRealMetrics(realDisplayMetrics);
        brightnessv = PreferenceUtil.getInstance(this).getLastBrightness();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        speedv = PreferenceUtil.getInstance(this).getLastSpeed();
        orientation = PreferenceUtil.getInstance(getApplicationContext()).getOrientation();

        appPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        playerView = findViewById(R.id.player_view);

        position = appPreferences.getInt("position", 0);
        videolist = (ArrayList<VideoModel>) getIntent().getSerializableExtra("list");
        current = appPreferences.getLong("seek_position", 0);
        Log.d(TAG, "onCreate: ");
        Log.d("--next--", "onCreate: position " + position);

        initializePlayer();

        boolean is_from_mini_player = getIntent().getBooleanExtra("is_from_mini_player", false);
        if (is_from_mini_player) {
            appPreferences.edit().putBoolean("is_from_mini_player", false).apply();
            appPreferences.edit().putBoolean("is_orientation_changed", false).apply();

            long seekTo = appPreferences.getLong("seek_position", 0);
            player.seekTo(seekTo);
        }


        if (videolist != null) {
            videoSources = new MediaSource[videolist.size()];
            for (int i = 0; i < videolist.size(); i++) {
                videoSources[i] = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(videolist.get(i).getPath()));

                Log.e("kfdkfkjknj", "onCreate: " + videolist.get(i).getPath());
            }

            videoSource = videoSources.length == 1 ? videoSources[0] : new ConcatenatingMediaSource(videoSources);
        } else {
            Log.d(TAG, "onCreate: " + getIntent().getDataString());
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(getIntent().getDataString()));
        }

        attributes = getWindow().getAttributes();
        attributes.screenBrightness = brightnessv;
        getWindow().setAttributes(attributes);

        parameters = new PlaybackParameters(speedv);
        player.setPlaybackParameters(parameters);
        playerView.setPlayer(player);
        player.prepare(videoSource);
        player.seekTo(position, current);
        player.setPlayWhenReady(false);
        int resume1 = PreferenceUtil.getInstance(this).getResumestatus();
        int poscurrent = player.getCurrentWindowIndex();
        long time = PreferenceUtil.getInstance(this).getresumeVideotime(this, videolist.get(poscurrent).getTitle());

        Log.e("kmmjjenndi", poscurrent + " onCreate:r rst= " + resume1 + " time= " + time);

        if (PreferenceUtil.getInstance(VideoPlayerActivity.this).getResumBool()) {
            if (resume1 == 0) {
                player.setPlayWhenReady(true);
                player.seekTo(position, time);
            } else if (resume1 == 1) {
                player.setPlayWhenReady(true);
                player.seekTo(position, current);
            } else {
                resumeAskDialog();
            }
        } else {
            player.setPlayWhenReady(true);
            player.seekTo(position, current);
            PreferenceUtil.getInstance().saveResumBool(true);
        }
//        player.seekTo(position, current);

        playerView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }

            @Override
            public void onDoubleClick(View view) {
                if (db) {
                    Log.d("--fling--", "onDoubleClick: false");
                    player.setPlayWhenReady(false);
                    db = false;
                } else {
                    Log.d("--fling--", "onDoubleClick: true");
                    player.setPlayWhenReady(true);
                    db = true;
                }
            }
        }));


        equalizer = findViewById(R.id.equalizer);
        rotate = findViewById(R.id.rotate);
        lock = findViewById(R.id.lock);
        unlock = findViewById(R.id.unlock);
        crop = findViewById(R.id.exo_crop);
        back = findViewById(R.id.back);
        share = findViewById(R.id.share);
        imgMore = findViewById(R.id.imgMore);
        takePicture = findViewById(R.id.takePicture);
        brightness = findViewById(R.id.brightness);
//       subtitle = findViewById(R.id.subtitle);
        volume = findViewById(R.id.exo_volume);
        pspeed = findViewById(R.id.pspeed);
        repeat = findViewById(R.id.repeat);
        popup = findViewById(R.id.popup);
//       playlist = findViewById(R.id.playlist);
        sheetmain = findViewById(R.id.sheetmain);
        img_quick_quit = findViewById(R.id.quick_quit);
        setting_menu_control = findViewById(R.id.setting_menu_control);
        control_menu_bar = findViewById(R.id.control_menu_bar);

//        bottomSheetDialog = new BottomSheetDialog(this);
//        View modal = getLayoutInflater().inflate(R.layout.playlist_sheet, null);
//        RecyclerView r1 = findViewById(R.id.r1);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        r1.setLayoutManager(layoutManager);
//        r1.getLayoutManager().scrollToPosition(position);
//        PlayListAdapter adapter = new PlayListAdapter(this, list, player);
//        r1.setAdapter(adapter);
//        bottomSheetDialog.setContentView(modal);

        pspeed.setText(String.format("%sX", speedv));

        control_menu_bar.fullScroll(HorizontalScrollView.FOCUS_RIGHT / View.FOCUS_LEFT);
        control_menu_bar.smoothScrollBy(0, 500);

        setting_menu_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (control_menu_bar.getVisibility() == View.GONE) {
                    control_menu_bar.setVisibility(View.VISIBLE);
                } else {
                    control_menu_bar.setVisibility(View.GONE);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_quick_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickQuitActivity.exitApplication(VideoPlayerActivity.this);
            }
        });

        exo_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("--next--", " before exo_next: " + position);
                position = position + 1;
                if (position < videolist.size()) {
                    Log.d("--next--", "after exo_next: " + position);
                    appPreferences.edit().putInt("position", position).apply();

                    appPreferences.edit().putBoolean("video_orientation", true).apply();
                    initializePlayer();
                } else {
                    Toast.makeText(VideoPlayerActivity.this, "No Video available to play on next", Toast.LENGTH_SHORT).show();
                    Log.d("--next--", "else exo_next: " + position);
                    position--;
                }
            }
        });

        exo_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = position - 1;
                if (position >= 0) {
                    Log.d("--position--", "exo_prev: " + position);
                    appPreferences.edit().putInt("position", position).apply();

                    appPreferences.edit().putBoolean("video_orientation", true).apply();
                    initializePlayer();
                } else {
                    Toast.makeText(VideoPlayerActivity.this, "No Video available to play on previous", Toast.LENGTH_SHORT).show();
                    position++;
                }
            }
        });


//        subtitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerTestActivity.this, R.style.CustomDialog);
//                LayoutInflater inflater = LayoutInflater.from(VideoPlayerTestActivity.this);
//                View v1 = inflater.inflate(R.layout.subtitle_dialog, null);
//
//                TextView txtonline = v1.findViewById(R.id.txtonline);
//                TextView txtofline = v1.findViewById(R.id.txtoffline);
//                FrameLayout txtnone = v1.findViewById(R.id.txtnone);
//                subcheck = v1.findViewById(R.id.none_check);
//                builder.setView(v1);
//                final AlertDialog dialog = builder.create();
//                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//                wmlp.gravity = Gravity.TOP | Gravity.START;
//                wmlp.x = view.getLeft();
//                wmlp.y = view.getTop();
//
//                subcheck.setChecked(subbool);
//
//                txtofline.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        dialog.dismiss();
//                        ArrayList<String> collection = new ArrayList<>();
//                        collection.add(".srt");
//
//                        Intent intd = new Intent(VideoPlayerTestActivity.this, FilePicker.class);
//
//                        intd.putExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS, collection);
//
//                        startActivityForResult(intd, SUBTITLEGET);
//
//
//                    }
//                });
//
//                txtnone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        dialog.dismiss();
//
//                        if (!subbool) {
//                            player.prepare(videoSource, false, false);
//                            player.setPlayWhenReady(true);
//                            subbool = true;
//                        } else {
//                            if (mergedSource != null) {
//                                player.prepare(mergedSource, false, false);
//                                player.setPlayWhenReady(true);
//                            }
//                            subbool = false;
//                        }
//                    }
//                });
//
//                dialog.show();
//            }
//        });

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = view.getLeft() - (view.getWidth() * 2);
                int y = view.getTop() + (view.getHeight() * 2);
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.CustomDialog);
                LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                View v1 = inflater.inflate(R.layout.more_dialog, null);

                TextView txtShareVideo = v1.findViewById(R.id.txtShareVideo);
                TextView txtSleepTimer = v1.findViewById(R.id.txtSleepTimer);
                CheckBox cbRepeatOne = v1.findViewById(R.id.cbRepeatOne);
                TextView txtSetting = v1.findViewById(R.id.txtSetting);
//                TextView txtbackground = v1.findViewById(R.id.txtbackground);
                TextView txtAudioTrack = v1.findViewById(R.id.txtAudioTrack);
                TextView txtother = v1.findViewById(R.id.txtother);

                //boolean isRepeatOne = PreferenceUtil.getInstance(VideoPlayerActivity.this).getRepeatOne();
                cbRepeatOne.setChecked(repeatstatus);
                builder.setView(v1);
                final AlertDialog dialog = builder.create();
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.START;
                wmlp.x = view.getLeft();
                wmlp.y = view.getTop();


                txtother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.CustomDialog);
                        LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                        View v1 = inflater.inflate(R.layout.other_dialog, null);
                        builder.setView(v1);

                        CheckBox swcheckone = v1.findViewById(R.id.swcheckOne);
                        TextView txtdelete = v1.findViewById(R.id.txtdelete);
//                        TextView txtlock = v1.findViewById(R.id.txtlock);
//                        TextView txtrename = v1.findViewById(R.id.txtrename);
                        TextView txtproperty = v1.findViewById(R.id.txtproperty);

                        final AlertDialog dialog = builder.create();
                        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                        wmlp.gravity = Gravity.TOP | Gravity.END;
                        wmlp.x = v1.getLeft();
                        wmlp.y = v1.getTop();
                        swcheckone.setChecked(swdecoder);

                        txtproperty.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                propertiDialog();

                            }
                        });

                       /* txtrename.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                renameVideofile();
                            }
                        });*/

                        txtdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                player.setPlayWhenReady(false);
                                deleteVideo();
                            }
                        });

                        swcheckone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!swdecoder) {
                                    swcheckone.setChecked(true);
                                    swdecoder = !swdecoder;
                                } else {
                                    swcheckone.setChecked(false);
                                    swcheckone.setChecked(true);
                                    swdecoder = !swdecoder;
                                }
                                dialog.dismiss();

                            }
                        });
                        dialog.show();
                    }

                });

                txtAudioTrack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
//                        Toast.makeText(VideoPlayerActivity.this, "outer", Toast.LENGTH_SHORT).show();

                        if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {

                            // Toast.makeText(VideoPlayerActivity.this, "inner", Toast.LENGTH_SHORT).show();
                            TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector, dismissedDialog ->
                                    isShowingTrackSelectionDialog = false
                            );
                            trackSelectionDialog.show(getSupportFragmentManager(), null);
                        }

                    }
                });


//                txtbackground.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                          minimize();
//                        int pos = player.getCurrentWindowIndex();
//                        long curt = player.getCurrentPosition();
//
//                        Intent intent = new Intent(VideoPlayerTestActivity.this, BackgroundSoundService.class);
//                        appPreferences.edit().putLong("seek_position", player.getCurrentPosition()).apply();
//                        appPreferences.edit().putInt("position", player.getCurrentWindowIndex()).apply();
//                        intent.putExtra("list", (Serializable) videolist);
////                        intent.putExtra("list", (Serializable) videolist);
////                        intent.putExtra("position", pos);
////                        intent.putExtra("current", curt);
//                        startService(intent);
////                        startService(new Intent(VideoPlayerActivity.this, SensorListener.class));
//                        player.setPlayWhenReady(false);
////
//                        PreferenceUtil.getInstance(VideoPlayerTestActivity.this).saveResumBool(false);
//                        finish();
//
//                    }
//                });

                txtSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        isOrientation = true;
                        Intent intent1 = new Intent(VideoPlayerActivity.this, SettingsActivity.class);
                        startActivity(intent1);
                    }
                });

                txtShareVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(videolist.get(position).getPath()));
                            player.setPlayWhenReady(false);
                            Intent my = new Intent(Intent.ACTION_SEND);
                            my.setType("video/*");
                            my.putExtra(Intent.EXTRA_STREAM, imageUri);
                            my.putExtra(Intent.EXTRA_TEXT, videolist.get(position).getTitle());
                            my.putExtra(Intent.EXTRA_SUBJECT, videolist.get(position).getTitle());
                            startActivity(Intent.createChooser(my, "Share Video"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                cbRepeatOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*if (((CompoundButton) view).isChecked()) {
                            PreferenceUtil.getInstance(VideoPlayerActivity.this).saveRepeatOne(true);
                            cbRepeatOne.setChecked(true);
                            player.setRepeatMode(Player.REPEAT_MODE_ALL);
                        } else {
                            PreferenceUtil.getInstance(VideoPlayerActivity.this).saveRepeatOne(false);
                            cbRepeatOne.setChecked(false);
                            player.setRepeatMode(Player.REPEAT_MODE_OFF);
                        }*/

                        if (!repeatstatus) {
                            cbRepeatOne.setChecked(true);
                            repeat.setImageResource(R.drawable.ic_repeatone);
                            player.setRepeatMode(Player.REPEAT_MODE_ONE);
                            repeatstatus = !repeatstatus;
                        } else {
                            cbRepeatOne.setChecked(false);
                            repeat.setImageResource(R.drawable.ic_repeatno);
                            player.setRepeatMode(Player.REPEAT_MODE_OFF);
                            repeatstatus = !repeatstatus;
                        }
                    }
                });

                txtSleepTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (isPlaying()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.PropertyDialog);
                            LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                            View v1 = inflater.inflate(R.layout.dialog_sleep_timer, null);

                            SeekBar seekArc = (SeekBar) v1.findViewById(R.id.seek_arc);
                            TextView txtTitle = (TextView) v1.findViewById(R.id.txtTitle);
                            TextView timerDisplay = (TextView) v1.findViewById(R.id.timer_display);
                            TextView txtCancel = (TextView) v1.findViewById(R.id.txtCancel);
                            TextView txtDisable = (TextView) v1.findViewById(R.id.txtDisable);
                            TextView txtSave = (TextView) v1.findViewById(R.id.txtSave);
                            TextView timer_cancel = (TextView) v1.findViewById(R.id.timer_cancel);

                            builder.setView(v1);
                            final AlertDialog dialog = builder.create();

                            seekArcProgress = PreferenceUtil.getInstance(getApplicationContext()).getLastSleepTimerValue();
                            timerDisplay.setText("Stop Playing After: " + seekArcProgress + " minute(s)");
                            seekArc.setProgress(seekArcProgress);

                            seekArc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    if (i < 1) {
                                        seekArc.setProgress(1);
                                        return;
                                    }

                                    seekArcProgress = i;
                                    timerDisplay.setText("Stop Playing After: " + seekArcProgress + " minute(s)");
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    PreferenceUtil.getInstance(getApplicationContext()).setLastSleepTimerValue(seekArcProgress);
                                }
                            });

                            txtSave.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("StringFormatInvalid")
                                @Override
                                public void onClick(View view) {
                                    final int minutes = seekArcProgress;
                                    final long nextSleepTimerElapsedTime = minutes * 60 * 1000;
                                    Log.e("nextSleepTimer", " : " + nextSleepTimerElapsedTime);
                                    MyService.millisecs = nextSleepTimerElapsedTime;

                                    Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                            getApplicationContext(), 234324243, intent, 0);
                                    AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                            + nextSleepTimerElapsedTime, pendingIntent);

                                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.sleep_timer_set, minutes), Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                }
                            });

                            txtCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            txtDisable.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                            getApplicationContext(), 234324243, intent, 0);
                                    if (pendingIntent != null) {
                                        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                        am.cancel(pendingIntent);
                                        pendingIntent.cancel();
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.PropertyDialog);
                            LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                            View v1 = inflater.inflate(R.layout.dialog_sleep_timer_off, null);

                            TextView txtTitle = (TextView) v1.findViewById(R.id.txtTitle);
                            TextView timerDisplay = (TextView) v1.findViewById(R.id.timer_display);
                            TextView txtCancel = (TextView) v1.findViewById(R.id.txtCancel);

                            builder.setView(v1);
                            final AlertDialog dialog = builder.create();

                            txtCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }

                    }
                });

                dialog.show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayWhenReady(false);
                Intent my = new Intent(Intent.ACTION_SEND);
                my.setType("video/*");
                my.putExtra(Intent.EXTRA_STREAM, Uri.parse(videolist.get(position).getPath()));
                my.putExtra(Intent.EXTRA_TEXT, videolist.get(position).getTitle());
                my.putExtra(Intent.EXTRA_SUBJECT, videolist.get(position).getTitle());
                startActivity(Intent.createChooser(my, "Share Video"));
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaMetadataRetriever.setDataSource(VideoPlayerActivity.this, Uri.parse(videolist.get(position).getPath()));
                final Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime((long) (player.getCurrentPosition() * 1000));
                save_image(bmFrame);
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock();
            }
        });

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlock();
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (view) {
                    case 0:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        view = 3;
                        crop.setImageResource(R.drawable.ic_fill_screen);
                        break;
                    case 3:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                        crop.setImageResource(R.drawable.ic_zoom);
                        view = 4;
                        break;
                    case 4:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        crop.setImageResource(R.drawable.ic_fit);
                        view = 0;
                        break;
                }

            }
        });

        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.CustomDialog);
                LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                View view = inflater.inflate(R.layout.brightness_dialog, null);
                final TextView t1 = view.findViewById(R.id.progress);
                final ImageView imgClose = view.findViewById(R.id.imgClose);
                SeekBar seekBar = view.findViewById(R.id.seekBar);
                int x = (int) (PreferenceUtil.getInstance(VideoPlayerActivity.this).getLastBrightness() * 100);
                seekBar.setProgress(x);
                t1.setText(Integer.toString(x));

                builder.setView(view);
                final AlertDialog dialog = builder.create();

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        attributes.screenBrightness = (progress / 100.0f);
                        getWindow().setAttributes(attributes);
                        seekBar.setProgress(progress);
                        t1.setText(Integer.toString(progress));
                        PreferenceUtil.getInstance(getApplicationContext()).saveLastBrightness(attributes.screenBrightness);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.CustomDialog);
                LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                View v1 = inflater.inflate(R.layout.volume_dialog, null);
                tvolume = v1.findViewById(R.id.progress);
                vseekBar = v1.findViewById(R.id.seekBar);
                final ImageView imgClose = v1.findViewById(R.id.imgClose);
                int x = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                vseekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                vseekBar.setProgress(x);
                tvolume.setText(Integer.toString(x));

                builder.setView(v1);
                final AlertDialog dialog = builder.create();

                vseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekBar.setProgress(progress);
                        tvolume.setText(Integer.toString(progress));
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    presetReverb = new PresetReverb(0, player.getAudioSessionId());
                    enableDisable(PreferenceUtil.getInstance(getApplicationContext()).geteqSwitch());
                    Equalizers.initEq(player.getAudioSessionId());
                    BassBoosts.initBass(player.getAudioSessionId());
                    Virtualizers.initVirtualizer(player.getAudioSessionId());
                    Loud.initLoudnessEnhancer(player.getAudioSessionId());

                    loadEqualizer();

                } catch (Exception e) {
                    Log.e("ikmhhhuhuj", "onClick: " + e.getMessage());
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.CustomDialog);
                LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                View view = inflater.inflate(R.layout.equalizer_dialog, null);

                ImageView imgClose = view.findViewById(R.id.imgClose);
                LinearLayout llEqualizerView = view.findViewById(R.id.llEqualizerView);

                SwitchCompat switch_button = view.findViewById(R.id.switch_button);
                Spinner reverb_spinner = view.findViewById(R.id.reverb_spinner);
                rvEqualizer = view.findViewById(R.id.rvEqualizer);
                rvEqualizer.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false));
                rvEqualizer.setFocusable(true);
                //rvEqualizer.setHasFixedSize(true);

                myAdapter = new MyAdapter(getApplicationContext(), equalizerListData);
                rvEqualizer.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();

                //Init reverb presets.
                ArrayList<String> reverbPresets = new ArrayList<String>();
                reverbPresets.add(getString(R.string.preset_none));
                reverbPresets.add(getString(R.string.preset_large_hall));
                reverbPresets.add(getString(R.string.preset_large_room));
                reverbPresets.add(getString(R.string.preset_medium_hall));
                reverbPresets.add(getString(R.string.preset_medium_room));
                reverbPresets.add(getString(R.string.preset_small_room));
                reverbPresets.add(getString(R.string.preset_plate));

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview_to_spinner, reverbPresets);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reverb_spinner.setAdapter(dataAdapter);
                reverb_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                        try {
                            if (index == 0) {
                                presetReverb.setPreset(PresetReverb.PRESET_NONE);
                                reverbSetting = 0;
                            } else if (index == 1) {
                                presetReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
                                reverbSetting = 1;
                            } else if (index == 2) {
                                presetReverb.setPreset(PresetReverb.PRESET_LARGEROOM);
                                reverbSetting = 2;
                            } else if (index == 3) {
                                presetReverb.setPreset(PresetReverb.PRESET_MEDIUMHALL);
                                reverbSetting = 3;
                            } else if (index == 4) {
                                presetReverb.setPreset(PresetReverb.PRESET_MEDIUMROOM);
                                reverbSetting = 4;
                            } else if (index == 5) {
                                presetReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
                                reverbSetting = 5;
                            } else if (index == 6) {
                                presetReverb.setPreset(PresetReverb.PRESET_PLATE);
                                reverbSetting = 6;
                            } else {
                                reverbSetting = 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                switch_button.setChecked(PreferenceUtil.getInstance(getApplicationContext()).geteqSwitch());
                //switch_button.getThumbDrawable().setColorFilter(ThemeStore.accentColor(getApplicationContext()), PorterDuff.Mode.MULTIPLY);
                //switch_button.getTrackDrawable().setColorFilter(ThemeStore.accentColor(getApplicationContext()), PorterDuff.Mode.MULTIPLY);

                if (PreferenceUtil.getInstance(getApplicationContext()).geteqSwitch()) {
                    llEqualizerView.setVisibility(View.GONE);
                } else {
                    llEqualizerView.setVisibility(View.VISIBLE);
                }

                switch_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switch_button.isChecked()) {
                            PreferenceUtil.getInstance(getApplicationContext()).eqSwitch(true);
                            enableDisable(true);
                            llEqualizerView.setVisibility(View.GONE);
                        } else {
                            PreferenceUtil.getInstance(getApplicationContext()).eqSwitch(false);
                            enableDisable(false);
                            llEqualizerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

                SeekBar seek_bar_bass_boots = (SeekBar) view.findViewById(R.id.seek_bar_bass_boots);
                SeekBar seek_bar_virtualizer = (SeekBar) view.findViewById(R.id.seek_bar_virtualizer);

                //seek_bar_bass_boots.getProgressDrawable().setColorFilter(ThemeStore.accentColor(getApplicationContext()), PorterDuff.Mode.SRC_IN);
                //seek_bar_bass_boots.getThumb().setColorFilter(ThemeStore.accentColor(getApplicationContext()), PorterDuff.Mode.SRC_IN);

                //seek_bar_virtualizer.getProgressDrawable().setColorFilter(ThemeStore.accentColor(getApplicationContext()), PorterDuff.Mode.SRC_IN);
                //seek_bar_virtualizer.getThumb().setColorFilter(ThemeStore.accentColor(getApplicationContext()), PorterDuff.Mode.SRC_IN);

                seek_bar_bass_boots.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        short str = (short) (((float) 1000 / 20) * (progress));
                        BassBoosts.setBassBoostStrength(str);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                int savedBass = (PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.BASS_BOOST, 0) * 20) / 1000;
                if (savedBass > 0) {
                    seek_bar_bass_boots.setProgress(savedBass);
                    BassBoosts.setBassBoostStrength((short) savedBass);
                } else {
                    seek_bar_bass_boots.setProgress(1);
                }

                seek_bar_virtualizer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        short virtualstr = (short) (((float) 1000 / 20) * (progress));
                        Virtualizers.setVirtualizerStrength(virtualstr);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                int saveVirtual = (PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.VIRTUAL_BOOST, 0) * 20) / 1000;
                if (saveVirtual > 0) {
                    seek_bar_virtualizer.setProgress(saveVirtual);
                    Virtualizers.setVirtualizerStrength((short) saveVirtual);
                } else {
                    seek_bar_virtualizer.setProgress(1);
                }

                initEq(view);
                builder.setView(view);
                final AlertDialog dialog = builder.create();

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        pspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.PropertyDialog);
                LayoutInflater inflater = LayoutInflater.from(VideoPlayerActivity.this);
                View v1 = inflater.inflate(R.layout.playback_dialog, null);
                ImageButton sdown, sup;
                sdown = v1.findViewById(R.id.sdown);
                sup = v1.findViewById(R.id.sup);
                dspeed = v1.findViewById(R.id.dspeed);
                final AtomicInteger d = new AtomicInteger((int) (PreferenceUtil.getInstance(VideoPlayerActivity.this).getLastSpeed() * 100));
                dspeed.setText(Integer.toString(d.get()));
                sdown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        if (d.get() <= 100) {
                            d.set(d.get() - 5);
                            if (d.get() < 24) {
                                d.set(d.get() + 5);
                            }
                        } else {
                            d.set(d.get() - 10);
                        }
                        setSpeed(d.get());
                    }
                });
                sup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v22) {
                        if (d.get() < 100) {
                            d.set(d.get() + 5);
                        } else {
                            d.set(d.get() + 10);
                            if (d.get() > 401) {
                                d.set(d.get() - 10);
                            }
                        }
                        setSpeed(d.get());
                    }
                });

                builder.setView(v1);
                builder.show();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!repeatstatus) {
                    PreferenceUtil.getInstance(VideoPlayerActivity.this).saveRepeatOne(true);
                    repeat.setImageResource(R.drawable.ic_repeatone);
                    player.setRepeatMode(Player.REPEAT_MODE_ONE);
                    repeatstatus = !repeatstatus;
                } else {
                    PreferenceUtil.getInstance(VideoPlayerActivity.this).saveRepeatOne(false);
                    repeat.setImageResource(R.drawable.ic_repeatno);
                    player.setRepeatMode(Player.REPEAT_MODE_OFF);
                    repeatstatus = !repeatstatus;
                }
            }
        });


        int change = getResources().getConfiguration().orientation;
        // int change = rotatechange();
        if (change == Configuration.ORIENTATION_PORTRAIT) {
            width = realDisplayMetrics.widthPixels;
            titlepot();
        } else {
            width = realDisplayMetrics.heightPixels;
            titleland();
        }
        if (videolist != null) {
            title.setText(videolist.get(position).getTitle());
            Gson gson = new Gson();
            List<VideoModel> textList = new ArrayList<VideoModel>();
            textList.addAll(videolist);
            PreferenceUtil.getInstance(getApplicationContext()).setVideoURL(gson.toJson(textList).toString());
            PreferenceUtil.getInstance(getApplicationContext()).setVideoPosition(position);
        } else {
            title.setText(uri2filename());
        }

        Log.e("dfdjmkk", "onClick: " + orientation);

        // rotateScreen();

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOrientation = false;
                orientation = PreferenceUtil.getInstance(getApplicationContext()).getOrientation();

                Log.e("dfdjmkk", "onClick: " + orientation);
                if (orientation == 0) {
                    Toast.makeText(getApplicationContext(), "Landscape Locked", Toast.LENGTH_SHORT).show();
                    rotate.setImageDrawable(getResources().getDrawable(R.drawable.ic_screen_lock_landscape_black_24dp));
                    PreferenceUtil.getInstance(getApplicationContext()).saveOrientation(1);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else if (orientation == 1) {
                    Toast.makeText(getApplicationContext(), "Portrait Locked", Toast.LENGTH_SHORT).show();
                    rotate.setImageDrawable(getResources().getDrawable(R.drawable.ic_screen_lock_portrait_black_24dp));
                    PreferenceUtil.getInstance(getApplicationContext()).saveOrientation(2);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    Toast.makeText(getApplicationContext(), "Auto Rotate", Toast.LENGTH_SHORT).show();
                    rotate.setImageDrawable(getResources().getDrawable(R.drawable.ic_rotate));
                    PreferenceUtil.getInstance(getApplicationContext()).saveOrientation(0);
//                    rotateScreen();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                }

               /* int ori = getResources().getConfiguration().orientation;
                if (ori == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }*/
            }
        });

        player.addListener(new Player.EventListener() {

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                if (trackGroups != lastSeenTrackGroupArray) {
                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                    if (mappedTrackInfo != null) {
                        if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                            Toast.makeText(VideoPlayerActivity.this, "Media includes video tracks, but none are playable by this device", Toast.LENGTH_SHORT).show();
                        }
                        if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                            Toast.makeText(VideoPlayerActivity.this, "Media includes audio tracks, but none are playable by this device", Toast.LENGTH_SHORT).show();
                        }
                    }
                    lastSeenTrackGroupArray = trackGroups;
                }
            }


            @Override
            public void onPositionDiscontinuity(int reason) {
                //   player.setPlayWhenReady(false);
                if (!PreferenceUtil.getInstance().getAutoplaynext()) {
                    if (Player.DISCONTINUITY_REASON_PERIOD_TRANSITION == reason) {
                        player.setPlayWhenReady(false);
                    }
                }


                int change = player.getCurrentWindowIndex();
                if (change != position) {
                    position = change;
                    title.setText(videolist.get(change).getTitle());
                    Gson gson = new Gson();
                    List<VideoModel> textList = new ArrayList<VideoModel>();
                    textList.addAll(videolist);
                    PreferenceUtil.getInstance(getApplicationContext()).setVideoURL(gson.toJson(textList).toString());
                    PreferenceUtil.getInstance(getApplicationContext()).setVideoPosition(position);
                    // bottomSheetDialog.dismiss();
                }
            }


            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (adbool && isloaded) {
                    if (playWhenReady) {
                        vadfrm.setVisibility(View.GONE);
                    } else {
                        vadfrm.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.EventListener.super.onIsPlayingChanged(isPlaying);

                long seekPosition = player.getCurrentPosition();
                appPreferences.edit().putLong("seek_position", seekPosition).apply();
            }
        });

        playerView.setOnTouchListener(new OnSwipeTouchListener(VideoPlayerActivity.this, player, playerView, audioManager));

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                } else {
                    popup();
                }
            }
        });

        sheetmain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sheetmain.setVisibility(View.GONE);
                return false;
            }
        });

//        playlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                sheetmain.setVisibility(View.VISIBLE);
////                Intent x = new Intent(VideoPlayerTestActivity.this, PlayerService.class);
////                x.putExtra("list", (Serializable) list);
////                bindService(x, serviceConnection, Context.BIND_AUTO_CREATE);
////                startService(x);
////                onBackPressed();
//
//                Intent intent = new Intent(VideoPlayerTestActivity.this, PlayerService.class);
//                intent.putExtra("list", (Serializable) list);
//                intent.putExtra("position", position);
//                Log.d("--list--", "onClick: position---"+position+"--list--"+list.size());
//                startService(intent);
//
//                /*Intent callService = new Intent(VideoPlayerActivity.this, PlayerService.class);
//                callService.putExtra("list", (Serializable) list);
//                startService(callService);
//                bindService(callService, serviceConnection, Context.BIND_AUTO_CREATE);*/
//
//                 bottomSheetDialog.show();
//            }
//        });

        try {
            presetReverb = new PresetReverb(0, player.getAudioSessionId());
            Equalizers.initEq(player.getAudioSessionId());
            BassBoosts.initBass(player.getAudioSessionId());
            Virtualizers.initVirtualizer(player.getAudioSessionId());
            Loud.initLoudnessEnhancer(player.getAudioSessionId());

            loadEqualizer();

            enableDisable(PreferenceUtil.getInstance(getApplicationContext()).geteqSwitch());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayer() {

        TrackSelection.Factory trackSelectionFactory;
        trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
        trackSelector.setParameters(trackSelectorParameters);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        stopService(new Intent(this, BackgroundSoundService.class));

        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getPackageName()));

        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
        }

        if (position == 0) {
            exo_prev.setVisibility(View.GONE);
        } else {
            exo_prev.setVisibility(View.VISIBLE);
        }

        // TODO: Maintain Visibility of Next Icon when there is last index of array == not done
        if (position == (videolist.size() - 1)) {
            exo_next.setVisibility(View.GONE);
        } else {
            exo_next.setVisibility(View.VISIBLE);
        }

        Log.d("--next--", "initializePlayer:-- " + position);
        VideoModel fileModel = videolist.get(position);

        String path = fileModel.getPath();
        Log.d("--next--", "initializePlayer: path " + videolist.get(0).getPath());

//        if (!videoViewModel.isFileExist(path)) {
        String filename = new File(path).getName();
        String result = filename.substring(0, filename.lastIndexOf("."));
        title.setText(result);

        Uri uri = Uri.parse(path);
        player = ExoPlayerFactory.newSimpleInstance(this);
        DataSource.Factory factory = new DefaultDataSourceFactory(VideoPlayerActivity.this, Util.getUserAgent(VideoPlayerActivity.this, String.valueOf(R.string.app_name)));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);

        player.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.EventListener.super.onIsPlayingChanged(isPlaying);

                long seekPosition = player.getCurrentPosition();
                appPreferences.edit().putLong("seek_position", seekPosition).apply();
            }
        });

        if (appPreferences.getBoolean("video_orientation", true)) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            Bitmap frame = retriever.getFrameAtTime();
            int width = frame.getWidth();
            int height = frame.getHeight();
            retriever.release();

            if (width < height) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            appPreferences.edit().putBoolean("video_orientation", false).apply();
        }
//        }
    }


    public boolean isPlaying() {
        return player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("--next--", "onResume: " + position);

        if (isOrientation) {
            orientation = PreferenceUtil.getInstance(getApplicationContext()).getOrientation();
            Log.e("llmdfmkd", "onResume: " + orientation);
            if (orientation == 0) {
                //  rotateScreen();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                rotate.setImageDrawable(getResources().getDrawable(R.drawable.rotate));
            } else if (orientation == 1) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                rotate.setImageDrawable(getResources().getDrawable(R.drawable.ic_screen_lock_landscape_black_24dp));
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                rotate.setImageDrawable(getResources().getDrawable(R.drawable.ic_screen_lock_portrait_black_24dp));
            }
        }
        setUpLocale();
        String navbarColor = PrefUtil.getstringPref("navbarColor", VideoPlayerActivity.this);
        if (navbarColor.equals("black")) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        } else if (navbarColor.equals("transparent")) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.transparent));
        }
    }

    private List<String> getPresetNames() {
        List<String> presets = new ArrayList<>();
        short presetsNo = Equalizers.getPresetNo();
        if (presetsNo != 0) {
            for (short n = 0; n < presetsNo; n++) {
                presets.add(Equalizers.getPresetNames(n));
            }
            presets.add("Custom");
        }
        return presets;
    }

    private void loadEqualizer() {
        List<String> presetList = getPresetNames();
        Log.e("presetList ", " : " + presetList.size());
        if (presetList.size() == 0) {
            return;
        }

        int presetPos = PreferenceUtil.getInstance(getApplicationContext()).getPresetPos();

        equalizerListData = new ArrayList<EqualizerDataList>();
        for (int presets = 0; presets < presetList.size(); presets++) {
            if (presets == presetPos) {
                equalizerListData.add(presets, new EqualizerDataList(presetList.get(presets), true));
            } else {
                equalizerListData.add(presets, new EqualizerDataList(presetList.get(presets), false));
            }
        }
        Log.e("equalizerListData ", " : " + equalizerListData.size());
    }

    private void enableDisable(Boolean onoroff) {
        Equalizers.setEnabled(onoroff);
        BassBoosts.setEnabled(onoroff);
        Virtualizers.setEnabled(onoroff);
        Loud.setEnabled(onoroff);
        presetReverb.setEnabled(onoroff);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void resumeAskDialog() {

        defualt = false;
        dislis = true;

        View checkboxview = LayoutInflater.from(this).inflate(R.layout.pcheckbox, null);

        long resume1 = PreferenceUtil.getInstance(this).getresumeVideotime(this, videolist.get(position).getTitle());
        Log.e("plkkkmmejjj", resume1 + " resumeAskDialog: " + position + "  " + videolist.get(position).getTitle());

        if (resume1 == 0) {
            Log.e("kmmjjenndi", "resumeAskDialog:==0 ");
            player.setPlayWhenReady(true);
            player.seekTo(position, current);
        } else {
            Log.e("kmmjjenndi", "resumeAskDialog:>=0 ");
            AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this, R.style.PropertyDialog);
            alertDialog1.setTitle(videolist.get(position).getTitle());
            alertDialog1.setMessage("Do you wish to resume from where you stopped?");
            alertDialog1.setView(checkboxview);
            alertDialog1.setPositiveButton("RESUME", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dislis = false;
                    if (defualt) {
                        PreferenceUtil.getInstance(VideoPlayerActivity.this).saveResumestatus(0);
                    }
                    player.setPlayWhenReady(true);
                    player.seekTo(position, resume1);

                }
            });
            alertDialog1.setNegativeButton("START OVER", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dislis = false;
                    if (defualt) {
                        PreferenceUtil.getInstance(VideoPlayerActivity.this).saveResumestatus(1);
                    }
                    player.setPlayWhenReady(true);
                    player.seekTo(position, current);
                }
            });

            alertDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Log.e("kkkjne", "onDismiss: ");

                    if (dislis) {
                        player.setPlayWhenReady(true);
                        player.seekTo(position, current);
                    }
                }
            });

            //   alertDialog1.setCancelable(false);
            alertDialog1.show();


            CheckBox checkBox = (CheckBox) checkboxview.findViewById(R.id.checkbox);
            checkBox.setText("Use by default");
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    defualt = isChecked;
                }
            });

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adjustFullScreen(newConfig);
//        int change = getResources().getConfiguration().orientation;
//        if (change == Configuration.ORIENTATION_LANDSCAPE) titleland();
//        else if (change == Configuration.ORIENTATION_PORTRAIT) titlepot();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        int pp = player.getCurrentWindowIndex();
        Log.e("kmmke", videolist.get(pp).getTitle() + " onPositionDiscontinuity: ");
        PreferenceUtil.getInstance(this).setResumeVideotime(this, player.getContentPosition(), videolist.get(pp).getTitle());
        Log.e("kmmke", "onDestroy: " + player.getContentPosition());

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause() {
        if (lockstatus) {
            unlock();
        }
        Log.d("--next--", "onPause: " + position);
        player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        boolean isActivityOpen = appPreferences.getBoolean("isActivityOpen", true);
        Log.d("isActivityOpen", "Video to Home : isActivityOpen " + isActivityOpen);
        if (!isActivityOpen) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
        }
        if (lockstatus) {
            unlock();
        }
        appPreferences.edit().putString("recent_path", videolist.get(position).getPath()).apply();
        appPreferences.edit().putBoolean("video_orientation", true).apply();
        super.onBackPressed();
    }

    @SuppressLint("Range")
    private String uri2filename() {

        String ret = "";
        String scheme = getIntent().getData().getScheme();

        if (scheme.equals("file")) {
            ret = getIntent().getData().getLastPathSegment();
        } else if (scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(getIntent().getData(), null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return ret;
    }

    private void lock() {
        lockstatus = !lockstatus;
        LinearLayout l1, l2;
        ImageView i1, i2;
        ConstraintLayout c1;
        l1 = findViewById(R.id.bottom_control);
        l1.setVisibility(View.INVISIBLE);
        c1 = findViewById(R.id.constraint_menu);
        c1.setVisibility(View.INVISIBLE);
        i2 = findViewById(R.id.brightness);
        i2.setVisibility(View.INVISIBLE);
        i1 = findViewById(R.id.exo_volume);
        i1.setVisibility(View.INVISIBLE);
        l2 = findViewById(R.id.top_control);
        l2.setVisibility(View.INVISIBLE);
        unlock.setVisibility(View.VISIBLE);
        PreferenceUtil.getInstance(getApplicationContext()).setLock(true);
    }

    private void unlock() {
        lockstatus = !lockstatus;
        LinearLayout l1, l2;
        ImageView i1, i2;
        ConstraintLayout c1;

        l1 = findViewById(R.id.bottom_control);
        l1.setVisibility(View.VISIBLE);
        l2 = findViewById(R.id.top_control);
        l2.setVisibility(View.VISIBLE);
        c1 = findViewById(R.id.constraint_menu);
        c1.setVisibility(View.VISIBLE);
        i2 = findViewById(R.id.brightness);
        i2.setVisibility(View.VISIBLE);
        i1 = findViewById(R.id.exo_volume);
        i1.setVisibility(View.VISIBLE);
        unlock.setVisibility(View.INVISIBLE);
        PreferenceUtil.getInstance(getApplicationContext()).setLock(false);
    }

    private void titlepot() {
        ViewGroup.LayoutParams params = title.getLayoutParams();
        params.width = width / 7;
        title.setLayoutParams(params);
    }

    private void titleland() {
        ViewGroup.LayoutParams params = title.getLayoutParams();
        params.width = width;
        title.setLayoutParams(params);
    }

    private void setSpeed(int d) {
        dspeed.setText(Integer.toString(d));
        float x = d / 100.0f;
        pspeed.setText(String.format("%sX", x));
        parameters = new PlaybackParameters(x);
        player.setPlaybackParameters(parameters);
        PreferenceUtil.getInstance(getApplicationContext()).saveLastSpeed(x);
    }

    private void popup() {
        Format videoFormat = player.getVideoFormat();
        int width = videoFormat.width;
        int height = videoFormat.height;

        Intent x = new Intent(VideoPlayerActivity.this, floating.class);
        appPreferences.edit().putLong("seek_position", player.getCurrentPosition()).apply();
        appPreferences.edit().putInt("position", player.getCurrentWindowIndex()).apply();
        x.putExtra("list", (Serializable) videolist);
        appPreferences.edit().putInt("width", width).apply();
        appPreferences.edit().putInt("height", height).apply();
        startService(x);
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                popup();
            } else { //Permission is not available
                checkSetPer();
                finish();
            }
        } else if (requestCode == SUBTITLEGET) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                    File selectedFile = new File(Objects.requireNonNull(data.getStringExtra(FilePicker.EXTRA_FILE_PATH)));
//                    Toast.makeText(this, ""+selectedFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    Uri subtitleUri = Uri.fromFile(selectedFile);

                    if (subtitle != null) {

                        subtitleFormat = Format.createTextSampleFormat(
                                null, // An identifier for the track. May be null.
                                MimeTypes.APPLICATION_SUBRIP, // The mime type. Must be set correctly.
                                Format.NO_VALUE, // Selection flags for the track.
                                "en"); // The subtitle language. May be null.

                        subtitleSource = new SingleSampleMediaSource
                                .Factory(dataSourceFactory)
                                .createMediaSource(subtitleUri, subtitleFormat, C.TIME_UNSET);


                        mergedSource = new MergingMediaSource(videoSource, subtitleSource);
                        player.prepare(mergedSource, false, false);
                        player.setPlayWhenReady(true);
                        subbool = false;
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initEq(View view) {
        try {
            for (short i = 0; i < Equalizers.getNumberOfBands(); i++) {
                short eqbands = i;
                short[] bandLevel = Equalizers.getBandLevelRange();
                seekBar = new VerticalSeekBar(getApplicationContext());
                textView = new TextView(getApplicationContext());
                switch (i) {
                    case 0:
                        seekBar = (VerticalSeekBar) view.findViewById(R.id.seek_bar1);
                        textView = (TextView) view.findViewById(R.id.level1);
                        //seekBar.getProgressDrawable().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //seekBar.getThumb().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        seekBar = (VerticalSeekBar) view.findViewById(R.id.seek_bar2);
                        textView = (TextView) view.findViewById(R.id.level2);
                        //seekBar.getProgressDrawable().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //seekBar.getThumb().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        seekBar = (VerticalSeekBar) view.findViewById(R.id.seek_bar3);
                        textView = (TextView) view.findViewById(R.id.level3);
                        //seekBar.getProgressDrawable().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //seekBar.getThumb().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 3:
                        seekBar = (VerticalSeekBar) view.findViewById(R.id.seek_bar4);
                        textView = (TextView) view.findViewById(R.id.level4);
                        //seekBar.getProgressDrawable().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //seekBar.getThumb().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 4:
                        seekBar = (VerticalSeekBar) view.findViewById(R.id.seek_bar5);
                        textView = (TextView) view.findViewById(R.id.level5);
                        //seekBar.getProgressDrawable().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //seekBar.getThumb().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_IN);
                        //textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
                seekBarFinal[eqbands] = seekBar;
                seekBar.setId(i);

                if (bandLevel != null) {
                    seekBar.setMax(bandLevel[1] - bandLevel[0]);
                    int presetPos = PreferenceUtil.getInstance().getPresetPos();
                    if (presetPos < Equalizers.getPresetNo()) {
                        seekBarFinal[eqbands].setProgress(Equalizers.getBandLevel(eqbands) - bandLevel[0]);
                    } else {
                        seekBarFinal[i].setProgress(PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.BAND_LEVEL + i, 0) - bandLevel[0]);
                    }
                }

                int frequency = Equalizers.getCenterFreq(eqbands);
                if (frequency < 1000 * 1000) {
                    textView.setText((frequency / 1000) + "Hz");
                } else {
                    textView.setText((frequency / (1000 * 1000)) + "kHz");
                }

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                        try {
                            if (fromUser) {
                                if (bandLevel != null) {
                                    int level = seekbar.getProgress() + bandLevel[0];
                                    Equalizers.setBandLevel(eqbands, (short) level);
                                    int presetNo = Equalizers.getPresetNo();

                                    if (presetNo != 0) {
                                        rvEqualizer.smoothScrollToPosition(Equalizers.getPresetNo());
                                        for (int i = 0; i < equalizerListData.size(); i++) {
                                            if (i == Equalizers.getPresetNo()) {
                                                equalizerListData.set(i, new EqualizerDataList(equalizerListData.get(i).getName(), true));
                                            } else {
                                                equalizerListData.set(i, new EqualizerDataList(equalizerListData.get(i).getName(), false));
                                            }
                                        }
                                        myAdapter.notifyDataSetChanged();
                                    } else {
                                        rvEqualizer.smoothScrollToPosition(0);
                                        for (int i = 0; i < equalizerListData.size(); i++) {
                                            if (i == 0) {
                                                equalizerListData.set(i, new EqualizerDataList(equalizerListData.get(i).getName(), true));
                                            } else {
                                                equalizerListData.set(i, new EqualizerDataList(equalizerListData.get(i).getName(), false));
                                            }
                                        }
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    Equalizers.savePrefs(eqbands, level);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("", "Failed to init eq");
        }
    }

    public void save_image(Bitmap bitmap) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + getResources().getString(R.string.app_name) + "/");
        Log.d("--folder--", "save_image: directory " + dir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, videolist.get(position).getTitle() + "_" + new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()) + ".jpeg");
        Log.d("--folder--", "save_image: file " + file.toString());

        try {
            OutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            Toast.makeText(getApplicationContext(), "Saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MakeSureFileWasCreatedThenMakeAvabile(file);
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file) {
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.toString()}, new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

            }
        });
    }

    private void checkSetPer() {
        Log.d("--popup--", "onClick: popup 6 ");

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void openSettings() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
        }
        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        Log.d("--popup--", "onClick: popup 7 ");

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void minimize() {
        if (playerView == null) {
            return;
        }
        Rational aspectRatio = new Rational(playerView.getWidth(), playerView.getHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            adjustFullScreen(getResources().getConfiguration());
        }
    }

    private void adjustFullScreen(Configuration config) {
        final View decorView = getWindow().getDecorView();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            // control_layout.setVisibility(View.GONE);
            Log.e("wwwwww", "adjustFullScreen: first");

            //  playerView.setAdjustViewBounds(false);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            control_layout.setVisibility(View.VISIBLE);

            Log.e("wwwwww", "adjustFullScreen: sec");
            //  mMovieView.setAdjustViewBounds(true);
        }
    }

    private void rotateScreen() {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Bitmap bmp;
            retriever.setDataSource(this, Uri.parse(videolist.get(position).getPath()));
            bmp = retriever.getFrameAtTime();

            int videoWidth = bmp.getWidth();
            int videoHeight = bmp.getHeight();

            if (videoWidth > videoHeight) {
                //Set orientation to landscape
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            if (videoWidth < videoHeight) {
                //Set orientation to portrait
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        } catch (RuntimeException ex) {
            //error occurred
            Log.e("MediaMetadataRetriever", "- Failed to rotate the video");

        }
    }

    private void renameVideofile() {
        int pos = player.getCurrentWindowIndex();

        File file = new File(videolist.get(pos).getPath());
        Log.e("file ", " : " + file.getAbsoluteFile().getParent());
        Log.e("file ", " : " + file.getName());
        String pPath = file.getAbsoluteFile().getParent();
        String fName = file.getName();
        String aName = fName.substring(0, fName.lastIndexOf("."));
        String bName = fName.substring(fName.lastIndexOf("."));
        Log.e("Name : " + aName, " : " + bName);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.CustomDialog);
        LayoutInflater inflater1 = LayoutInflater.from(this);
        View view = inflater1.inflate(R.layout.dialog_file_rename, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        EditText edtName = (EditText) view.findViewById(R.id.edtName);
        TextView txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        TextView txtSave = (TextView) view.findViewById(R.id.txtSave);

        builder1.setView(view);
        final AlertDialog dialog = builder1.create();

        edtName.setText(aName);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(VideoPlayerActivity.this, "Enter file name", Toast.LENGTH_LONG).show();
                } else {
                    File oldFolder = new File(pPath, fName);
                    File newFolder = new File(pPath, edtName.getText().toString().trim() + bName);
                    boolean success = oldFolder.renameTo(newFolder);
                    Log.e("success : ", " : " + success);

                    Uri uri = Uri.fromFile(newFolder);
                    Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    sendBroadcast(scanFileIntent);

                    Uri uri1 = Uri.fromFile(oldFolder);
                    Intent scanFileIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri1);
                    sendBroadcast(scanFileIntent1);
                    dialog.dismiss();
                    finish();
                    finish();

                    /*   Uri filesUri = Uri.fromFile(oldFolder);

                     *//*    final ContentResolver contentResolver = getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put("_data", );

                    contentResolver.update(filesUri,where,selectionArgs,)*/


                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void propertiDialog() {

        int pos = player.getCurrentWindowIndex();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PropertyDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        builder.setTitle("  ");
        View v1 = inflater.inflate(R.layout.properties_player, null);
        TextView t1 = v1.findViewById(R.id.name);
        t1.setText(videolist.get(position).getTitle());
        TextView t2 = v1.findViewById(R.id.duration);
        t2.setText(videolist.get(pos).getDuration());
        TextView t3 = v1.findViewById(R.id.fsize);
        t3.setText(videolist.get(pos).getSize());
        TextView t4 = v1.findViewById(R.id.location);
        t4.setText(videolist.get(pos).getPath());
        TextView t5 = v1.findViewById(R.id.date);
        t5.setText(videolist.get(pos).getDate());
        builder.setView(v1).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void deleteVideo() {

        int pos = player.getCurrentWindowIndex();

        if (videolist.size() != 0) {
            View checkboxview = LayoutInflater.from(this).inflate(R.layout.checkbox, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.PropertyDialog);
            alertDialog.setTitle("Delete video from device?");
            alertDialog.setMessage("Video will be deleted permanently from device.");
            alertDialog.setView(checkboxview);
            alertDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (checktrue) {

                        List<PrivateItem> lastVideoPlay;
                        String videoURL = PreferenceUtil.getInstance(VideoPlayerActivity.this).getRecycleVideo();
                        if (!videoURL.equalsIgnoreCase("")) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<PrivateItem>>() {
                            }.getType();
                            lastVideoPlay = gson.fromJson(videoURL, type);
                        } else {
                            lastVideoPlay = new ArrayList<>();
                        }

                        File dir = new File(String.valueOf(getExternalFilesDir(getResources().getString(R.string.recycler_folder_name))));
                        File file = new File(videolist.get(position).getPath());

                        Gson gson = new Gson();
                        lastVideoPlay.add(new PrivateItem(file.getParent(), videolist.get(position).getTitle()));
                        PreferenceUtil.getInstance(VideoPlayerActivity.this).setRecycleVideo(gson.toJson(lastVideoPlay).toString());

                        try {
                            moveFile(file, dir);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        final String where = MediaStore.MediaColumns.DATA + "=?";
                        final String[] selectionArgs = new String[]{
                                file.getAbsolutePath()
                        };
                        final ContentResolver contentResolver = getContentResolver();
                        final Uri filesUri = MediaStore.Files.getContentUri("external");

                        contentResolver.delete(filesUri, where, selectionArgs);
                        if (file.exists()) {
                            contentResolver.delete(filesUri, where, selectionArgs);
                        }

                    } else {
                        File file = new File(videolist.get(pos).getPath());

                        boolean bb = file.delete();

                        final String where = MediaStore.MediaColumns.DATA + "=?";
                        final String[] selectionArgs = new String[]{
                                file.getAbsolutePath()
                        };
                        final ContentResolver contentResolver = getContentResolver();
                        final Uri filesUri = MediaStore.Files.getContentUri("external");


                        contentResolver.delete(filesUri, where, selectionArgs);
                        if (file.exists()) {
                            contentResolver.delete(filesUri, where, selectionArgs);
                        }
                    }
                    finish();
                }
            });
            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    checktrue = false;
                }
            });

            CheckBox checkBox = (CheckBox) checkboxview.findViewById(R.id.checkbox);
            checkBox.setText("Move to Recycle Bin");
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checktrue = isChecked;
                }
            });
        }
    }

    public void moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());

        Log.e("ffdff", "moveFile: " + file.getAbsolutePath());
        Log.e("ffdff", "moveFile: " + dir.getAbsolutePath());

        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }


    }

    private void setUpLocale() {
        String savedLocale = PrefUtil.getstringPref("locale", VideoPlayerActivity.this);
        Locale locale = new Locale(savedLocale);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context context;
        private ArrayList<EqualizerDataList> equalizerList;


        public MyAdapter(Context context, ArrayList<EqualizerDataList> equalizerList) {
            this.context = context;
            this.equalizerList = equalizerList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_equalizer, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.txtName.setText(equalizerList.get(holder.getAdapterPosition()).getName());

            holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < equalizerListData.size(); i++) {
                        if (i == position) {
                            equalizerListData.set(i, new EqualizerDataList(equalizerListData.get(i).getName(), true));
                        } else {
                            equalizerListData.set(i, new EqualizerDataList(equalizerListData.get(i).getName(), false));
                        }
                    }

                    if (position < equalizerListData.size()) {
                        PreferenceUtil.getInstance(getApplicationContext()).savePresetPos(holder.getAdapterPosition());
                        short presetNo = Equalizers.getPresetNo();
                        short bandNo = Equalizers.getNumberOfBands();
                        if (presetNo != -1 && bandNo != -1) {
                            if (position < presetNo) {
                                short presetPosition = (short) position;
                                Equalizers.usePreset(presetPosition);
                                for (short i = 0; i < bandNo; i++) {
                                    final short[] range = Equalizers.getBandLevelRange();
                                    short level = Equalizers.getBandLevel(i);
                                    if (range != null) {
                                        if (i < seekBarFinal.length) {
                                            seekBarFinal[i].setProgress(level - range[0]);
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < bandNo; i++) {
                                    final short[] range = Equalizers.getBandLevelRange();
                                    if (range != null) {
                                        if (i < seekBarFinal.length) {
                                            seekBarFinal[i].setProgress(PreferenceUtil.getInstance().saveEq().getInt(PreferenceUtil.BAND_LEVEL + i, 0) - range[0]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            if (equalizerList.get(position).isSelectItem()) {
                holder.txtName.getBackground().setColorFilter(getResources().getColor(R.color.t15), PorterDuff.Mode.SRC_ATOP);
                holder.txtName.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.txtName.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                holder.txtName.setTextColor(getResources().getColor(R.color.black));
            }

        }

        @Override
        public int getItemCount() {
//            Log.d("equalizerListData", "getItemCount: equalizerListData size " + equalizerList.size());
            return this.equalizerList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            Button txtName;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txtName = (Button) itemView.findViewById(R.id.txtName);
            }
        }
    }

}