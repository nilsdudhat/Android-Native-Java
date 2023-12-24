package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views;

import android.app.Dialog;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GestureDialog extends Dialog {

    MyGestureDetector gestDetec;
    GestureDetector gestureDetector;

    View.OnTouchListener gestureListener;

    public GestureDialog (Context context, int theme) {
        super(context, theme);

        gestDetec = new MyGestureDetector();    //inital setup
        gestureDetector = new GestureDetector(gestDetec);
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    static class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            System.out.println( "Help im being touched!" );
            return false;
        }
    }
}
