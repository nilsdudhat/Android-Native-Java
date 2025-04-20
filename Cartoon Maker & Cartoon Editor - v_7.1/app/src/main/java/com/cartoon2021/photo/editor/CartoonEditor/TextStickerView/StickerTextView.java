package com.cartoon2021.photo.editor.CartoonEditor.TextStickerView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;

import com.cartoon2021.photo.editor.CartoonEditor.Custom.StickerView;

public class StickerTextView extends StickerView {
    private AutoResizeTextView tv_main;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StickerTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public View getMainView() {
        AutoResizeTextView autoResizeTextView = this.tv_main;
        if (autoResizeTextView != null) {
            return autoResizeTextView;
        }
        this.tv_main = new AutoResizeTextView(getContext());
        this.tv_main.setTextColor(-1);
        this.tv_main.setGravity(17);
        this.tv_main.setTextSize(400.0f);
        this.tv_main.setShadowLayer(4.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
        this.tv_main.setMaxLines(1);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.tv_main.setLayoutParams(layoutParams);
        if (getImageViewFlip() != null) {
            getImageViewFlip().setVisibility(View.VISIBLE);
        }
        return this.tv_main;
    }

    public void setText(String str) {
        AutoResizeTextView autoResizeTextView = this.tv_main;
        if (autoResizeTextView != null) {
            autoResizeTextView.setText(str);
        }
    }

    public void setSize(int i) {
        AutoResizeTextView autoResizeTextView = this.tv_main;
        if (autoResizeTextView != null) {
            autoResizeTextView.setSize(i);
        }
    }

    public void setColor(int i) {
        AutoResizeTextView autoResizeTextView = this.tv_main;
        if (autoResizeTextView != null) {
            autoResizeTextView.setTextColor(i);
        }
    }

    public void setTypeFace(Typeface typeface) {
        AutoResizeTextView autoResizeTextView = this.tv_main;
        if (autoResizeTextView != null) {
            autoResizeTextView.setTypeface(typeface);
        }
    }

    public String getText() {
        AutoResizeTextView autoResizeTextView = this.tv_main;
        if (autoResizeTextView != null) {
            return autoResizeTextView.getText().toString();
        }
        return null;
    }

    public static float pixelsToSp(Context context, float f) {
        return f / context.getResources().getDisplayMetrics().scaledDensity;
    }


    public void onScaling(boolean z) {
        super.onScaling(z);
    }
}
