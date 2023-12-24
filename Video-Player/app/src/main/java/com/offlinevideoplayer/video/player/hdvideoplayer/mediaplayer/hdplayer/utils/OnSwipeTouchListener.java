package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.equalizer.VerticalSeekBar;

import java.util.concurrent.TimeUnit;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final SimpleExoPlayer player;
    private final PlayerView playerView;
    private final AudioManager audioManager;
    private final Context context;
    int volumeUp = 0;
    int volumeDown = 0;


    public OnSwipeTouchListener(Context c, SimpleExoPlayer player, PlayerView playerView, AudioManager audioManager) {
        gestureDetector = new GestureDetector(c, new GestureListener());
        this.player = player;
        this.playerView = playerView;
        this.audioManager = audioManager;
        this.context = c;
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {

        return gestureDetector.onTouchEvent(motionEvent);
    }

//    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
//            try {
//                float diffY = e2.getY() - e1.getY();
//                float diffX = e2.getX() - e1.getX();
//                if (Math.abs(diffX) > Math.abs(diffY)) {
//                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX > 0) {
//                            onSwipeRight(Math.abs(diffX));
//                        } else {
//                            onSwipeLeft(Math.abs(diffX));
//                        }
//                        result = true;
//                    }
//                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (e1.getX() > playerView.getRootView().getWidth() / 2) {
//                        if (diffY > 0) {
//                            volumeDown(Math.abs(diffY));
//                        } else {
//                            volumeUp(Math.abs(diffY));
//                        }
//                        result = true;
//                    } else {
//                        if (diffY > 0) {
//                            brightnessDown(Math.abs(diffY));
//                        } else {
//                            brightnessUp(Math.abs(diffY));
//                        }
//                        result = true;
//                    }
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            return result;
//        }
//    }

    private void onSwipeRight(float abs) {
        TextView t1 = ((Activity) this.context).findViewById(R.id.text);
        if (player.getPlayWhenReady()) {
            player.setPlayWhenReady(false);
            player.seekTo((long) (player.getCurrentPosition() + Math.abs(abs) * 60));
            player.setPlayWhenReady(true);
            t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_fast_forward, 0);
            t1.setText(duration(player.getCurrentPosition()));
            t1.setVisibility(View.VISIBLE);
            setIn(t1);
        } else {
            player.seekTo((long) (player.getCurrentPosition() + Math.abs(abs) * 60));
            t1.setText(duration(player.getCurrentPosition()));
            t1.setVisibility(View.VISIBLE);
            setIn(t1);

        }

    }
