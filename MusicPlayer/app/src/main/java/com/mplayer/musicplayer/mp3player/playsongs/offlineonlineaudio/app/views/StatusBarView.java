package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class StatusBarView extends View {

    public StatusBarView(Context context) {
        super(context);
    }

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int result = 0;
        int resourceId = this.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        setMeasuredDimension(widthMeasureSpec, result);
    }
}