//
////        @Override
////        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
////            try {
////                float diffY = e2.getY() - e1.getY();
////                float diffX = e2.getX() - e1.getX();
////
////                if (Math.abs(diffX) > Math.abs(diffY)) {
////                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(distanceX) > SWIPE_VELOCITY_THRESHOLD) {
////                        if (diffX > 0) {
////                            if (!PreferenceUtil.getInstance(context).getLock()) {
////                                onSwipeRight(Math.abs(diffX));
////                            }
////                        } else {
////                            if (!PreferenceUtil.getInstance(context).getLock()) {
////                                onSwipeLeft(Math.abs(diffX));
////                            }
////                        }
////                    }
////                } else {
////
////                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(distanceY) > SWIPE_VELOCITY_THRESHOLD) {
////                        if (e1.getX() > playerView.getRootView().getWidth() / 2) {
////                            if (diffY > 0) {
////                                if (!PreferenceUtil.getInstance(context).getLock()) {
////                                    volumeDown(Math.abs(diffY));
////                                }
////                            } else {
////                                if (!PreferenceUtil.getInstance(context).getLock()) {
////                                    volumeUp(Math.abs(diffY));
////                                }
////                            }
////
////                        } else {
////                            if (diffY > 0) {
////                                if (!PreferenceUtil.getInstance(context).getLock()) {
////                                    brightnessDown(Math.abs(diffY));
////                                }
////                            } else {
////                                if (!PreferenceUtil.getInstance(context).getLock()) {
////                                    brightnessUp(Math.abs(diffY));
////                                }
////                            }
////                        }
////                    }
////                }
////            } catch (Exception exception) {
////                exception.printStackTrace();
////            }
////            return true;
////        }
//    }

    private void onSwipeLeft(float abs) {
        TextView t1 = ((Activity) this.context).findViewById(R.id.text);
        if (player.getPlayWhenReady()) {
            player.setPlayWhenReady(false);
            player.seekTo((long) (player.getCurrentPosition() - Math.abs(abs) * 60));
            player.setPlayWhenReady(true);
            t1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fast_rewind, 0, 0, 0);
            t1.setText(duration(player.getCurrentPosition()));
            t1.setVisibility(View.VISIBLE);
            setIn(t1);
        } else {
            player.seekTo((long) (player.getCurrentPosition() - Math.abs(abs) * 60));
            t1.setText(duration(player.getCurrentPosition()));
            t1.setVisibility(View.VISIBLE);
            setIn(t1);
        }
    }

    @SuppressLint("DefaultLocale")
    private void brightnessUp(float abs) {
        LinearLayout brightll = ((Activity) this.context).findViewById(R.id.bright_ll);


        WindowManager.LayoutParams attributes = ((Activity) this.context).getWindow().getAttributes();
        float x = PreferenceUtil.getInstance(context).getLastBrightness();
        TextView t1 = ((Activity) this.context).findViewById(R.id.text);
        VerticalSeekBar brightseek = ((Activity) this.context).findViewById(R.id.volumeseek);
        brightseek.setEnabled(false);
        brightseek.setMax(100);
        if (x + (abs / 1000.0f) < 1) {
            attributes.screenBrightness = x + abs / 1000.0f;
            ((Activity) this.context).getWindow().setAttributes(attributes);
            PreferenceUtil.getInstance(context).saveLastBrightness(attributes.screenBrightness);
            t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            t1.setText(String.format("Brightness:  %d%%", (int) Math.floor(attributes.screenBrightness * 100)));
            brightseek.setProgress((int) Math.floor(attributes.screenBrightness * 100));
            brightll.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            setIn(t1);
            setSeek(brightll);
        } else {
            attributes.screenBrightness = 1;
            t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ((Activity) this.context).getWindow().setAttributes(attributes);
            PreferenceUtil.getInstance(context).saveLastBrightness(attributes.screenBrightness);
            t1.setText(String.format("Brightness:  %d%%", (int) Math.floor(attributes.screenBrightness * 100)));
            t1.setVisibility(View.VISIBLE);
            brightseek.setProgress((int) Math.floor(attributes.screenBrightness * 100));
            brightll.setVisibility(View.VISIBLE);
            setIn(t1);
            setSeek(brightll);
        }
    }

    @SuppressLint("DefaultLocale")
    private void brightnessDown(float abs) {

        LinearLayout brightll = ((Activity) this.context).findViewById(R.id.bright_ll);

        WindowManager.LayoutParams attributes = ((Activity) this.context).getWindow().getAttributes();
        float x = PreferenceUtil.getInstance(context).getLastBrightness();
        TextView t1 = ((Activity) this.context).findViewById(R.id.text);
        t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        VerticalSeekBar brightseek = ((Activity) this.context).findViewById(R.id.volumeseek);
        brightseek.setMax(100);
        brightseek.setEnabled(false);

        if (x - (abs / 1000.0f) > 0) {
            attributes.screenBrightness = x - (abs / 1000.0f);
            ((Activity) this.context).getWindow().setAttributes(attributes);
            PreferenceUtil.getInstance(context).saveLastBrightness(attributes.screenBrightness);
            t1.setText(String.format("Brightness:  %d%%", (int) Math.floor(attributes.screenBrightness * 100)));
            t1.setVisibility(View.VISIBLE);
            brightll.setVisibility(View.VISIBLE);
            brightseek.setProgress((int) Math.floor(attributes.screenBrightness * 100));

            setIn(t1);
            setSeek(brightll);
        } else {
            attributes.screenBrightness = 0;
            ((Activity) this.context).getWindow().setAttributes(attributes);
            PreferenceUtil.getInstance(context).saveLastBrightness(attributes.screenBrightness);
            t1.setText(String.format("Brightness:  %d%%", (int) Math.floor(attributes.screenBrightness * 100)));
            t1.setVisibility(View.VISIBLE);
            brightll.setVisibility(View.VISIBLE);
            brightseek.setProgress((int) Math.floor(attributes.screenBrightness * 100));
            setIn(t1);
            setSeek(brightll);
        }
    }

    private void setSeek(final View t1) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t1.setVisibility(View.INVISIBLE);
            }
        }, 1500);
    }

    private void setIn(final View t1) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t1.setVisibility(View.INVISIBLE);
            }
        }, 1500);
    }

    @SuppressLint("DefaultLocale")
    private void volumeUp(float abs) {

        LinearLayout volumll = ((Activity) this.context).findViewById(R.id.volum_ll);
        int c = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        VerticalSeekBar volumseek = ((Activity) this.context).findViewById(R.id.brightseek);
        volumseek.setMax(max);
        volumseek.setEnabled(false);

        TextView t1 = ((Activity) this.context).findViewById(R.id.text);
        int x = (int) (abs / 100);
        t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        Log.d("--volume--", "volumeUp: x " + x);
        Log.d("--volume--", "volumeUp: c " + c);
        if (x + c < max) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, x + c, 0);
            t1.setText(String.format("Volume:  %d", x + c));
            t1.setVisibility(View.VISIBLE);
            volumseek.setProgress(x + c);
            volumll.setVisibility(View.VISIBLE);
            setSeek(volumll);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
            t1.setText(String.format("Volume:  %d", max));
            t1.setVisibility(View.VISIBLE);
            volumll.setVisibility(View.VISIBLE);
            volumseek.setProgress(max);
            setIn(t1);
            setSeek(volumll);
        }

    /*  if(x + c > max){
            volumseek.setVisibility(View.INVISIBLE);

        }
       else {

            t1.setText(String.format(" %d", x + c));
            t1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volumn, 0, 0, 0);
            t1.setVisibility(View.VISIBLE);
            swseek.setVisibility(View.VISIBLE);
            swseek.setProgress(30);
            setIn(t1);
        }

       if (VideoPlayerTestActivity.swdecoder){

            if (x+c<max*2){
                volumseek.setVisibility(View.VISIBLE);
                volumseek.setProgress(x + c);
            }

        }*/

    }

    @SuppressLint("DefaultLocale")
    private void volumeDown(float abs) {

        LinearLayout volumll = ((Activity) this.context).findViewById(R.id.volum_ll);

        int c = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        TextView t1 = ((Activity) this.context).findViewById(R.id.text);
        t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        VerticalSeekBar volumseek = ((Activity) this.context).findViewById(R.id.brightseek);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumseek.setMax(max);
        volumseek.setEnabled(false);
        int x = (int) (abs / 100);
        if (c - x > 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, c - x, 0);
            t1.setText(String.format("Volume:  %d", c - x));
            t1.setVisibility(View.VISIBLE);
            volumll.setVisibility(View.VISIBLE);
            volumseek.setProgress(c - x);
            setSeek(volumll);
            setIn(t1);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            t1.setText("Volume:  0");
            t1.setVisibility(View.VISIBLE);
            volumll.setVisibility(View.VISIBLE);
            volumseek.setProgress(0);
            setSeek(volumll);
            setIn(t1);
        }

    }

    @SuppressLint("DefaultLocale")
    private String duration(Long x) {

        long ho = TimeUnit.MILLISECONDS.toHours(x);
        long mo = TimeUnit.MILLISECONDS.toMinutes(x) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(x));
        long so = TimeUnit.MILLISECONDS.toSeconds(x) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(x));

        if (ho >= 1) return String.format("%02d:%02d:%02d", ho, mo, so);
        else return String.format("%02d:%02d", mo, so);

    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                Log.e("TAG", "X: " + Math.abs(diffX));
                Log.e("TAG", "Y: " + Math.abs(diffY));


                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            if (!PreferenceUtil.getInstance(context).getLock()) {
                                onSwipeRight(Math.abs(diffX));
                            }
                        } else {
                            if (!PreferenceUtil.getInstance(context).getLock()) {
                                onSwipeLeft(Math.abs(diffX));
                            }
                        }
                    }
                } else {

                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (e1.getX() > playerView.getRootView().getWidth() / 2) {
                            if (diffY > 0) {
                                if (!PreferenceUtil.getInstance(context).getLock()) {
                                    volumeDown(Math.abs(diffY));
                                }
                            } else {
                                if (!PreferenceUtil.getInstance(context).getLock()) {
                                    volumeUp(Math.abs(diffY));
                                }
                            }
                        } else {
                            if (diffY > 0) {
                                if (!PreferenceUtil.getInstance(context).getLock()) {
                                    brightnessDown(Math.abs(diffY));
                                }
                            } else {
                                if (!PreferenceUtil.getInstance(context).getLock()) {
                                    brightnessUp(Math.abs(diffY));
                                }
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return true;
        }
    }

}